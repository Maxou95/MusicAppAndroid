package com.ald47.project.musicapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import com.ald47.project.musicapp.R
import com.ald47.project.musicapp.databinding.FragmentRechercherBinding
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.navigation.fragment.findNavController
import com.ald47.project.musicapp.Interface
import com.ald47.project.musicapp.helpers.ApiHelper
import com.ald47.project.musicapp.recycle.AlbumRecherche
import com.ald47.project.musicapp.recycle.ArtistesRecherche
import com.ald47.project.musicapp.modeles.RechercheAlbum
import com.ald47.project.musicapp.modeles.RechercheArtiste
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_rechercher.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RechercherFragment : Fragment(R.layout.fragment_rechercher) {

    private lateinit var binding : FragmentRechercherBinding
    private lateinit var retrofit: Retrofit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRechercherBinding.bind(view)

        retrofit = Retrofit.Builder()
            .baseUrl(ApiHelper.apiBaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        binding.searchET.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = searchET.text.trim().toString().lowercase()
                if(query.length>2){
                    binding.searchResultViews.visibility = View.VISIBLE
                    searchArtist(query)
                    searchAlbums(query)
                }
                else showSnack("Search must contain at least 3 characters.")

                return@OnEditorActionListener true
            }
            false
        })

        binding.clearTextIV.setOnClickListener {
            binding.searchET.text.clear()
        }
    }

    private fun searchArtist(query : String) {
        val artistsCall = retrofit.create(Interface::class.java)
            .searchArtist(query)
        artistsCall.enqueue(object : Callback<RechercheArtiste>{
            override fun onResponse(
                call: Call<RechercheArtiste>,
                response: Response<RechercheArtiste>
            ) {
                binding.artistsPrg.visibility = View.GONE
                if(response.code()==200){
                    if(response.body()!=null){
                        val artists = response.body()!!.artistes
                        if(!artists.isNullOrEmpty()){
                            val adapter = ArtistesRecherche(requireContext(),artists,findNavController())
                            binding.artistsRV.adapter = adapter
                        }
                        else{
                            showSnack("No Artists found.")
                        }
                    }
                }
                else{
                    Log.e("apiFailures","Failed to fetch artists. ResponseCode : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RechercheArtiste>, t: Throwable) {
                binding.artistsPrg.visibility = View.GONE
                Log.e("apiFailures","Failed to fetch artist. Reason : ${t.message}")
            }
        })
    }

    private fun searchAlbums(query: String) {
        val albumsCall = retrofit.create(Interface::class.java)
            .searchAlbum(query)
        albumsCall.enqueue(object : Callback<RechercheAlbum>{
            override fun onResponse(
                call: Call<RechercheAlbum>,
                response: Response<RechercheAlbum>
            ) {
                binding.albumsPrg.visibility = View.GONE
                if(response.code()==200){
                    if(response.body()!=null){
                        val albums = response.body()!!.album
                        if(!albums.isNullOrEmpty()){
                            val adapter = AlbumRecherche(requireContext(),albums,findNavController())
                            binding.albumsRV.adapter = adapter
                        }
                        else{
                            showSnack("No Albums found.")
                        }
                    }
                }
                else{
                    Log.e("apiFailures","Failed to fetch albums. ResponseCode : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RechercheAlbum>, t: Throwable) {
                binding.albumsPrg.visibility = View.GONE
                Log.e("apiFailures","Failed to fetch artist albums. Reason : ${t.message}")
            }
        })
    }

    private fun showSnack(msg : String){
        Snackbar.make(binding.root,msg,Snackbar.LENGTH_SHORT).show()
    }

}