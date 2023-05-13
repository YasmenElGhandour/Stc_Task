package com.ibuild.stc_task_preinterview.utils.common

import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache

class Constants {

    companion object {
        const val  BASE_URL="https://posts-demo-production.up.railway.app/"
        const val IMAGES_BASE_URL="https://posts-demo-production.up.railway.app/"
        const val CACHE_SIZE = 5 * 1024 * 1024L // 5 MB
        const val IMAGE_CHOOSE = 1000;
        const val PERMISSION_CODE = 1001;
    }

}