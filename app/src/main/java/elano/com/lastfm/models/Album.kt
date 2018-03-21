package elano.com.lastfm.models

import com.google.gson.annotations.SerializedName

data class Album(val results: AlbumResults)

data class AlbumDetails(val name: String, val artist: String, val images: ArrayList<AlbumImage>)

data class AlbumResults(val albumMatches: AlbumMatches)

data class AlbumMatches(val albums: ArrayList<AlbumDetails>)

data class AlbumImage(@SerializedName("#text") val image: String)