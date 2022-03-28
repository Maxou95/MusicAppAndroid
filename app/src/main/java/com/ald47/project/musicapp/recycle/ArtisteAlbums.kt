package com.ald47.project.musicapp.recycle

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.ald47.project.musicapp.R
import com.ald47.project.musicapp.databinding.ItemAlbumSearchResultBinding
import com.ald47.project.musicapp.modeles.Album
import com.bumptech.glide.Glide

class ArtisteAlbums(
    private val context: Context,
    private val itemsList: List<Album>,
    private val navController: NavController
) : RecyclerView.Adapter<ArtisteAlbums.ArtistsPostsViewHolder>() {

    inner class ArtistsPostsViewHolder(val binding: ItemAlbumSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistsPostsViewHolder {
        val binding = ItemAlbumSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistsPostsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistsPostsViewHolder, pos: Int) {
        val album = itemsList[pos]
        with(holder) {
            binding.apply {
                itemTitle.text = album.strAlbum
                if (!album.strAlbumThumb.isNullOrEmpty()) {
                    Glide.with(context)
                         .load(album.strAlbumThumb)
                         .placeholder(R.drawable.user_placeholder)
                         .into(itemImg)
                }
                searchItem.setOnClickListener {
                    // TODO : Navigate to artist details page
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size // returns the size of the List
    }

}