package com.kelaspemula.submission1.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kelaspemula.submission1.Adapter.SectionsPagerAdapter
import com.kelaspemula.submission1.Adapter.UserAdapter
import com.kelaspemula.submission1.R
import com.kelaspemula.submission1.Model.User
import com.kelaspemula.submission1.ViewModel.MainViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail_acivity.*
import org.json.JSONObject

class DetailAcivity : AppCompatActivity() {
    companion object {
        const val EXTRA_VALUE = "extra_value"
    }

    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_acivity)

        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.detailUser)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        pb_detail.visibility = View.VISIBLE

        initItem()
        initData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initData() {
        val user = intent.getParcelableExtra(EXTRA_VALUE) as User
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = user.name

        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
    }

    private fun initItem() {
        val user = intent.getParcelableExtra(EXTRA_VALUE) as User
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/${user.name}"
        client.addHeader("Authorization", "token a55286e4a0babdcb7d28e1bcb121da749828b374")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                val listDetail = ArrayList<User>()
                val result = String(responseBody)
                pb_detail.visibility = View.INVISIBLE
                Log.d("resultt", result.toString())

                try {
                    val item = JSONObject(result)
                    user.location = item.getString("location")
                    user.company = item.getString("company")
                    user.username = item.getString("name")
                    user.login = item.getString("login")
                    user.avatar = item.getString("avatar_url")

                    Glide.with(this@DetailAcivity).load(user.avatar).into(img_detail)
                    tv_username.text = user.username
                    tv_login.text = user.login
                    tv_location.text = user.location
                    tv_company.text = user.company

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable
            ) {
                pb_detail.visibility = View.INVISIBLE
                Log.d("onFailur", error.message.toString())
            }
        })
    }
}