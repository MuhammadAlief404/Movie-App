package com.quantumhiggs.movieapp.ui.genres

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.quantumhiggs.movieapp.databinding.BottomSheetGenresFragmentBinding
import com.quantumhiggs.movieapp.di.Injection
import com.quantumhiggs.movieapp.util.Cons.GENRE_KEY

class BottomSheetGenresFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetGenresFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: GenresViewModel
    private val adapter = GenreAdapter { genresItem ->
        findNavController().previousBackStackEntry?.savedStateHandle?.set(GENRE_KEY, genresItem.id)
        findNavController().popBackStack()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetGenresFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory())
            .get(GenresViewModel::class.java)

        viewModel.getGenres()

        binding.listGenre.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.listGenre.adapter = adapter
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.listGenre.addItemDecoration(decoration)

        viewModel.genre.observe(viewLifecycleOwner, Observer {
            adapter.updateData(it.genres)
        })
    }

}