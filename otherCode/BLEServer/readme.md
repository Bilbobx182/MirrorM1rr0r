This is the BLE server, pretty simple.
It's made using BLENO. I used the BLENO test.js as the sample for this server.

What it does is it takes in inputs, Android sends the information to the server in chunks of 20byes(SMDH).
So we can't just easily save everything in one go. Chunking logic is used, so that when the client disconnects the file gets saved as of course, no more information will get sent.

I deleted the contents of node modules. Since Windows and GIT don't like really long file names.
So I'd advise trying to do NPM install _before_ running anything.

- Ciaran.