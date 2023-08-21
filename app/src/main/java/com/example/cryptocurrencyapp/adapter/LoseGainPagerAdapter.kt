package com.example.cryptocurrencyapp.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cryptocurrencyapp.Ui.TopLoseGainFragment

class LoseGainPagerAdapter(fragment : Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
      val fragment = TopLoseGainFragment()
        val bundle= Bundle()
        bundle.putInt("position",position)
        fragment.arguments = bundle
        return fragment
    }
}