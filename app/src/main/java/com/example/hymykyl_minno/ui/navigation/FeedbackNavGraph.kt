package com.example.hymykyl_minno.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hymykyl_minno.ui.screens.*

enum class FeedbackScreen {
    NPS,
    ServiceSelection,
    DetailedFeedback,
    Comment,
    ThankYou
}

@Composable
fun FeedbackNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = FeedbackScreen.NPS.name,
        modifier = modifier
    ) {
        composable(route = FeedbackScreen.NPS.name) {
            NpsScreen(
                onNext = { navController.navigate(FeedbackScreen.ServiceSelection.name) }
            )
        }
        composable(route = FeedbackScreen.ServiceSelection.name) {
            ServiceSelectionScreen(
                onNext = { navController.navigate(FeedbackScreen.DetailedFeedback.name) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(route = FeedbackScreen.DetailedFeedback.name) {
            DetailedFeedbackScreen(
                onNext = { navController.navigate(FeedbackScreen.Comment.name) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(route = FeedbackScreen.Comment.name) {
            CommentScreen(
                onNext = { navController.navigate(FeedbackScreen.ThankYou.name) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(route = FeedbackScreen.ThankYou.name) {
            ThankYouScreen(
                onFinish = {
                    navController.popBackStack(FeedbackScreen.NPS.name, inclusive = false)
                }
            )
        }
    }
}
