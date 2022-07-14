package com.aks.doggydoo.homemodule.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.aks.doggydoo.R
import com.aks.doggydoo.commonutility.hide
import com.aks.doggydoo.databinding.SingleNotificationBinding
import com.aks.doggydoo.homemodule.ui.HomeActivity
import com.aks.doggydoo.myprofile.ui.EditProfileActivity
import com.aks.doggydoo.utils.MyApp

class NotificationAdapter(
    var context: Context,
    var comingFrom: String,
    var titleList: List<String>,
    private var getStatusOfType: () -> Unit
) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    var mapType:String ="Standard"
    private val descriptionList = listOf( "Change map styles.", "Edit/Update your profile information.")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding =
            SingleNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        if (comingFrom == "Setting") {
            holder.binding.check.hide()
            holder.binding.notificationDescription.text = descriptionList[position]
        }
        holder.bind(titleList[position])
    }

    override fun getItemCount(): Int {
        return titleList.size
    }

    inner class NotificationViewHolder(var binding: SingleNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String) {
            binding.notificationTitle.text = title

            binding.main.setOnClickListener{
                when(title){
                    "Account Settings"->{

                    }

                    "Map Settings"->{
                        mapStyleDialog()
                    }

                    "Edit Profile"->{
                        context.startActivity(
                            Intent(context, EditProfileActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                    }
                }
            }
        }
    }


    private fun mapStyleDialog() {
        val dialog = Dialog(context, R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.gmap_style)

        val cbStandard = dialog.findViewById<View>(R.id.cbStandard) as CheckBox
        val cbSilver = dialog.findViewById<View>(R.id.cbSilver) as CheckBox
        val cbRetro = dialog.findViewById<View>(R.id.cbRetro) as CheckBox
        val cbDark = dialog.findViewById<View>(R.id.cbDark) as CheckBox
        val cbNight = dialog.findViewById<View>(R.id.cbNight) as CheckBox
        val cbAubergine = dialog.findViewById<View>(R.id.cbAubergine) as CheckBox
        val cross = dialog.findViewById<View>(R.id.ivCross) as ImageView
        val btNo = dialog.findViewById<View>(R.id.btNo) as Button

        mapType=MyApp.getSharedPref().userReqType
        if(mapType.equals("Standard")){
            cbStandard.isChecked=true
        }else if(mapType.equals("Silver")){
            cbSilver.isChecked=true
        }else if(mapType.equals("Retro")){
            cbRetro.isChecked=true
        }else if(mapType.equals("Dark")) {
            cbDark.isChecked = true
        }else if(mapType.equals("Night")) {
        cbNight.isChecked = true
        }else if(mapType.equals("Aubergine")) {
            cbAubergine.isChecked = true
        }

    cbStandard.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b){
                mapType =  "Standard"
                cbSilver.isChecked = false
                cbRetro.isChecked = false
                cbDark.isChecked = false
                cbNight.isChecked = false
                cbAubergine.isChecked = false
            }
        })

        cbSilver.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b){
                mapType =  "Silver"
                cbStandard.isChecked = false
                cbRetro.isChecked = false
                cbDark.isChecked = false
                cbNight.isChecked = false
                cbAubergine.isChecked = false
            }
        })

        cbRetro.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b){
                mapType =  "Retro"
                cbStandard.isChecked = false
                cbSilver.isChecked = false
                cbDark.isChecked = false
                cbNight.isChecked = false
                cbAubergine.isChecked = false
            }
        })

        cbDark.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b){
                mapType =  "Dark"
                cbStandard.isChecked = false
                cbSilver.isChecked = false
                cbRetro.isChecked = false
                cbNight.isChecked = false
                cbAubergine.isChecked = false
            }
        })

        cbNight.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b){
                mapType =  "Night"
                cbStandard.isChecked = false
                cbSilver.isChecked = false
                cbRetro.isChecked = false
                cbDark.isChecked = false
                cbAubergine.isChecked = false
            }
        })

        cbAubergine.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b){
                mapType =  "Aubergine"
                cbStandard.isChecked = false
                cbSilver.isChecked = false
                cbRetro.isChecked = false
                cbDark.isChecked = false
                cbNight.isChecked = false
            }
        })

        cross.setOnClickListener {
            dialog.dismiss()
        }

        btNo.setOnClickListener {
            MyApp.getSharedPref().userReqType = mapType
            System.out.println("map style>>" + MyApp.getSharedPref().userReqType)
            context.startActivity(
                Intent(context, HomeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            dialog.dismiss()
        }

        dialog.show()
    }
}