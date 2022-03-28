package com.ald47.project.musicapp

import com.ald47.project.musicapp.modeles.*
import retrofit2.Call
import retrofit2.http.*

interface Interface {

    @GET("trending.php")
    fun getTrending(
        @Query("country") country: String,
        @Query("type") type: String,
        @Query("format") format: String,
    ): Call<MeilleurTitre>


    @GET("search.php")
    fun searchArtist(@Query("s") artistName: String): Call<RechercheArtiste>

    @GET("searchalbum.php")
    fun searchAlbum(@Query("s") artist: String) : Call<RechercheAlbum>



    @GET("artist.php")
    fun getArtistDetails(@Query("i") artistId : String) : Call<ArtisteDetails>

    @GET("album.php")
    fun getArtistAlbums(@Query("i") artistId : String) : Call<RechercheAlbum>

    @GET("track-top10-mb.php")
    fun getArtistMostLikedTracks(@Query("s") mbId : String) : Call<ArtisteMeilleurSon>



    @GET("album.php")
    fun getAlbumDetails(@Query("m") albumMbId : String) : Call<RechercheAlbum>

    @GET("track.php")
    fun getAlbumTracks(@Query("m") albumId : String) : Call<ArtisteMeilleurSon>
}