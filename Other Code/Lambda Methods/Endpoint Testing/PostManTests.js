//-------------------------------------------------
// CREATING QUEUE
//-------------------------------------------------

pm.test("startOfQueueURL", function () {
    pm.expect(pm.response.text()).to.include("https://eu-west-1.queue.amazonaws.com");
});

pm.test("incorrectParameterResponse", function () {
    pm.expect(pm.response.text()).to.include("Parameter 'queueName' was not passed");
});

pm.test("incorrectParameterStatus404", function () {
    pm.response.to.have.status(404);
});

//-------------------------------------------------
// SENDING MESSAGE
//-------------------------------------------------

pm.test("Message Contents True", function () {
    pm.expect(pm.response.text()).to.include("Contents");
});

pm.test("MessagePayloadTrue", function () {
    pm.expect(pm.response.text()).to.include("messagePayload");
});

pm.test("noPostQueueSent", function () {
    pm.expect(pm.response.text()).to.include("POST takes queueurl : your-queue-here, it wasn't present");
});

pm.test("noPostDataSent", function () {
    pm.expect(pm.response.text()).to.include("POST Takes data {...} it wasn't present");
});

//-------------------------------------------------
// GET MESSAGE
//-------------------------------------------------

pm.test("messageContentsTrue", function () {
    pm.expect(pm.response.text()).to.include("Contents");
});

pm.test("messagePayloadTrue", function () {
    pm.expect(pm.response.text()).to.include("messagePayload");
});

//-------------------------------------------------
// GET MESSAGE COUNT
//-------------------------------------------------

pm.test("countResult", function () {
    pm.expect(pm.response.text()).to.include("result");
});

pm.test("countResultPositive", function () {
    var data = pm.response.json();
    pm.expect(parseInt(data.result)).to.be.above(-1);
});

pm.test("countResultErrorMessage", function () {
    var data = pm.response.json();
    pm.expect(data.result).to.include("no queuename param supplied");
});
