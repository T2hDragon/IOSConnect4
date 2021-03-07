package com.example.connectfour.gameLogic


class GameState(private var xLength: Int, private var yLength: Int) {


    private var buttons :Array<Array<GameButton>> = Array(xLength) { Array(yLength) { GameButton(GameStateEnums.Neutral) } }
    private var columnsData = Array(xLength) { 0 }
    private var gameWon = false
    private var playerOneTurn = true
    private var playerWins :Array<Int> = Array(2) {0}
    private var serializeCode = "%;%"

    fun  getGameButton(x:Int, y:Int): GameButton {
        return buttons[x][y]
    }


    private fun  setGameButtonPlayer(x:Int, y:Int, gameState: GameStateEnums) {
        buttons[x][y].gameState = gameState
    }


    fun checkPlayerOneTurn() : Boolean{
        return playerOneTurn
    }


    private fun playerTurnEnd() {
        playerOneTurn  = !playerOneTurn
    }


    fun getNextFreeColumnSlot(x: Int) :Int {
        return columnsData[x]
    }


    fun buttonPlaced(x: Int, y: Int) {
        if (checkIfInScope(x, y) && !gameWon) {
            columnsData[x]++
            this.setGameButtonPlayer(x, y, if (this.checkPlayerOneTurn()) GameStateEnums.PlayerOne else GameStateEnums.PlayerTwo)
            if (checkIfWon(x, y)) {
                gameWon = true
                playerWins[if (playerOneTurn) 0 else 1] += 1
            } else {
                this.playerTurnEnd()

            }

        }

    }


    private fun checkIfWon(x: Int, y: Int): Boolean {
        if (!gameWon) {
            if (getDirectionPoints(x, y, 1, 0) + getDirectionPoints(x, y, -1, 0) >= 3) {
                return true
            }
            if (getDirectionPoints(x, y, 0, 1) + getDirectionPoints(x, y, 0, -1) >= 3) {
                return true
            }
            if (getDirectionPoints(x, y, 1, 1) + getDirectionPoints(x, y, -1, -1) >= 3) {
                return true
            }
            if (getDirectionPoints(x, y, 1, -1) + getDirectionPoints(x, y, -1, 1) >= 3) {
                return true
            }
        }
        return false
    }
    private fun getDirectionPoints(xCurrent:Int, yCurrent:Int, xChange: Int, yChange: Int): Int{
        if (!checkIfInScope(xCurrent  + xChange, yCurrent  + yChange)) {
            return 0
        }
        if (this.getGameButton(xCurrent + xChange, yCurrent + yChange).gameState == if (playerOneTurn) GameStateEnums.PlayerOne else GameStateEnums.PlayerTwo){
            return 1 + getDirectionPoints(xCurrent + xChange,yCurrent + yChange, xChange, yChange)
        }
        return 0
    }

    fun checkIfInScope(x: Int, y: Int): Boolean{
        return x in 0 until xLength && y in 0 until yLength
    }

    fun isGameWon():Boolean{
        return gameWon
    }

    fun restartGameBoard() {
        buttons =  Array(7) { Array(6) { GameButton(GameStateEnums.Neutral) } }
        columnsData = Array(7) { 0 }
        gameWon = false
    }

    fun getPlayerOneWins() :Int {
        return playerWins[0]
    }

    fun getPlayerTwoWins() :Int {
        return playerWins[1]
    }



    fun serialize() : String {

        var serialization = ""
        for (column in 0 until xLength) {
            for (row in 0 until yLength){
                serialization +="" + buttons[column][row].gameState.toString() + serializeCode
            }
            serialization = serialization.substring(0, serialization.length - serializeCode.length)
            serialization += "\n"
        }
        for (column in 0 until xLength){
            serialization +=  columnsData[column].toString() + serializeCode
        }
        serialization = serialization.substring(0, serialization.length - serializeCode.length)
        serialization += "\n" + gameWon.toString() + "\n" + playerOneTurn.toString() + "\n"
        for (player in 0..1){
            serialization +=  playerWins[player].toString() + serializeCode
        }
        serialization = serialization.substring(0, serialization.length - serializeCode.length)
        return serialization
    }

    fun deSerialize(serializedGameState: String){
        if (serializedGameState != "") {
            val data = serializedGameState.split("\n")
            buttons = Array(xLength) { Array(yLength) { GameButton(GameStateEnums.Neutral) } }
            for (column in 0 until xLength) {
                for (row in 0 until yLength) {
                    val buttonColumn = data[column].split(serializeCode)
                    val state = buttonColumn[row]
                    if (state != "Neutral") {
                        this.setGameButtonPlayer(
                            column,
                            row,
                            if (state == "PlayerOne") GameStateEnums.PlayerOne else GameStateEnums.PlayerTwo
                        )
                    }
                }
            }
            val columnsDataString = data[7].split(serializeCode)
            for (index in columnsDataString.indices) {

                columnsData[index] = columnsDataString[index].toInt()
            }
            gameWon = data[8] == "true"
            playerOneTurn = data[9] == "true"
            val playerWinsStringArray = data[10].split(serializeCode)
            for (index in playerWinsStringArray.indices) {
                playerWins[index] = playerWinsStringArray[index].toInt()
            }
        }
    }


}