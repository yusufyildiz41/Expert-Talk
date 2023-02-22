package com.yusufyildiz.experttalk.common

import com.yusufyildiz.experttalk.R
import com.yusufyildiz.experttalk.data.model.IntroPageModel

object RecyclerViewItems {

    fun getIntroPageData() = arrayListOf(
        IntroPageModel(
            R.drawable.intro_page_first,
            "Find Trusted Professional",
            "Type the meet of the specialist you want to meet or the field of specialization in the search box."
        ),
        IntroPageModel(
            R.drawable.intro_page_second,
            "Choose Best Professional",
            "You can look at their command to find the best one, or you can take advantage of the information in their profiles."
        ),
        IntroPageModel(
            R.drawable.intro_page_third,
            "Call Now Or Make An Appointment",
            "If your chosen professional is online, you can call right away. Or you can make an appointment for later."
        )
    )
}