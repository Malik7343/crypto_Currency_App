package com.example.cryptocurrencyapp.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrencyapp.R
import com.example.cryptocurrencyapp.Ui.HomeFragmentDirections
import com.example.cryptocurrencyapp.Ui.WatchListFragmentDirections
import com.example.cryptocurrencyapp.databinding.TopCurrencyLayoutBinding
import com.example.cryptocurrencyapp.models.CryptoCurrency


class TopMarketAdapter(val context: Context, val List : List<CryptoCurrency>) :
    RecyclerView.Adapter<TopMarketAdapter.TopMarketViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopMarketViewHolder {

        return TopMarketViewHolder(LayoutInflater.from(context).inflate(R.layout.top_currency_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return List.size
    }

    override fun onBindViewHolder(holder: TopMarketViewHolder, position: Int) {
        val item= List[position]

        holder.binding.topCurrencyNameTextView.text=item.name
        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/"+item.id +".png"
        ).thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.topCurrencyImageView)

        if (item.quotes!![0].percentChange24h > 0){
            holder.binding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.topCurrencyChangeTextView.text = "+${item.quotes[0].percentChange24h} %"
        }else{
            holder.binding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.topCurrencyChangeTextView.text = "- ${item.quotes[0].percentChange24h} %"
        }

        holder.itemView.setOnClickListener{
            Navigation.findNavController(it).navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailsFragment(item)
            )
        }

    }

    inner class TopMarketViewHolder(View : View) : RecyclerView.ViewHolder(View){
        var binding= TopCurrencyLayoutBinding.bind(View)


        }
    }

