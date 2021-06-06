const mongoose = require('mongoose')

const userSchema = new mongoose.Schema({
    username: { type: 'string', unique: true },
    summonerId: 'string'
})

module.exports = mongoose.model('User', userSchema)