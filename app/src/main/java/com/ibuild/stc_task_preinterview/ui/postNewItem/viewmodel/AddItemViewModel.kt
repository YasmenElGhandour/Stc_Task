package com.ibuild.stc_task_preinterview.ui.postNewItem.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.test.platform.app.InstrumentationRegistry
import com.ibuild.stc_task_preinterview.R
import com.ibuild.stc_task_preinterview.data.model.PostsResult
import com.ibuild.stc_task_preinterview.ui.postNewItem.repositry.AddItemRepository
import com.ibuild.stc_task_preinterview.utils.common.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject


@HiltViewModel
class AddItemViewModel @Inject constructor(var addItemRepository: AddItemRepository) : ViewModel() {
    val postLiveData = MutableLiveData<PostsResult>()
    lateinit var filePartImage: MultipartBody.Part

    fun addNewPost(context: Context, title: String, filePart: MultipartBody.Part?, test: Boolean) {
        viewModelScope.launch {
        if (isValidImage(context, filePart,test) && isValidTitle(context, title,test))
                try {
                    val response = addItemRepository.addNewPost(title, title, filePart)
                    postLiveData.postValue(response.body())
                } catch (e: Exception) {
                    Log.d("addNewPost", e.toString())
                }
            }


    }


     fun isValidTitle(context: Context, title: String , test:Boolean): Boolean {
        if (title.isNullOrEmpty()) {
            context?.let {
                if (!test)
                    showMessage(context.resources.getString(R.string.title_validation), it)

            }
            return false
        } else
            return true
    }

     fun isValidImage(context: Context, filePartImage: MultipartBody.Part?, test:Boolean): Boolean {
        if (filePartImage != null)
            return true
        else {
                context?.let {
                    if (!test)
                    showMessage(
                        context.resources.getString(R.string.image_validation),
                        it
                    )
                }

            return false
        }
    }

    fun observePostsLiveData(): LiveData<PostsResult> {
        return postLiveData
    }

    fun openGalleryIntent(): Intent {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            return intent
        }
    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    fun uploadFile(uri: Uri?, activity: Activity): MultipartBody.Part {
        val imageType = activity?.contentResolver?.getType(uri!!)
        val extension = imageType!!.substring(imageType.indexOf("/") + 1)
        uri?.let {
            activity?.contentResolver?.openInputStream(it)?.use { inputStream ->
                filePartImage = MultipartBody.Part.createFormData(
                    "image",
                    "image.$extension",
                    inputStream.readBytes().toRequestBody("*/*".toMediaType())
                )
            }
        }
        return filePartImage
    }

    fun checkCameraPermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA),
                Constants.REQUEST_PERMISSION
            )
        }
    }

     fun showMessage(msg: String, context: Context) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

     }

}
