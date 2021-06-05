
const express = require('express')
const app = express()
const port = 5001

app.get('/', (req, res) => {
  res.send('Hello World!')
})

app.listen(port, () => {
  console.log(`Starting NexScore at http://localhost:${port}`)
})

var intervalId = setInterval(function() {
    console.log("Interval reached every 5s")
  }, 5000);
