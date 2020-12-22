package com.example.memorygame.models

import com.example.memorygame.utils.DEFAULT_ICONS

class MemoryGame(val boardSize: BoardSize) {

    val cards :List<MemoryCard>
    var numPairesFound =0
    var totalMoves =0
    private var singleselectedCard :Int? = null
    init {

        val icons = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomImages = (icons + icons).shuffled()
        cards = randomImages.map { MemoryCard(it) }

    }

    fun flipcard(position: Int) :Boolean{

        val card = cards[position]
        var foundCard=false
        //when 0 or 2 cards are already flipped
        if (singleselectedCard ==null) {
            restoreCards()
            singleselectedCard =position
        }
        //when 1 card is selected
        else{
            //if there is a match
            if (cards[singleselectedCard!!].identifier ==cards[position].identifier)
            {
                cards[singleselectedCard!!].isMatched= true
                cards[position].isMatched = true
                numPairesFound++
                foundCard=true
            }
           singleselectedCard =null
        }
        card.isFaceUp= !card.isFaceUp
        totalMoves++
        return foundCard
    }

    private fun restoreCards() {
         cards.map { if (!it.isMatched) it.isFaceUp=false}
    }
     fun isWin():Boolean{
        return numPairesFound == boardSize.getNumPairs()
    }
}