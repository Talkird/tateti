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
import com.losauro.tateti.game.SoundPlayer
import com.losauro.tateti.ui.theme.InputBackground
import com.losauro.tateti.ui.theme.Primary
import com.losauro.tateti.ui.theme.heading
import com.losauro.tateti.ui.theme.gameFont
import androidx.compose.ui.platform.LocalContext
import com.losauro.tateti.game.TicTacToe

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

@Composable
fun GameScreen(playerName: String, selectedOption: String) {
    val ticTacToe = remember { TicTacToe(selectedOption) }
    val context = LocalContext.current

    LaunchedEffect(ticTacToe.currentPlayer) {
        if (!ticTacToe.gameOver && ticTacToe.currentPlayer != selectedOption) {
            ticTacToe.makeAIMove()
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
                text = "Juega $playerName con ${if (selectedOption == "X") "las Cruces" else "los Círculos"}",
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
                                onClick = { ticTacToe.makePlayerMove(row, col) },
                                modifier = Modifier.size(80.dp).padding(4.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = InputBackground)
                            ) {
                                Text(
                                    text = ticTacToe.board[row][col],
                                    style = gameFont,
                                    color = Color.White,
                                )
                            }
                        }
                    }
                }
            }

            if (ticTacToe.gameOver) {
                when (ticTacToe.winner) {
                    selectedOption -> SoundPlayer.playVictorySound(context)
                    ticTacToe.aiSymbol -> SoundPlayer.playDefeatSound(context)
                    "Empate" -> SoundPlayer.playDrawSound(context)
                }
            }

            if (ticTacToe.gameOver) {
                Text(
                    text = when (ticTacToe.winner) {
                        "Empate" -> "¡Es un empate! 🤝"
                        selectedOption -> "¡Ganaste, $playerName! \uD83D\uDE01"
                        else -> "¡La IA gana! \uD83E\uDD16"
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            Button(
                onClick = { ticTacToe.resetGame() },
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
}
