import json
import boto3

sqs = boto3.client('sqs', region_name='eu-west-1')

def getMessageFromQueue(queue, currentItem):
    outputJSON = {}

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

    outputJSON[str(currentItem)] = {
        "Contents": response['Messages'][0]['Body'],
        "SentTimestamp": response['Messages'][0]['Attributes']['SentTimestamp']
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
    loopCount = 0
    if (not event['queryStringParameters']['queue'] or not event['queryStringParameters']['count']):
        result['statusCode'] = 400
        result['body'] = "Queue or count not specified"
    else:
        messageCountRequested = event['queryStringParameters']['count']
        queue = event['queryStringParameters']['queue']

        while (loopCount < messageCountRequested):
            result[loopCount] = getMessageFromQueue(queue, messageCountRequested)
            loopCount += 1

        result['statusCode'] = 200
        result ['body'] = json.dumps(result)

    result['headers'] = {
        "content-type": "application-json"
    }
    return (result)
