
const express = require('express')
const app = express()
const port = 5001
const db = require('./db')
const User = require('./model/user')
const Match = require('./model/match')
require('dotenv').config();


app.use(express.json())
app.use(express.urlencoded({ extended: true }))

app.post('/user', (req, res) => {
  console.log(req.body)

  // We create a new document and then save it in database    
  const user = new User({
    username: req.body.username,
    summonerId: req.body.summonerId
  });

  // Save is asynchronous and can fail
  user.save(function(err) {
    if(err) {
      console.log(err)
      res.status(409).send('User already exists')
    } else {
      res.status(200).send('User added')
    }
  })
})

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

  console.log(process.env.RIOT_API_KEY)
})

var intervalId = setInterval(function() {
    updateData()
  }, 5000);

function updateData() {
  User.find({}, function(err, users) {
    if(err) {
      console.log(err)
    } else {
      users.forEach(function(user) {
        updateUser(user)
      })
    }
  });
}

function updateUser(user) {
  console.log(user)
}
