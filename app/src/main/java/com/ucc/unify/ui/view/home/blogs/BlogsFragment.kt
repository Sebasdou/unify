package com.ucc.unify.ui.view.home.blogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.ucc.unify.data.model.Comment
import com.ucc.unify.data.model.Publication
import com.ucc.unify.data.model.User
import com.ucc.unify.databinding.FragmentBlogsBinding
import com.ucc.unify.ui.view.home.blogs.comments.CommentsFragment

class BlogsFragment : Fragment() {
    private var _binding: FragmentBlogsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBlogsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val publicationsAdapter = PublicationsAdapter(::showComments)
        val users = listOf(User("María Rivero"), User("Juan Escutia"),
            User("Francisco Mader"), User("Rodolfo Márquez")
        )
        binding.rvPublications.adapter = publicationsAdapter
        publicationsAdapter.submitList(
            listOf(
                Publication(0, users[0], "Saqué 100 en ingles!!",
                    listOf(Comment(0, users[3], "Felicidades", "2022-12-01 12:30"),
                        Comment(1, users[1], "Felicidades", "2022-12-01 12:32")),
                    "2022-12-01 12:02"
                ),
                Publication(0, users[2], "Aburrido!!",
                    listOf(Comment(2, users[1], "qué haces?", "2022-12-01 12:30")),
                    "2022-12-01 12:02"
                ),
                Publication(0, users[3], "Está lleno el gimnasio?",
                    listOf(Comment(3, users[2], "No sé", "2022-12-01 12:30"),
                        Comment(4, users[1], "Nooo!", "2022-12-01 12:32")),
                    "2022-12-01 12:02"
                ),
                Publication(0, users[3], "Está lleno el gimnasio?",
                    listOf(Comment(5, users[2], "No sé", "2022-12-01 12:30"),
                        Comment(6, users[1], "Nooo!", "2022-12-01 12:32")),
                    "2022-12-01 12:02"
                ),
                Publication(0, users[1], "Cuándo hay exámen?",
                    listOf(Comment(7, users[2], "Ayer", "2022-12-01 12:30")),
                    "2022-12-01 12:02"
                ),
                Publication(0, users[3], "Qué comieron?",
                    listOf(Comment(8, users[2], "Nada", "2022-12-01 12:30")),
                    "2022-12-01 12:02"
                ),
            )
        )
    }
    private fun showComments (comments: List<Comment>) {
        val _comments = comments.map { it.comment }.toTypedArray()
        val _users = comments.map { it.user.name }.toTypedArray()
        val _dates = comments.map { it.date }.toTypedArray()
        val modalBottomSheet = CommentsFragment.newInstance(_comments, _users, _dates)
        modalBottomSheet.show(requireActivity().supportFragmentManager, "CommentsBottomSheetFragment")
    }
}