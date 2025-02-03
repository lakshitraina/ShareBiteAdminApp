package com.example.dishhh_admin

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.dishhh_admin.databinding.ActivityAddItemBinding
import com.google.firebase.firestore.FirebaseFirestore

class AddItemActivity : AppCompatActivity() {

    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private lateinit var foodIngredient: String
    private var foodImageUri: Uri? = null

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize Firebase Firestore
        val db = FirebaseFirestore.getInstance()

        binding.AddItemButton.setOnClickListener {
            foodName = binding.foodName.text.toString().trim()
            foodPrice = binding.foodPrice.text.toString().trim()
            foodDescription = binding.description.text.toString().trim()
            foodIngredient = binding.ingredients.text.toString().trim()

            if (foodName.isNotBlank() && foodPrice.isNotBlank() && foodDescription.isNotBlank() && foodIngredient.isNotBlank()) {
                uploadData(db)
                Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Fill All The Details", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle image selection (optional)
        binding.selectImage.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun uploadData(db: FirebaseFirestore) {
        // Create a map with food item details
        val foodItem = hashMapOf(
            "name" to foodName,
            "price" to foodPrice,
            "description" to foodDescription,
            "ingredients" to foodIngredient
        )

        // Save the item to Firestore
        db.collection("food")
            .add(foodItem)
            .addOnSuccessListener {
                Toast.makeText(this, "Food item added successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error adding food item: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.selectedImage.setImageURI(uri)
            foodImageUri = uri
        }
    }
}
