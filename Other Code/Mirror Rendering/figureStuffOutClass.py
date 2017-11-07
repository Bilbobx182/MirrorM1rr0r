import json
import boto3

sqs = boto3.client('sqs', region_name='eu-west-1')


def getMessageFromQueue(messageCountRequested, queue):
    maxcount = int(messageCountRequested)
    count = 0
    messageItem = {}
    receipts = []

    while (count < maxcount):
        response = sqs.receive_message(
            QueueUrl=queue,
            AttributeNames=[
                'SentTimestamp'
            ],
            MaxNumberOfMessages=maxcount,
            MessageAttributeNames=[
                'All'
            ],
            VisibilityTimeout=0,
            WaitTimeSeconds=0
        )
        messageItem[str(count)] = {
            "Contents": response['Messages'][0]['Body']
        }
        # Because we want to itterate around N amount of messages and only delete the new ones.
        if (response['Messages'][0]['ReceiptHandle'] not in receipts):
            receipts.append(response['Messages'][0]['ReceiptHandle'])
            deleteResult = sqs.delete_message(QueueUrl=queue, ReceiptHandle=response['Messages'][0]['ReceiptHandle'])
            count += 1

    return (messageItem)


def lambda_handler(event, context):
    out = {}

    if (not event['queryStringParameters']['queue']):
        out['statusCode'] = 400
        out['body'] = "Queue not specified"
    else:
        messageCountRequested = event['queryStringParameters']['count']
        queue = event['queryStringParameters']['queue']
        contents = json.dumps(getMessageFromQueue(messageCountRequested, queue))
        out['statusCode'] = 200
        out['body'] = contents

    out['headers'] = {
        "content-type": "application-json"
    }

    return (out)
