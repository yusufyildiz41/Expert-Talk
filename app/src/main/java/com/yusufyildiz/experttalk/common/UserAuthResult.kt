package com.yusufyildiz.experttalk.common

sealed class UserAuthResult<T>(val data: T? = null){
    class UserAuthorized<T>(data: T? = null): UserAuthResult<T>(data)
    class UserUnAuthorized<T>: UserAuthResult<T>()
    class UserUnknownError<T>: UserAuthResult<T>()
}