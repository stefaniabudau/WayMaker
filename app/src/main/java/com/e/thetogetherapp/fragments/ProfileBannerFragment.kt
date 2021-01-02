package com.e.thetogetherapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.e.thetogetherapp.R
import com.e.thetogetherapp.profile.NeedyProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileBannerFragment: Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var auth:FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.component_user_profile_banner_info, container, false)

        database = Firebase.database.reference
        auth = Firebase.auth

        val requests= view.findViewById<TextView>(R.id.needyNoOfRequests)
        val donations= view.findViewById<TextView>(R.id.needyNoOfDonations)
        val activities= view.findViewById<TextView>(R.id.needyNoOfOngoingActivities)

        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w("Banner", "loadBannerData:onCancelled", error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val uid = auth.currentUser!!.uid

                val allRequests = snapshot.child("requests").children
                val userRequests = allRequests.filter { it.child("needy").value == uid }
                val ongoingRequests = userRequests.filter { it.child("status").value == "ongoing" }

                val allDonations = snapshot.child("donations").children
                val userDonations = allDonations.filter { it.child("needy").value == uid }
                val ongoingDonations = userDonations.filter { it.child("status").value == "ongoing" }

                val ongoingActivities = ongoingDonations.count() + ongoingRequests.count()

                requests.text = userRequests.count().toString()
                donations.text = userDonations.count().toString()
                activities.text = ongoingActivities.toString()

            }
        })


        return view
    }
}