import json
import boto3
import operator
import json

sqs = boto3.client('sqs', region_name='eu-west-1')


def getMessageFromQueue(queue, currentItem):
    # Receive message from SQS queue
    response = sqs.receive_message(
        QueueUrl=queue,
        MaxNumberOfMessages=1,
        AttributeNames=[
            'All'
        ],
        MessageAttributeNames=[
            'All'
        ],
    )

    outputJSON = {
        "Contents": response['Messages'][0]['Body']
    }

    message = response['Messages'][0]
    receipt_handle = message['ReceiptHandle']

    # Change visibility timeout of message from queue
    sqs.change_message_visibility(
        QueueUrl=queue,
        ReceiptHandle=receipt_handle,
        VisibilityTimeout=36000
    )
    deleteResult = sqs.delete_message(QueueUrl=queue, ReceiptHandle=receipt_handle)

    return outputJSON


def lambda_handler(event, context):
    result = {}
    outJSON = {}
    loopCount = 0

    messageCountRequested = int(event['queryStringParameters']['count'])
    queue = event['queryStringParameters']['queueurl']

    while (loopCount < messageCountRequested):
        result["Message " + str(loopCount)] = getMessageFromQueue(queue, messageCountRequested)
        loopCount += 1

    outJSON['statusCode'] = 200
    outJSON['body'] = json.dumps(result)

    outJSON['headers'] = {
        "content-type": "application-json"
    }
    return (outJSON)
