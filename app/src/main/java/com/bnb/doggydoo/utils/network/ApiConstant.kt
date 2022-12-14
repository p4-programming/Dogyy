package com.bnb.doggydoo.utils.network


object ApiConstant {

    //"https://clients.aksinteractive.com/doggydo/API/"
    const val API_BASE_URL = "https://doggydoo.in/API/"
    const val HEADER_CACHE_CONTROL = "Cache-Control"
    const val HEADER_PRAGMA = "Pragma"
    const val CONNECT_TIME_OUT = 30L
    const val READ_TIME_OUT = 30L
    const val WRITE_TIME_OUT = 60L
    const val NETWORK_INTERCEPTOR_MAX_AGE = 5

    //image constants
    const val PROFILE_IMAGE_BASE_URL =
        "https://doggydoo.in/assets/uploads/profile/"
    const val BLOG_IMAGE_BASE_URL = "https://doggydoo.in/assets/uploads/blog/"
    const val PLACE_IMAGE_BASE_URL =
        "https://doggydoo.in/assets/uploads/place/"
    const val PET_IMAGE_BASE_URL = "https://doggydoo.in/assets/uploads/pets/"
    const val PET_DOC_IMAGE_BASE_URL =
        "https://doggydoo.in/uploads/pet_document/"
    const val NEARBY_DOC_IMAGE_BASE_URL =
        "https://doggydoo.in/assets/uploads/doctor/"
    const val NEARBY_HOS_IMAGE_BASE_URL =
        "https://doggydoo.in/assets/uploads/place/"
}