package com.ald47.project.musicapp.recycle

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.ald47.project.musicapp.R
import com.ald47.project.musicapp.databinding.ItemTitresRankingBinding
import com.ald47.project.musicapp.modeles.Album
import com.bumptech.glide.Glide

class AlbumsJaime(
    private val context: Context,
    private val albums: List<Album>,
    private val navController: NavController
) : RecyclerView.Adapter<AlbumsJaime.LikedAlbumsViewHolder>() {

    inner class LikedAlbumsViewHolder(val binding: ItemTitresRankingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedAlbumsViewHolder {
        val binding =
            ItemTitresRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LikedAlbumsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LikedAlbumsViewHolder, pos: Int) {
        val album = albums[pos]
        with(holder) {
            binding.apply {
                rankTV.text = (pos + 1).toString()
                artistNameTV.text = album.strArtist
                titleTV.text = album.strAlbum
                if (!album.strAlbumThumb.isNullOrEmpty()) {
                    Glide.with(context)
                        .load(album.strAlbumThumb)
                        .placeholder(R.drawable.place_holder_colored)
                        .into(itemCoverIV)
                }
                listItem.setOnClickListener {
                    val extras = Bundle()
                    extras.putString("albumId",album.idAlbum)
                    navController.navigate(R.id.action_favorisFragment_to_albumDetailsFragment,extras)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return albums.size // returns the size of the List
    }

}