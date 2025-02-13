package com.nicolas.nicolsflix.upcoming.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicolas.nicolsflix.common.ViewState
import com.nicolas.nicolsflix.databinding.UpcomingFragmentBinding
import com.nicolas.nicolsflix.upcoming.adapters.UpcomingAdapter
import com.nicolas.nicolsflix.upcoming.domain.model.UpcomingUiDomain
import com.nicolas.nicolsflix.upcoming.utils.DataState
import com.nicolas.nicolsflix.common.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpcomingFragment : Fragment() {

    private val viewModel: UpcomingViewModel by viewModel()

    private var _binding: UpcomingFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UpcomingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchMovieUpcoming()
    }

    private fun fetchMovieUpcoming() {
        viewModel.getMovieComing()
        viewModel.movieUpcoming.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ViewState.Loading -> {
                }
                is ViewState.Success -> {
                    setRecyclerMovieUpcoming(state.data)
                }
                is ViewState.Empty, is ViewState.Error -> {
                    showToast("No connection internet.")
                }
            }
        }
    }

    private fun setRecyclerMovieUpcoming(listMovieUpcoming: List<UpcomingUiDomain>) {
        with(binding.reyclerUpcoming) {
            setHasFixedSize(true)
            adapter = UpcomingAdapter(listMovieUpcoming) { movieClick ->

            }
        }
    }
}