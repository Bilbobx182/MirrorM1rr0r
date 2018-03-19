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

        result = {}
        result['result'] = "Success"
        return {
            'statusCode': 200,
            'isBase64Encoded': False,
            'body': json.dumps(result),
            'headers': {
                'content-type': 'application/json'
            }
        }
    else:
        result = {}
        if (data['data'] is None):
            result['result'] = "POST Takes data {...} it wasn't present"
        if (data['queueurl'] is None):
            result['result'] = "POST takes queueurl : your-queue-here, it wasn't present"
        return {
            'statusCode': 400,
            'isBase64Encoded': False,
            'body': json.dumps(result),
            'headers': {
                'content-type': 'application/json'
            }
        }
