import json

import boto3

print('Loading function')


def lambda_handler(event, context):
    out = {}
    out['statusCode'] = 200
    out['headers'] = {
        "content-type": "application-json"
    }

    count = {}

    try:
        sqs = boto3.resource('sqs')
        queueName = (event['queryStringParameters']['queueurl']).split("/")[4]
        queue = sqs.get_queue_by_name(QueueName=queueName)
        count['result'] = queue.attributes.get('ApproximateNumberOfMessages')
        out['body'] = json.dumps(count)

    except:
        out['statusCode'] = 400
        count['result'] = "no queuename param supplied"
        out['body'] = json.dumps(count)
    return (out)
