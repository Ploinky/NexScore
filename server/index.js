
const express = require('express')
const app = express()
const port = 5001
const db = require('./db')
const { v4: uuidv4} = require('uuid')
const https = require('https');
const User = require('./model/user')
const Match = require('./model/match')
const cors = require('cors')
require('dotenv').config();


app.use(express.json())
app.use(express.urlencoded({ extended: true }))

app.use(cors())

app.post('/user', (req, res) => {
  console.log(req.body)
  db.run('INSERT INTO User (id, username, server, region) VALUES (?, ?, ?, ?)',
  [uuidv4(), req.body.username, req.body.server, req.body.region],
  function(err) {
    if (err) {
      return console.log('Error inserting User: ' + err.message);
    }
    
    console.log('User (' + req.body.username + ', ' + req.body.server + ', ' + req.body.region +  ') created.')
    res.status(200).send('user created')
  });
})

app.get('/', (req, res) => {
  
  db.all('SELECT username, server, SUM(score) AS score, SUM(1) AS total FROM Match LEFT JOIN User USING(id) GROUP BY id, server ORDER BY score DESC', (err, rows) => {
    if(err) {
      console.log('Error fetching users: ' + err)
    } else {
      rows.forEach(function(user) {
        console.log(user)
      })
      res.status(200).send(rows)
    }
  })
})

app.put('/updateuser', (req, res) => {
  db.all('SELECT * FROM User WHERE username = ? AND server = ? and region = ?',
	[req.body.username, req.body.server, req.body.region], (err, rows) => {
    if(err) {
      console.log('Error updating user: ' + err)
    } else {
      rows.forEach(function(user) {
        updateUser(user)
      })
      res.status(200).send('User <' + req.body.username + ", " + req.body.server + ", " + req.body.region + "> was updated")
    }
  })
})

app.listen(port, () => {
  console.log(`Starting NexScore at http://localhost:${port}`)
})

var intervalId = setInterval(function() {
    updateData()
  }, 3600000);

function updateData() {
  db.all('SELECT * FROM User', (err, rows) => {
    if(err) {
      console.log('Error fetching users: ' + err)
    } else {
      rows.forEach(function(user) {
        updateUser(user)
      })
    }
  })
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
        db.run('UPDATE User SET puuid = ? WHERE id = ?', [data.puuid, user.id])
      })   
    }).on("error", (err) => {
      console.log("Error updating puuid of user <" + user.username + ">: " + err.message);
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
      
      if(data && Object.prototype.toString.call(data) === '[object Array]')
      {
        data.forEach((id) => {
          if(id) {
            db.run('INSERT OR IGNORE INTO Match(id, matchid) VALUES (?, ?)', [user.id, id], function(err) {
              if (err) {
                return console.log('Error inserting Match: ' + err.message);
              }
              
            })
          }
        })
      }
    })   
  }).on("error", (err) => {
    console.log("Error: " + err.message);
  })

  db.all('SELECT * FROM Match WHERE Score IS NULL AND id = ?', [user.id], (err, rows) => {
    if(err) {
      console.log('Error fetching matches without score for user <' + user.id + '>: ' + err)
    } else {
      rows.forEach(function(match) {

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
                  db.run('UPDATE Match SET score = ? WHERE id = ? AND matchid = ?', [p.nexusKills, user.id, match.matchid])
                  console.log('Update score on match <' + user.id + ", " + match.matchid + '>')
                }
              })
            }
          })   
        }).on("error", (err) => {
          console.log("Error: " + err.message);
        })
      })
    }
  })

}
