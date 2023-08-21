package com.example.cryptocurrencyapp.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrencyapp.R
import com.example.cryptocurrencyapp.Ui.HomeFragmentDirections
import com.example.cryptocurrencyapp.Ui.MarketFragmentDirections
import com.example.cryptocurrencyapp.Ui.WatchListFragmentDirections
import com.example.cryptocurrencyapp.databinding.CurrencyItemLayoutBinding
import com.example.cryptocurrencyapp.models.CryptoCurrency


class MarketAdapter(var context: Context, var List: List<CryptoCurrency>, var type: String) :
    RecyclerView.Adapter<MarketAdapter.MarketViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {

        return MarketViewHolder(LayoutInflater.from(context).inflate(R.layout.currency_item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return List.size
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val item= List[position]


        holder.binding.currencyNameTextView.text=item.name
        holder.binding.currencySymbolTextView.text=item.symbol

        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/"+item.id +".png"
        ).thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.currencyImageView)

        Glide.with(context).load(
            "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/"+item.id +".png"
        ).thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.currencyChartImageView)


        holder.binding.currencyPriceTextView.text=
        "${String.format("$%.02f",item.quotes[0].price)}"

        if (item.quotes!![0].percentChange24h > 0){
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.currencyChangeTextView.text = "+${String.format("%.02f",item.quotes[0].percentChange24h)} %"
        }else{
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.currencyChangeTextView.text =  "-${String.format("%.02f",item.quotes[0].percentChange24h)} %"
        }


        holder.itemView.setOnClickListener {
            if (type == "home") {
                findNavController(it).navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailsFragment(item)
                )


            } else if (type == "Market") {
                findNavController(it).navigate(
                    MarketFragmentDirections.actionMarketFragmentToDetailsFragment2(item)

                )
            }else {
                findNavController(it).navigate(
                    WatchListFragmentDirections.actionWatchListFragmentToDetailsFragment2(item)
                )
            }
        }


    }

    class MarketViewHolder(View : View) : RecyclerView.ViewHolder(View){
        var binding= CurrencyItemLayoutBinding.bind(View)


        }

      fun updateDate(dataItem :  List<CryptoCurrency>){
          List = dataItem
          notifyDataSetChanged()
      }


    }


