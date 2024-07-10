package com.bmprj.weatherforecast.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.weatherforecast.data.remote.api.ApiRepositoryImpl
import com.bmprj.weatherforecast.model.SearchCityItem
import com.bmprj.weatherforecast.util.ApiResources
import com.bmprj.weatherforecast.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val apiRepository: ApiRepositoryImpl,
): ViewModel() {

    private val _search = MutableStateFlow<UiState<List<SearchCityItem>>>(UiState.Loading)
    val search get() = _search.asStateFlow()

    fun refreshData(key:String,query:String){
        getDataFromApi(key, query)
    }

    private fun getDataFromApi(key:String, query:String) = viewModelScope.launch {
        if(query.isEmpty()) return@launch
        apiRepository.getSearch(key,query)
            .onStart {
                _search.emit(UiState.Loading)
            }
            .catch {
                _search.emit(UiState.Error(it))
            }
            .collect{
                when(it){
                    is ApiResources.Failure -> {
                        _search.emit(UiState.Error(Throwable(it.exception.errorMessage)))
                    }
                    is ApiResources.Success -> {
                        _search.emit(UiState.Success(it.result))
                    }
                }

            }

    }
}