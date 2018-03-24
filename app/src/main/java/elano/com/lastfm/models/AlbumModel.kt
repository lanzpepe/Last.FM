package elano.com.lastfm.models

import com.google.gson.annotations.SerializedName

data class AlbumModel(val results: Results)

data class Results(@SerializedName("albummatches") val albumMatches: AlbumMatches)

data class AlbumMatches(val album: ArrayList<Album>)

data class Album(val name: String, val artist: String, @SerializedName("image") val albumImages: ArrayList<AlbumImage>)

data class AlbumImage(@SerializedName("#text") val image: String)