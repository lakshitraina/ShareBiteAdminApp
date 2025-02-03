package com.example.dishhh_admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dishhh_admin.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class MainActivity : AppCompatActivity() {

    // Initialize the binding object
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Declare Firestore and ListenerRegistration variables
    private lateinit var firestore: FirebaseFirestore
    private lateinit var orderCountListener: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Set up Firestore listener for real-time updates
        listenForOrderCountUpdates()

        // Button click listeners for other activities
        binding.addMenu.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }

        binding.allItemMenu.setOnClickListener {
            val intent = Intent(this, AllItemActivity::class.java)
            startActivity(intent)
        }

        binding.outForDeliveryButton.setOnClickListener {
            val intent = Intent(this, OutForDeliveryActivity::class.java)
            startActivity(intent)
        }

        binding.profile.setOnClickListener {
            val intent = Intent(this, AdminProfileActivity::class.java)
            startActivity(intent)
        }

        binding.cresteUser.setOnClickListener {
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        }

        binding.pendingOrderTextView.setOnClickListener {
            val intent = Intent(this, PendingOrderActivity::class.java)
            startActivity(intent)
        }

        // Handle window insets for UI adjustment
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Set up Firestore listener for order count updates
    private fun listenForOrderCountUpdates() {
        val orderCountRef = firestore.collection("order_count").document("count")

        // Real-time listener on the Firestore document
        orderCountListener = orderCountRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("MainActivity", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                // Get the updated order count
                val orderCount = snapshot.getLong("count") ?: 0

                // Update the porder TextView with the new order count
                binding.porder.text = orderCount.toString()
            } else {
                Log.d("MainActivity", "No such document")
            }
        }
    }

    // Remove the listener when the activity stops
    override fun onStop() {
        super.onStop()
        // Clean up listener to prevent memory leaks
        orderCountListener.remove()
    }
}
