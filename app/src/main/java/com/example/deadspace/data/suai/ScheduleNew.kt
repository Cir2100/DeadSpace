package com.example.deadspace.data.suai

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.deadspace.DeadSpace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.IOException
import java.net.MalformedURLException

suspend inline fun <reified T> getObjectRes(link : String) : Result<T> {

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
    }
}

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            }
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            }
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}

suspend inline fun <reified T> getObject(link : String) : T {

    if (!isOnline(DeadSpace.appContext)) {
        throw Exception("No internet connection")
    }

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

                return@withContext obj
            }
        } catch (e : MalformedURLException) {
            throw e
        } catch (e : IOException) {
            throw e
        } catch (e : Throwable) {
            throw e
        }
    }
}