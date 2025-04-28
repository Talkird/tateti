package com.losauro.tateti.game

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlin.random.Random

class TicTacToe(private val playerSymbol: String) {
    val aiSymbol = if (playerSymbol == "X") "O" else "X"

    var board by mutableStateOf(List(3) { MutableList(3) { "" } })
        private set

    var currentPlayer by mutableStateOf("X")
        private set

    var gameOver by mutableStateOf(false)
        private set

    var winner by mutableStateOf<String?>(null)
        private set

    fun resetGame() {
        board = List(3) { MutableList(3) { "" } }
        currentPlayer = "X"
        gameOver = false
        winner = null

        if (playerSymbol == "O") {
            currentPlayer = aiSymbol
        }
    }

    fun makePlayerMove(row: Int, col: Int) {
        if (!gameOver && board[row][col].isEmpty() && currentPlayer == playerSymbol) {
            board = board.mapIndexed { r, rowList ->
                rowList.mapIndexed { c, value ->
                    if (r == row && c == col) playerSymbol else value
                }.toMutableList()
            }
            winner = checkWinner()
            gameOver = winner != null
            if (!gameOver) currentPlayer = aiSymbol
        }
    }

    suspend fun makeAIMove() {
        delay(Random.nextLong(500, 2000))
        val emptyCells = board.flatMapIndexed { r, row ->
            row.mapIndexedNotNull { c, value -> if (value.isEmpty()) r to c else null }
        }
        if (emptyCells.isNotEmpty()) {
            val (row, col) = emptyCells.random()
            board = board.mapIndexed { r, rowList ->
                rowList.mapIndexed { c, value ->
                    if (r == row && c == col) aiSymbol else value
                }.toMutableList()
            }
            winner = checkWinner()
            gameOver = winner != null
            if (!gameOver) currentPlayer = playerSymbol
        }
    }

    private fun checkWinner(): String? {
        val lines = listOf(
            // Filas
            listOf(board[0][0], board[0][1], board[0][2]),
            listOf(board[1][0], board[1][1], board[1][2]),
            listOf(board[2][0], board[2][1], board[2][2]),
            // Columnas
            listOf(board[0][0], board[1][0], board[2][0]),
            listOf(board[0][1], board[1][1], board[2][1]),
            listOf(board[0][2], board[1][2], board[2][2]),
            // Diagonales
            listOf(board[0][0], board[1][1], board[2][2]),
            listOf(board[0][2], board[1][1], board[2][0])
        )
        for (line in lines) {
            if (line.all { it == "X" }) return "X"
            if (line.all { it == "O" }) return "O"
        }
        return if (board.flatten().all { it.isNotEmpty() }) "Empate" else null
    }
}