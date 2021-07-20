package com.example.deadspace.data.suai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.IOException
import java.net.MalformedURLException

suspend inline fun <reified T> getObject(link : String) : Result<T> {

    //TODO : check internet connect

    return withContext(Dispatchers.IO) {

        try {

            val format = Json { coerceInputValues = true }

            val url =  URL(link)

            with(url.openConnection() as HttpURLConnection) {
                setRequestProperty("Content-Type", "application/json; utf-8")
                setRequestProperty("Accept", "application/json")

                var obj: T

                inputStream.bufferedReader().use {
                    obj = format.decodeFromString(it.readText())
                }

                return@withContext Result.Success(obj)
            }
        } catch (e : MalformedURLException) {
            return@withContext Result.Error(Exception(e.message!!))
        } catch (e : IOException) {
            return@withContext Result.Error(Exception(e.message!!))
        }
        return@withContext Result.Error(Exception("Cannot open HttpURLConnection"))
    }
}