package com.nicolas.nicolsflix.upcoming.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicolas.nicolsflix.databinding.UpcomingFragmentBinding
import com.nicolas.nicolsflix.upcoming.adapters.UpcomingAdapter
import com.nicolas.nicolsflix.upcoming.domain.model.UpcomingUiDomain
import com.nicolas.nicolsflix.upcoming.utils.DataState
import com.nicolas.nicolsflix.utils.hide
import com.nicolas.nicolsflix.utils.show
import com.nicolas.nicolsflix.utils.showToast
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
        viewModel.run {
            getMovieComing()
            movieUpcoming.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is DataState.Loading -> {
                    }
                    is DataState.Success -> {
                        setRecyclerMovieUpcoming(state.result)
                    }
                    is DataState.Empty, is DataState.Error -> {
                        showToast("No connection internet.")
                    }
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