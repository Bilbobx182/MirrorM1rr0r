import json
import boto3


def lambda_handler(event, context):
    sqs = boto3.client('sqs')
    response = sqs.send_message(QueueUrl=event['queryStringParameters']['queueurl'],
                                MessageBody=event['queryStringParameters']['message'], MessageAttributes={
            'MessageGroupId': {
                "id": "somethingId"}
        })

    out = {}
    out['statusCode'] = 200
    out['body'] = "Success"
    out['headers'] = {
        "content-type": "application-json"
    }

    return (out)
