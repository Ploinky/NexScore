const { rejects } = require('assert/strict');
const sqlite = require('sqlite');

class DbConn {
    constructor() {
        this.db = new sqlite.Database('/home/joba/sqlite/nexscore.db', (err) => {
            if(err) {
                return console.log("Could not connect to sqlite database: " + err)
            }
            
            return console.log("Connected to sqlite database")
        })
    }

    run(sql, params = []) {
        return new Promise((resolve, reject) => {
            this.db.run(sql, params, function(err) {
                if(err) {
                    console.log('Error running sql: ' + sql)
                    console.log(err)
                    reject(err)
                } else {
                    resolve({id: this.lastId})
                }
            })
        })
    }
}

module.exports = DbConn