package com.example.pz_23_25_guselnikova_PR23106

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

class Code : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CodeScreen(
                onBackClick = { navigateToLogin() },
                onCodeEntered = { code -> if (code == "1234") navigateToCreatePassword() }
            )
        }
    }

    private fun navigateToCreatePassword() {
        val intent = Intent(this, CreatePassword::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LogInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeScreen(
    onBackClick: () -> Unit = {},
    onCodeEntered: (String) -> Unit = {}
) {
    var code by remember { mutableStateOf(List(4) { "" }) }
    var timerText by remember { mutableStateOf("Отправить код повторно можно будет через 59 секунд") }

    LaunchedEffect(Unit) {
        val timerDuration = 60000L
        val interval = 1000L
        object : CountDownTimer(timerDuration, interval) {
            override fun onTick(millisUntilFinished: Long) {
                timerText = "Отправить код повторно можно будет через ${millisUntilFinished / 1000} секунд"
            }

            override fun onFinish() {
                timerText = "Отправить код повторно"
            }
        }.start()
    }

    val focusRequesters = List(4) { FocusRequester() }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(Color.White),
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }

            Spacer(modifier = Modifier.height(120.dp))

            Text(
                text = "Введите код из E-mail",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                code.forEachIndexed { index, text ->
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .border(
                                width = 1.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(Color.White, RoundedCornerShape(8.dp))
                    ) {
                        OutlinedTextField(
                            value = text,
                            onValueChange = { newValue ->
                                if (newValue.length <= 1) {
                                    code = code.toMutableList().apply { this[index] = newValue }
                                    if (newValue.isNotEmpty() && index < focusRequesters.size - 1) {
                                        focusRequesters[index + 1].requestFocus()
                                    } else if (newValue.isEmpty() && index > 0) {
                                        focusRequesters[index - 1].requestFocus()
                                    }
                                    if (code.joinToString("") == "1234") {
                                        onCodeEntered("1234")
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .focusRequester(focusRequesters[index]),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = TextAlign.Center,
                                fontSize = 22.sp,
                                color = Color.Black
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            visualTransformation = VisualTransformation.None,
                            colors = outlinedTextFieldColors(
                                containerColor = Color(0xFFF5F4F9),
                                focusedBorderColor = Color(0xFFEAEAEA),
                                unfocusedBorderColor = Color(0xFFEAEAEA)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = timerText,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCodeScreen() {
    CodeScreen()
}