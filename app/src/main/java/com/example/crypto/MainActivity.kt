package com.example.crypto


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.crypto.adapter.Bitcoinadapter
import com.example.crypto.databinding.ActivityMainBinding
import com.example.crypto.models.Model

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var data: ArrayList<Model>
    private lateinit var originalData: ArrayList<Model>
    private lateinit var bitcoinadapter: Bitcoinadapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = ArrayList()
        originalData = ArrayList()

        bitcoinadapter = Bitcoinadapter(this, data)
        bitcoinadapter.setOnItemClickListener { position ->
            val selectedCoinId = data[position].id
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            intent.putExtra("coinId", selectedCoinId)
            startActivity(intent)
        }

        binding.recyclerview.adapter = bitcoinadapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)



        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterData(newText)
                return true
            }
        })

        fetchAllCoinData()

    }


    private fun fetchAllCoinData() {
        val url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&x_cg_demo_api_key=CG-vpTfa9k82N5G8LWakKCDfi7T"

        val queue = Volley.newRequestQueue(this)

        binding.progressBar.visibility = View.VISIBLE

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                try {
                    // Process each coin in the response
                    for (i in 0 until response.length()) {
                        val coin = response.getJSONObject(i)
                        val id = coin.getString("id")
                        val name = coin.getString("name")
                        val symbol = coin.getString("symbol")
                        val price = coin.getDouble("current_price")
                        val image = coin.getString("image")

                        data.add(Model(id, name, symbol, "$$price", image))
                        originalData.add(Model(id, name, symbol, "$$price", image))
                    }
                    bitcoinadapter.notifyItemInserted(data.size - 1)
                } catch (e: Exception) {
                    e.printStackTrace()
                }finally {
                    binding.progressBar.visibility = View.GONE
                }
            },
            { error ->
                error.printStackTrace()
            }
        )

        queue.add(jsonArrayRequest)
    }

    private fun filterData(query: String?) {
        if (query.isNullOrBlank()) {
            data.clear()
            data.addAll(originalData)
        } else {
            val filteredData = originalData.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.symbol.contains(query, ignoreCase = true)
            }
            data.clear()
            data.addAll(filteredData)
        }
        bitcoinadapter.notifyDataSetChanged()
    }

}
