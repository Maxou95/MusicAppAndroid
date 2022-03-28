package com.ald47.project.musicapp.recycle

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ald47.project.musicapp.databinding.ItemTopTracksBinding
import com.ald47.project.musicapp.modeles.TrackList

class ListeMusique(
    private val tracksList: List<TrackList>
) : RecyclerView.Adapter<ListeMusique.ArtistsPostsViewHolder>() {

    inner class ArtistsPostsViewHolder(val binding: ItemTopTracksBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistsPostsViewHolder {
        val binding = ItemTopTracksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistsPostsViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ArtistsPostsViewHolder, pos: Int) {
        val track = tracksList[pos]
        with(holder) {
            binding.apply {
                itemNumberTV.text = "${pos+1}"
               trackNameTV.text = track.strTrack
            }
        }
    }

    override fun getItemCount(): Int {
        return tracksList.size // returns the size of the List
    }

}