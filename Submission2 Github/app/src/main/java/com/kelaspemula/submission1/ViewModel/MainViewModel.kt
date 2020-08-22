package com.kelaspemula.submission1.ViewModel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kelaspemula.submission1.Activity.DetailAcivity
import com.kelaspemula.submission1.BuildConfig
import com.kelaspemula.submission1.Fragment.FollowerFragment
import com.kelaspemula.submission1.Model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_follower.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainViewModel : ViewModel() {
    companion object {
        const val AUTH = "Authorization"
        const val AGENT = "User-Agent"
        const val REQ = "request"
    }

    val listUsers = MutableLiveData<ArrayList<User>>()

    fun homeData() {
        val client = AsyncHttpClient()
        val url = " https://api.github.com/search/users?q=a"
        client.addHeader(AUTH, BuildConfig.ApiKey)
        client.addHeader(AGENT, REQ)
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                val listUser = ArrayList<User>()
                val result = String(responseBody)

                try {
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val user = User()
                        user.name   = item.getString("login")
                        user.photo = item.getString("avatar_url")
                        user.type = item.getString("type")
                        listUser.add(user)
                    }
                    listUsers.postValue(listUser)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
            }
        })
    }

    fun setUser(username: String) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"
        client.addHeader(AUTH, BuildConfig.ApiKey)
        client.addHeader(AGENT, REQ)
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                val listUser = ArrayList<User>()
                val result = String(responseBody)

                try {
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val user = User()
                        user.name   = item.getString("login")
                        user.photo = item.getString("avatar_url")
                        user.type = item.getString("type")
                        listUser.add(user)
                    }
                    listUsers.postValue(listUser)
                } catch (e: Exception) {
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
            }
        })
    }

    fun getUser(): LiveData<ArrayList<User>> {
        return listUsers
    }
}