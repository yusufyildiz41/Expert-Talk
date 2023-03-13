package com.yusufyildiz.experttalk.common

import com.yusufyildiz.experttalk.R
import com.yusufyildiz.experttalk.data.model.recycler_view.ExpertCategoriesRecyclerModel
import com.yusufyildiz.experttalk.data.model.recycler_view.ExpertRecyclerModel
import com.yusufyildiz.experttalk.data.model.recycler_view.ExpertWorkedRecyclerModel
import com.yusufyildiz.experttalk.data.model.recycler_view.IntroPageModel

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

    fun getExpertCategoriesData() = arrayListOf(
        ExpertCategoriesRecyclerModel(
            R.drawable.computer_engineer_image,
            "Mühendis"
        ),
        ExpertCategoriesRecyclerModel(
            R.drawable.dentist_image,
            "Diş Hekimi"
        ),
        ExpertCategoriesRecyclerModel(
            R.drawable.mechanic_image,
            "Tamirci"
        ),
        ExpertCategoriesRecyclerModel(
            R.drawable.architect_image,
            "Mimar"
        ),
        ExpertCategoriesRecyclerModel(
            R.drawable.calling_presenter_image,
            "Müşteri Temsilcisi"
        ),

    )

    fun getWorkedExpertData() = arrayListOf(
        ExpertWorkedRecyclerModel(
            R.drawable.engineer,
            "Yusuf Yıldız",
            "Mühendis",
            "3.5"
        ),
        ExpertWorkedRecyclerModel(
            R.drawable.doctor,
            "Ali Yıldız",
            "Doktor",
            "2.5"
        ),
        ExpertWorkedRecyclerModel(
            R.drawable.engineer,
            "Ahmet Yıldız",
            "Mühendis",
            "4.5"
        ),
        ExpertWorkedRecyclerModel(
            R.drawable.doctor,
            "Selim Yıldız",
            "Doktor",
            "2"
        )

    )

    fun getExpertData() = arrayListOf(
        ExpertRecyclerModel(
            R.drawable.doctor,
            "Yusuf",
            "95",
            "2"
        ),
        ExpertRecyclerModel(
            R.drawable.engineer,
            "Ali",
            "35",
            "5"
        ),
        ExpertRecyclerModel(
            R.drawable.doctor,
            "Sezer",
            "120",
            "4"
        ),
        ExpertRecyclerModel(
            R.drawable.engineer,
            "Kaan",
            "95",
            "2"
        ),
        ExpertRecyclerModel(
            R.drawable.doctor,
            "Mehmet",
            "55",
            "4"
        ),
        ExpertRecyclerModel(
            R.drawable.engineer,
            "Yusuf",
            "95",
            "2"
        )
    )

}