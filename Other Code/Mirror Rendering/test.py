import json
import boto3


def lambda_handler(event, context):
    sqs = boto3.resource('sqs')
    sqs.send_message(QueueUrl = event['queryStringParameters']['queueurl'], MessageBody = '{{"job":"a_job","data":{"some":"variable"}}', MessageGroupId = '586474de88e03')

    out = {}
    out['statusCode'] = 200
    out['body'] = "Success"
    out['headers'] = {
        "content-type": "application-json"
    }

    return (out)