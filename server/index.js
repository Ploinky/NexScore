
const express = require('express')
const app = express()
const port = 5001
const db = require('./db')

app.get('/', (req, res) => {
  res.send('Hello World!')
})

app.listen(port, () => {
    console.log(`Starting NexScore at http://localhost:${port}`)
    const MONGO_HOSTNAME = 'localhost'// process.env.MONGO_HOSTNAME;
    const MONGO_PORT = 27017 // process.env.MONGO_PORT;
    const MONGO_DB = 'nexscore' // process.env.MONGO_DB;
    const DB_URL = 'mongodb://' + MONGO_HOSTNAME + ':' + MONGO_PORT + '/' + MONGO_DB
    db.connect(DB_URL)
    console.log('Connected to mongodb at ' + MONGO_HOSTNAME)
})

var intervalId = setInterval(function() {
    updateData()
  }, 5000);

function updateData() {
    console.log("Interval reached every 5s")
}

