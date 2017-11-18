import json
import boto3
import operator
import json

sqs = boto3.client('sqs', region_name='eu-west-1',
                   aws_access_key_id='AKIAIDMELKX2YW6VOJ7Q',
                   aws_secret_access_key='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')


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


def lambda_handler():
    result = {}
    outJSON = {}
    loopCount = 0

    messageCountRequested = 2
    queue = 'https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo'

    while (loopCount < messageCountRequested):
        result[loopCount] = getMessageFromQueue(queue, messageCountRequested)
        loopCount += 1

    outJSON['statusCode'] = 200
    outJSON['body'] = result

    outJSON['headers'] = {
            "content-type": "application-json"
        }
    return (json.dumps(outJSON))


print(lambda_handler())
