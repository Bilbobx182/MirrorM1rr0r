import json
import boto3


def lambda_handler(event, context):
    out = {}
    out['statusCode'] = 200
    out['headers'] = {
        "content-type": "application-json"
    }

    try:
        sqs = boto3.resource('sqs')
        queue = sqs.get_queue_by_name(QueueName=event['queryStringParameters']['queuename'])
        out['body'] = queue.attributes.get('ApproximateNumberOfMessages')
    except:
        isEmpty = True
        out['statusCode'] = 400
        out['body'] = "It messed up, you probably gave it wrong input. Sample correct  queueName=ciaranVis.fifo"
    return (out)
