package com.ald47.project.musicapp.room_db

import androidx.room.*
import com.ald47.project.musicapp.modeles.Album
import com.ald47.project.musicapp.modeles.Artiste

@Dao
interface MusicAppDao {

    @Query("SELECT * FROM albums_table")
    fun getLikedAlbums(): List<Album>?

    @Query("SELECT * FROM artists_table")
    fun getLikedArtists(): List<Artiste>?

    @Query("SELECT * FROM albums_table WHERE idAlbum LIKE (:albumId)")
    fun getAlbumById(albumId : String): List<Album>?

    @Query("SELECT * FROM artists_table WHERE idArtist LIKE (:artistId)")
    fun getArtistById(artistId : String): List<Artiste>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAlbumToLikes(album: Album?)

    @Delete
    suspend fun removeAlbum(album: Album?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArtistToLikes(artiste: Artiste?)

    @Delete
    suspend fun removeArtist(artiste: Artiste?)

}