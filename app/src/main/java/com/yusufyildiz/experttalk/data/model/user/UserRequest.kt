package com.yusufyildiz.experttalk.data.model.user

data class UserRequest(
    var email: String?=null,
    var username: String?=null,
    var password: String?=null,
    var phone: String?=null,
    var _id: UserId?= UserId()
)

data class TransformedUserRequest(
    var email: String?=null,
    var password: String?=null
)

data class UserId(
    var `$oid`: String?=null
)
