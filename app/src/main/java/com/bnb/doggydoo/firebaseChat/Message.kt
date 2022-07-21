package com.bnb.doggydoo.firebaseChat

class Message {
    var message: String? = null
    var senderId: String? = null
    var messageType: String? = null
    var imageMessage: String? = null

    constructor(){}
    constructor(message:String?, senderId:String?, messageType: String?,imageMessage: String?){
        this.message = message
        this.senderId = senderId
        this. messageType = messageType
        this.imageMessage = imageMessage
    }
}