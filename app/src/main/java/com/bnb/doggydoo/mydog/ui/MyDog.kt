package com.bnb.doggydoo.mydog.ui

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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bnb.doggydoo.R
import com.bnb.doggydoo.commonutility.hide
import com.bnb.doggydoo.commonutility.loadImageFromString
import com.bnb.doggydoo.commonutility.show
import com.bnb.doggydoo.commonutility.snack
import com.bnb.doggydoo.databinding.ActivityMyDogBinding
import com.bnb.doggydoo.mydog.adapter.ImageViewPagerAdapter
import com.bnb.doggydoo.mydog.adapter.MyPetDocumentAdapter
import com.bnb.doggydoo.mydog.adapter.MyPetReminder
import com.bnb.doggydoo.mydog.datasource.model.petdescriptionmodel.PetDescriptionResponse
import com.bnb.doggydoo.mydog.viewmodel.MyDogViewModel
import com.bnb.doggydoo.newsfeed.util.RecyclerTouchListener
import com.bnb.doggydoo.utils.CommonMethod
import com.bnb.doggydoo.utils.MultipartFile
import com.bnb.doggydoo.utils.MyApp
import com.bnb.doggydoo.utils.helper.Result
import com.bnb.doggydoo.utils.network.ApiConstant.PET_IMAGE_BASE_URL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyDog : AppCompatActivity() {
    private var petId: String = ""
    private lateinit var binding: ActivityMyDogBinding
    private lateinit var bitmap: Bitmap
    private lateinit var petDocumentAdapter: MyPetDocumentAdapter
    private lateinit var petReminder: MyPetReminder
    private lateinit var adapter: ImageViewPagerAdapter

    //initialized view model here
    private lateinit var myDogViewModel: MyDogViewModel
    private var reminder = ArrayList<String>()
    private var reminderName = ArrayList<String>()
    private var reminderTime = ArrayList<String>()
    private var reminderid: String = ""
    private var flagStatus: Int = 0
    private var petName: String = ""
    private var petImage: String = "";
    private var checkedStatus: String = "0"
    private var startDate: String = ""
    private var endDate: String = ""
    private var startTime: String = ""
    private var endTime: String = ""
    private var startDateSelect: String = ""
    private var endDateSelect: String = ""
    private var from: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyDogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInit()
        callGetDogDescriptionAPI()
    }

    private fun getInit() {
        myDogViewModel = ViewModelProvider(this).get(MyDogViewModel::class.java)
        petId = intent.getStringExtra("petId").toString()
        petDocumentAdapter = MyPetDocumentAdapter(this)
        petReminder = MyPetReminder(this)
        binding.documentRv.adapter = petDocumentAdapter
        binding.reminderRv.adapter = petReminder

        binding.llReminder.visibility = View.GONE
        binding.reminderRv.addOnItemTouchListener(
            RecyclerTouchListener(
                applicationContext,
                binding.reminderRv,
                object : RecyclerTouchListener.ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        binding.llReminder.visibility = View.VISIBLE
                        binding.llShare.setOnClickListener {
                            val reminderName: String = reminderName.get(position)
                            val reminderTime: String = reminderTime.get(position)
                            binding.llReminder.visibility = View.GONE
                            val intent = Intent()
                            intent.action = Intent.ACTION_SEND
                            intent.type = "text/plain"
                            intent.putExtra(
                                Intent.EXTRA_TEXT,
                                "$petName has a reminder for: $reminderName at $reminderTime"
                            )
                            startActivity(Intent.createChooser(intent, "Share with:"))
                        }

                        binding.llDelete.setOnClickListener {
                            binding.llReminder.visibility = View.GONE
                            val reminderid: String = reminder.get(position)
                            deletePetReminderAPI(reminderid, position)
                        }

                        binding.ivClose.setOnClickListener {
                            binding.llReminder.visibility = View.GONE
                        }
                    }

                    override fun onLongClick(view: View?, position: Int) {}
                })
        )

        /* val viewPager = binding.sliderviewpager
         adapter = ImageViewPagerAdapter(this)
         viewPager.adapter = adapter
         binding.dotsIndicator.setViewPager2(viewPager)*/

        binding.setReminder.setOnClickListener {
            Intent(this, SetReminder::class.java).putExtra("petId", petId).apply {
                reminderResultLauncher.launch(this)
            }
        }
        binding.addDoc.setOnClickListener {
            startActivity(Intent(this, AddDocument::class.java).putExtra("petId", petId))
        }
        binding.backButton.setOnClickListener { finish() }
        binding.llUploadPic.setOnClickListener {
            openGallery()
        }

        binding.ivEditProfile.setOnClickListener {
            startActivity(Intent(this, EditPetProfileActivity::class.java).putExtra("petId", petId))
        }


        binding.llGuide.visibility = View.GONE


        binding.dogSittingCheck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            flagStatus = 1
            if (isChecked) {
                openDialog(
                    "Dog Sitting Submission",
                    "Suitable dog sitters looking to sit a dog will be shown your dog`s profile.",
                    "You are requesting for dog sitting. if yes great, select yes and continue.",
                    "Agree to Dog Sitting Terms & Conditions.",
                    "doggysit"
                )
                //updatePetInfoAPI("1", "doggysit")
            } else {
                updatePetInfoAPI("0", "doggysit")
            }
        })

        binding.fosterCheck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            flagStatus = 1
            if (isChecked) {
                openDialog(
                    "Find a foster parent",
                    "You are opting for your dog to be available for Fostering!. Upbringing a dog requires patience, compassionate nature & some knowledge of dog behavior.We also recommend if you are facing difficultyin handling the dog, please watch basic training videos from our dog training section.",
                    "Are you sure you want to put up your dog for fostering to parents searching to foster? if yes, select yes and continue.",
                    "Agree to Fostering Terms & Conditions.",
                    "fostering  "
                )
                //updatePetInfoAPI("1", "fostering")
            } else {
                updatePetInfoAPI("0", "fostering")
            }
        })

        binding.adoptCheck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            flagStatus = 1
            if (isChecked) {
                openDialog(
                    "Adoption Submission",
                    "You are opting for your dog to be available for adoption to parents looking for adopting!",
                    "You are requesting to put up your dog for adoption to parents searching to adopt? if yes, select yes and continue.",
                    "Agree to Adoption Terms & Conditions.",
                    "adoption"
                )
                //updatePetInfoAPI("1", "adoption")
            } else {
                updatePetInfoAPI("0", "adoption")
            }
        })

        binding.matingCheck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            flagStatus = 1
            if (isChecked) {
                openDialog(
                    "Finding a Mate",
                    "This dog profile will be shown as interested in finding a mate. will be displayed to opposite gender & similar breeds.",
                    "You are requesting to find a mate.if yes the cupid is on it`s way, select yes and continue.",
                    "Agree to Find a mate Terms & Conditions.",
                    "mating"
                )
                // updatePetInfoAPI("1", "mating")
            } else {
                updatePetInfoAPI("0", "mating")
            }
        })


    }

    private fun openDialog(
        title: String,
        text1: String,
        text2: String,
        term: String,
        type: String
    ) {
        binding.llGuide.visibility = View.VISIBLE
        binding.tvTitle.text = title
        binding.text1.text = text1
        binding.text2.text = text2
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

                if (type == "doggysit") {
                    startActivity(
                        Intent(this, SelectStartDateTimeActivity::class.java)
                            .putExtra("petId", petId)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    binding.rbTerm.isChecked = false
                } else {
                    updatePetInfoAPI("1", type)
                }

            } else {
                Toast.makeText(this, "Please check the condition button", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        binding.tvCancel.setOnClickListener {
            binding.llGuide.visibility = View.GONE
            checkedStatus = "0"
            callGetDogDescriptionAPI()
        }
    }

    private fun callGetDogDescriptionAPI() {
        System.out.println("ped id>>" + petId.toString())
        System.out.println("ped id>>" + MyApp.getSharedPref().userId)
        myDogViewModel.getPetDescriptionData(petId.toString(), MyApp.getSharedPref().userId)
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                        binding.llUploadPic.hide()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        if (it.data?.responseCode.equals("0")) {
                            it.data?.responseMessage?.snack(
                                Color.RED,
                                binding.parent
                            )
                            return@Observer
                        }
                        petDocumentAdapter.submitList(it.data?.documentDetail)

                        for (category in it.data?.petReminder!!) {
                            reminder.add(category.reminder_id)
                            reminderName.add(category.type)
                            reminderTime.add(category.create.toString())
                        }

                        petReminder.submitList(it.data.petReminder)
                        setDataInUI(it.data)
                    }
                    Result.Status.ERROR -> {
                        binding.llUploadPic.show()
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun setDataInUI(data: PetDescriptionResponse) {
        petName = data.petdetail.pet_name
        petId = data.petdetail.id
        binding.petName.text = data.petdetail.pet_name + "," + data.petdetail.pet_gender
        binding.tvDocument.text = data.petdetail.pet_name + "'" + "s" + " " + "Document"
        binding.breedAge.text =
            "${data.petdetail.breed}, ${data.petdetail.pet_age} year, ${data.petdetail.pet_age_month} month old"

        if (data.petdetail.pet_gender == "male") {
            binding.ivGender.setImageResource(R.drawable.gender_male)
        } else {
            binding.ivGender.setImageResource(R.drawable.gender_female)
        }

        if (data.petdetail.is_pet_vaccinated == "yes") {
            binding.height.text = "Vaccinated"
            binding.ivVaccination.setImageResource(R.drawable.green_verified_tick)
        } else {
            binding.height.text = "Not vaccinated"
            binding.ivVaccination.setImageResource(R.drawable.yellow_verified_tick)
        }

        //binding.height.text = data.petdetail.is_pet_vaccinated
        binding.gender.text = data.petdetail.pet_gender
        binding.weight.text = data.petdetail.pet_weight + "Kg " + data.petdetail.pet_weight_gm + "gm"
        binding.descriptionText.text = data.petdetail.pet_description
        binding.fosterCheck.isChecked = data.petdetail.fostering == "1"
        binding.adoptCheck.isChecked = data.petdetail.adoption == "1"
        binding.dogSittingCheck.isChecked = data.petdetail.doggysit == "1"
        binding.matingCheck.isChecked = data.petdetail.mating == "1"
        flagStatus = 0
        if (data.petdetail.pet_image == "user.png") {
            binding.llUploadPic.show()
        } else {
            if (data.petImage.size > 0) {
                petImage = PET_IMAGE_BASE_URL + data.petImage[0]
                binding.dogImage.loadImageFromString(
                    this,
                    PET_IMAGE_BASE_URL + data.petImage[0]
                )
                binding.dogImage.show()
                binding.llUploadPic.hide()
            } else {
                binding.dogImage.setImageResource(R.drawable.dummy_pet)
            }

        }

        checkedStatus = "0"
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    val data = result.data!!.data
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        val source = ImageDecoder.createSource(contentResolver, data!!)
                        bitmap = ImageDecoder.decodeBitmap(source)
                        binding.dogImage.setImageBitmap(bitmap)
                    } else {
                        binding.dogImage.setImageURI(data)
                    }
                    binding.dogImage.show()
                    binding.llUploadPic.hide()
                    data?.let { uploadDogImageAPI(it) }
                }
            }
        }

    private var reminderResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                callGetDogDescriptionAPI()
            }
        }

    private fun openGallery() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            this.type = "image/*"
            resultLauncher.launch(this)
        }
    }

    override fun onResume() {
        super.onResume()
        callGetDogDescriptionAPI()
    }

    private fun uploadDogImageAPI(uri: Uri) {
        myDogViewModel.getUploadProfileImageData(
            intent.getStringExtra("petId").toString(),
            MultipartFile.prepareFilePart(this, "user_profile[]", uri)
        )
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                        binding.backButton.isEnabled = false
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        binding.llUploadPic.hide()
                        binding.backButton.isEnabled = true
                        if (it.data?.responseCode?.equals("0")!!) {
                            it.data.responseMessage.snack(Color.RED, binding.parent)
                            return@Observer
                        }
                        "Image Upload Successfully".snack(R.color.docbuton, binding.parent)
                    }
                    Result.Status.ERROR -> {
                        binding.progressBar.hide()
                        binding.backButton.isEnabled = true
                        it.message?.snack(Color.RED, binding.parent)
                        binding.dogImage.hide()
                        binding.llUploadPic.show()
                    }
                }
            })
    }

    private fun updatePetInfoAPI(status: String, type: String) {
        myDogViewModel.getPetStatusUpdateData(
            petId.toString(),
            status,
            type,
            startDate,
            startTime,
            endDate,
            endTime
        )
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                        binding.llUploadPic.hide()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        if (it.data?.responseCode.equals("0")) {
                            it.data?.responseMessage?.snack(
                                Color.RED,
                                binding.parent
                            )
                            return@Observer
                        }
                        showMesage()
                    }
                    Result.Status.ERROR -> {
                        binding.llUploadPic.show()
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

    private fun deletePetReminderAPI(reminderid: String, position: Int) {
        System.out.println("ped id>>" + reminderid)
        myDogViewModel.getPetDeleteStatusData(reminderid, MyApp.getSharedPref().userId)
            .observe(this, Observer {
                when (it.status) {
                    Result.Status.LOADING -> {
                        binding.progressBar.show()
                        binding.llUploadPic.hide()
                    }
                    Result.Status.SUCCESS -> {
                        binding.progressBar.hide()
                        if (it.data?.responseCode.equals("0")) {
                            it.data?.responseMessage?.snack(
                                Color.RED,
                                binding.parent
                            )
                            return@Observer
                        }
                        callGetDogDescriptionAPI()
                    }
                    Result.Status.ERROR -> {
                        binding.llUploadPic.show()
                        binding.progressBar.hide()
                        it.message?.snack(Color.RED, binding.parent)
                    }
                }
            })
    }
}