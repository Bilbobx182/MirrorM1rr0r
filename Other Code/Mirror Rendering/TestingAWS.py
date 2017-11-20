import boto3
import json


def sendMessage(queueURL, message):
    sqs = boto3.client('sqs', region_name='eu-west-1',
                       aws_access_key_id='AKIAIDMELKX2YW6VOJ7Q',
                       aws_secret_access_key='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')

    # Have a method to clean the variables ie recursive try and catch
    response = sqs.send_message(QueueUrl=queueURL,
                                MessageBody=json.dumps(message),
                                MessageGroupId="ID1182")


def lambda_handler(event):
    if 'messagePayload' in event['body']:
        messagePayload = (event['body']['messagePayload'])
        if 'queueURL' in event['body']:
            queueURL = (event['body']['queueURL'])

        message = {'messagePayload': messagePayload}

        if ('fontSize' in event['body']):
            fontSize = (event['body']['fontSize'])
            message['fontSize'] = fontSize

        if 'fontColour' in event['body']:
            fontColour = (event['body']['fontColour'])
            message['fontColour'] = fontColour

        sendMessage(queueURL, message)

        # Optional parameters



    else:
        # status 404
        # body
        body = "Queue or message payload were not passed :("

    return (json.dumps(message))


sample = {
    "body": {
        "queueURL": "https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo",
        "messagePayload": "blink182",
        "fontSize": " ",
        "fontColour": " "
    }
}

print(lambda_handler(sample))
