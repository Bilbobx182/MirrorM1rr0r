import boto3
import json

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
        messageItem[count] = {
            "Contents": response['Messages'][0]['Body'],
            "Timestamp": int(response['Messages'][0]['Attributes']['SentTimestamp'])
        }
        # Because we want to itterate around N amount of messages and only delete the new ones.
        # if (response['Messages'][0]['ReceiptHandle'] not in receipts):
        #     receipts.append(response['Messages'][0]['ReceiptHandle'])
        #     deleteResult = sqs.delete_message(QueueUrl=queue, ReceiptHandle=response['Messages'][0]['ReceiptHandle'])
        count += 1

    return (messageItem)


def lambda_handler():
    out = {}

    messageCountRequested = 2
    queue = 'https://sqs.us-west-2.amazonaws.com/186314837751/ciaranDedup.fifo'
    contents = json.dumps(getMessageFromQueue(messageCountRequested, queue))
    out['statusCode'] = 200
    out['body'] = contents

    out['headers'] = {
        "content-type": "application-json"
    }
    return (out)


def extract_time(json):
    try:
        # Also convert to int since update_time will be string.  When comparing
        # strings, "10" is smaller than "2".
        return int(json['page']['update_time'])
    except KeyError:
        return 0


# lines.sort() is more efficient than lines = lines.sorted()
# lines.sort(key=extract_time, reverse=True)

# WHAT I WAS WORKING ON WAS A) CONVERTING DICTIONARY TO LIST, B SORTING THAT DICTIONARY BASED ON TIMESTAMP
dictContents = json.loads(lambda_handler()['body'])
print(dictContents)
