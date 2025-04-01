package com.losauro.tateti

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.losauro.tateti.ui.theme.Primary
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.losauro.tateti.ui.components.Input
import com.losauro.tateti.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Theme {
                HomeScreen()
            }
        }
    }

    @Composable
    fun HomeScreen() {
        var playerName by remember { mutableStateOf(TextFieldValue("")) }
        var selectedOption by remember { mutableStateOf("X") }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Text(
                    text = "Ta Te Ti" ,
                    style = heading,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )

                Input(value = playerName, onValueChange = { playerName = it }, label = "Nombre")

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Selecciona tu símbolo:", style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedOption == "X",
                            onClick = { selectedOption = "X" },
                            colors = RadioButtonDefaults.colors(selectedColor = Primary)
                        )
                        Text(
                            text = "X",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedOption == "O",
                            onClick = { selectedOption = "O" },
                            colors = RadioButtonDefaults.colors(selectedColor = Primary)
                        )
                        Text(
                            text = "O",

                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                    }
                }

                Button(
                    onClick = {
                        val intent = Intent(this@MainActivity, GameActivity::class.java).apply {
                            putExtra("playerName", playerName.text)
                            putExtra("selectedOption", selectedOption)
                        }
                        startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                ) {
                    Text(
                        text = "COMENZAR JUEGO",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    )
                }
            }
        }
    }
}