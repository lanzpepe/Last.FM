package elano.com.lastfm

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.gson.GsonBuilder
import elano.com.lastfm.adapters.AlbumAdapter
import elano.com.lastfm.models.Album
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity(), Callback {

    private var mAlbums: ArrayList<Album>? = null
    private var mAdapter: AlbumAdapter? = null
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
        textInputLayout.setOnClickListener { fetchAlbumJson() }
    }

    private fun fetchAlbumJson() {
        val search = etSearch.text.toString()

        if (TextUtils.isEmpty(search))
            etSearch.error = "Please input search name."
        else {
            val request = Request.Builder().url("http://ws.audioscrobbler.com/2.0/?method=album.search&album=$search&api_key=0106ac76eb5987f30b74ddc0842fd287&format=json").build()
            val client = OkHttpClient()

            progressBar.visibility = View.VISIBLE
            client.newCall(request).enqueue(this)
        }
    }

    override fun onResponse(call: Call?, response: Response?) {
        val body = response?.body()?.string()
        val jsonObject = JSONObject(body)
        val totalResults = jsonObject.getString("opensearch:totalResults").toInt()
        val album = GsonBuilder().create().fromJson(body, Album::class.java)

        mAlbums = ArrayList()
        mAdapter = AlbumAdapter(this, mAlbums)

        if (jsonObject.isNull(KEY_NAME))
            toast("Album not found.")
        else {
            for (i in 0..totalResults)
        }
    }

    override fun onFailure(call: Call?, e: IOException?) {
        toast("Failed to fetch results.")
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val ALBUM_LIMIT = 50
        const val KEY_NAME = "name"
    }
}
