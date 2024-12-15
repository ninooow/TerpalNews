package com.example.terpalnews.ui

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.emoji2.text.DefaultEmojiCompatConfig
import androidx.emoji2.text.EmojiCompat
import com.example.terpalnews.database.entities.UserEntity
import com.example.terpalnews.databinding.ActivityRegisterBinding
import com.example.terpalnews.model.User
import com.example.terpalnews.model.UserSend
import com.example.terpalnews.network.ApiClient
import com.example.terpalnews.ui.publik.MainPublicActivity
import com.example.terpalnews.utils.EncryptionUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val config = DefaultEmojiCompatConfig.create(this)
        if (config != null) {
            EmojiCompat.init(config)
        }
        with(binding){
            btnRegister.setOnClickListener {
                val name = inputName.text.toString()
                val username = inputUsn.text.toString()
                val email = inputEmail.text.toString()
                val password = inputPsw.text.toString()
                if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Mohon isi semua data",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val encryptedPassword = EncryptionUtil.encryptPassword(password)
                    Log.d(TAG, "Encrypted password: $encryptedPassword")
                    val newUser = User(name=name, username=username, email=email, password=encryptedPassword, role="public")
                    registerUser(newUser)
                }
            }

            toLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }
    }
    private fun registerUser(user: User) {
        val apiService = ApiClient.getInstance()

        apiService.registerUser(user).enqueue(object :Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Log.d("RegisterActivity", "Registrasi berhasil: ${response.body()}")
                    Toast.makeText(this@RegisterActivity, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                    saveUserSession(user.username, "public")
                    val intent = Intent(this@RegisterActivity, MainPublicActivity::class.java)
                    intent.putExtra("NAME", user.name)
                    intent.putExtra("USERNAME", user.username)
                    intent.putExtra("EMAIL", user.email)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("RegisterActivity", "Error: ${response.errorBody()?.string()}")
                    val message = response.errorBody()?.string()
                    Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("RegisterActivity", "Gagal registrasi: ${t.message}")
                Toast.makeText(this@RegisterActivity, "Gagal registrasi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserSession(username: String, level: String) {
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.putString("level", level)
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
    }
}
