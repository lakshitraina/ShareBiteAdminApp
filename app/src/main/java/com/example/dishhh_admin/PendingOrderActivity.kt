package com.example.dishhh_admin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dishhh_admin.adapter.DeliveryAdapter
import com.example.dishhh_admin.adapter.PendingOrderAdapter
import com.example.dishhh_admin.databinding.ActivityAllItemBinding
import com.example.dishhh_admin.databinding.ActivityPendingOrderBinding
import com.example.dishhh_admin.databinding.PendingOrdersIterBinding

class PendingOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPendingOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingOrderBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        val orderedCustomerName = arrayListOf("Lakshit","Aashima","John")
        val orderedQuantity = arrayListOf("8","6","5")
        val orderedFoodImage = arrayListOf(R.drawable.menu1,R.drawable.menu2,R.drawable.menu3)
        val adapter = PendingOrderAdapter(orderedCustomerName,orderedQuantity,orderedFoodImage, this)
        binding.pendingOrderRecyclerView.adapter = adapter
        binding.pendingOrderRecyclerView.layoutManager = LinearLayoutManager(this)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}