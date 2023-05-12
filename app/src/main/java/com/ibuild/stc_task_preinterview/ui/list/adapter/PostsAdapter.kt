package com.ibuild.stc_task_preinterview.ui.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibuild.stc_task_preinterview.utils.common.Constants
import com.ibuild.stc_task_preinterview.data.model.PostsResult
import com.ibuild.stc_task_preinterview.databinding.ItemLayoutBinding
import com.ibuild.stc_task_preinterview.utils.common.onItemsClickListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsAdapter @Inject constructor() :
    PagingDataAdapter<PostsResult, PostsAdapter.ViewHolder>(differCallback) {
    private lateinit var onItemClickListener: onItemsClickListener<PostsResult>
    private lateinit var binding: ItemLayoutBinding
    private lateinit var context: Context

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<PostsResult>() {
            override fun areItemsTheSame(oldItem: PostsResult, newItem: PostsResult): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: PostsResult, newItem: PostsResult): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ViewHolder : RecyclerView.ViewHolder(this.binding.root) {
        fun bind(item: PostsResult) {
            binding?.apply {
                Glide.with(context).load(Constants.IMAGES_BASE_URL + item.image)
                    .into(binding.itemImage)
                binding.itemTitle.text = item.title
                root.setOnClickListener {
                    onItemClickListener.onItemClicked(item)
                }
            }
        }
    }

    fun setOnItemsClickListener(listener: onItemsClickListener<PostsResult>) {
        this.onItemClickListener = listener
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemLayoutBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder()
    }
}