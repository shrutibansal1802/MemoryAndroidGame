package com.example.memorygame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.memorygame.models.BoardSize
import com.example.memorygame.utils.EXTRA_BOARD_SIZE

class CreateActivity : AppCompatActivity() {

    private lateinit var boardSize:BoardSize

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
         boardSize = intent.getSerializableExtra(EXTRA_BOARD_SIZE) as BoardSize
        supportActionBar?.title ="Choose Images : 0/4"

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}