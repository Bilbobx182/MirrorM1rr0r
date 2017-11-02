import json
import boto3

sqs = boto3.client('sqs', region_name='eu-west-1')
queue_url = 'https://sqs.eu-west-1.amazonaws.com/186314837751/MirrorM1rr0r'

# Takes in input of Count messages will make it take in a queue too once done testing
def getMessageFromQueue(messageCountRequested):
    maxcount = float(messageCountRequested)
    count = 0
    messageItem = {}

    try:
        while (count < maxcount):
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

    # Because sometimes we don't get a correct response we just try the above again
    except KeyError as e:
        getMessageFromQueue(maxcount)


def lambda_handler(event, context):
    contents = json.dumps(getMessageFromQueue( messageCountRequested = event['queryStringParameters']['count']))
    out = {}
    out['statusCode'] = 200
    out['body'] = contents
    out['headers'] = {
        "content-type": "application-json"
    }

    return (out)
