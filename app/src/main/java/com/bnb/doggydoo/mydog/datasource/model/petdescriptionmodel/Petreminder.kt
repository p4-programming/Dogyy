package com.bnb.doggydoo.mydog.datasource.model.petdescriptionmodel
import com.google.gson.annotations.SerializedName

data class Petreminder (
	@SerializedName("reminder_id") val reminder_id : String,
	@SerializedName("Type") val type : String,
	@SerializedName("create") val create : String?,
	@SerializedName("Bath_img") val bath_img : String?
)