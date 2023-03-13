package com.yusufyildiz.experttalk.data.model.expert

import com.yusufyildiz.experttalk.data.model.category_model.CategoryItems

data class ExpertRequest(
    var email: String?=null,
    var username: String?=null,
    var password: String?=null,
    var phone: String?=null,
    var expertCategory: String?=null,
    var expertId: String?=null,
    var expertTime: String?=null,
    var expertPrice: String?=null,
    var about: String?=null,
    var longAbout: String?=null,
    var expertCategoryDetail: ArrayList<CategoryItems>?=null,
    var _Expert_id: ExpertId?= ExpertId()
)

data class TransformedExpertRequest(
    var email: String?=null,
    var password: String?=null
)

data class ExpertId(
    var `$oid`: String?=null
)