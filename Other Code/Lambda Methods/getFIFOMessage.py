import json
import boto3

sqs = boto3.client('sqs', region_name='eu-west-1')


def getMessageFromQueue(messageCountRequested, queue):
    maxcount = int(messageCountRequested)
    count = 0
    receipts = []
    outputJSON = {}

    # Receive message from SQS queue
    while (count < maxcount):
        response = sqs.receive_message(
            QueueUrl=queue,
            MaxNumberOfMessages=maxcount,
            AttributeNames=[
                'SentTimestamp'
            ],
            MessageAttributeNames=[
                'All'
            ],
        )

        outputJSON[str(count)] = {
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
        print('Received and changed visibilty timeout of message: %s' % message)
        if (response['Messages'][0]['ReceiptHandle'] not in receipts):
            receipts.append(response['Messages'][0]['ReceiptHandle'])
            count += 1
    return outputJSON


def lambda_handler(event, context):
    out = {}
    if (not event['queryStringParameters']['queue'] or not event['queryStringParameters']['count']):
        out['statusCode'] = 400
        out['body'] = "Queue or count not specified"
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
