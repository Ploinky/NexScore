const mongoose = require('mongoose')

const matchSchema = new mongoose.Schema({
    username: String,
    matchId: String,
    score: Boolean
})

module.exports = mongoose.model('Match', matchSchema)