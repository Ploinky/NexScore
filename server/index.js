
const express = require('express')
const app = express()
const port = 5001
const db = require('./db')
const { v4: uuidv4} = require('uuid')
const https = require('https');
const User = require('./model/user')
const Match = require('./model/match')
require('dotenv').config();


app.use(express.json())
app.use(express.urlencoded({ extended: true }))

app.post('/user', (req, res) => {
  db.run('INSERT INTO User VALUES(id, username, server, region)',
  [uuidv4(), req.body.username, req.body.server, req.body.region],
  function(err) {
    if (err) {
      return console.log(err.message);
    }
    
    console.log(`A row has been inserted`)
    res.status(200).send('user created')
  });
})

app.get('/', (req, res) => {
  res.send('Hello World!')
})

app.get('/all', (req, res) => {
  Match.aggregate([{'$match': {'score': true }},
  { '$group': { _id: '$puuid', count: { $sum: 1 }}}])
  .then(res => {
    console.log(res)
  })
  res.send('Hello world')
})

app.listen(port, () => {
  console.log(`Starting NexScore at http://localhost:${port}`)
})

var intervalId = setInterval(function() {
    updateData()
  }, 50000);

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
  if(!user.puuid)
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
        user.puuid = data.puuid
        user.save()
      })   
    }).on("error", (err) => {
      console.log("Error: " + err.message);
    });

    return;
  }

  var options = {
    headers: { 'X-Riot-Token': process.env.RIOT_API_KEY },
    host: user.region + '.api.riotgames.com',
    path: '/lol/match/v5/matches/by-puuid/' + user.puuid + '/ids'
  }

  https.get(options, (resp) => {
    let data = '';
  
    // A chunk of data has been received.
    resp.on('data', (chunk) => {
      data += chunk;
    })
    
    resp.on('end', () => {
      data = JSON.parse(data)
      
      data.forEach((id) => {
        const match = new Match({
          puuid: user.puuid,
          region: user.region,
          matchid: id
        })

        Match.countDocuments({puuid: match.puuid, region: match.region, matchid: match.matchid},
          function (err, count){ 
            if(count == 0){
              match.save()
            }
        })
      })
    })   
  }).on("error", (err) => {
    console.log("Error: " + err.message);
  })

  Match.find({ puuid: user.puuid, region: user.region },
    function(err, matches) {
      if(err) {
        console.log(err)
      } else {
        matches.forEach((match) => {
          if(!match.score) {

            options = {
              headers: { 'X-Riot-Token': process.env.RIOT_API_KEY },
              host: user.region + '.api.riotgames.com',
              path: '/lol/match/v5/matches/' + match.matchid
            }

            https.get(options, (resp) => {
              let data = '';
            
              // A chunk of data has been received.
              resp.on('data', (chunk) => {
                data += chunk;
              })
              
              resp.on('end', () => {
                data = JSON.parse(data)
                if(!data.info) {
                  console.log(data)
                } else {
                  data.info.participants.forEach((p) => {
                    if(p.puuid === user.puuid) {
                      match.score = p.nexusKills
                      match.save()
                    }
                  })
                }
              })   
            }).on("error", (err) => {
              console.log("Error: " + err.message);
            })


          }
        })
      }
    })
}
