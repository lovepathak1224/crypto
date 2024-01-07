package com.example.crypto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import java.text.DecimalFormat
import com.example.crypto.databinding.ActivityHomeBinding



class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val coinId= intent.getStringExtra("coinId")!!
        fetchData(coinId)

        binding.back.setOnClickListener {
            onBackPressed()
        }

    }


    fun fetchData(id:String){
        val decimalFormat = DecimalFormat("#,##0.00")
        val url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=${id}&x_cg_demo_api_key=CG-vpTfa9k82N5G8LWakKCDfi7T"

        val queue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                try {
                        val coin = response.getJSONObject(0)
                        binding.coinName.text = coin.getString("name")
                        binding.coinSymbol.text = coin.getString("symbol")
                        binding.atl.text = "$" + decimalFormat.format(coin.getDouble("atl"))
                        binding.ath.text = "$" + decimalFormat.format(coin.getDouble("ath"))
                        binding.currentPrice.text = "$" + decimalFormat.format(coin.getDouble("current_price"))
                        binding.priceChange.text = decimalFormat.format(coin.getDouble("price_change_percentage_24h")) + "%"
                        binding.marketCap.text = coin.getLong("market_cap").toString()
                        binding.marketCapChange.text = decimalFormat.format(coin.getDouble("market_cap_change_percentage_24h")) + "%"
                        binding.totalVolume.text = decimalFormat.format(coin.getDouble("total_volume"))
                        binding.circulatingSupply.text = decimalFormat.format(coin.getDouble("circulating_supply"))
                    Glide.with(this)
                        .load(coin.getString("image"))
                        .placeholder(R.drawable.bitcoin)
                        .into(binding.image)

                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    binding.constraint.visibility = View.VISIBLE
                }
            },
            { error ->
                error.printStackTrace()
            }
        )

        queue.add(jsonArrayRequest)

    }
}