package com.kelaspemula.submission1.Activity

import android.content.ContentValues
import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.kelaspemula.submission1.Adapter.SectionsPagerAdapter
import com.kelaspemula.submission1.Adapter.UserAdapter
import com.kelaspemula.submission1.BuildConfig
import com.kelaspemula.submission1.Model.User
import com.kelaspemula.submission1.R
import com.kelaspemula.submission1.ViewModel.MainViewModel
import com.kelaspemula.submission1.db.DatabaseContract
import com.kelaspemula.submission1.db.DatabaseContract.UserColums.Companion.CONTENT_URI
import com.kelaspemula.submission1.db.DatabaseContract.UserColums.Companion.NAME
import com.kelaspemula.submission1.db.DatabaseContract.UserColums.Companion.PHOTO
import com.kelaspemula.submission1.db.DatabaseContract.UserColums.Companion.TYPE
import com.kelaspemula.submission1.db.UserHelper
import com.kelaspemula.submission1.helper.MappingHelper
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail_acivity.*
import kotlinx.android.synthetic.main.item_list.*
import org.json.JSONObject

class DetailAcivity : AppCompatActivity() {
    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mMenu: Menu
    private lateinit var userHelper: UserHelper
    private lateinit var username: String
    private lateinit var photo: String
    private lateinit var type: String

    private lateinit var image: String
    private var statusFav: Boolean = false
    private var user: User? = null
    private lateinit var uriId: Uri


    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_PHOTO = "extra_photo"
        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_VALUE = "extra_value"
        const val AUTH = "Authorization"
        const val AGENT = "User-Agent"
        const val REQ = "request"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_acivity)

        uriId = Uri.parse(CONTENT_URI.toString() + "/" + user?.id)
        val cursor = contentResolver.query(uriId, null, null, null, null)
        if (cursor != null) {
            user = MappingHelper.mapCursorToObject(cursor)
            cursor.close()
        }

        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.detailUser)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        pb_detail.visibility = View.VISIBLE

        initItem()
        favoriteCheck()
        initData()
    }

    private fun initItem() {
        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        val user = intent.getParcelableExtra(EXTRA_VALUE) as User
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/${user.name}"
        client.addHeader(AUTH, BuildConfig.ApiKey)
        client.addHeader(AGENT, REQ)
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                pb_detail.visibility = View.INVISIBLE

                try {
                    val item = JSONObject(result)
                    user.location = item.getString("location")
                    user.company = item.getString("company")
                    user.username = item.getString("name")
                    user.login = item.getString("login")
                    user.avatar = item.getString("avatar_url")

                    Glide.with(this@DetailAcivity).load(user.avatar)
                        .apply {
                            RequestOptions().placeholder(R.drawable.ic_error)
                                .error(R.drawable.ic_error)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .priority(Priority.HIGH)
                        }
                        .into(img_detail)
                    image = user.avatar.toString()
                    tv_username.text = user.username
                    tv_login.text = user.login
                    tv_location.text = user.location
                    tv_company.text = user.company

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
                pb_detail.visibility = View.INVISIBLE
            }
        })
    }

    private fun favoriteCheck() {
        //val username = tv_username.text.toString()
        username = intent?.getStringExtra(user?.name).toString()
        val result = userHelper.queryById(username)
        val favorite = (1..result.count).map {
            result.apply {
                moveToNext()
                getInt(result.getColumnIndexOrThrow(DatabaseContract.UserColums.NAME))
            }
        }
        if (favorite.isNotEmpty()) statusFav = false
    }

    private fun initData() {
        val user = intent.getParcelableExtra(EXTRA_VALUE) as User
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = user.name

        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.like_menu, menu)
        mMenu = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.like -> {
                if (statusFav)
                    remmoveFav() else addFav()

                statusFav = !statusFav
                setFavorite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addFav() {
        try {
            val username = tv_username.text.toString()
            val photo = image
            val type = tv_type.text.toString()
                    //username = intent?.getStringExtra(users.name).toString()
            //photo = intent?.getStringExtra(EXTRA_PHOTO).toString()
            //type = intent?.getStringExtra(EXTRA_TYPE).toString()

            val contentValues = ContentValues().apply {
                put(NAME, username) // cek nilai companionnya, masih null, oprek dari tv_name yg ada nilai nya dari detail
                put(PHOTO, photo)
                put(TYPE, type)
            }
            contentResolver.insert(CONTENT_URI, contentValues)
            //statusFav = true

            showSnackbarMessage("Tambah ke Favorite")
            Log.d("INSERT USER", contentValues.toString())
        } catch (e: SQLiteConstraintException) {
            finish()
        }
    }

    private fun remmoveFav() {
        try {
            val username = tv_username.text.toString()
            val delete = contentResolver.delete(uriId, null, null)

            showSnackbarMessage("Hapus dari Favorite")
            Log.d("DELETE USER", delete.toString())
        } catch (e: SQLiteConstraintException) {
            showSnackbarMessage("" + e.localizedMessage)
            finish()
        }
    }

    private fun setFavorite() {
        if (statusFav) {
            mMenu.getItem(0).icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_1)
        } else {
            mMenu.getItem(0).icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        userHelper.close()
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(view_pager, message, Snackbar.LENGTH_SHORT).show()
    }
}