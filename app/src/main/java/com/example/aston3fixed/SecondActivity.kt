package com.example.aston3fixed

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.webkit.URLUtil
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

const val KEY_IMAGE = "key"
const val IMAGE_START = "https://static3.tgstat.ru/channels/_0/cb/cb672e912fb790639e8a0bce0695b3e2.jpg"

class SecondActivity : AppCompatActivity() {
    private lateinit var imageUrl: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val edSearch = findViewById<EditText>(R.id.edSearch)
        val edSearchCoil = findViewById<EditText>(R.id.edSearchCoil)
        val edSearchPicasso = findViewById<EditText>(R.id.edSearchPicasso)
        val edSearchAndroid = findViewById<EditText>(R.id.edSearchAndroid)
        val imView = findViewById<ImageView>(R.id.imageView)

        imageUrl = savedInstanceState?.getString(KEY_IMAGE)
            ?: IMAGE_START
        renderState(imageUrl, imView)

        edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                imageUrl = edSearch.text.toString()
                if (URLUtil.isValidUrl(imageUrl)) {
                    Glide.with(this@SecondActivity)
                        .load(imageUrl)
                        .centerCrop()
                        .placeholder(R.drawable.baseline_downloading_24)
                        .into(imView)
                } else Toast.makeText(
                    this@SecondActivity,
                    R.string.error_download,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        edSearchCoil.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                imageUrl = edSearchCoil.text.toString()
                if (URLUtil.isValidUrl(imageUrl)) {

                    imView.load(imageUrl) {
                        crossfade(true)
                        crossfade(500)
                        placeholder(R.drawable.baseline_downloading_24)
                        transformations(CircleCropTransformation())
                    }
                } else Toast.makeText(
                    this@SecondActivity,
                    R.string.error_download,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        edSearchPicasso.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                imageUrl = edSearchPicasso.text.toString()
                if (URLUtil.isValidUrl(imageUrl)) {
                    Picasso.with(this@SecondActivity)
                        .load(imageUrl)
                        .placeholder(R.drawable.baseline_downloading_24)
                        .into(imView)
                } else Toast.makeText(
                    this@SecondActivity,
                    R.string.error_download,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        edSearchAndroid.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                imageUrl = edSearchAndroid.text.toString()
                DownloadImageFromInternet(imView).execute(imageUrl)
            }
        })
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_IMAGE, imageUrl)
    }
    private fun renderState(url: String, view: ImageView) {
        Glide.with(this)
            .load(url)
            .into(view)
    }
    @SuppressLint("StaticFieldLeak")
    @Suppress("DEPRECATION")
    private inner class DownloadImageFromInternet(var imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
        init {
            Toast.makeText(applicationContext, "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show()
        }
        override fun doInBackground(vararg urls: String): Bitmap? {
            val imageURL = urls[0]
            var image: Bitmap? = null
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
            }
            catch (e: Exception) {
                Log.e("Error Message", e.message.toString())
                e.printStackTrace()
            }
            return image
        }
        override fun onPostExecute(result: Bitmap?) {
            imageView.setImageBitmap(result)
        }
    }
}


