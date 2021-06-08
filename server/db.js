
const sqlite3 = require('sqlite3')
require('dotenv').config();

let db = new sqlite3.Database(process.env.DB_FILE.toString(), (err) => {
    if(err) {
        console.log("Could not connect to sqlite database: " + err)
        throw err
    }

    console.log("Connected to sqlite database")

    db.run(`CREATE TABLE IF NOT EXISTS User (
        username String not null, 
        server String not null,
        region String not null
        puuid String,
        PRIMARY KEY (username, server, region)
        )`,
        (err) => {
            if (err) {
                console.log('Error creating table User! ' + err)
            } else {
                console.log('Table User ok.')
            }
        }
    )

    db.run(`CREATE TABLE IF NOT EXISTS Match (
        username String not null,
        server String not null,
        region String not null,
        matchid String not null,
        score Boolean,
        PRIMARY KEY (username, server, region, matchid)
        )`,
        (err) => {
            if (err) {
                console.log('Error creating table Match!' + err)
            } else {
                console.log('Table Match ok.')
            }
        }
    )
})



module.exports = db