package com.ald47.project.musicapp.recycle

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.ald47.project.musicapp.R
import com.ald47.project.musicapp.databinding.ItemArtistsSearchResultBinding
import com.ald47.project.musicapp.modeles.Artiste
import com.bumptech.glide.Glide

class ArtistesRecherche(
    private val context: Context,
    private val itemsList: List<Artiste>,
    private val navController: NavController
) : RecyclerView.Adapter<ArtistesRecherche.ArtistsPostsViewHolder>() {

    inner class ArtistsPostsViewHolder(val binding: ItemArtistsSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistsPostsViewHolder {
        val binding = ItemArtistsSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistsPostsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistsPostsViewHolder, pos: Int) {
        val artist = itemsList[pos]
        with(holder) {
            binding.apply {
                itemTitle.text = artist.strArtist
                if (!artist.strArtistThumb.isNullOrEmpty()) {
                    Glide.with(context)
                         .load(artist.strArtistThumb)
                         .placeholder(R.drawable.user_placeholder)
                         .into(itemImg)
                }
                searchItem.setOnClickListener {
                    val extras = Bundle()
                    extras.putString("artistId",artist.idArtist)
                    navController.navigate(R.id.action_rechercherFragment_to_artistDetailsFragment,extras)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size // returns the size of the List
    }

}