package com.example.cryptocurrencyapp.Ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.cryptocurrencyapp.adapter.MarketAdapter
import com.example.cryptocurrencyapp.apis.ApiInterface
import com.example.cryptocurrencyapp.apis.ApiUtilites
import com.example.cryptocurrencyapp.databinding.FragmentMarketBinding
import com.example.cryptocurrencyapp.models.CryptoCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


class MarketFragment : Fragment() {

    private lateinit var binding: FragmentMarketBinding
    private lateinit var list: List<CryptoCurrency>
    private lateinit var adapter : MarketAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMarketBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        list = listOf()
        adapter= MarketAdapter(requireContext(),list, "market")
        binding.currencyRecyclerView.adapter=adapter


        lifecycleScope.launch(Dispatchers.IO){
            val res = ApiUtilites.getInstance().create(ApiInterface::class.java).getMarketData()

            if (res.body() != null){

               withContext(Dispatchers.Main){
                   list=res.body()!!.data.cryptoCurrencyList

                   adapter.updateDate(list)
                   binding.spinKitView.visibility= GONE
               }
            }
        }

        searchCoin()

    }



    lateinit var searchText : String
    private fun searchCoin() {
        binding.searchEditText.addTextChangedListener (object  : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                searchText = p0.toString().toLowerCase()

                updateRecycleView()
            }
        })

    }

    private fun updateRecycleView() {
        val data = ArrayList<CryptoCurrency>()

        for (item in list){
            val coinName= item.name.lowercase(Locale.getDefault())
            val coinSymbol= item.symbol.lowercase(Locale.getDefault())

            if (coinName.contains(searchText) || coinSymbol.contains(searchText)){
                data.add(item)
            }

        }
        adapter.updateDate(data)
    }


}
