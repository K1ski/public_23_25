package com.example.pz_23_25_guselnikova_PR23106

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            OnboardingScreen()
        }
    }
}

data class OnboardingPage(
    val bottomImageRes: Int,
    val title: String,
    val description: String,
    val isLastPage: Boolean,
    val bottomImageHeight: Dp,
    val bottomImagePaddingHorizontal: Dp
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen() {
    val context = LocalContext.current
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val pages = listOf(
        OnboardingPage(
            bottomImageRes = R.drawable.analysis_image,
            title = "Анализы",
            description = "Экспресс сбор и получение проб",
            isLastPage = false,
            bottomImageHeight = 201.dp,
            bottomImagePaddingHorizontal = 85.dp
        ),
        OnboardingPage(
            bottomImageRes = R.drawable.notifications_image,
            title = "Уведомления",
            description = "Вы быстро узнаете о результатах",
            isLastPage = false,
            bottomImageHeight = 217.dp,
            bottomImagePaddingHorizontal = 4.dp
        ),
        OnboardingPage(
            bottomImageRes = R.drawable.monitoring_image,
            title = "Мониторинг",
            description = "Наши врачи всегда наблюдают \nза вашими показателями здоровья",
            isLastPage = true,
            bottomImageHeight = 269.dp,
            bottomImagePaddingHorizontal = 7.dp
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = if (pagerState.currentPage == pages.lastIndex) "Завершить" else "Пропустить",
                color = Color(0xFF57A9FF),
                fontSize = 22.sp,
                modifier = Modifier
                    .clickable (
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        if (pagerState.currentPage == pages.lastIndex) {
                            val intent = Intent(context, LogInActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            context.startActivity(intent)
                        } else {
                            coroutineScope.launch {
                                pagerState.scrollToPage(pages.lastIndex)
                            }
                        }
                    }
            )
            Image(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(180.dp)
            )
        }

        HorizontalPager(
            count = pages.size,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageScreen(
                page = pages[page],
                pagerState = pagerState
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingPageScreen(
    page: OnboardingPage,
    pagerState: PagerState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = page.title,
            color = Color(0xFF00B612),
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = page.description,
            color = Color(0xFF939396),
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp),
            activeColor = Color(0xFF57A9FF),
            inactiveColor = Color.LightGray,
            indicatorWidth = 12.dp,
            spacing = 8.dp
        )

        Spacer(modifier = Modifier.height(50.dp))

        Image(
            painter = painterResource(id = page.bottomImageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(page.bottomImageHeight)
                .padding(horizontal = page.bottomImagePaddingHorizontal)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    OnboardingScreen()
}
