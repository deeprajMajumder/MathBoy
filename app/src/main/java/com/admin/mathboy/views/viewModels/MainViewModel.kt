package com.admin.mathboy.views.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private var _uiState = MutableStateFlow<String?>(null)
    val uiState: StateFlow<String?> = _uiState.asStateFlow()

    private val _formattedInput = MutableStateFlow<String?>(null)
    val formattedInput: StateFlow<String?> = _formattedInput

    fun updateInput(input: String) {
        val formatted = formatInput(input)
        if (formatted != _formattedInput.value) {
            _formattedInput.value = formatted
        }
    }

    fun formatInput(input: String): String {
        return input.replace(Regex("(\\d)(?=\\d)"), "$1,")
    }

    fun calculateResults(input1: String, input2: String, input3: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list1 = input1.split(',').mapNotNull { it.trim().toIntOrNull() }
            val list2 = input2.split(',').mapNotNull { it.trim().toIntOrNull() }
            val list3 = input3.split(',').mapNotNull { it.trim().toIntOrNull() }

            val intersect = list1.intersect(list2.toSet()).intersect(list3.toSet())
            val union = list1.union(list2).union(list3)
            val maxNumber = union.maxOrNull()

            _uiState.value = """
            Intersection: ${intersect.joinToString(", ")}
            Union: ${union.joinToString(", ")}
            Highest Number: $maxNumber
            """.trimIndent()
        }
    }
}