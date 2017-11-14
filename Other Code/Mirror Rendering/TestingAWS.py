import json
import boto3
import operator

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
            VisibilityTimeout=0,
            WaitTimeSeconds=0
        )
        messageItem[str(count)] = {
            "Message": response['Messages'][0]['Body'],
            "SentTimestamp": response['Messages'][0]['Attributes']['SentTimestamp']
        }
        if (response['Messages'][0]['ReceiptHandle'] not in receipts):
            receipts.append(response['Messages'][0]['ReceiptHandle'])
            deleteResult = sqs.delete_message(QueueUrl=queue, ReceiptHandle=response['Messages'][0]['ReceiptHandle'])
            count += 1

    return (messageItem)


def lambda_handler():
    out = {}

    if (True):
        messageCountRequested = 2
        queue = 'https://sqs.eu-west-1.amazonaws.com/186314837751/hellociaran'
        contents = json.dumps(getMessageFromQueue(messageCountRequested, queue))
        out['statusCode'] = 200
        out['body'] = contents

        out['headers'] = {
            "content-type": "application-json"
        }
    else:
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


duct = (lambda_handler())
print(json.loads(duct['body']))
duct = json.loads(duct['body'])

contents = []
for key, value in duct.items():
    item = {}
    item['Message'] = value['Message']
    item['Timestamp'] = int(value['SentTimestamp'])
    contents.append(item);

print("hello")
result = (sorted(contents, key=operator.itemgetter('Message', 'Timestamp')))
print(result)
