package com.example.travelapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TravelViewModel : ViewModel() {
    private val _recs = MutableStateFlow<List<String>>(emptyList())
    val recs: StateFlow<List<String>> = _recs

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun solicitarRecomendacion(req: TravelRequest) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val resp = ApiClient.travelApi.predict(req)
                _recs.value = resp.recomendaciones
            } catch (e: Exception) {
                _recs.value = listOf("Error: ${e.localizedMessage}")
            } finally {
                _loading.value = false
            }
        }
    }
}
