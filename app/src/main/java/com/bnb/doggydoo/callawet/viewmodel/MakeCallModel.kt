package com.bnb.doggydoo.callawet.viewmodel

import androidx.lifecycle.ViewModel
import com.bnb.doggydoo.callawet.datasource.repo.call.CallRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MakeCallModel @Inject constructor(var callrepo: CallRepo) : ViewModel() {
    fun getCallData(user_id: String,
                    doctor_id: String,
                    reason: String,
                    type: String) = callrepo.getCallLiveData(user_id,doctor_id,reason,type)
}