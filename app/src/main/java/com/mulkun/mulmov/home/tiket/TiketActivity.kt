package com.mulkun.mulmov.home.tiket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mulkun.mulmov.R
import com.mulkun.mulmov.model.Checkout
import com.mulkun.mulmov.model.Film
import kotlinx.android.synthetic.main.activity_tiket.*

class TiketActivity : AppCompatActivity() {

    private var dataList = ArrayList<Checkout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tiket)

        var data = intent.getParcelableExtra<Film>("data")

        tv_title.text = data?.judul
        tv_genre.text = data?.genre
        tv_rate.text = data?.rating

        Glide.with(this)
            .load(data?.poster)
            .into(iv_poster_image)

        rc_checkout.layoutManager = LinearLayoutManager(this)
        dataList.add(Checkout("C1", ""))
        dataList.add(Checkout("C2", ""))

        rc_checkout.adapter = TiketAdapter(dataList){

        }
    }
}