import boto3
import json


def sendMessage(queueURL, message):
    sqs = boto3.client('sqs', region_name='eu-west-1')

    response = sqs.send_message(QueueUrl=queueURL,
                                MessageBody=json.dumps(message),
                                MessageGroupId="ID1182")


def lambda_handler(event):
    out = {}
    if 'message' in event['queryStringParameters']:
        messagePayload = (event['queryStringParameters']['message'])
        if 'queueurl' in event['queryStringParameters']:
            queueURL = (event['queryStringParameters']['queueurl'])

        message = {'messagePayload': messagePayload}

        if ('fontsize' in event['queryStringParameters']):
            fontSize = (event['queryStringParameters']['fontsize'])
            message['fontSize'] = fontSize

        if 'fontcolour' in event['queryStringParameters']:
            fontColour = (event['queryStringParameters']['fontcolour'])
            message['fontColour'] = fontColour

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


sample = {
    "queryStringParameters": {
        "queueurl": "https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo",
        "message": "Ciaran Is Cool",
        "fontcolour": "#008080",
        "fontsize": "12"
    }
}

print(lambda_handler(sample))
