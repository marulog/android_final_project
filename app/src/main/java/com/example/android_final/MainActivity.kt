package com.example.android_final

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)

        fetchImage()
    }

    private fun fetchImage() {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val api: RestApi = Retrofit.Builder()
            .baseUrl("https://via.placeholder.com/") // URL 수정
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RestApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.getImage().execute()
                if (response.isSuccessful) {
                    val img: ResponseBody? = response.body()
                    val bitmap = BitmapFactory.decodeStream(img?.byteStream())

                    runOnUiThread {
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap)
                            Log.d("MainActivity", "Image loaded successfully")
                        } else {
                            Log.e("MainActivity", "Bitmap is null")
                        }
                    }
                } else {
                    Log.e("MainActivity", "Response not successful: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error fetching image", e)
            }
        }
    }
}
