package com.kelaspemula.submission1.Activity

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kelaspemula.submission1.Adapter.UserAdapter
import com.kelaspemula.submission1.Model.User
import com.kelaspemula.submission1.R
import com.kelaspemula.submission1.db.DatabaseContract.UserColums.Companion.CONTENT_URI
import com.kelaspemula.submission1.db.UserHelper
import com.kelaspemula.submission1.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var userHelper: UserHelper

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_PHOTO = "extra_photo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.detailFav)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handle = Handler(handlerThread.looper)
        val observer = object : ContentObserver(handle) {
            override fun onChange(selfChange: Boolean) {
                loadFavorite()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, observer)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        if (savedInstanceState == null) {
            loadFavorite()
        } else {
            val listFavorite = savedInstanceState.getParcelableArrayList<User>(EXTRA_NAME)
            if (listFavorite != null) {
                adapter.setData(listFavorite)
            }
        }
        setData()
    }

    private fun loadFavorite() {
        GlobalScope.launch {
            progressBar.visibility = View.VISIBLE
            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBar.visibility = View.INVISIBLE
            val favorite = deferredFavorite.await()
            if (favorite.size > 0) {
                adapter.setData(favorite)
            } else {
                adapter.setData(ArrayList())
                Snackbar.make(rv_favorite, "Data Kosong", Snackbar.LENGTH_INDEFINITE).show()
            }
        }
    }

    private fun setData() {
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(this@FavoriteActivity, DetailAcivity::class.java)
                intent.putExtra(DetailAcivity.EXTRA_VALUE, data)
                startActivity(intent)
            }
        })
    }



    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}