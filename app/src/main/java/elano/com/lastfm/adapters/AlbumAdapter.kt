package elano.com.lastfm.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import elano.com.lastfm.R
import elano.com.lastfm.models.Album

class AlbumAdapter(private val context: Context, private val albums: ArrayList<Album>?) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    fun add(album: Album) {
        albums?.add(album)
        notifyDataSetChanged()
    }

    fun clear() {
        val size = albums!!.size

        albums.clear()
        notifyItemRangeRemoved(0, size)
    }

    override fun getItemCount(): Int = albums!!.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AlbumViewHolder =
            AlbumViewHolder(LayoutInflater.from(context).inflate(R.layout.album_row, parent, false))

    override fun onBindViewHolder(holder: AlbumViewHolder?, position: Int) {
        val album = albums!![position]

        if (album.albumImages[2].image.isEmpty())
            holder?.imageAlbum?.setImageResource(R.mipmap.ic_launcher)
        else
            Picasso.with(context).load(album.albumImages[2].image)?.into(holder?.imageAlbum)
        holder?.songName?.text = album.name
        holder?.singer?.text = album.artist
    }

    inner class AlbumViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val imageAlbum = itemView?.findViewById<ImageView>(R.id.ivAlbum)
        val songName = itemView?.findViewById<TextView>(R.id.tvName)
        val singer = itemView?.findViewById<TextView>(R.id.tvSinger)
    }
}