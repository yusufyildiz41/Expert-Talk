package com.yusufyildiz.experttalk.common

sealed class ExpertAuthResult<T>(val data: T? = null){
    class ExpertAuthorized<T>(data: T? = null): ExpertAuthResult<T>(data)
    class ExpertUnAuthorized<T>: ExpertAuthResult<T>()
    class ExpertUnknownError<T>: ExpertAuthResult<T>()
}