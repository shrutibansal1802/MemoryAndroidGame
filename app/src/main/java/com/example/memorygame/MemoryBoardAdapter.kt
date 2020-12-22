package com.example.memorygame

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.memorygame.models.BoardSize
import com.example.memorygame.models.MemoryCard
import kotlin.math.min

class MemoryBoardAdapter(private val context: Context, private val boardSize: BoardSize, private val memoryCards: List<MemoryCard>, private val cardClickListener:CardClickListener) :
        RecyclerView.Adapter<MemoryBoardAdapter.ViewHolder>() {

    companion object{
        private const val Margin= 10
        private const val TAG ="MemoryBoardAdapter"
    }
    interface CardClickListener{
        fun onCardClick(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val cardWidth = parent.width/boardSize.getWidth() -(Margin*2)
        val cardHeight = parent.height/boardSize.getHeight() -(Margin *2)
        val sideLength = min(cardWidth,cardHeight)
         val view =LayoutInflater.from(context).inflate(R.layout.recyclercard, parent,false)
        val layoutParams = view.findViewById<CardView>(R.id.ImageCard).layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.height = sideLength
        layoutParams.width = sideLength
        layoutParams.setMargins(Margin, Margin, Margin, Margin)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = boardSize.numCards

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)

        fun bind(position: Int) {
            val memoryCard =memoryCards[position]

            imageButton.setImageResource( if (memoryCard.isFaceUp) memoryCard.identifier else R.drawable.ic_launcher_background)
            imageButton.alpha = if (memoryCard.isMatched) 0.4f else 1.0f
            val colorStateList= if (memoryCard.isMatched) ContextCompat.getColorStateList(context,R.color.gray) else null
            ViewCompat.setBackgroundTintList(imageButton,colorStateList)

            imageButton.setOnClickListener(){
                Log.i(TAG, "imageButton clicked number $position ")
                cardClickListener.onCardClick(position)
                  }
        }

    }
}
