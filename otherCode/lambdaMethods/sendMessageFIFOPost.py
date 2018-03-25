import json
import boto3

def sendMessage(message, queueURL):
    sqs = boto3.client('sqs', region_name='eu-west-1')
    return sqs.send_message(QueueUrl=queueURL,
                            MessageBody=json.dumps(message),
                            MessageGroupId="ID1182")


def lambda_handler(event, context):
    # Event['body'] will throw exceptions testing in Lambda, but works with actual code. Update test cases.
    data = json.loads(event['body'])
    result = {}

    out = {}
    out['isBase64Encoded']: False
    out['headers'] = {
        "content-type": "application-json"
    }

    if ('data' not in data):
        result['result'] = "POST Takes data {...} it wasn't present"
        out['statusCode'] = 400
        out['body'] = json.dumps(result)

    elif ('queueurl' not in data):
        result['result'] = "POST takes queueurl : your-queue-here, it wasn't present"
        out['statusCode'] = 400
        out['body'] = json.dumps(result)

    elif (data['data'] is not None and data['queueurl'] is not None):

        sendMessage(data['data'], data['queueurl'])
        result = {}
        result['result'] = "Success"

        out['statusCode'] = 200
        out['body'] = json.dumps(result)
    return out
