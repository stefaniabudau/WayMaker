package com.e.thetogetherapp.pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.e.thetogetherapp.R
import com.e.thetogetherapp.data.model.Event
import com.e.thetogetherapp.data.model.Rating
import com.e.thetogetherapp.data.model.User
import com.e.thetogetherapp.profile.UserProfileActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class AssignedEventPage : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_assigned_event)
        val database = Firebase.database.reference

        var uidNeedy: String? = null
        var uidVolunteer: String? = null
        var eventId: String? = null
        var eventType: String? = null
        var userType: String? = null

        val extras: Bundle? = intent.extras

        if(extras != null){
            userType = extras.getString("userType")
            eventType = extras.getString("eventType")
            eventId = extras.getString("eventId")
            uidNeedy = extras.getString("uidNeedy")
            uidVolunteer = extras.getString("uidVolunteer")
        }

        // View
        val eventTitle = findViewById<TextView>(R.id.eventTitle)
        val eventLocation = findViewById<TextView>(R.id.eventLocation)
        val eventDate = findViewById<TextView>(R.id.eventDate)
        val eventTime = findViewById<TextView>(R.id.eventTime)
        val eventDescription = findViewById<TextView>(R.id.eventDescription)
        val eventCategory = findViewById<TextView>(R.id.eventCategory)

        //get view
        val eventRef = database.child(eventType!!).child(eventId!!)
        eventRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w("Header", "loadUserData:onCancelled", error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val eventData = snapshot.getValue<Event>()
                eventTitle.text = eventData!!.title
                eventLocation.text = eventData!!.location
                eventDate.text = eventData!!.date
                eventTime.text = eventData!!.time
                eventDescription.text = eventData!!.description
                eventCategory.text = eventData!!.category

            }
        })

        val needyNicknameReviewText = findViewById<TextView>(R.id.creatorNicknameReviewText)
        val volunteerNicknameReviewText = findViewById<TextView>(R.id.assigneeNicknameReviewText)

        val userTypeText = findViewById<TextView>(R.id.userTypeText)
        val userNicknameText = findViewById<TextView>(R.id.userNicknameText)

        // Get user info
        val userRef = database.child("users").child(uidNeedy!!)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w("Header", "loadUserData:onCancelled", error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue<User>()

                needyNicknameReviewText.text = userData!!.nickname
                userNicknameText.text = userData!!.nickname
                userTypeText.text = userData!!.type
            }
        })

        val userTypeText2 = findViewById<TextView>(R.id.userTypeText2)
        val userNicknameText2 = findViewById<TextView>(R.id.userNicknameText2)

        // Get user info
        val userRef2 = database.child("users").child(uidVolunteer!!)
        userRef2.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w("Header", "loadUserData:onCancelled", error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue<User>()

                volunteerNicknameReviewText.text = userData!!.nickname
                userNicknameText2.text = userData!!.nickname
                userTypeText2.text = userData!!.type
            }
        })

        //Buttons
        val completedEventButton = findViewById<Button>(R.id.completedEventButton)
        val cancelEventButton = findViewById<Button>(R.id.cancelEventButton)
        val eventAssignedGoBackButton = findViewById<View>(R.id.eventAssignedGoBackButton)

        eventAssignedGoBackButton.setOnClickListener{
            finish()
        }

        completedEventButton.setOnClickListener{
            startActivity(Intent(this@AssignedEventPage, GiveReviewPage::class.java))
        }

        cancelEventButton.setOnClickListener{
            val event = database.child("event").child(eventId!!)
            if(userType.equals("needy")){

            }
            if(userType.equals("volunteer")){

            }
            finish()
        }


    }
}