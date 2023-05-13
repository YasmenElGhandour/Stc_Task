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
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ibuild.stc_task_preinterview.R
import com.ibuild.stc_task_preinterview.databinding.FragmentAddBinding
import com.ibuild.stc_task_preinterview.ui.postNewItem.viewmodel.AddItemViewModel
import com.ibuild.stc_task_preinterview.utils.common.Constants
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException

@AndroidEntryPoint
class AddFragment : Fragment() {

    var filePartImage: MultipartBody.Part? = null
    lateinit var binding: FragmentAddBinding
    val viewModel: AddItemViewModel by viewModels()
    var uri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.addImgCard.setOnClickListener { openBottomSheet() }
        binding.addBtn.setOnClickListener {
            addNewPost()
        }
    }

    private fun openBottomSheet() {
        val dialog = context?.let { it1 -> BottomSheetDialog(it1) }
        val view = layoutInflater.inflate(R.layout.dialog_choose_image, null)
        view.findViewById<TextView>(R.id.btn_take_picture).setOnClickListener {
            capturePhoto()
            dialog?.dismiss()
        }
        view.findViewById<TextView>(R.id.btn_from_gallery).setOnClickListener {
            openGallery()
            dialog?.dismiss()
        }
        dialog?.setContentView(view)
        dialog?.show()
    }

    private fun openGallery() {
        activity?.packageManager?.let {
            viewModel.openGalleryIntent().let { intent ->
                intent.resolveActivity(it)?.also {
                    startActivityForResult(intent, Constants.REQUEST_PICK_IMAGE)
                }
            }
        }

    }

    private fun addNewPost() {
        context?.let { viewModel.addNewPost(it, binding.addtitleEdt.text.toString(), filePartImage) }
        observePostsList()
    }

    private fun observePostsList() {
        viewModel.observePostsLiveData().observe(viewLifecycleOwner) {
            context?.let { it1 ->
                viewModel.showMessage(resources.getString(R.string.message), it1) }
                back()
        }
    }

    private fun back() {
        findNavController().popBackStack()
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkCameraPermission(requireActivity())
    }

    private fun capturePhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, Constants.REQUEST_IMAGE_CAPTURE)
    }

    private fun setImageFromGallery(data: Intent?) {
        uri = data?.data
        binding.addImg.setImageURI(uri)
        uri?.let { data -> activity?.let {
            filePartImage = viewModel.uploadFile(uri, it)
        } }

    }

    private fun setImageFromCamera(data: Intent?) {
        val bitmap = data?.extras?.get("data") as Bitmap
        binding.addImg.setImageBitmap(bitmap)
        uri = context?.let { context -> viewModel.getImageUriFromBitmap(context, bitmap) }
        uri?.let { data -> activity?.let {
            filePartImage = viewModel.uploadFile(uri, it)
        } }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUEST_IMAGE_CAPTURE) {
                setImageFromCamera(data)
            } else if (requestCode == Constants.REQUEST_PICK_IMAGE) {
                setImageFromGallery(data)

            }
        }
    }


}

