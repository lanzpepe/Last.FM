package elano.com.lastfm

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.google.gson.GsonBuilder
import elano.com.lastfm.adapters.AlbumAdapter
import elano.com.lastfm.models.AlbumModel
import elano.com.lastfm.models.Album
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.apache.commons.lang3.StringUtils
import java.io.IOException

class MainActivity : AppCompatActivity(), TextView.OnEditorActionListener, Callback {

    private var mAlbums: ArrayList<Album>? = null
    private var mAdapter: AlbumAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
        etSearch.setOnEditorActionListener(this)
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            fetchAlbumJson()
            return true
        }
        return false
    }

    private fun fetchAlbumJson() {
        val search = StringUtils.capitalize(etSearch.text.toString())

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
        val album = GsonBuilder().create().fromJson(body, AlbumModel::class.java)

        mAlbums = ArrayList()
        mAdapter = AlbumAdapter(this, mAlbums)

        for (i in 0 until SEARCH_LIMIT) {
            runOnUiThread {
                println(body)
                tvMessage.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                recyclerView.adapter = mAdapter
                mAdapter?.add(album.results.albumMatches.album[i])

            }
            Thread.sleep(1000)
        }
        runOnUiThread { progressBar.visibility = View.GONE }
    }

    override fun onFailure(call: Call?, e: IOException?) {
        runOnUiThread {
            toast("Failed to fetch results.")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.clear -> mAdapter?.clear()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val SEARCH_LIMIT = 50
    }
}
