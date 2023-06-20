package kz.kcell.kcellbootcamp.presentation.moviesDetail


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kz.kcell.kcellbootcamp.BuildConfig
import kz.kcell.kcellbootcamp.R
import kz.kcell.kcellbootcamp.data.entities.Movie
import kz.kcell.kcellbootcamp.databinding.FragmentMovieDetailBinding
import kz.kcell.kcellbootcamp.utils.Constants.ARG_MOVIE_ID
import kz.kcell.kcellbootcamp.utils.Resource
import kz.kcell.kcellbootcamp.utils.loadImage
import kz.kcell.kcellbootcamp.utils.toast

@AndroidEntryPoint
class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    private val binding by viewBinding(FragmentMovieDetailBinding::bind)
    private val viewModel by viewModels<MovieDetailViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        arguments?.getInt(ARG_MOVIE_ID)?.let { viewModel.onSetId(it) }
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.content.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    bindMovie(resource.data)
                    binding.progressBar.visibility = View.GONE
                }
                Resource.Status.ERROR ->
                    resource.message?.let { msg -> toast(requireContext(), msg) }
                Resource.Status.LOADING ->
                    binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun bindMovie(movie: Movie?) = with(binding) {
        movie?.let {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = movie.title
            movieItemTitle.text = movie.title
            detailDesc.text = movie.overview
            movieItemRelease.text = movie.releaseDate
            movieItemRatingbar.rating = movie.voteAverage.toFloat()
            movieItemPoster.loadImage(
                imageUrl = BuildConfig.IMAGE_URL + movie.posterPath,
                progressBar = imageProgressBar
            )
        }
    }
}
