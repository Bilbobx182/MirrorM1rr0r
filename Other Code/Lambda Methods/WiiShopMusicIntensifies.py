def getInformationFromQueue(self):
    url = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/getmessage?queue=https://sqs.eu-west-1.amazonaws.com/186314837751/hello&count=1"
    lm_json = requests.get(url).json()
    duct = lm_json['0']['Contents']
    print(json.loads(duct['body']))
    duct = json.loads(duct['body'])

    contents = []
    for key, value in duct.items():
        item = {}
        item['Message'] = value['Message']
        item['Timestamp'] = int(value['SentTimestamp'])
        contents.append(item);

    return contents.sort(key=operator.itemgetter('Message', 'Timestamp'))
