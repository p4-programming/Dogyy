package com.aks.doggydoo.onboarding.adapter

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.CommonFunctions
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.commonutility.show
import com.aks.doggydoo.databinding.SingleDogInfoEditTextBinding
import dagger.hilt.android.AndroidEntryPoint

//the size should always 7
private const val ITEM_COUNT = 7
private const val TAG = "dogetAdaptTag"

class DogEditTextAdapter(
    var context: Context,
    var hintList: List<String>,
    var showOrHideRightLayoutList: ArrayList<String>,
    private val callOptionBS: (hint: String, valueOfEditText: String) -> Unit,
    private var showDogChart: () -> Unit
) :
    RecyclerView.Adapter<DogEditTextAdapter.DogEditTextViewHolder>() {

    //using it to store the editText value, so that it won't insert the previous entered text in other position
    private var listOfValueInEditText = arrayListOf("", "", "", "", "", "", "no")
    private var monthType = "years"
    private var weightType = "Kgs"

    //not using right now (could be use later so don't remove it)
    private var textValue: String = ""

    //update only right layout text
    fun updateHint(rightLayoutText: String, position: Int, valueOfEditText: String) {
//        textValue = valueOfEditText
        if (rightLayoutText != "") {
            showOrHideRightLayoutList[position] = rightLayoutText
        } else {
            listOfValueInEditText[position] = valueOfEditText
        }
        notifyItemChanged(position)
    }

    //update only editText Value, which is selected by user from OptionBS
    fun updateText(value: String, position: Int) {
        listOfValueInEditText[position] = value
//        this.textValue = value
        notifyItemChanged(position)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogEditTextViewHolder {
        val binding = SingleDogInfoEditTextBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DogEditTextViewHolder(binding, callOptionBS)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holderDogEditText: DogEditTextViewHolder, position: Int) {
        holderDogEditText.bindView(
            hintList[position],
            showOrHideRightLayoutList[position],
            listOfValueInEditText[position]
        )
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    inner class DogEditTextViewHolder(
        var binding: SingleDogInfoEditTextBinding,
        callOptionBS: (hint: String, valueOfEditText: String) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.male.setOnClickListener {
                binding.male.backgroundTintList =
                    context.resources.getColorStateList(R.color.male_female_dog_gender)
                binding.female.backgroundTintList =
                    context.resources.getColorStateList(R.color.white)
                listOfValueInEditText[absoluteAdapterPosition] = "Male"
            }
            binding.female.setOnClickListener {
                binding.male.backgroundTintList =
                    context.resources.getColorStateList(R.color.white)
                binding.female.backgroundTintList =
                    context.resources.getColorStateList(R.color.male_female_dog_gender)
                listOfValueInEditText[absoluteAdapterPosition] = "Female"
            }
            binding.rightLayout.setOnClickListener {
                if (binding.name.hint.toString() == "Pet's age") {
                    monthType = "months"
                    callOptionBS(binding.rightText.text.toString(), binding.name.text.toString())
                } else if (binding.name.hint.toString() == "Pet's weight") {
                    weightType = "Lbs"
                    callOptionBS(binding.rightText.text.toString(), binding.name.text.toString())
                } else {
                    callOptionBS(binding.name.hint.toString(), binding.name.text.toString())
                }
                //storing the entered text in list so that it won't be removed or update
                if (showOrHideRightLayoutList[absoluteAdapterPosition] == "arrow") {
                    listOfValueInEditText[absoluteAdapterPosition] =
                        listOfValueInEditText[absoluteAdapterPosition].substring(
                            0,
                            listOfValueInEditText[absoluteAdapterPosition].length - 1
                        )
                } else listOfValueInEditText[absoluteAdapterPosition] = binding.name.text.toString()

            }
            binding.name.setOnClickListener {
                if (binding.name.hint.toString() == "Pet's age") {
                    callOptionBS("age", binding.name.text.toString())
                } else if (binding.name.hint.toString() == "Pet's weight") {
                    callOptionBS("weight", binding.name.text.toString())
                } else if (binding.name.hint.toString() == "Pet's breed") {
                    callOptionBS(binding.name.hint.toString(), binding.name.text.toString())
                }
            }

            binding.weightImage.setOnClickListener {
                showDogChart()
            }
        }

        fun bindView(hintText: String, showOrHide: String, value: String) {
            binding.apply {
                name.setOnFocusChangeListener { v, hasFocus ->
                    if (!hasFocus) {
                        CommonFunctions.hideKeyBoard(v!!, context)
                    } else {
                        if (absoluteAdapterPosition == 5) {
                            name.hint = "Brief Description..."
                            name.isSingleLine = false
                            name.gravity = Gravity.START
                            val params = name.layoutParams
                            params.height = 400
                            name.layoutParams = params
                        } else if (absoluteAdapterPosition == 6) {
                            name.hint = "Medical conditions if any..."
                            name.isSingleLine = false
                            name.gravity = Gravity.START
                            val params = name.layoutParams
                            params.height = 400
                            name.layoutParams = params
                        }
                    }
                }
                //set hint text in edittext
                name.hint = (hintText)
                //set text in edit text
                name.setText(value)
                Log.d(TAG, "bindView: $absoluteAdapterPosition")

                System.out.println("position is" + value)

                if (absoluteAdapterPosition == 7) {
                    name.hint = "Brief Description..."
                    name.isSingleLine = false
                    name.gravity = Gravity.START
                    val params = main.layoutParams
                    params.height = 400
                    main.layoutParams = params
                } else
                    if (absoluteAdapterPosition == 9) {
                        name.hint = "Medical conditions if any..."
                        name.isSingleLine = false
                        name.gravity = Gravity.START
                        val params = name.layoutParams
                        params.height = 400
                        name.layoutParams = params
                    }

                name.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        listOfValueInEditText[absoluteAdapterPosition] = s.toString().trim()
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {

                    }

                })
                //using it to check whether I have to show  rightLayout view or only arrow or nothing
                when (showOrHide) {
                    "" -> {
                        name.isFocusable = true
                        name.isClickable = true
                        rightLayout.visibility = GONE
                        singleDrop.visibility = GONE
                        name.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                        name.isEnabled = true
                        genderLayout.hide()
                        weightImage.hide()
                        otherLayout.show()
                    }
                    "arrow" -> {
                        weightImage.hide()
                        name.isFocusable = false
                        name.isClickable = true
                        name.isEnabled = true
                        rightLayout.visibility = GONE
                        singleDrop.visibility = VISIBLE
                        name.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                        genderLayout.hide()
                        otherLayout.show()
                    }
                    "gender" -> {
                        weightImage.hide()
                        otherLayout.hide()
                        genderLayout.show()
                    }
                    "Kgs", "Lbs" -> {
                        name.isFocusable = false
                        name.isClickable = true
                        name.isEnabled = true
                        genderLayout.hide()
                        otherLayout.show()
                        weightImage.show()
                        rightLayout.show()
                        rightText.text = showOrHide
                    }
                    else -> {
                        weightImage.hide()
                        name.isFocusable = false
                        name.isClickable = true
                        rightLayout.show()
                        rightText.text = showOrHide
                        singleDrop.hide()
                        name.isEnabled = true
                        name.inputType =
                            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
                        genderLayout.hide()
                        otherLayout.show()
                    }
                }

            }
        }
    }

    fun getPetMonthAndWeightType(): Pair<String, String> {
        return Pair(monthType, weightType)
    }

    fun getTextOfParticularPosition(position: Int): String {
        return listOfValueInEditText[position]
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return ITEM_COUNT
    }
}