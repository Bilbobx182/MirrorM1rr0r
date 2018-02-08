import json
import boto3


def sendMessage(queueURL, message):
    sqs = boto3.client('sqs', region_name='eu-west-1')

    response = sqs.send_message(QueueUrl=queueURL,
                                MessageBody=json.dumps(message),
                                MessageGroupId="ID1182")


def lambda_handler(event, context):
    out = {}
    if 'message' in event['queryStringParameters']:
        messagePayload = (event['queryStringParameters']['message'])
        if 'queueurl' in event['queryStringParameters']:
            queueURL = (event['queryStringParameters']['queueurl'])

        message = {'messagePayload': messagePayload}

        if ('location' in event['queryStringParameters']):
            message['location'] = (event['queryStringParameters']['location'])

        if ('fontsize' in event['queryStringParameters']):
            message['fontSize'] = (event['queryStringParameters']['fontsize'])

        if 'fontcolour' in event['queryStringParameters']:
            message['fontColour'] = (event['queryStringParameters']['fontcolour'])

        sendMessage(queueURL, message)

        out['statusCode'] = 201
        out['body'] = json.dumps("Success, message sent :)")

    else:
        out['statusCode'] = 400
        out['body'] = json.dumps("Queue or message payload were not passed :(")

    out['headers'] = {
        "content-type": "application-json"
    }

    return (out)
