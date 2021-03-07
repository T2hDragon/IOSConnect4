package com.example.connectfour

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun HumanVsHumanbuttonOnClick(view: View) {
        val intent = Intent(this, MainGameActivity::class.java)
        startActivity(intent)
    }


    fun notImplementedOnClick(view: View) {
        val alert = AlertDialog.Builder(this)
        alert.setMessage("Feature not yet implemented")
        alert.show()
    }
    fun ExitButtonOnClick(view: View) {
        finish()
    }

}