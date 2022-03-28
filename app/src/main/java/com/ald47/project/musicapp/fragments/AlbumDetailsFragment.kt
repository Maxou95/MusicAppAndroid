package com.ald47.project.musicapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ald47.project.musicapp.R
import com.ald47.project.musicapp.Interface
import com.ald47.project.musicapp.databinding.FragmentAlbumDetailsBinding
import com.ald47.project.musicapp.helpers.ApiHelper
import com.ald47.project.musicapp.recycle.ListeMusique
import com.ald47.project.musicapp.modeles.Album
import com.ald47.project.musicapp.modeles.ArtisteMeilleurSon
import com.ald47.project.musicapp.modeles.RechercheAlbum
import com.ald47.project.musicapp.room_db.AppDatabase
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AlbumDetailsFragment : Fragment(R.layout.fragment_album_details) {
    private var albumId: String? = null
    private lateinit var binding: FragmentAlbumDetailsBinding
    private lateinit var retrofit: Retrofit
    private lateinit var db : AppDatabase
    private var isLiked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            albumId = it.getString("albumId")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAlbumDetailsBinding.bind(view)
        db = AppDatabase(requireContext())
        retrofit = Retrofit.Builder()
            .baseUrl(ApiHelper.apiBaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        binding.backBtn.setOnClickListener { findNavController().popBackStack() }

        if (albumId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Failed to parse album id.", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
        else {
            isAlreadyLiked(albumId!!)
            getAlbumDetails(albumId!!)
            getAlbumTracks(albumId!!)
        }
    }

    private fun getAlbumDetails(albumId: String) {
        val albumDetailsCall = retrofit.create(Interface::class.java)
            .getAlbumDetails(albumId)
        albumDetailsCall.enqueue(object : Callback<RechercheAlbum> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<RechercheAlbum>,
                response: Response<RechercheAlbum>
            ) {
                binding.detailsPrg.visibility = View.GONE
                if (response.code() == 200 && response.body() != null) {
                    if (!response.body()!!.album.isNullOrEmpty()) {
                        val album : Album = response.body()!!.album[0]
                        binding.apply {
                            artistNameTV.text = album.strArtist
                            albumTitleTV.text = album.strAlbum
                            if (!album.strAlbumThumb.isNullOrEmpty()) {

                                Glide.with(requireActivity())
                                    .load(album.strAlbumThumb)
                                    .into(albumCoverIV)

                                Glide.with(requireActivity())
                                    .load(album.strAlbumThumb)
                                    .into(albumThumbIV)
                            }
                            if(!album.intScore.isNullOrEmpty()){
                                albumRatingTV.text = album.intScore
                            }
                            if(!album.intScoreVotes.isNullOrEmpty()){
                                votesCountTV.text = "${album.intScoreVotes} votes"
                            }
                            albumDescriptionTV.text = album.strDescriptionEN
                            binding.likeBtn.setOnClickListener {
                                if(isLiked){
                                    // Remove from Likes
                                    heartIV.setImageResource(R.drawable.heart_unliked)
                                    showSnack("Album removed from your likes.")
                                    GlobalScope.launch (Dispatchers.IO){
                                        db.musicAppDao().removeAlbum(album)
                                        isLiked = false
                                    }
                                }
                                else{
                                    heartIV.setImageResource(R.drawable.heart_green)
                                    showSnack("Album saved to your likes.")
                                    GlobalScope.launch (Dispatchers.IO){
                                        db.musicAppDao().saveAlbumToLikes(album)
                                        isLiked = true
                                    }
                                }
                            }
                        }
                    } else showSnack("Failed to get album details.")

                } else showSnack("Failed to get album details.")
            }

            override fun onFailure(call: Call<RechercheAlbum>, t: Throwable) {
                binding.detailsPrg.visibility = View.GONE
                showSnack("Failed to get album details.")
            }

        })
    }

    private fun isAlreadyLiked(albumId : String){
        GlobalScope.launch(Dispatchers.IO) {
            val result = db.musicAppDao().getAlbumById(albumId)
            GlobalScope.launch (Dispatchers.Main){
                if(result!=null && result.isNotEmpty()){ // Already liked
                    binding.heartIV.setImageResource(R.drawable.heart_green)
                    isLiked = true
                }
            }
        }
    }
    private fun getAlbumTracks(idAlbum: String) {
        val tracksCall = retrofit.create(Interface::class.java)
            .getAlbumTracks(idAlbum)

        tracksCall.enqueue(object : Callback<ArtisteMeilleurSon> {
            override fun onResponse(
                call: Call<ArtisteMeilleurSon>,
                response: Response<ArtisteMeilleurSon>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    val tracks = response.body()!!.trackList
                    if (!tracks.isNullOrEmpty()) {
                        binding.albumTracksRV.adapter = ListeMusique(tracks)
                    }
                } else {
                    showSnack("Failed to fetch artist tracks. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ArtisteMeilleurSon>, t: Throwable) {
                showSnack("Failed to fetch artist top tracks. Reason: ${t.message}")
            }

        })
    }

    private fun showSnack(msg: String) {
        Snackbar.make(
            binding.root,
            msg,
            Snackbar.LENGTH_SHORT
        ).show()
    }

}