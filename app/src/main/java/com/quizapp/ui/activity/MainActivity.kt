package com.quizapp.ui.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.quizapp.R
import com.quizapp.api.ApiInterface
import com.quizapp.api.ApiUtilities
import com.quizapp.databinding.ActivityMainBinding
import com.quizapp.model.User
import com.quizapp.repository.QuizRepository
import com.quizapp.viewmodel.QuizViewModel
import com.quizapp.viewmodel.QuizViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val email = intent.getStringExtra("email")!!
        val name = intent.getStringExtra("name")!!

        binding.bStartQuiz.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }
        binding.bProfile.setOnClickListener {
            showProfileDialog(email,name,firebaseAuth)
        }

        binding.bSignOut.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    private fun showProfileDialog(name:String, email:String, firebaseAuth: FirebaseAuth){
        val uid = firebaseAuth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.profile_dialog)


        val etEmail = dialog.findViewById(R.id.etEmail) as EditText
        etEmail.setText(email)
        val etName = dialog.findViewById(R.id.etName) as EditText
        etName.setText(name)
        val btnsave = dialog.findViewById<Button>(R.id.bSave)

        val user = User(name, email)

        btnsave.setOnClickListener{
            if(uid != null){
                databaseReference.child(uid).setValue(user).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this,"Successfully Saved",Toast.LENGTH_SHORT).show()
                    }

                }.addOnFailureListener{
                    Toast.makeText(this,"Failed to Save",Toast.LENGTH_SHORT).show()
                    Log.i("Vibhuti", it.toString())
                }
            }
        }

        dialog.show()
    }
}