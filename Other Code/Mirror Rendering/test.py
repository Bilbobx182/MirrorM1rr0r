import json
import boto3

print('Loading function')


def lambda_handler(event, context):
    out = {}
    out['statusCode'] = 200
    out['headers'] = {
        "content-type": "application-json"
    }

    try:
        sqs = boto3.resource('sqs')
        result = sqs.create_queue(QueueName=event['queryStringParameters']['queueName'] + '.fifo',
                                  Attributes={
                                      'FifoQueue': 'true',
                                      'ContentBasedDeduplication': 'true',
                                      'VisibilityTimeout': str(43200)
                                  })
        out['body'] = result.url
    except:
        isEmpty = True
        out['statusCode'] = 404
        out['body'] = "Parameter 'queueName' was not passed"

    return (out)
