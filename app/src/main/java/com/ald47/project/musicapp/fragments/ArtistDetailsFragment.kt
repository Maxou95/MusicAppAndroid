package com.ald47.project.musicapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ald47.project.musicapp.R
import com.ald47.project.musicapp.Interface
import com.ald47.project.musicapp.databinding.FragmentArtistDetailsBinding
import com.ald47.project.musicapp.helpers.ApiHelper
import com.ald47.project.musicapp.recycle.ArtisteAlbums
import com.ald47.project.musicapp.recycle.ListeMusique
import com.ald47.project.musicapp.modeles.*
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ArtistDetailsFragment : Fragment(R.layout.fragment_artist_details) {

    private var artistId: String? = null
    private lateinit var binding : FragmentArtistDetailsBinding
    private lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            artistId = it.getString("artistId")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArtistDetailsBinding.bind(view)
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        if(artistId.isNullOrEmpty()){
            Toast.makeText(requireContext(),"Failed to parse artist id.",Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
        else{
            // Artist Id is now safe to use
            // Create objet of Retrofit to use in this fragment
            retrofit = Retrofit.Builder()
                .baseUrl(ApiHelper.apiBaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            fetchArtistDetails(artistId!!)
            getArtistAlbums(artistId!!)
        }

    }

    private fun fetchArtistDetails(artistID : String) {
        val artistDetailsCall =  retrofit.create(Interface::class.java)
            .getArtistDetails(artistID)
        artistDetailsCall.enqueue(object : Callback<ArtisteDetails>{
            override fun onResponse(
                call: Call<ArtisteDetails>,
                response: Response<ArtisteDetails>
            ) {
                binding.detailsPrg.visibility = View.GONE
                if(response.code() == 200){
                    if(response.body()!=null){
                        if(! response.body()!!.artistes.isNullOrEmpty()){
                            val artistInfo = response.body()!!.artistes[0]
                            binding.apply {
                                artistNameTV.text = artistInfo.strArtist
                                locationTV.text = artistInfo.strCountry
                                artistGenreTV.text = artistInfo.strGenre
                                artistBioTV.text = artistInfo.strBiographyEN
                                if(!artistInfo.strArtistWideThumb.isNullOrEmpty()){
                                    Glide.with(requireActivity())
                                        .load(artistInfo.strArtistWideThumb)
                                        .into(artistCoverIV)
                                }
                                if(!artistInfo.strMusicBrainzID.isNullOrEmpty()){
                                    getArtistTracks(artistInfo.strMusicBrainzID)
                                }

                            }
                        }
                    }
                    else{
                        showSnack("API response returned Null.")
                    }
                }
                else{
                    showSnack("Failed to fetch artist details.\nResponse Code : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ArtisteDetails>, t: Throwable) {
                binding.detailsPrg.visibility = View.GONE
                showSnack("Failed to fetch artist details.\nReason: ${t.message}")
            }

        })
    }

    private fun getArtistAlbums(artistID : String){
        val albumsCall = retrofit.create(Interface::class.java)
            .getArtistAlbums(artistID)
        albumsCall.enqueue(object : Callback<RechercheAlbum>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<RechercheAlbum>,
                response: Response<RechercheAlbum>
            ) {
                if(response.code() == 200){
                    if(response.body()!=null){
                        if(! response.body()!!.album.isNullOrEmpty()){
                            val albums = response.body()!!.album
                            binding.albumsCountTV.text = "(${albums.size})"
                            val adapter = ArtisteAlbums(requireContext(),albums,findNavController())
                            binding.artistAlbumsRV.adapter = adapter
                        }
                    }
                    else showSnack("Failed to fetch artist albums. API result empty.")
                }
                else showSnack("Failed to fetch artist albums. Code : ${response.code()}")

            }
            override fun onFailure(call: Call<RechercheAlbum>, t: Throwable) {
                showSnack("Failed to fetch artist albums. Reason : ${t.message}")
            }
        })
    }

    private fun getArtistTracks(mbID : String){
        val tracksCall = retrofit.create(Interface::class.java)
            .getArtistMostLikedTracks(mbID)
        tracksCall.enqueue(object : Callback<ArtisteMeilleurSon>{
            override fun onResponse(
                call: Call<ArtisteMeilleurSon>,
                response: Response<ArtisteMeilleurSon>
            ) {
                if(response.code() == 200 && response.body()!=null){
                    val tracks = response.body()!!.trackList
                    if(!tracks.isNullOrEmpty()){
                        binding.artistTracksRV.adapter = ListeMusique(tracks)
                    }
                }
                else{
                    showSnack("Failed to fetch artist tracks. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ArtisteMeilleurSon>, t: Throwable) {
                showSnack("Failed to fetch artist top tracks. Reason: ${t.message}")
            }

        })
    }

    private fun showSnack(msg : String){
        Snackbar.make(binding.root,msg,Snackbar.LENGTH_SHORT).show()
    }

}