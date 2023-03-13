package com.yusufyildiz.experttalk.ui.signup.expert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufyildiz.experttalk.common.ExpertAuthResult
import com.yusufyildiz.experttalk.data.model.category_model.CategoryItems
import com.yusufyildiz.experttalk.domain.repository.auth.ExpertRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpExpertViewModel @Inject constructor(
    private val expertRepository: ExpertRepository
): ViewModel() {

    private val resultChannel = Channel<ExpertAuthResult<Unit>>()
    val authResult = resultChannel.receiveAsFlow()

    fun expertSignUp(
        email: String,
        username: String,
        password: String,
        phone: String,
        expertCategory: String,
        expertId: String,
        expertTime: String,
        expertPrice: String,
        about: String,
        longAbout: String,
        expertCategoryDetail: ArrayList<CategoryItems>
    ){
        viewModelScope.launch {
            val expertSignUpResult = expertRepository.signUp(
                email = email,
                username = username,
                password = password,
                phone = phone,
                expertCategory = expertCategory,
                expertId = expertId,
                expertTime = expertTime,
                expertPrice = expertPrice,
                about = about,
                longAbout = longAbout,
                expertCategoryDetail = expertCategoryDetail
            )
            resultChannel.send(expertSignUpResult)
        }
    }

}