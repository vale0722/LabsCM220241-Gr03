package com.example.jetcaster.core.data.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration

object RetrofitClient {
    private const val BASE_URL = "http://demo3776148.mockable.io/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(OffsetDateTime::class.java, OffsetDateTimeAdapter())
        .registerTypeAdapter(Duration::class.java, DurationAdapter())
        .create()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}


class OffsetDateTimeAdapter : TypeAdapter<OffsetDateTime>() {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    override fun write(out: JsonWriter, value: OffsetDateTime) {
        out.value(value.format(formatter))
    }

    override fun read(input: JsonReader): OffsetDateTime {
        return OffsetDateTime.parse(input.nextString(), formatter)
    }
}


class DurationAdapter : TypeAdapter<Duration>() {
    override fun write(out: JsonWriter, value: Duration) {
        out.value(value.toString())
    }

    override fun read(input: JsonReader): Duration {
        return Duration.parse(input.nextString())
    }
}