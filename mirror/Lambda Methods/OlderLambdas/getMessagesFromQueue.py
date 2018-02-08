import json
import boto3

# Note the region is still hardcoded to Ireland. This may change later.
sqs = boto3.client('sqs', region_name='eu-west-1',
                   aws_access_key_id='AKIAIDMELKX2YW6VOJ7Q',
                   aws_secret_access_key='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')


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
            ]
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


def lambda_handler():
    out = {}

    messageCountRequested = 1
    queue = 'https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo'

    while True:
        response = sqs.receive_message(QueueUrl=queue)
        print(response)
    return (out)

lambda_handler()