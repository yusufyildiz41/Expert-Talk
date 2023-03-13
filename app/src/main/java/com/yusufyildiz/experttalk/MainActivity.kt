package com.yusufyildiz.experttalk

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.yusufyildiz.experttalk.common.UserAuthResult
import com.yusufyildiz.experttalk.databinding.ActivityMainBinding
import com.yusufyildiz.experttalk.ui.profile.expert.ExpertProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences("STATE", Context.MODE_PRIVATE)


        supportActionBar?.hide()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.findNavController()

        NavigationUI.setupWithNavController(
            binding.bottomNavUser,
            navController
        )

        binding.bottomNavUser.setOnItemSelectedListener {
            val state= sharedPreferences.getString("state","").toString()
            showToast("durum : $state")
            when (it.itemId) {
                R.id.homeFragment -> navController.navigate(R.id.homeFragment)
                R.id.messagesFragment -> {
                    when(state){
                        "user"-> {
                            navController.navigate(R.id.messagesFragment)
                        }
                        "expert"-> {
                            navController.navigate(R.id.expertMessageListFragment)
                        }
                    }


                }
                R.id.meetListFragment -> navController.navigate(R.id.meetListFragment)
                R.id.userProfileFragment -> {
                    when(state){
                        "user"-> {
                            navController.navigate(R.id.userProfileFragment)
                        }
                        "expert"-> {
                            navController.navigate(R.id.expertProfileFragment)
                        }
                    }
                }
            }
            true
        }


        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.signInFragment,
                R.id.signUpFragment,
                R.id.expertSignInFragment,
                R.id.signUpExpertFragment,
                R.id.introFragment,
                R.id.expertMessagesFragment,
                R.id.userMessagesFragment,
                R.id.expertSearchFragment,
                R.id.expertDetailFragment,
                R.id.appointmentFragment-> {
                    binding.bottomNavUser.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavUser.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navController.navigateUp()
    }

    enum class UserType{
        USER,
        EXPERT
    }



}