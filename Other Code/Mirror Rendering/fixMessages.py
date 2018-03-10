import json
import boto3

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
            ],
            VisibilityTimeout=43200,
            WaitTimeSeconds=0
        )
        messageItem[str(count)] = {
            "Contents": response['Messages'][0]['Body'],
            "SentTimestamp": response['Messages'][0]['Attributes']['SentTimestamp']
        }
        if (response['Messages'][0]['ReceiptHandle'] not in receipts):
            receipts.append(response['Messages'][0]['ReceiptHandle'])
            try:
                deleteResult = sqs.delete_message(QueueUrl=queue, ReceiptHandle=response['Messages'][0]['ReceiptHandle'])
            except(Exception):
                print("shit")
            count += 1

    return (messageItem)


out = {}

# if (not event['queryStringParameters']['queue']):
#         out['statusCode'] = 400
#         out['body'] = "Queue not specified"
if (False):
    print("lol what")
else:
    # messageCountRequested = event['queryStringParameters']['count']
    # queue = event['queryStringParameters']['queue']
    messageCountRequested = 1
    queue = "https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo"
    contents = json.dumps(getMessageFromQueue(messageCountRequested, queue))
    out['statusCode'] = 200
    out['body'] = contents

    out['headers'] = {
        "content-type": "application-json"
    }

    print(out)
