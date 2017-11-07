import boto3

import json

def getMessageFromQueue(messageCountRequested, queue):
    maxcount = int(messageCountRequested)
    count = 0
    messageItem = {}

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
        count += 1
    return (messageItem)


def lambda_handler():
    out = {}

    messageCountRequested = 2
    queue = 'https://sqs.eu-west-1.amazonaws.com/186314837751/ciaran'
    contents = json.dumps(getMessageFromQueue(messageCountRequested, queue))
    out['statusCode'] = 200
    out['body'] = contents

    out['headers'] = {
        "content-type": "application-json"
    }

    return (out)

print(lambda_handler())