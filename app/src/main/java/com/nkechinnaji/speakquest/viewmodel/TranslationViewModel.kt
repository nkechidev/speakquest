package com.nkechinnaji.speakquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nkechinnaji.speakquest.BuildConfig
import com.nkechinnaji.speakquest.api.DetectRequest
import com.nkechinnaji.speakquest.api.Language
import com.nkechinnaji.speakquest.api.LibreTranslateService
import com.nkechinnaji.speakquest.api.TranslateRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TranslationUiState(
    val translatedText: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val languages: List<Language> = emptyList(),
    val detectedLanguage: String? = null
)

class TranslationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TranslationUiState())
    val uiState: StateFlow<TranslationUiState> = _uiState.asStateFlow()

    // API key from BuildConfig (set in build.gradle.kts per flavor)
    private val apiKey: String? = BuildConfig.LIBRETRANSLATE_API_KEY.ifEmpty { null }

    init {
        loadLanguages()
    }

    fun translate(
        text: String,
        sourceLanguage: String,
        targetLanguage: String
    ) {
        if (text.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Please enter text to translate")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val response = LibreTranslateService.api.translate(
                    TranslateRequest(
                        q = text,
                        source = sourceLanguage,
                        target = targetLanguage,
                        api_key = apiKey
                    )
                )
                _uiState.value = _uiState.value.copy(
                    translatedText = response.translatedText,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Translation failed",
                    isLoading = false
                )
            }
        }
    }

    fun detectLanguage(text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            try {
                val response = LibreTranslateService.api.detectLanguage(
                    DetectRequest(q = text, api_key = apiKey)
                )
                if (response.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        detectedLanguage = response.first().language
                    )
                }
            } catch (e: Exception) {
                // Silently fail - detection is optional
            }
        }
    }

    private fun loadLanguages() {
        viewModelScope.launch {
            try {
                val languages = LibreTranslateService.api.getLanguages()
                _uiState.value = _uiState.value.copy(languages = languages)
            } catch (e: Exception) {
                // Use default common languages if API fails
                _uiState.value = _uiState.value.copy(
                    languages = listOf(
                        Language("en", "English"),
                        Language("es", "Spanish"),
                        Language("fr", "French"),
                        Language("de", "German"),
                        Language("it", "Italian"),
                        Language("pt", "Portuguese"),
                        Language("ru", "Russian"),
                        Language("zh", "Chinese"),
                        Language("ja", "Japanese"),
                        Language("ko", "Korean"),
                        Language("ar", "Arabic")
                    )
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearTranslation() {
        _uiState.value = _uiState.value.copy(translatedText = "", detectedLanguage = null)
    }
}
