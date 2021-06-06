class User {
    constructor(dao) {
        this.dao = dao
    }

    createTable() {
        const sql = `CREATE TABLE IF NOT EXISTS User
            id String not null,
            puuid String,
            username String,
            server String,
            region String
        )`
    }
}

module.exports = User