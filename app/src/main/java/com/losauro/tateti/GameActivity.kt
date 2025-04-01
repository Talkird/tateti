package com.losauro.tateti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.losauro.tateti.ui.theme.DarkBackground
import com.losauro.tateti.ui.theme.Theme
import androidx.compose.ui.unit.dp
import com.losauro.tateti.ui.theme.InputBackground
import com.losauro.tateti.ui.theme.Primary
import kotlinx.coroutines.delay
import kotlin.random.Random
import com.losauro.tateti.ui.theme.heading
import com.losauro.tateti.ui.theme.gameFont

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val playerName = intent.getStringExtra("playerName")?.takeIf { it.isNotBlank() } ?: "Extraño"
        val selectedOption = intent.getStringExtra("selectedOption") ?: "X"

        setContent {
            Theme {
                GameScreen(playerName, selectedOption)
            }
        }
    }
}

@Composable
fun GameScreen(playerName: String, selectedOption: String) {
    var board by remember { mutableStateOf(List(3) { MutableList(3) { "" } }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var gameOver by remember { mutableStateOf(false) }
    var winner by remember { mutableStateOf<String?>(null) }
    val playerSymbol = selectedOption
    val aiSymbol = if (playerSymbol == "X") "O" else "X"

    fun checkWinner(): String? {
        val lines = listOf(
            // Rows
            listOf(board[0][0], board[0][1], board[0][2]),
            listOf(board[1][0], board[1][1], board[1][2]),
            listOf(board[2][0], board[2][1], board[2][2]),
            // Columns
            listOf(board[0][0], board[1][0], board[2][0]),
            listOf(board[0][1], board[1][1], board[2][1]),
            listOf(board[0][2], board[1][2], board[2][2]),
            // Diagonals
            listOf(board[0][0], board[1][1], board[2][2]),
            listOf(board[0][2], board[1][1], board[2][0])
        )
        for (line in lines) {
            if (line.all { it == "X" }) return "X"
            if (line.all { it == "O" }) return "O"
        }
        return if (board.flatten().all { it.isNotEmpty() }) "Empate" else null
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
            currentPlayer = if (!gameOver) playerSymbol else ""
        }
    }

    LaunchedEffect(currentPlayer) {
        if (!gameOver && currentPlayer == aiSymbol) {
            makeAIMove()
        }
    }

    LaunchedEffect(Unit) {
        if (playerSymbol == "O") {
            currentPlayer = aiSymbol
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ) {
            Text(
                text = "Juega $playerName con ${if (playerSymbol == "X") "las Cruces" else "los Círculos"}",
                color = Color.White,
                style = heading,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )

            Column {
                for (row in 0..2) {
                    Row {
                        for (col in 0..2) {
                            Button(
                                onClick = {
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
                                },
                                modifier = Modifier.size(80.dp).padding(4.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = InputBackground)
                            ) {
                                Text(
                                    text = board[row][col],
                                    style = gameFont,
                                    color = Color.White,
                                )
                            }
                        }
                    }
                }
            }

            if (gameOver) {
                Text(
                    text = when (winner) {
                        "Empate" -> "¡Es un empate! 🤝"
                        playerSymbol -> "¡Ganaste, $playerName! \uD83D\uDE01"
                        else -> "¡La IA gana! \uD83E\uDD16"
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            Button(
                onClick = {
                    board = List(3) { MutableList(3) { "" } }
                    currentPlayer = "X"
                    gameOver = false
                    winner = null
                },
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            ) {
                Text(
                    text = "JUGAR DE NUEVO",
                    color = Color.White,

                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                )
            }
        }
    }
}
