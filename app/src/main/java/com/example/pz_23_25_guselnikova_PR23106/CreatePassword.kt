package com.example.pz_23_25_guselnikova_PR23106

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.pointer.pointerInput

class CreatePassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CreatePasswordScreen(onSkip = {
                navigateToPatientCard()
            }, onPasswordComplete = {
                navigateToPatientCard()
            })
        }
    }

    private fun navigateToPatientCard() {
        val intent = Intent(this, Card::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }
}

@Composable
fun CreatePasswordScreen(onSkip: () -> Unit, onPasswordComplete: () -> Unit) {
    var password by remember { mutableStateOf("") }
    val maxPasswordLength = 4

    Scaffold(
        modifier = Modifier.fillMaxSize().background(Color.White),
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Пропустить",
                    color = Color.Blue,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onSkip()
                        }
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "Создайте пароль",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Для защиты ваших персональных данных",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (i in 1..maxPasswordLength) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(
                                color = if (i <= password.length) Color(0xFF1A6FEE) else Color.Transparent,
                                shape = CircleShape
                            )
                            .border(
                                width = 1.dp,
                                color = Color(0xFF1A6FEE),
                                shape = CircleShape
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val numbers = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("", "0", "⌫")
                )

                numbers.forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        row.forEach { number ->
                            NumberButton(
                                label = number,
                                onClick = {
                                    when (number) {
                                        "⌫" -> if (password.isNotEmpty()) password = password.dropLast(1)
                                        else -> if (password.length < maxPasswordLength) password += number
                                    }
                                    if (password.length == maxPasswordLength) {
                                        onPasswordComplete()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NumberButton(label: String, onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(80.dp)
            .background(
                color = when {
                    label == "⌫" -> Color.Transparent
                    label == "" -> Color.Transparent
                    isPressed -> Color(0xFF1A6FEE)
                    else -> Color(0xFFF5F5F9)
                },
                shape = CircleShape
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            }
    ) {
        Text(
            text = label,
            fontSize = 24.sp,
            color = if (isPressed && label != "⌫") Color.White else Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreatePreviewPassword() {
    CreatePasswordScreen(
        onSkip = {  },
        onPasswordComplete = {  }
    )
}