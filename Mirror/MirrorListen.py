import boto3

sqs = boto3.resource('sqs', region_name='us-west-2',
                     aws_access_key_id='AKIAIDMELKX2YW6VOJ7Q',
                     aws_secret_access_key='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')
# Create the queue. This returns an SQS.Queue instance
queue = sqs.create_queue(QueueName='test', Attributes={'DelaySeconds': '5'})

# You can now access identifiers and attributes
print(queue.url)
print(queue.attributes.get('DelaySeconds'))

response = queue.send_message(MessageBody='I AM A REAL ONE')

# The response is NOT a resource, but gives you a message ID and MD5
print(response.get('MessageId'))
print(response.get('MD5OfMessageBody'))

while True:
    for message in queue.receive_messages(MessageAttributeNames=['Author']):
        author_text = ''
        if message is not None:
            print(message.body)
            # Let the queue know that the message is processed
        message.delete()
