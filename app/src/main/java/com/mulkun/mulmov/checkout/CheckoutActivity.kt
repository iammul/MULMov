package com.mulkun.mulmov.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mulkun.mulmov.R
import com.mulkun.mulmov.model.Checkout
import com.mulkun.mulmov.model.Film
import com.mulkun.mulmov.utils.Preferences
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_pilih_bangku.*

class CheckoutActivity : AppCompatActivity() {


    private var dataList = ArrayList<Checkout>()
    private var total:Int = 0
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        preferences = Preferences(this)
        dataList = intent.getSerializableExtra("data") as ArrayList<Checkout>

        for (a in dataList.indices){
            total += dataList[a].harga!!.toInt()
        }

        dataList.add(Checkout("Total harus dibayar", total.toString()))

        rc_checkout.layoutManager = LinearLayoutManager(this)
        rc_checkout.adapter = CheckoutAdapter(dataList){

        }

        btn_tiket.setOnClickListener {
            var intent = Intent(this, CheckoutSuccesActivity::class.java)
            startActivity(intent)
        }

    }

}