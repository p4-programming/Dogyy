package com.aks.doggydoo.training.viewmodel

import androidx.lifecycle.ViewModel
import com.aks.doggydoo.training.datasource.repo.TrainingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor(var trainingRepo: TrainingRepo) : ViewModel() {

    fun getTrainingData() =
        trainingRepo.trainingListLiveData()

    fun getTrainingDetailData(id: String) =
        trainingRepo.trainingDetailLiveData(id)

    fun uploadTrainingData(user_id: String, title: String, url: String) =
        trainingRepo.submitTrainingVideoLiveData(user_id, title, url)
}