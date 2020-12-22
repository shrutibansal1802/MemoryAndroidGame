package com.example.memorygame

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memorygame.models.BoardSize
import com.example.memorygame.models.MemoryGame
import com.example.memorygame.utils.EXTRA_BOARD_SIZE
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES as AppCompatDelegateMODE_NIGHT_YES


class MainActivity : AppCompatActivity() {

    private lateinit var rvBoard : RecyclerView
    private lateinit var clroot :ConstraintLayout
    private  lateinit var numMoves : TextView
    private lateinit var numScore : TextView
    private lateinit var memoryGame: MemoryGame

    companion object{
        private val TAG ="MainActivity"
        private const val  CREATE_REQUEST_CODE = 100
    }

    private var boardSize: BoardSize =com.example.memorygame.models.BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        rvBoard = findViewById(R.id.rvBoard)
        numMoves = findViewById(R.id.numMoves)
        numScore = findViewById(R.id.numScore)
        clroot = findViewById(R.id.clroot)

        setupboard()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.refreshbtn -> {
                if (memoryGame.totalMoves > 0 && !memoryGame.isWin()) {
                    setAlert( "Quit the Game?",view = null,positiveButtonClickListener = View.OnClickListener {  setupboard()})
                }
                else{
                    setupboard()
                }
                return true
            }
            R.id.newSize->{
                setSizeAlert()
                return true
            }
            R.id.change_theme -> {
                Log.i(TAG, "onOptionsItemSelected: Change Theme , ${AppCompatDelegate.getDefaultNightMode()}")
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                return true
            }

            R.id.custom_game ->{
                //creating a custom game by asking user for images

                //asking the size of new game using setAlert
                val BoardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size,null)
                val radioGroupView = BoardSizeView.findViewById<RadioGroup>(R.id.radio_group)

                setAlert("Choose new Board Size", BoardSizeView, positiveButtonClickListener =View.OnClickListener {
                    val newdesiredBoardSize:BoardSize = when(radioGroupView.checkedRadioButtonId)
                    {
                        R.id.btn_easy -> BoardSize.EASY
                        R.id.btn_medium -> BoardSize.MEDIUM
                        R.id.btn_hard -> BoardSize.HARD
                        else -> BoardSize.EASY
                    }
                    //starting up create activity
                    val intent =Intent(this, CreateActivity::class.java)
                    intent.putExtra(EXTRA_BOARD_SIZE ,newdesiredBoardSize)
                    startActivityForResult(intent, CREATE_REQUEST_CODE)
                })

            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setSizeAlert()
    {
        val BoardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size,null)
        val radioGroupView = BoardSizeView.findViewById<RadioGroup>(R.id.radio_group)
        when(boardSize)
        {
            BoardSize.EASY ->radioGroupView.check(R.id.btn_easy)
            BoardSize.MEDIUM -> radioGroupView.check(R.id.btn_medium)
            BoardSize.HARD -> radioGroupView.check(R.id.btn_hard)
        }
        setAlert("Choose new Board Size", BoardSizeView, positiveButtonClickListener =View.OnClickListener {
           boardSize = when(radioGroupView.checkedRadioButtonId)
           {
               R.id.btn_easy -> BoardSize.EASY
               R.id.btn_medium -> BoardSize.MEDIUM
               R.id.btn_hard -> BoardSize.HARD
               else -> boardSize
           }
            setupboard()
        })

    }

    private fun setAlert( title:String, view: View?, positiveButtonClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
                .setTitle(title)
                .setView(view)
                .setNegativeButton("cancel", null)
                .setPositiveButton("OK"){_,_->
                    positiveButtonClickListener.onClick(null)
                }.show()

    }

    private fun setupboard() {
        memoryGame = MemoryGame(boardSize)

        rvBoard.adapter = MemoryBoardAdapter(this, boardSize, memoryGame.cards, object : MemoryBoardAdapter.CardClickListener {
            override fun onCardClick(position: Int) {
                updateGamewithFlip(position)
            }

        })
        rvBoard.layoutManager =GridLayoutManager(this,boardSize.getWidth())

        numScore.text ="Score: ${memoryGame.numPairesFound}/${boardSize.getNumPairs()}" //update the score
        numMoves.text ="Moves:${memoryGame.totalMoves/2} "

    }

    private fun updateGamewithFlip(position: Int) {

        if (!memoryGame.cards[position].isFaceUp) {

            if (memoryGame.flipcard(position)){ //if a match is made
                numScore.text ="Score: ${memoryGame.numPairesFound}/${boardSize.getNumPairs()}" //update the score

                //if the user wins the game
                if (memoryGame.isWin()) {
                    Snackbar.make(clroot, "You Won!!",Snackbar.LENGTH_LONG).show()
                }
            }
            numMoves.text ="Moves:${memoryGame.totalMoves/2} "   //update the moves
        }

        rvBoard.adapter?.notifyDataSetChanged()
    }

}