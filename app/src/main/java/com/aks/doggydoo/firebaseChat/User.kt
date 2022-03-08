package com.aks.doggydoo.firebaseChat

class User {
    var mobile: String?= null
    var profilePic: String?= null
    var uname: String?= null
    var userId:String? = null
    var uid:String? = null
    var userType: String? = null
    var chatStatus: String? = null
    var firebaseToken: String? = null
    var lastMsg: String? = null

    constructor(){}
    constructor(mobile: String, profilePic: String, uname: String, userId: String, uid: String,chatStatus: String?,
                firebaseToken: String?,lastMsg: String?){
        this.mobile = mobile
        this.profilePic = profilePic
        this.uname = uname
        this.userId = userId
        this.uid = uid
        this.userType= userType
        this.chatStatus = chatStatus
        this.firebaseToken = firebaseToken
        this.lastMsg = lastMsg
    }
}