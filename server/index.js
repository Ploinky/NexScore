
const express = require('express')
const app = express()
const port = 5001
const db = require('./db')
const https = require('https');
const User = require('./model/user')
const Match = require('./model/match')
const cors = require('cors')
require('dotenv').config();

const fs = require('fs')

const options = {
    key: fs.readFileSync(process.env.KEY_FILE),
    cert: fs.readFileSync(process.env.CHAIN_FILE)
}

app.use(express.json())
app.use(express.urlencoded({ extended: true }))

app.use(cors())

app.post('/user', (req, res) => {

  db.all('SELECT * FROM User  WHERE username = ? AND server = ? AND region = ?',
  [req.body.username, req.body.server, req.body.region],
  (err, rows) => {
    if(err) {
      console.log('Error fetching users: ' + err)
    } else {
      if(rows.length > 0) {
        updateUser(rows[0])
	res.status(200).send('user updated')
      } else {
        db.run('INSERT INTO User (username, server, region) VALUES (?, ?, ?)',
        [req.body.username, req.body.server, req.body.region],
        function(err) {
          if (err) {
            return console.log('Error inserting User: ' + err.message);
          }
          
          console.log('User (' + req.body.username + ', ' + req.body.server + ', ' + req.body.region +  ') created.')
          res.status(200).send('user created')
        });
      }
    }
  })
})

app.get('/', (req, res) => {
  
  db.all('SELECT username, server, IFNULL(SUM(score), 0) AS score, SUM(1) AS total, IFNULL(CAST(CAST(SUM(score) AS REAL) / SUM(1) * 100 AS INTEGER), 0) AS pct FROM Match LEFT JOIN User USING(username, server, region) GROUP BY username, server, region ORDER BY score DESC', (err, rows) => {
    if(err) {
      console.log('Error fetching users: ' + err)
    } else {
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

var intervalId = setInterval(function() {
    updateData()
  }, 60000);

function updateData() {
  db.all('SELECT * FROM User WHERE lastupdate IS NULL OR lastupdate < strftime("%s", "now") - 3600', (err, rows) => {
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
        db.run('UPDATE User SET puuid = ? WHERE username = ? AND server = ? AND region = ?', [data.puuid, user.username, user.server, user.region])
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
  
    resp.on('data', (chunk) => {
      data += chunk;
    })
    
    resp.on('end', () => {
      data = JSON.parse(data)
      
      if(data && Object.prototype.toString.call(data) === '[object Array]')
      {
        data.forEach((id) => {
          if(id) {
            db.run('INSERT OR IGNORE INTO Match(username, server, region, matchid) VALUES (?, ?, ?, ?)', [user.username, user.server, user.region, id], function(err) {
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

  db.all('SELECT * FROM Match WHERE Score IS NULL AND username = ? AND server = ? AND region = ?', [user.username, user.server, user.region], (err, rows) => {
    if(err) {
      console.log('Error fetching matches without score for user <' + user.username + '>: ' + err)
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
                  db.run('UPDATE Match SET score = ? WHERE username = ? AND server = ? AND region = ? AND matchid = ?',
                  [p.nexusKills, user.username, user.server, user.region, match.matchid])
                  db.run("UPDATE User SET lastupdate = strftime('%s', 'now') WHERE username = ? AND server = ? AND region = ?", [user.username, user.server, user.region])
                  console.log('Update score on match <' + user.username + ", " + match.matchid + '>')
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

https.createServer(options, app).listen(port, () => {
  console.log(`Starting NexScore at https://localhost:${port}`)
})
