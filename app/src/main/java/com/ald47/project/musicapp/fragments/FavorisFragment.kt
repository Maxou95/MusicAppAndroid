package com.ald47.project.musicapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.ald47.project.musicapp.R
import com.ald47.project.musicapp.databinding.FragmentFavorisBinding
import com.ald47.project.musicapp.recycle.AlbumsJaime
import com.ald47.project.musicapp.recycle.ArtistesJaime
import com.ald47.project.musicapp.room_db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FavorisFragment : Fragment(R.layout.fragment_favoris) {

    private lateinit var binding : FragmentFavorisBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavorisBinding.bind(view)

        val dao = AppDatabase(requireContext()).musicAppDao()
        GlobalScope.launch (Dispatchers.IO){
            val albums  = dao.getLikedAlbums()
            val artists = dao.getLikedArtists()
            if(albums!=null){
                if(albums.isNotEmpty()){
                    // Show in RV
                    val adapter = AlbumsJaime(requireContext(),albums,findNavController())
                    GlobalScope.launch (Dispatchers.Main){
                        binding.albumsRV.adapter = adapter
                    }
                }
                else{
                    //binding.noAlbumsTV.visibility = View.VISIBLE
                }
            }
            if(artists!=null){
                if(artists.isNotEmpty()){
                    val adapter = ArtistesJaime(requireContext(),artists,findNavController())
                    GlobalScope.launch (Dispatchers.Main){
                        binding.artistsRV.adapter = adapter
                    }
                }
                else{
                    //binding.noArtistsTV.visibility = View.VISIBLE
                }
            }
        }
    }

}