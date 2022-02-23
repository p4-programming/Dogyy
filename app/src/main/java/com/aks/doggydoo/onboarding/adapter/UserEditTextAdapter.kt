package com.aks.doggydoo.onboarding.adapter

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.commonutility.CommonFunctions
import com.aks.doggydoo.databinding.SingleUserInfoEditTextBinding


//size will remain 3
private const val ITEM_COUNT = 3
private const val TAG = "useretAdaptTag"

class UserEditTextAdapter(
    var context: Context
) :
    RecyclerView.Adapter<UserEditTextAdapter.ViewHolder>() {

    private var listOfValueInEditText = arrayListOf("", "", "")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SingleUserInfoEditTextBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return ITEM_COUNT
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView()
    }

    inner class ViewHolder(var binding: SingleUserInfoEditTextBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView() {
            binding.apply {
                name.setOnFocusChangeListener { v, hasFocus ->
                    if (!hasFocus) {
                        CommonFunctions.hideKeyBoard(v!!, context)
                    }else{
                        if (absoluteAdapterPosition==2){
                            name.isSingleLine = false
                            name.gravity = Gravity.START
                            val params = name.getLayoutParams()
                            params.height = 400
                            name.setLayoutParams(params)
                        }
                    }
                }
                //setting the hint value according to the position
                when (absoluteAdapterPosition) {
                    0 -> {
                        name.hint = "Your name"
                        name.isSingleLine = true
                    }
                    1 -> {
                        name.hint = "Your age"
                        name.inputType = InputType.TYPE_CLASS_NUMBER
                        name.filters = arrayOf(InputFilter.LengthFilter(3))
                        name.isSingleLine = true
                    }
                    2 -> {
                        name.hint = "Brief Description..."
                    }
                }


            }


            binding.name.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    listOfValueInEditText[absoluteAdapterPosition] = s.toString()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

            })
        }
    }

    fun getTextOfParticularPosition(position: Int): String {
        return listOfValueInEditText[position]
    }
}