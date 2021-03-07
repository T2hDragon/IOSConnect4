package com.example.connectfour

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.example.connectfour.gameLogic.GameState
import com.example.connectfour.gameLogic.GameStateEnums
import kotlinx.android.synthetic.main.stats.*


class MainGameActivity : AppCompatActivity() {

    private val gameState = GameState(7,6)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_game)
        playerOneWinsCount.text = gameState.getPlayerOneWins().toString()
        playerTwoWinsCount.text = gameState.getPlayerTwoWins().toString()
    }


    fun goBackButtonClick(view: View) {
        finish()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("gameStateString", gameState.serialize())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        gameState.deSerialize(savedInstanceState.getString("gameStateString", ""))
        playerOneWinsCount.text = gameState.getPlayerOneWins().toString()
        playerTwoWinsCount.text = gameState.getPlayerTwoWins().toString()
        for (x in 0..6) {
            for (y in 0..5) {
                val id: Int = resources.getIdentifier("imageView$x$y", "id", packageName)
                val button = findViewById<View>(id) as ImageView
                if (gameState.getGameButton(x,y).gameState == GameStateEnums.Neutral){
                    button.setImageResource(R.drawable.circle_default)
                    button.visibility = View.INVISIBLE
                } else {
                    button.setImageResource(if (gameState.getGameButton(x,y).gameState == GameStateEnums.PlayerOne) R.drawable.circle_red else R.drawable.circle_yellow)
                    button.visibility = View.VISIBLE
                }

            }
        }
    }



    fun gameColumnOnClick(view: View) {
        if (!gameState.isGameWon()) {
            val x = view.tag.toString().toInt()
            placeCircle(x)
            if (gameState.isGameWon()) {
                val alert = AlertDialog.Builder(this)
                alert.setMessage(if (gameState.checkPlayerOneTurn()) "Red Player wins!" else "Yellow Player Wins!")
                alert.show()
                playerOneWinsCount.text = gameState.getPlayerOneWins().toString()
                playerTwoWinsCount.text = gameState.getPlayerTwoWins().toString()
            }
        }
    }


    fun gameRestartButtonOnClick(view: View) {

        for (x in 0..6) {
            for (y in 0..5) {
                val id: Int = resources.getIdentifier("imageView$x$y", "id", packageName)
                val button = findViewById<View>(id) as ImageView
                button.setImageResource(R.drawable.circle_default)
                button.visibility = View.INVISIBLE
            }
        }
        gameState.restartGameBoard()
    }



    private fun placeCircle(x: Int) {
        val y = gameState.getNextFreeColumnSlot(x)
        if (gameState.checkIfInScope(x, y)) {
            val circleColor = if (gameState.checkPlayerOneTurn()) R.drawable.circle_red else R.drawable.circle_yellow
            val id: Int = resources.getIdentifier("imageView$x$y", "id", "com.example.connectfour")
            val button = findViewById<View>(id) as ImageView
            button.setImageResource(circleColor)
            button.visibility = View.VISIBLE
            ObjectAnimator.ofFloat(button, "translationY", button.width * -6f, 0f).apply {
                duration = 200
                start()
            }
            gameState.buttonPlaced(x, y)

        }

    }


}
