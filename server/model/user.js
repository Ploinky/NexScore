const mongoose = require('mongoose')

const userSchema = new mongoose.Schema({
    username: String,
    server: String,
    summonerId: String
})

userSchema.index({ username: 1, server: 1}, { unique: true });

module.exports = mongoose.model('User', userSchema)