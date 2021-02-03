package com.nicolas.nicolsflix.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nicolas.nicolsflix.R
import com.nicolas.nicolsflix.data.db.NicolsDatabase
import com.nicolas.nicolsflix.data.model.Movie
import com.nicolas.nicolsflix.data.repository.DatabaseDataSource
import com.nicolas.nicolsflix.ui.details.adapter.MovieSimilarAdapter
import com.nicolas.nicolsflix.utils.FormatDate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.details_fragment.*

class DetailsFragment : Fragment(R.layout.details_fragment) {

    private val viewModel: DetailsViewModel by viewModels(
        factoryProducer = {
            val database = NicolsDatabase.getInstance(requireContext())
            DetailsViewModelFactory(databaseDataSource = DatabaseDataSource(database.movieDao))
        }
    )
    private val args: DetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponentsDetails()
    }

    private fun initComponentsDetails() {

        val detailsMovieArgs = args.movie
        backDetails.setOnClickListener {
            findNavController().popBackStack()
        }

        textNameMovie.text = detailsMovieArgs.title
        textValueRatingMovie.text = detailsMovieArgs.rating
        textDateMovie.text = detailsMovieArgs.date?.let { FormatDate.getDateMovie(it) }
        textDescriptionMovie.text = detailsMovieArgs.description
        Picasso.get().load("https://image.tmdb.org/t/p/w500/${detailsMovieArgs.posterDetails}")
            .into(imagePosterMovie)

        saveMovieToList(detailsMovieArgs)

        detailsMovieArgs.id?.let { viewModel.getMovieSimilar(it) }
        viewModel.listMovieSimilar.observe(viewLifecycleOwner) { listMovie ->

            if (listMovie.isNullOrEmpty()) {
                movieSimiliarRecyclerView.visibility = View.GONE
                textSimilar.visibility = View.GONE
            } else {
                with(movieSimiliarRecyclerView) {
                    layoutManager =
                        LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                    setHasFixedSize(true)
                    adapter = MovieSimilarAdapter(listMovie) {
                        val directions = DetailsFragmentDirections.actionDetailsFragmentSelf(it)
                        findNavController().navigate(directions)
                    }
                }
            }
        }
    }

    private fun saveMovieToList(movie: Movie) {

        viewModel.isMovieMyList(movie)
        viewModel.isMovieMyList.observe(viewLifecycleOwner) { isMovieSave ->
            if (isMovieSave) {

                imageSaveMovie.setOnClickListener(deleteMovieMyList(movie))
                imageSaveMovie.setImageResource(R.drawable.ic_round_check)

            } else {
                imageSaveMovie.setOnClickListener(addMovieMyList(movie))
                imageSaveMovie.setImageResource(R.drawable.ic_round_add)
            }
        }
    }

    private fun addMovieMyList(movie: Movie) = View.OnClickListener {
        viewModel.insertMovieMyList(movie)
        imageSaveMovie.setImageResource(R.drawable.ic_round_check)
        Toast.makeText(requireContext(),"Movie saved to your list.", Toast.LENGTH_SHORT).show()
    }

    private fun deleteMovieMyList(movie: Movie) = View.OnClickListener {
        viewModel.deleteMovieMyList(movie)
        imageSaveMovie.setImageResource(R.drawable.ic_round_add)
        Toast.makeText(requireContext(),"Movie removed from your list.", Toast.LENGTH_SHORT).show()
    }
}