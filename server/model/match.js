class Match {
    constructor(dao) {
        this.dao = dao
    }

    createTable() {
        const sql = `CREATE TABLE IF NOT EXISTS Match(
            id String not null,
            matchid String not null,
            score Boolean,
            FOREIGN KEY(id) REFERENCES User(id  )
            PRIMARY KEY(id, matchid)
            )`

        return this.dao.run(sql)
    }
}

module.exports = Match