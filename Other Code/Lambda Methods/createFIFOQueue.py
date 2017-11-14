import json
import boto3

print('Loading function')


def lambda_handler(event, context):
    sqs = boto3.resource('sqs')
    result = sqs.create_queue(QueueName=event['queryStringParameters']['queueName'] + '.fifo',
                              Attributes={
                                  'FifoQueue': 'true',
                                  'ContentBasedDeduplication': 'true' })
    out = {}
    out['statusCode'] = 200
    out['body'] = result.url
    out['headers'] = {
        "content-type": "application-json"
    }

    return (out)