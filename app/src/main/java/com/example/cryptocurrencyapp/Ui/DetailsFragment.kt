package com.example.cryptocurrencyapp.Ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.cryptocurrencyapp.R
import com.example.cryptocurrencyapp.adapter.LoseGainPagerAdapter
import com.example.cryptocurrencyapp.adapter.TopMarketAdapter
import com.example.cryptocurrencyapp.apis.ApiInterface
import com.example.cryptocurrencyapp.apis.ApiUtilites
import com.example.cryptocurrencyapp.databinding.FragmentDetailsBinding
import com.example.cryptocurrencyapp.databinding.FragmentHomeBinding
import com.example.cryptocurrencyapp.models.CryptoCurrency
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val item : DetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val data : CryptoCurrency = item.data!!
        setUpDetails(data)
        loadChat(data)
        setButtonOnClick(data)
        addToWatchList(data)



    }

    var watchList : ArrayList<String>? = null
    var watchListIsChecked = false

    private fun addToWatchList(data: CryptoCurrency) {
        readData()
        watchListIsChecked = if (watchList!!.contains(data.symbol)){
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
            true
        }else{
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
            false
        }

        binding.addWatchlistButton.setOnClickListener {
            watchListIsChecked=
                if (!watchListIsChecked){
                    if (!watchList!!.contains(data.symbol)){
                        watchList!!.add(data.symbol)
                    }
                    storeData()
                    binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
                    true
                }else{
                    binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
                    watchList!!.remove(data.symbol)
                    storeData()
                    false
                }
        }
    }
         private fun storeData(){
             val sharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE)
             val editor = sharedPreferences.edit()
             val gson = Gson()
             val json = gson.toJson(watchList)
             editor.putString("watchlist",json)
            editor.apply()

         }
    private fun readData() {
        val sharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("watchlist", ArrayList<String>().toString())
        val type = object : TypeToken<ArrayList<String>>(){}.type
        watchList = gson.fromJson(json,type)
    }


    private fun setButtonOnClick(item: CryptoCurrency) {

        val oneMonth=binding.button
        val oneWeak=binding.button1
        val oneDAy=binding.button2
        val fourHour=binding.button3
        val oneHour=binding.button4
        val fifteenMinute=binding.button5

        val clickListener=View.OnClickListener {
            when(it.id){
                fifteenMinute.id -> loadCharData(it,"15",item,oneDAy,oneMonth,oneWeak,fourHour,oneHour)
                oneDAy.id -> loadCharData(it,"D",item,fifteenMinute,oneMonth,oneWeak,fourHour,oneHour)
                oneHour.id -> loadCharData(it,"1h",item,oneDAy,oneMonth,oneWeak,fourHour,fifteenMinute)
                oneMonth.id -> loadCharData(it,"M",item,oneDAy,fifteenMinute,oneWeak,fourHour,oneHour)
                oneWeak.id -> loadCharData(it,"W",item,oneDAy,oneMonth,fifteenMinute,fourHour,oneHour)
                fourHour.id -> loadCharData(it,"1h",item,oneDAy,oneMonth,oneWeak,fifteenMinute,oneHour)
            }
        }
        fifteenMinute.setOnClickListener (clickListener)
        oneDAy.setOnClickListener (clickListener)
        oneHour.setOnClickListener(clickListener)
        oneMonth.setOnClickListener (clickListener)
        oneWeak.setOnClickListener (clickListener)
        fourHour.setOnClickListener (clickListener)

    }

    private fun loadCharData(
        it: View?,
        s: String,
        item: CryptoCurrency,
        oneDAy: AppCompatButton,
        oneMonth: AppCompatButton,
        oneWeak: AppCompatButton,
        fourHour: AppCompatButton,
        oneHour: AppCompatButton
    ) {

        dissableButton(oneDAy,oneHour,oneMonth,oneWeak,fourHour)
        it!!.setBackgroundResource(R.drawable.active_button)
        
        binding.detaillChartWebView.settings.javaScriptEnabled= true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol" + item.symbol
                .toString() + "USD&interval="+s+"&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg" +
                    "=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides" +
                    "={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source" +
                    "=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )

    }

    private fun dissableButton(oneDAy: AppCompatButton, oneHour: AppCompatButton,
                               oneMonth: AppCompatButton, oneWeak: AppCompatButton,
                               fourHour: AppCompatButton) {

        oneDAy.background=null
        oneMonth.background=null
        oneHour.background=null
        oneWeak.background=null
        fourHour.background=null

    }

    private fun loadChat(item: CryptoCurrency) {
        binding.detaillChartWebView.settings.javaScriptEnabled= true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol" + item.symbol
                .toString() + "USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg" +
                    "=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides" +
                    "={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source" +
                    "=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )

    }

    private fun setUpDetails(data: CryptoCurrency) {
        binding.detailSymbolTextView.text=data.symbol

        Glide.with(requireContext()).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/"+data.id +".png"
        ).thumbnail(Glide.with(requireContext()).load(R.drawable.spinner))
            .into(binding.detailImageView)

        binding.detailPriceTextView.text=
            "${String.format("$%.4f",data.quotes[0].price)}"

        if (data.quotes!![0].percentChange24h > 0){
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.green))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_up)
            binding.detailChangeTextView.text = "+${String.format("%.02f",data.quotes[0].percentChange24h)} %"
        }else{
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.red))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_down)
            binding.detailChangeTextView.text =  "${String.format("%.02f",data.quotes[0].percentChange24h)} %"
        }

    }

}