package com.ibuild.stc_task_preinterview.ui.list.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ibuild.stc_task_preinterview.R
import com.ibuild.stc_task_preinterview.data.model.PostsResult
import com.ibuild.stc_task_preinterview.databinding.FragmentListBinding
import com.ibuild.stc_task_preinterview.utils.common.onItemsClickListener
import com.ibuild.stc_task_preinterview.ui.list.adapter.PostsAdapter
import com.ibuild.stc_task_preinterview.ui.list.viewModel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment(), onItemsClickListener<PostsResult> {

    lateinit var binding: FragmentListBinding
    val viewModel: ListViewModel by activityViewModels()

    @Inject
    lateinit var postsAdapter: PostsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentListBinding.inflate(layoutInflater)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        getPosts()
        addPost()
    }

    fun addPost() {
        binding.addFab.setOnClickListener {
            this.findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
    }

    fun getPosts() {
        viewModel.list.observe(viewLifecycleOwner) {
            postsAdapter.submitData(lifecycle, it)
            postsAdapter.setOnItemsClickListener(this)

        }

    }

    fun showProgress(loading: Boolean) {
        if (loading) {
            binding.loadingView.visibility = View.VISIBLE
        } else {
            binding.loadingView.visibility = View.GONE
        }
    }

    fun prepareRecyclerView() {
        binding.rvMovies.apply {
            layoutManager = GridLayoutManager(activity?.applicationContext, 2)
            adapter = postsAdapter
        }
    }

    fun navigateToDetails(item: PostsResult) {
        val action = ListFragmentDirections.actionListFragmentToDetailsFragment(
            item
        )
        this.findNavController().navigate(action)

    }

    override fun onItemClicked(item: PostsResult) {
        navigateToDetails(item)
    }
}