package com.kelaspemula.submission1.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.kelaspemula.submission1.Activity.DetailAcivity
import com.kelaspemula.submission1.Adapter.UserAdapter
import com.kelaspemula.submission1.Model.User
import com.kelaspemula.submission1.R
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray
import java.lang.Exception

class FollowingFragment : Fragment() {
    private lateinit var adapter: UserAdapter

    companion object {
        private val TAG = FollowingFragment::class.java.simpleName
        private val ARG_USERNAME = "username"

        fun newInstance(username: String?): FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        pb_following.visibility = View.VISIBLE

        rv_following.layoutManager = LinearLayoutManager(context)
        rv_following.adapter = adapter

        setDataFollowing()
        onClick()
    }

    private fun setDataFollowing() {
        val username = arguments?.getString(ARG_USERNAME)

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/following"
        client.addHeader("Authorization", "token a55286e4a0babdcb7d28e1bcb121da749828b374")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                val listUser = ArrayList<User>()
                val result = String(responseBody)
                pb_following.visibility = View.INVISIBLE

                try {
                    val users = JSONArray(result)
                    for (i in 0 until users.length()) {
                        val item = users.getJSONObject(i)
                        val user = User()
                        user.name = item.getString("login")
                        user.photo = item.getString("avatar_url")
                        user.type = item.getString("type")
                        listUser.add(user)
                    }
                    adapter.setData(listUser)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable) {
                pb_following.visibility = View.INVISIBLE
                Log.d(TAG, error.message.toString())
            }
        })
    }

    private fun onClick() {
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(activity, DetailAcivity::class.java)
                intent.putExtra(DetailAcivity.EXTRA_VALUE, data)
                startActivity(intent)
            }
        })
    }
}