import boto3

# Create SQS client
sqs = boto3.client('sqs', aws_access_key_id='YO',
                   aws_secret_access_key='YO', region_name='us-west-2')

queue_url = 'https://sqs.us-west-2.amazonaws.com/186314837751/CiaranTest'
while (True):
    response = sqs.receive_message(
        QueueUrl=queue_url,
        AttributeNames=[
            'SentTimestamp'
        ],
        MaxNumberOfMessages=1,
        MessageAttributeNames=[
            'All'
        ],
        VisibilityTimeout=0,
        WaitTimeSeconds=0
    )

    message = response['Messages'][0]
    receipt_handle = message['ReceiptHandle']

    # Delete received message from queue
    sqs.delete_message(
        QueueUrl=queue_url,
        ReceiptHandle=receipt_handle
    )
    # TODO This won't work long run? Why? Because of that SQS client above. That's not great fool. What would be great was if we just passed the QUEUE to the pi. Then it was able to call a "Get Next message" resource on the API giving it the queue.
    # TODO CONTINUED ABOVE: This way. We still keep all the components dumb. I Could return message + MESSAGESLEFT IN QUEUE. that way it could loop around if I needed it to.
    # I will keep this here Anyway because it worked afaik.
    print('Received and deleted message: %s' % message)
