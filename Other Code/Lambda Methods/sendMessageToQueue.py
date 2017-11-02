import json
import boto3

def lambda_handler(event, context):

    sqs = boto3.resource('sqs')
    event['queryStringParameters']['queueName']
    queue = sqs.create_queue(QueueName = event['queryStringParameters']['queueName'])
    response = queue.send_message(MessageBody = event['queryStringParameters']['message'])
    
    out = {}
    out['statusCode'] = 200
    out['body'] = "Success"
    out['headers']= {
		"content-type": "application-json"
	}
        
    return(out)