package com.example.cryptocurrencyapp.Ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.cryptocurrencyapp.R
import com.example.cryptocurrencyapp.adapter.LoseGainPagerAdapter
import com.example.cryptocurrencyapp.adapter.TopMarketAdapter
import com.example.cryptocurrencyapp.apis.ApiInterface
import com.example.cryptocurrencyapp.apis.ApiUtilites
import com.example.cryptocurrencyapp.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout.TabGravity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import retrofit2.create
import kotlin.math.log


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getTopCurrencyList()
        setTabLayout()
    }

    private fun setTabLayout() {
        val adapter = LoseGainPagerAdapter(this)
        binding.contentViewPager.adapter=adapter

        binding.contentViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0){
                    binding.topGainIndicator.visibility= VISIBLE
                    binding.topLoseIndicator.visibility = GONE
                }else{
                    binding.topGainIndicator.visibility= GONE
                    binding.topLoseIndicator.visibility = VISIBLE
                }
            }

        })


        TabLayoutMediator(binding.tabLayout,binding.contentViewPager){
            tab, position ->
            var title= if (position == 0){
                "Top Gainers"
            }else{
                "Top Lose"
            }
            tab.text=title
        }.attach()


    }

    private fun getTopCurrencyList(){
        lifecycleScope.launch(Dispatchers.IO){
            val res = ApiUtilites.getInstance().create(ApiInterface::class.java).getMarketData()

            withContext(Dispatchers.Main){
                binding.topCurrencyRecyclerView.adapter = TopMarketAdapter(requireContext(),res.body()!!.data.cryptoCurrencyList)
            }

            Log.d("MALIK","getTopCurrencyList: ${res.body()!!.data.cryptoCurrencyList}")
        }
    }



}

