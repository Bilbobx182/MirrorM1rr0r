import json
import boto3


# Takes in input of Count messages

def getMessageFromQueue(quename):
    sqs = boto3.client('sqs', region_name='eu-west-1')
    queue_url = 'https://sqs.eu-west-1.amazonaws.com/186314837751/MirrorM1rr0r'
    count = 0
    messageItem = {}

    try:
        while (count < messageCountRequested):
            response = sqs.receive_message(
                QueueUrl=queue_url,
                AttributeNames=[
                    'SentTimestamp'
                ],
                MaxNumberOfMessages=1,
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


    except KeyError as e:
        getMessageFromQueue(queue_url)


def lambda_handler(event, context):
    messageCountRequested = int(event['queryStringParameters']['count'])

    out = {}
    out['statusCode'] = 200
    out['body'] = json.dumps(messageItem)
    out['headers'] = {
        "content-type": "application-json"
    }

    return (out)
