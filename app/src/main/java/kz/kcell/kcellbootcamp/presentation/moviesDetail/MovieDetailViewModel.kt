package kz.kcell.kcellbootcamp.presentation.moviesDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.kcell.kcellbootcamp.data.entities.Movie
import kz.kcell.kcellbootcamp.data.repository.MovieRepository
import kz.kcell.kcellbootcamp.utils.Resource
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _id = MutableLiveData<Int>()

    val content: LiveData<Resource<Movie>> = _id.switchMap { id ->
        repository.getMovie(id)
    }

    fun onSetId(id: Int) {
        _id.value = id
    }
}