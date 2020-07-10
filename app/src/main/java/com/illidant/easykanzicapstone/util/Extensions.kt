package com.illidant.easykanzicapstone.util

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.text.Html
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Example
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

object Extensions {

    fun Context.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @Throws(IOException::class)
    fun HttpURLConnection.build(method: String) = apply {
        requestMethod = method
        connect()
    }

    @Throws(IOException::class)
    fun InputStreamReader.getString(): String {
        val reader = BufferedReader(this)
        return StringBuilder().apply {
            do {
                val inputLine = reader.readLine()
                inputLine?.let { append(inputLine) }
            } while (inputLine != null)
            reader.close()
        }.toString()
    }

//    fun String.parseImage(): String = this.let {
//        try {
//            val imageUrl = DataRequest(
//                Constants.SCHEME_HTTP,
//                Constants.AUTHORITY_MINNA_MAZIL,
//                listOf(Constants.PATH_IMAGE, Constants.PATH_IKANJI, "$it.jpg")
//            ).toUrl()
//            val connection = URL(imageUrl).openConnection()
//            val inputStream = connection.getInputStream()
//            Base64.encodeToString(inputStream.readBytes(), Base64.DEFAULT)
//        } catch (e: IOException) {
//            Constants.EMPTY_STRING
//        }
//    }

    fun MutableList<Example>.parseExamples(input: String) = this.apply {
        val examplesString = input.split(Constants.CHARACTER_SPLIT_1)
        for (index in 1 until examplesString.size) {
            val exampleString = examplesString[index].split(Constants.CHARACTER_SPLIT_2)
            add(Example(exampleString[0], exampleString[1], exampleString[2]))
        }
    }

    fun ImageView.setImage(input: String) = this.apply {
        if (input.isNotEmpty()) {
            val byteArray = Base64.decode(input, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            setImageBitmap(bitmap)
        } else {
            setImageResource(R.mipmap.ic_launcher)
        }
    }

    fun TextView.setHtmlText(input: String) = this.apply {
        text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(input, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(input)
        }
    }

    fun String.getStringBySplit(input: Char, index: Int = 0) = split(input)[index]
}
