package com.yusufyildiz.experttalk.ui.signup.expert

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.yusufyildiz.experttalk.R
import com.yusufyildiz.experttalk.common.ExpertCategories
import com.yusufyildiz.experttalk.common.Utils
import com.yusufyildiz.experttalk.common.ExpertAuthResult
import com.yusufyildiz.experttalk.data.model.category_model.CategoryItems
import com.yusufyildiz.experttalk.databinding.CoachItemsLayoutBinding
import com.yusufyildiz.experttalk.databinding.ECommerceItemsBinding
import com.yusufyildiz.experttalk.databinding.FileNameAlertDialogBinding
import com.yusufyildiz.experttalk.databinding.FragmentSignUpExpertBinding
import com.yusufyildiz.experttalk.databinding.LanguagePracticeItemsBinding
import com.yusufyildiz.experttalk.databinding.LawyerItemsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler

@AndroidEntryPoint
class SignUpExpertFragment : Fragment() {

    private lateinit var binding: FragmentSignUpExpertBinding
    private lateinit var customAlertDialogBinding: FileNameAlertDialogBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private val signUpExpertViewModel : SignUpExpertViewModel by viewModels()
    private var fileListRecyclerAdapter = FileNamesRecyclerAdapter(arrayListOf())
    private var fileNameList = arrayListOf<String>()
    private var lawyerCheckedBoxItems = arrayListOf<CheckBox>()
    private var uri: Uri?=null
    private var checkedItems: Array<String>?=null
    private var categoryItems = arrayListOf<CategoryItems>()
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        print("error messsage ->  ${throwable.localizedMessage}")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignUpExpertBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fileNameList.clear()
        permissionRequest()
        val permissionLauncherShouldRequest = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){

        }

        with(binding){
            val categoryAdapterItems = ArrayAdapter(requireContext(),R.layout.category_list_item,getCategoryItems())
            categorySelectText.setAdapter(categoryAdapterItems)

            categorySelectText.setOnItemClickListener { adapterView, view, i, l ->
                when(adapterView.getItemAtPosition(i)){
                    "Avukat"-> {
                        categoryItems.clear()
                        lawyerCheckedBoxItems.clear()
                        subCategoryView.removeAllViews()
                        val binding = LawyerItemsBinding.inflate(layoutInflater)
                        val view = binding.root

                        subCategoryView.addView(view).also {
                            uploadFileButton.visibility = View.VISIBLE
                        }
                        checkedLawyerItems(binding)

                    }
                    "Dil Pratiği" -> {
                        categoryItems.clear()
                        lawyerCheckedBoxItems.clear()
                        subCategoryView.removeAllViews()
                        val binding = LanguagePracticeItemsBinding.inflate(layoutInflater)
                        val view = binding.root
                        subCategoryView.addView(view).also {
                            uploadFileButton.visibility = View.VISIBLE
                        }
                        checkedLanguagePracticeItems(binding)
                    }
                    "Yaşam Koçu" -> {
                        categoryItems.clear()
                        lawyerCheckedBoxItems.clear()
                        subCategoryView.removeAllViews()
                        val binding = CoachItemsLayoutBinding.inflate(layoutInflater)
                        val view = binding.root
                        subCategoryView.addView(view).also {
                            uploadFileButton.visibility = View.VISIBLE
                        }
                        checkedCoachItems(binding)
                    }
                    "E-Ticaret Uzmanı"->{
                        categoryItems.clear()
                        lawyerCheckedBoxItems.clear()
                        subCategoryView.removeAllViews()
                        val binding = ECommerceItemsBinding.inflate(layoutInflater)
                        val view = binding.root
                        subCategoryView.addView(view).also {
                            uploadFileButton.visibility = View.VISIBLE
                        }
                        checkedEcommerceItems(binding)
                    } else -> {
                        subCategoryView.removeAllViews().also {
                            uploadFileButton.visibility = View.VISIBLE
                        }
                    }
                }
            }
            uploadFileButton.setOnClickListener {

                for(items in categoryItems){
                    println("items : ${items.categoryItem.toString()}")
                }

                if(ContextCompat.checkSelfPermission(
                        requireContext(),
                        Utils.permissions[2]
                ) != PackageManager.PERMISSION_GRANTED){
                    shouldShowRationale(permissionLauncherShouldRequest)
                } else {
                    // permission granted

                    startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "*/*"
                        putExtra(Intent.EXTRA_MIME_TYPES,arrayOf(
                            "application/pdf",
                            "text/plain"
                        ))
                    },2)

                    if(fileNameList.size >= 10){
                        uploadFileButton.isEnabled = false
                        showToast("Max 10 dosya yükleyebilirsiniz")
                    }
                }
            }
            signUpExpertButton.setOnClickListener {
                val username = signUpExpertUserNameEditText.text.toString()
                val email = signUpExpertEmailEditText.text.toString()
                val password = signUpExpertPasswordEditText.text.toString()
                val passwordAgain = signUpExpertPasswordAgainEditText.text.toString()
                val phone = signUpExpertPhoneEditText.text.toString()
                val expertId = signUpExpertIdText.text.toString()
                val time = signUpExpertMinuteText.text.toString()
                val price= signUpExpertPriceText.text.toString()
                val category = categorySelectText.text.toString()
                val about = signUpExpertAboutText.text.toString()
                val longAbout = signUpExpertLongAboutText.text.toString()
                val categoryList = categoryItems

                if(checkFields(
                        username = username,
                        phone = phone,
                        email = email,
                        password = password,
                        passwordAgain = passwordAgain,
                        id = expertId,
                        time = time,
                        price = price,
                        about = about,
                        longAbout = longAbout,
                        category = category,
                        categoryDetails = categoryList
                )){
                    if(password.trim() != passwordAgain.trim()){
                        showToast("Şifreler uyuşmuyor")
                    } else {
                        signUpExpertViewModel.expertSignUp(
                            email = email,
                            username = username,
                            password = password,
                            phone = phone,
                            expertCategory = category,
                            expertId = expertId,
                            expertTime = time,
                            expertPrice = price,
                            about = about,
                            longAbout = longAbout,
                            expertCategoryDetail = categoryList
                        )
                    }
                }else {
                    showToast("Lütfen tüm alanları doldurun")
                }
            }
        }
        initObservers()
    }

    private fun initObservers(){
        lifecycleScope.launchWhenCreated {
            signUpExpertViewModel.authResult.collect{ authResult ->
                when(authResult){
                    is ExpertAuthResult.ExpertAuthorized -> {
                        findNavController().navigate(R.id.action_signUpExpertFragment_to_expertSignInFragment)
                    }
                    is ExpertAuthResult.ExpertUnAuthorized -> {
                        showToast("Email veya Şifre Hatalı")
                    }
                    is ExpertAuthResult.ExpertUnknownError -> {
                        showToast("Bilinmeyen bir hata oluştu")
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 2 && resultCode == Activity.RESULT_OK){
            data?.data?.also {uri->
                var fileName = getFileNameFromUri(uri)
                fileNameList.add(fileName.toString())
                makeAlertDialog()
            }
        }
    }

    private fun makeAlertDialog() {
        customAlertDialogBinding =
            FileNameAlertDialogBinding.inflate(LayoutInflater.from(requireContext()))

        var alert = AlertDialog.Builder(requireContext())
        alert.setTitle("Yüklenen Dosyalar")
        recyclerViewFileListData(customAlertDialogBinding)
        alert.setView(customAlertDialogBinding.root)
        alert.setPositiveButton("Tamam",object: DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                showToast("Success")
            }

        })
        alert.setNegativeButton("Vazgeç",object: DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                showToast("Fail")
            }

        })
        alert.show()
    }

    private fun recyclerViewFileListData(alertDialogBinding: FileNameAlertDialogBinding){
        val fileList = fileNameList
        with(alertDialogBinding){
            fileNamesRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            fileNamesRecyclerView.adapter = fileListRecyclerAdapter
            fileListRecyclerAdapter.updateFileList(fileList)
        }
    }

    private fun getFileNameFromUri(uri: Uri): String? {
        val contentResolver = requireContext().contentResolver
        val cursor = contentResolver.query(uri,null,null,null,null)
        var fileName : String? = null
        cursor?.use {
            if(it.moveToFirst()){
                val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if(displayNameIndex != -1){
                    fileName = it.getString(displayNameIndex)
                }
            }
        }
        return fileName
    }

    private fun permissionRequest() {
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { perm->
            if(perm == true){
                if(ContextCompat.checkSelfPermission(
                        requireContext(),
                        Utils.permissions[2]
                ) == PackageManager.PERMISSION_GRANTED){
                    showToast("Permission Granted")
                }
            } else {
                showToast("Permission Needed!!")
            }
        }
    }

    private fun shouldShowRationale(permissionLauncher: ActivityResultLauncher<String>){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Utils.permissions[2]
        )){
            Snackbar.make(
                binding.root,
                "Permission needed for file uploading",
                Snackbar.LENGTH_INDEFINITE
            ).setAction("Give Permission") {
                permissionLauncher.launch(
                    Utils.permissions[2]
                )
            }.show()
        } else {
            permissionLauncher.launch(
                Utils.permissions[2]
            )
        }
    }

    private fun getCategoryItems(): ArrayList<String>{
        val expertCategoryList = ExpertCategories.getExpertCategories()
        return expertCategoryList
    }

    private fun showToast(message: String){
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }

    private fun checkFields(
        username: String,
        email: String,
        phone: String,
        password: String,
        passwordAgain: String,
        id: String,
        time: String,
        price: String,
        category: String,
        about: String,
        longAbout: String,
        categoryDetails: ArrayList<CategoryItems>
    ): Boolean{
        val check = when {
            username.trim().isEmpty() -> false
            email.trim().isEmpty() -> false
            phone.trim().isEmpty() -> false
            password.trim().isEmpty() -> false
            passwordAgain.trim().isEmpty() -> false
            id.trim().isEmpty() -> false
            time.trim().isEmpty() -> false
            price.trim().isEmpty() -> false
            about.trim().isEmpty() -> false
            longAbout.trim().isEmpty() -> false
            category.trim().isEmpty() -> false
            categoryDetails.isEmpty() -> false
            else -> true
        }
        return check
    }

    private fun checkedLawyerItems(binding: LawyerItemsBinding){
        with(binding){

            addLawyerItems(binding)

            for (i in lawyerCheckedBoxItems){
                i.setOnCheckedChangeListener{ _,isChecked ->
                    if(isChecked){
                        showToast(i.text.toString())
                        categoryItems.add(CategoryItems("${i.text}"))
                    }else{
                        categoryItems.remove(CategoryItems("${i.text}"))
                    }
                }
            }
        }
    }

    private fun checkedLanguagePracticeItems(binding: LanguagePracticeItemsBinding){
        with(binding){
            addLanguagePracticeItems(binding)

            for (i in lawyerCheckedBoxItems){
                i.setOnCheckedChangeListener { _, isChecked ->
                    if(isChecked){
                        showToast(i.text.toString())
                        categoryItems.add(CategoryItems("${i.text}"))
                    } else {
                        categoryItems.remove(CategoryItems("${i.text}"))
                    }
                }
            }
        }
    }

    private fun checkedCoachItems(binding: CoachItemsLayoutBinding){
        with(binding){

            addCoachItems(binding)

            for(i in lawyerCheckedBoxItems){
                i.setOnCheckedChangeListener { _, isChecked ->
                    showToast(i.text.toString())
                    if(isChecked){
                        categoryItems.add(CategoryItems("${i.text}"))
                    } else {
                        categoryItems.remove(CategoryItems("${i.text}"))
                    }
                }
            }
        }
    }

    private fun checkedEcommerceItems(binding: ECommerceItemsBinding){
        with(binding){

            addEcommerceItems(binding)

            for(i in lawyerCheckedBoxItems){
                i.setOnCheckedChangeListener { _, isChecked ->
                    showToast(i.text.toString())
                    if(isChecked){
                        categoryItems.add(CategoryItems("${i.text}"))
                    } else {
                        categoryItems.remove(CategoryItems("${i.text}"))
                    }
                }
            }
        }
    }

    private fun addCoachItems(binding: CoachItemsLayoutBinding){
        with(binding){
            with(binding){

                lawyerCheckedBoxItems.add(coachTypeChcBx1)
                lawyerCheckedBoxItems.add(coachTypeChcBx2)
                lawyerCheckedBoxItems.add(coachTypeChcBx3)

                lawyerCheckedBoxItems.add(coachAreasChcBx1)
                lawyerCheckedBoxItems.add(coachAreasChcBx2)
                lawyerCheckedBoxItems.add(coachAreasChcBx3)
                lawyerCheckedBoxItems.add(coachAreasChcBx4)
                lawyerCheckedBoxItems.add(coachAreasChcBx5)
                lawyerCheckedBoxItems.add(coachAreasChcBx6)
                lawyerCheckedBoxItems.add(coachAreasChcBx7)
                lawyerCheckedBoxItems.add(coachAreasChcBx8)
                lawyerCheckedBoxItems.add(coachAreasChcBx9)
                lawyerCheckedBoxItems.add(coachAreasChcBx10)
                lawyerCheckedBoxItems.add(coachAreasChcBx11)
                lawyerCheckedBoxItems.add(coachAreasChcBx12)
                lawyerCheckedBoxItems.add(coachAreasChcBx13)
                lawyerCheckedBoxItems.add(coachAreasChcBx14)
                lawyerCheckedBoxItems.add(coachAreasChcBx15)

                lawyerCheckedBoxItems.add(coachLanguageChcBx1)
                lawyerCheckedBoxItems.add(coachLanguageChcBx2)
                lawyerCheckedBoxItems.add(coachLanguageChcBx3)
                lawyerCheckedBoxItems.add(coachLanguageChcBx4)
                lawyerCheckedBoxItems.add(coachLanguageChcBx5)
                lawyerCheckedBoxItems.add(coachLanguageChcBx6)
                lawyerCheckedBoxItems.add(coachLanguageChcBx7)
                lawyerCheckedBoxItems.add(coachLanguageChcBx8)
                lawyerCheckedBoxItems.add(coachLanguageChcBx9)
                lawyerCheckedBoxItems.add(coachLanguageChcBx10)
                lawyerCheckedBoxItems.add(coachLanguageChcBx11)
                lawyerCheckedBoxItems.add(coachLanguageChcBx12)
            }
        }
    }

    private fun addEcommerceItems(binding: ECommerceItemsBinding){
        with(binding){
            lawyerCheckedBoxItems.add(ecommerceChcBx1)
            lawyerCheckedBoxItems.add(ecommerceChcBx3)
            lawyerCheckedBoxItems.add(ecommerceChcBx4)
            lawyerCheckedBoxItems.add(ecommerceChcBx5)
            lawyerCheckedBoxItems.add(ecommerceChcBx6)
            lawyerCheckedBoxItems.add(ecommerceChcBx7)
        }
    }

    private fun addLanguagePracticeItems(binding: LanguagePracticeItemsBinding){
        with(binding){
            lawyerCheckedBoxItems.add(languageTypeChcBx1)
            lawyerCheckedBoxItems.add(languageTypeChcBx2)
            lawyerCheckedBoxItems.add(languageTypeChcBx3)
            lawyerCheckedBoxItems.add(languageTypeChcBx4)
            lawyerCheckedBoxItems.add(languageTypeChcBx5)
            lawyerCheckedBoxItems.add(languageTypeChcBx6)
            lawyerCheckedBoxItems.add(languageTypeChcBx7)
            lawyerCheckedBoxItems.add(languageTypeChcBx8)
            lawyerCheckedBoxItems.add(languageTypeChcBx9)
            lawyerCheckedBoxItems.add(languageTypeChcBx10)
            lawyerCheckedBoxItems.add(languageTypeChcBx11)
            lawyerCheckedBoxItems.add(languageTypeChcBx12)
            lawyerCheckedBoxItems.add(languageTypeChcBx13)
            lawyerCheckedBoxItems.add(languageTypeChcBx14)
            lawyerCheckedBoxItems.add(languageTypeChcBx15)
        }
    }

    private fun addLawyerItems(binding: LawyerItemsBinding){

        with(binding){
            lawyerCheckedBoxItems.add(lawyerTypeChcBx1)
            lawyerCheckedBoxItems.add(lawyerTypeChcBx2)
            lawyerCheckedBoxItems.add(lawyerTypeChcBx3)
            lawyerCheckedBoxItems.add(lawyerTypeChcBx4)
            lawyerCheckedBoxItems.add(lawyerTypeChcBx5)
            lawyerCheckedBoxItems.add(lawyerTypeChcBx6)
            lawyerCheckedBoxItems.add(lawyerTypeChcBx7)
            lawyerCheckedBoxItems.add(lawyerTypeChcBx8)
            lawyerCheckedBoxItems.add(lawyerTypeChcBx9)
            lawyerCheckedBoxItems.add(lawyerTypeChcBx10)
            lawyerCheckedBoxItems.add(lawyerTypeChcBx11)
            lawyerCheckedBoxItems.add(lawyerTypeChcBx12)
            lawyerCheckedBoxItems.add(lawyerTypeChcBx13)
            lawyerCheckedBoxItems.add(lawyerTypeChcBx14)
            lawyerCheckedBoxItems.add(lawyerTypeChcBx15)
            lawyerCheckedBoxItems.add(lawyerTypeChcBx16)
            lawyerCheckedBoxItems.add(lawyerTypeChcBx17)
        }
    }

}