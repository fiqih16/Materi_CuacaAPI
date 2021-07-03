package com.plete.newyorkweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    private val titleView: TextView by lazy { mainTitle }
    private val statusView: TextView by lazy { status }
    private val descView: TextView by lazy { desc }
    private val cuacaIcon: ImageView by lazy { iconCuaca }

    private val retrofit by lazy {
        Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(MoshiConverterFactory.create()).build()
    }

    private val weatherApiService by lazy {
        retrofit.create(OpenWeatherMapService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherApiService.getWeather("Bekasi", "c89205a53ea42f43546b0c39419f54ee")
                .enqueue(object : Callback<OpenWeatherMapResponseData> {
                    override fun onFailure(call: Call<OpenWeatherMapResponseData>, t: Throwable) {
                        showError("Response failed: ${t.message}")
                    }

                    override fun onResponse(call: Call<OpenWeatherMapResponseData>, response: Response<OpenWeatherMapResponseData>
                    ) = handleResponse(response)
                })
    }

    private fun handleResponse(response: Response<OpenWeatherMapResponseData>) =
            if (response.isSuccessful){
                response.body()?.let {
                    validResponse -> handleValidResponse(validResponse)
                }?:Unit
            } else {
                showError("Response was unsuccessful: ${response.errorBody()}")
            }

    private fun handleValidResponse(response: OpenWeatherMapResponseData){
        titleView.text = response.locationName

        response.weather.firstOrNull()?.let{
            weather -> statusView.text = weather.status
            descView.text = weather.description
            Glide.with(this)
                    .load("http://openweathermap.org/img/wn${weather.icon}@2x.png")
                    .centerInside()
                    .into(cuacaIcon)
        }
    }

    private fun showError(message: String) =
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}