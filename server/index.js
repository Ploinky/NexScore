
const express = require('express')
const app = express()
const port = 5001
const db = require('./db')
const User = require('./model/user')
const Match = require('./model/match')
const https = require('https');
require('dotenv').config();


app.use(express.json())
app.use(express.urlencoded({ extended: true }))

app.post('/user', (req, res) => {
  console.log(req.body)

  // We create a new document and then save it in database    
  const user = new User({
    username: req.body.username,
    server: req.body.server
  });

  // Save is asynchronous and can fail
  user.save(function(err) {
    if(err) {
      console.log(err)

      if(err.code === 11000) {
        res.status(409).send('User already exists')
      } else {
        res.status(500).send(err)
      }
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
  if(!user.summonerId)
  {
    var options = {
      headers: { 'X-Riot-Token': process.env.RIOT_API_KEY },
      host: user.server + '.api.riotgames.com',
      path: '/lol/summoner/v4/summoners/by-name/' + user.username 
    }

    https.get(options, (resp) => {
      let data = '';
    
      // A chunk of data has been received.
      resp.on('data', (chunk) => {
        data += chunk;
      })
      
      resp.on('end', () => {
        data = JSON.parse(data)
        user.summonerId = data.id
        user.save()
      })   
    }).on("error", (err) => {
      console.log("Error: " + err.message);
    });
  }
}
