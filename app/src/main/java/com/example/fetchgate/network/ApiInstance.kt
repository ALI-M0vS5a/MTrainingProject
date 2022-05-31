package com.example.fetchgate.network



import com.example.fetchgate.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiInstance {
    companion object {

        private const val baseURL = "https://api.github.com/orgs/google/"
        private const val baseAuthURL = "https://alimoussa.000webhostapp.com/week10_finalRevision/"

        fun getApiInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun <Api> buildApi(
            api: Class<Api>,
            authToken: String? = null
        ): Api {
            return Retrofit.Builder()
                .baseUrl(baseAuthURL)
                .client(OkHttpClient.Builder()
                    .addInterceptor{ chain ->
                        chain.proceed(chain.request().newBuilder().also {
                            it.addHeader("Authorization","Bearer $authToken")
                        }.build())
                    }
                    .also {
                    if (BuildConfig.DEBUG) {
                        val logging = HttpLoggingInterceptor()
                        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
                        it.addInterceptor(logging)
                    }
                }.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(api)

        }
    }
}