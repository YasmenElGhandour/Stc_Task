package com.ibuild.stc_task_preinterview.ui.list.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ibuild.stc_task_preinterview.utils.common.Constants
import com.ibuild.stc_task_preinterview.databinding.FragmentDetailsBinding

import com.ibuild.stc_task_preinterview.data.model.PostsResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

     lateinit var binding: FragmentDetailsBinding
     val args: DetailsFragmentArgs by navArgs()
     lateinit var item : PostsResult
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        item = args.result
        setData()
    }

    private fun setData() {
        binding.detailstitle.text = item.title
        binding.detailsDesc.text = item.details
        Glide.with(this).load(Constants.IMAGES_BASE_URL + item.image).into(binding.detailsImg)
    }
}