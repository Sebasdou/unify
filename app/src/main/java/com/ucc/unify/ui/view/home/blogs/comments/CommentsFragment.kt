package com.ucc.unify.ui.view.home.blogs.comments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ucc.unify.data.model.Comment
import com.ucc.unify.data.model.User
import com.ucc.unify.databinding.FragmentCommentsBinding

class CommentsFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentCommentsBinding? = null
    private val binding get() = _binding!!

    private var comments = emptyList<Comment>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCommentsBinding.inflate(inflater, container, false)
        val temp = arguments?.getStringArray(COMMENTS)!!
        val temp2 = arguments?.getStringArray(USERS)!!
        val temp3 = arguments?.getStringArray(DATES)!!
        var id = 0
        val commentsTemp = comments.toMutableList()
        for(i in temp.indices){
            commentsTemp.add(Comment(i, User(temp2.get(i)), temp[i], temp3[i]))
        }
        comments = commentsTemp
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val commentsAdapter = CommentsAdapter()
        binding.rvComments.adapter = commentsAdapter
        commentsAdapter.submitList(comments)
    }

    companion object {
        private val COMMENTS = "comments"
        private val USERS = "users"
        private val DATES = "dates"
        fun newInstance(comments: Array<String>, users: Array<String>, dates: Array<String>) =
            CommentsFragment().apply {
                arguments = Bundle().apply {
                    putStringArray(COMMENTS, comments)
                    putStringArray(USERS, users)
                    putStringArray(DATES, dates)
                }
            }
    }
}