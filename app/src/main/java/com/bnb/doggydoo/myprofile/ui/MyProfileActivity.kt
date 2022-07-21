package com.bnb.doggydoo.myprofile.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.*
import com.bnb.doggydoo.databinding.ActivityMyProfileBinding
import com.bnb.doggydoo.mydog.ui.MyDog
import com.bnb.doggydoo.myfrienddescription.adapter.FriendPetAdapter
import com.bnb.doggydoo.myfrienddescription.adapter.NewUploadAdapter
import com.bnb.doggydoo.myprofile.datasource.model.profile.MyProfileResponse
import com.bnb.doggydoo.myprofile.viewmodel.MyProfileViewModel
import com.bnb.doggydoo.newsfeed.ui.NewsFeedDashboardActivity
import com.bnb.doggydoo.onboarding.ui.PetOnBoardingActivity
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.MultipartFile
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import com.bnb.doggydoo.utils.network.ApiConstant.PROFILE_IMAGE_BASE_URL
import dagger.hilt.android.AndroidEntryPoint


const val TAG = "myProfileTAG"

@AndroidEntryPoint
class MyProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyProfileBinding
    private lateinit var bitmap: Bitmap
    private lateinit var adapter: FriendPetAdapter
    private lateinit var newUploadAdapter: NewUploadAdapter

    //initialized view model here
    private lateinit var myProfileViewModel: MyProfileViewModel
    private var flagStatus: Int = 0
    var checkedStatus: String = "0"

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        getMyProfileInfoAPI()
    }

    private fun getInit() {
        myProfileViewModel = ViewModelProvider(this).get(MyProfileViewModel::class.java)
        adapter = FriendPetAdapter(this) { view, petId ->
            val imageViewPair =
                Pair(
                    view,
                    view.transitionName
                )
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                imageViewPair
            )
            startActivity(
                Intent(this, MyDog::class.java)
                    .putExtra("petId", petId),
                options.toBundle()
            )
        }
        binding.myPetRv.adapter = adapter

        newUploadAdapter = NewUploadAdapter(this)
        binding.myNewUpdatesRv.adapter = newUploadAdapter


        binding.llUploadPic.setOnClickListener {
            openGallery()
        }
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.addPet.setOnClickListener {
            startActivity(
                Intent(this, PetOnBoardingActivity::class.java)
                    .putExtra("from", "myProfile")
            )
            finish()
        }

        binding.ivEditProfile.setOnClickListener {
            startActivity(
                Intent(this, EditProfileActivity::class.java)
            )
            finish()
        }
        binding.llGuide.visibility = View.GONE
        binding.dogSittingCheck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            flagStatus = 1
            if (isChecked) {
                openDialog(
                    "Dogsitting",
                    "Looking after a dog or dogs while their parent is away, location can be decided by you in the next steps.",
                    "You can be a student, dog lover, boarding house & an experienced dog sitter.",
                    "Your profile will be shown available for dog sitting, Are you sure? if yes great, select yes and continue.",
                    "Agree to Dogsitting Terms & Conditions.",
                    "dogsitter"
                )
            } else {
                updateUserInfoAPI("0", "dogsitter")
            }
        })

        binding.fosterCheck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            flagStatus = 1
            if (isChecked) {
                openDialog(
                    "Fostering",
                    "Temporary adopting a dog and nurturing for a while until they are dispatched to a permanent home with a family.",
                    "Requires patience, compassionate nature & some knowledge of dog behavior.We also recommend first time parents watching basic training videos from our dog training section.",
                    "Sure you want to opt for fostering?if yes great, you are a hero!select yes and continue.",
                    "Agree to Fostering Terms & Conditions.",
                    "fostering"
                )
            } else {
                updateUserInfoAPI("0", "fostering")
            }
        })

        binding.adoptCheck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            flagStatus = 1
            if (isChecked) {
                openDialog(
                    "Adopting",
                    "You’re ready to be mommy or daddy to a Dog? Instead of going to a breeder why not adopt a puppy in need? Not only will you be doing a good deed, but you’ll also have a rather grateful buddy following you around.",
                    "“When you look into the eyes of a dog you’ve rescued, you can’t help but fall in love.” — Paul Shaffer",
                    "You are opting for dog adoption.if yes great, select yes and continue.",
                    "Agree to Adoption Terms & Conditions.",
                    "adoption"

                )
            } else {
                updateUserInfoAPI("0", "adoption")
            }
        })


        binding.dogTrainerCheck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            flagStatus = 1
            if (isChecked) {
                updateUserInfoAPI("1", "dogtrainner")
            } else {
                updateUserInfoAPI("0", "dogtrainner")
            }
        })

        binding.homeChefCheck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            flagStatus = 1
            if (isChecked) {
                updateUserInfoAPI("1", "homechef")
            } else {
                updateUserInfoAPI("0", "homechef")
            }
        })

        binding.tvViewFeed.setOnClickListener {
            startActivity(
                Intent(
                    this, NewsFeedDashboardActivity::class.java
                )
                    .putExtra("from", "myprofile")
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            finish()
        }
    }

    private fun openDialog(
        title: String,
        text1: String,
        text2: String,
        text3: String,
        term: String,
        type: String
    ) {
        binding.llGuide.visibility = View.VISIBLE
        binding.tvTitle.text = title
        binding.text1.text = text1
        binding.text2.text = text2
        binding.text3.text = text3
        binding.rbTerm.text = term

        binding.rbTerm.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                checkedStatus = "1"

            } else {
                checkedStatus = "0"

            }
        })

        binding.tvConfirm.setOnClickListener {
            if (checkedStatus == "1") {
                binding.llGuide.visibility = View.GONE
                updateUserInfoAPI("1", type)
            } else {
                Toast.makeText(this, "Please check the condition button", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        binding.tvCancel.setOnClickListener {
            binding.llGuide.visibility = View.GONE
            checkedStatus = "0"
            binding.rbTerm.isChecked = false
            getMyProfileInfoAPI()
        }

    }

    private fun getMyProfileInfoAPI() {
        myProfileViewModel.getMyProfileInfoData(
            MyApp.getSharedPref().userId,
            MyApp.getSharedPref().userId
        )
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        if (it.data?.responseCode?.equals("0")!!) {
                            it.data.responseMessage.snack(Color.RED, binding.parent)
                            return@Observer
                        }
                        if (it.data.mypet.isEmpty()) {
                            binding.noPets.show()
                        }
                        if (it.data.newupload.isEmpty()) {
                            binding.noUploads.show()
                        }
                        setDataInUI(it.data)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun setDataInUI(profileResponse: MyProfileResponse) {
        binding.friendNameAndAge.text = profileResponse.userdetails[0].uname
        binding.detail.text = profileResponse.userdetails[0].description
        binding.dogSittingCheck.isChecked = profileResponse.userdetails[0].dogsitter == "yes"
        binding.fosterCheck.isChecked = profileResponse.userdetails[0].fostering == "yes"
        binding.adoptCheck.isChecked = profileResponse.userdetails[0].adoption == "yes"
        binding.homeChefCheck.isChecked = profileResponse.userdetails[0].dogtrainner == "yes"
        binding.dogTrainerCheck.isChecked = profileResponse.userdetails[0].homechef == "yes"

        flagStatus = 0
        if (profileResponse.userdetails[0].profile_pic == "user.png") {
            binding.llUploadPic.show()
            binding.userImage.setImageResource(R.drawable.dummy_user)
        } else {
            binding.userImage.loadImageFromString(
                this,
                PROFILE_IMAGE_BASE_URL + MyApp.getSharedPref().userImage
            )
            binding.userImage.show()
            binding.llUploadPic.hide()
        }

        adapter.submitList(profileResponse.mypet)
        newUploadAdapter.submitList(profileResponse.newupload)
        MyApp.getSharedPref().userEmail = profileResponse.userdetails[0].email

        if (profileResponse.userdetails[0].adoptionKing != "1") {
            binding.ivAdoptionKing.setColorFilter(
                ContextCompat.getColor(
                    this,
                    R.color.on_boarding_view
                )
            )
        }

        if (profileResponse.userdetails[0].rescueHero != "1") {
            binding.ivRescueHero.setColorFilter(
                ContextCompat.getColor(
                    this,
                    R.color.on_boarding_view
                )
            )
        }

        if (profileResponse.userdetails[0].dogsitChamp != "1") {
            binding.ivDogsitterHero.setColorFilter(
                ContextCompat.getColor(
                    this,
                    R.color.on_boarding_view
                )
            )
        }

        if (profileResponse.userdetails[0].dogtrainnerBadge != "1") {
            binding.ivDogTrainer.setColorFilter(
                ContextCompat.getColor(
                    this,
                    R.color.on_boarding_view
                )
            )
        }

        if (profileResponse.userdetails[0].homechefBadge != "1") {
            binding.ivHomeChef.setColorFilter(
                ContextCompat.getColor(
                    this,
                    R.color.on_boarding_view
                )
            )
        }

        checkedStatus = "0"
    }


    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    val data = result.data!!.data
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        val source = ImageDecoder.createSource(contentResolver, data!!)
                        bitmap = ImageDecoder.decodeBitmap(source)
                        binding.userImage.setImageBitmap(bitmap)
                    } else {
                        binding.userImage.setImageURI(result.data?.data)
                    }
                    binding.userImage.show()
                    binding.llUploadPic.hide()
                    data?.let { uploadUserImageAPI(it) }
                }
            }
        }

    private fun uploadUserImageAPI(uri: Uri) {
        myProfileViewModel.getUploadProfileImageData(
            MyApp.getSharedPref().userId,
            MultipartFile.prepareFilePart(this, "user_profile", uri)
        )
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                        binding.backButton.isEnabled = false
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        binding.backButton.isEnabled = true
                        if (it.data?.responseCode?.equals("0")!!) {
                            it.data.responseMessage.snack(Color.RED, binding.parent)
                            return@Observer
                        }
                        MyApp.getSharedPref().userImage = it.data.profile_image
                        "Image Upload Successfully".snack(R.color.docbuton, binding.parent)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        binding.backButton.isEnabled = true
                        it.message?.snack(Color.RED, binding.parent)
                        binding.userImage.hide()
                        binding.llUploadPic.show()
                    }
                }
            })
    }

    private fun openGallery() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            this.type = "image/*"
            resultLauncher.launch(this)
        }
    }

    private fun updateUserInfoAPI(status: String, type: String) {
        System.out.println("type is>>" + type)
        myProfileViewModel.getUserUpdateInfoData(MyApp.getSharedPref().userId, status, type)
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                    }
                    Result.Status.SUCCESS -> {

                        binding.progressBar.hide()
                        if (it.data?.responseCode?.equals("0")!!) {
                            it.data.responseMessage.snack(Color.RED, binding.parent)
                            return@Observer
                        }
                        showMesage()
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    private fun showMesage() {
        if (flagStatus != 0) {
            binding.rbTerm.isChecked = false
            CommonMethod.showSnack(binding.root, "Status changed successfully!!")
        }

    }
}