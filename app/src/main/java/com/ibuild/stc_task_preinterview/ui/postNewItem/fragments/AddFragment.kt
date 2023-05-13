package com.ibuild.stc_task_preinterview.ui.postNewItem.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ibuild.stc_task_preinterview.R
import com.ibuild.stc_task_preinterview.databinding.FragmentAddBinding
import com.ibuild.stc_task_preinterview.ui.postNewItem.viewmodel.AddItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException

@AndroidEntryPoint
class AddFragment : Fragment() {
    private val REQUEST_PERMISSION = 100
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2
    lateinit var filePart: MultipartBody.Part
    lateinit var filePartImage: MultipartBody.Part


    lateinit var binding: FragmentAddBinding
    val viewModel: AddItemViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.cameraBtn.setOnClickListener { capturePhoto() }
        binding.galleryBtn.setOnClickListener { openGallery() }
        binding.addBtn.setOnClickListener { addNewPost() }
    }

    private fun addNewPost() {
        try {
            viewModel.addNewPost(binding.addtitleEdt.text.toString(), filePartImage)
            viewModel.observePostsLiveData().observe(viewLifecycleOwner) {

                showMessage()
                findNavController().popBackStack()
            }
        } catch (e: IOException) {
            Log.d("errorApi", e.toString())
        }

    }

    private fun showMessage() {
        Toast.makeText(context,resources.getString(R.string.message),Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION
            )
        }
    }

    fun capturePhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }

    private fun openGallery() {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            activity?.packageManager?.let {
                intent.resolveActivity(it)?.also {
                    startActivityForResult(intent, REQUEST_PICK_IMAGE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                val bitmap = data?.extras?.get("data") as Bitmap
                binding.addImg.setImageBitmap(bitmap)
                uploadFile(context?.let { getImageUriFromBitmap(it, bitmap) })
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                val uri = data?.data
                binding.addImg.setImageURI(uri)
                data?.let { data ->
                    uploadFile(data.data)
                }

            }
        }
    }
    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    private fun uploadFile(uri: Uri?) {
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

    }
}

