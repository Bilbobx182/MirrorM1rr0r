import json
import boto3


def lambda_handler(event, context):
    sqs = boto3.client('sqs')
    queue = ''
    message = ''
    colour = ''
    size = ''
    location = ''

    # Have a method to clean the variables ie recursive try and catch
    response = sqs.send_message(QueueUrl=event['queryStringParameters']['queueurl'],
                                MessageBody=event['queryStringParameters']['message'],
                                MessageGroupId="1182")

    out = {}
    out['statusCode'] = 200
    out['body'] = "Success"
    out['headers'] = {
        "content-type": "application-json"
    }

    return (out)


import json
import boto3


def lambda_handler(event, context):
    sqs = boto3.client('sqs')
    queue = ''
    message = ''
    colour = ''
    size = ''
    location = ''

    message = {}
    message['contents'] = MessageBody = event['queryStringParameters']['message']

    # Have a method to clean the variables ie recursive try and catch
    response = sqs.send_message(QueueUrl=event['queryStringParameters']['queueurl'],
                                MessageBody = str(message),
                                MessageGroupId="1182")

    out = {}
    out['statusCode'] = 200
    out['body'] = "Success"
    out['headers'] = {
        "content-type": "application-json"
    }

    return (out)
