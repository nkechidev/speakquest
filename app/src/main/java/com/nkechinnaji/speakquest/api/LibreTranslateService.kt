package com.nkechinnaji.speakquest.api

import com.nkechinnaji.speakquest.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// Request data class
data class TranslateRequest(
    val q: String,
    val source: String,
    val target: String,
    val format: String = "text",
    val api_key: String? = null
)

// Response data class
data class TranslateResponse(
    val translatedText: String
)

// Language model
data class Language(
    val code: String,
    val name: String
)

// Detect request
data class DetectRequest(
    val q: String,
    val api_key: String? = null
)

// Detect response
data class DetectResponse(
    val confidence: Double,
    val language: String
)

// API Interface
interface LibreTranslateApi {
    @POST("translate")
    suspend fun translate(@Body request: TranslateRequest): TranslateResponse

    @GET("languages")
    suspend fun getLanguages(): List<Language>

    @POST("detect")
    suspend fun detectLanguage(@Body request: DetectRequest): List<DetectResponse>
}

// Singleton service
object LibreTranslateService {
    // URL from BuildConfig (set in build.gradle.kts per flavor)
    private val BASE_URL = BuildConfig.LIBRETRANSLATE_URL

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: LibreTranslateApi by lazy {
        retrofit.create(LibreTranslateApi::class.java)
    }

    // Helper to create service with custom URL (for self-hosted instances)
    fun createApi(baseUrl: String): LibreTranslateApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LibreTranslateApi::class.java)
    }
}
