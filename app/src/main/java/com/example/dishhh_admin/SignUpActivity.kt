package com.example.dishhh_admin


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dishhh_admin.databinding.ActivitySignUpBinding
import com.example.dishhh_admin.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var nameOfRestaurant: String
    private lateinit var database: DatabaseReference

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Firebase Auth Initialization
        auth = Firebase.auth
        //Firebase Database Initialize
        database = Firebase.database.reference


        binding.createUserButton.setOnClickListener {
            //Get Text From Edit Text
            userName = binding.name.text.toString().trim()
            nameOfRestaurant = binding.restaurantName.text.toString().trim()
            email = binding.emailOrPhone.text.toString().trim()
            password = binding.password.text.toString().trim()

            if (userName.isBlank() || nameOfRestaurant.isBlank() || email.isBlank() || password.isBlank()){
                Toast.makeText(this,"Please fill all the details",Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email,password)
            }

        }
        binding.alreadyHaveAccountButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val locationList = arrayOf("Lovely Professional University, Jalandhar")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(this,"Account created successfully",Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this,"Account creation failed",Toast.LENGTH_SHORT).show()
                Log.d("Account","createAccount: Failure",task.exception)
            }
        }
    }
    //Saving Data into Database
    private fun saveUserData() {
        userName = binding.name.text.toString().trim()
        nameOfRestaurant = binding.restaurantName.text.toString().trim()
        email = binding.emailOrPhone.text.toString().trim()
        password = binding.password.text.toString().trim()
        val user = UserModel(userName,nameOfRestaurant,email,password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        //Saving User Data in FirebaseDatabase
        database.child("user").child(userId).setValue(user)
    }
}