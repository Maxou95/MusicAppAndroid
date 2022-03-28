package com.ald47.project.musicapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.ald47.project.musicapp.R
import com.ald47.project.musicapp.Interface
import com.ald47.project.musicapp.ListeAbumTitres
import com.ald47.project.musicapp.databinding.FragmentClassementsBinding
import com.ald47.project.musicapp.helpers.ApiHelper
import com.ald47.project.musicapp.modeles.MeilleurTitre
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ClassementsFragment : Fragment(R.layout.fragment_classements) {

    private lateinit var binding : FragmentClassementsBinding
    private lateinit var retrofit: Retrofit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if(this::binding.isInitialized){ // this needs to be done so that the selected Albums/Titres tab selection remains intact when switching Fragments
            return binding.root
        }

        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_classements,container,false)
        setBtnBehaviour()

        // Create objet of Retrofit to use in this fragment
        retrofit = Retrofit.Builder()
            .baseUrl(ApiHelper.apiBaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        getTitres()
        return binding.root
    }

    private fun getTitres(){
        val titresCall = retrofit.create(Interface::class.java)
            .getTrending(
                format = "singles",
                country = "us",
                type = "itunes"
            )
        Log.d("apiRequests->","Request : ${titresCall.request().url()}")
        binding.prgBar.visibility = View.VISIBLE
        titresCall.enqueue(object : Callback<MeilleurTitre>{
            override fun onResponse(
                call: Call<MeilleurTitre>,
                response: Response<MeilleurTitre>
            ) {
                binding.prgBar.visibility = View.GONE
                if(response.code() == 200){
                    if(response.body()!=null){
                        val titres = response.body()!!.trending
                        if(titres.isNullOrEmpty()){
                            showSnack("Api did not return anything.")
                        }
                        else{
                            val adapter = ListeAbumTitres(requireContext(),"titres",titres,findNavController())
                            binding.resultsRV.adapter = adapter
                        }
                    }
                }
                else{
                    showSnack("Failed to fetch titres. ResponseCode: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MeilleurTitre>, t: Throwable) {
                binding.prgBar.visibility = View.GONE
                showSnack("Failed to fetch titres. Reason : ${t.message}")
            }
        })
    }

    private fun getAlbums(){
        val albumsCall = retrofit.create(Interface::class.java)
            .getTrending(
                format = "albums",
                country = "us",
                type = "itunes"
            )
        binding.prgBar.visibility = View.VISIBLE
        albumsCall.enqueue(object : Callback<MeilleurTitre>{
            override fun onResponse(
                call: Call<MeilleurTitre>,
                response: Response<MeilleurTitre>
            ) {
                binding.prgBar.visibility = View.GONE
                if(response.code() == 200){
                    if(response.body()!=null){
                        val titres = response.body()!!.trending
                        if(titres.isNullOrEmpty()){
                            showSnack("Api did not return anything.")
                        }
                        else{
                            val adapter = ListeAbumTitres(requireContext(),"albums",titres,findNavController())
                            binding.resultsRV.adapter = adapter
                        }
                    }
                }
                else{
                    showSnack("Failed to fetch Albums. ResponseCode: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MeilleurTitre>, t: Throwable) {
                binding.prgBar.visibility = View.GONE
                showSnack("Failed to fetch titres. Reason : ${t.message}")
            }
        })
    }

    private fun setBtnBehaviour(){
        binding.apply {

            titresTag.setOnClickListener {
                line1.visibility = View.VISIBLE
                line2.visibility = View.INVISIBLE
                getTitres()
            }
            albumsTag.setOnClickListener {
                line1.visibility = View.INVISIBLE
                line2.visibility = View.VISIBLE
                getAlbums()
            }
        }
    }

    private fun showSnack(msg : String){
        Snackbar.make(binding.root,msg,Snackbar.LENGTH_SHORT).show()
    }
}