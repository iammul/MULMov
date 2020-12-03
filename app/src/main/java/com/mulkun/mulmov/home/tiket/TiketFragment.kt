package com.mulkun.mulmov.home.tiket

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.mulkun.mulmov.R
import com.mulkun.mulmov.home.dashboard.ComingSoonAdapter
import com.mulkun.mulmov.model.Film
import com.mulkun.mulmov.utils.Preferences
import kotlinx.android.synthetic.main.fragment_tiket.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TiketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TiketFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var preferences: Preferences
    private lateinit var mDatabase: DatabaseReference
    private var dataList = ArrayList<Film>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tiket, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(context!!)
        mDatabase = FirebaseDatabase.getInstance().getReference("Film")

        rc_tiket.layoutManager=LinearLayoutManager(context)
        getData()
    }

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                dataList.clear()
                for (getdataSnapshot in p0.children){
                    val film = getdataSnapshot.getValue(Film::class.java)
                    dataList.add(film!!)
                }
                rc_tiket.adapter = ComingSoonAdapter(dataList){
                    var intent = Intent(context, TiketActivity::class.java).putExtra("data",it)
                    startActivity(intent)
                }
                tv_total.setText("${dataList.size} Movies")
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, ""+p0.message, Toast.LENGTH_LONG).show()
            }

        })
    }


}