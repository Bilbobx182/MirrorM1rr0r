import json
import boto3


def sendMessage(message, queueURL):
    sqs = boto3.client('sqs', region_name='eu-west-1')
    return sqs.send_message(QueueUrl=queueURL,
                            MessageBody=json.dumps(message),
                            MessageGroupId="ID1182")


def lambda_handler(event, context):
    data = json.loads(event['body'])

    if (data['data'] is not None and data['queueurl'] is not None):

        sendMessage(data['data'], data['queueurl'])
        return {
            'statusCode': 200,
            'isBase64Encoded': False,
            'body': "Success",
            'headers': {
                'content-type': 'application/json'
            }
        };
    else:
        return {
            'statusCode': 400,
            'isBase64Encoded': False,
            'body': "POST Takes data {...} and queueurl : your queue here. One or both weren't present",
            'headers': {
                'content-type': 'application/json'
            }
        };
