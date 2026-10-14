package com.example.hymykyl_minno.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hymykyl_minno.ui.screens.*
import com.example.hymykyl_minno.ui.viewmodel.AppViewModelProvider
import com.example.hymykyl_minno.ui.viewmodel.FeedbackViewModel

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
    modifier: Modifier = Modifier,
    viewModel: FeedbackViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    NavHost(
        navController = navController,
        startDestination = FeedbackScreen.NPS.name,
        modifier = modifier
    ) {
        composable(route = FeedbackScreen.NPS.name) {
            NpsScreen(
                onNext = { navController.navigate(FeedbackScreen.ServiceSelection.name) },
                viewModel = viewModel
            )
        }
        composable(route = FeedbackScreen.ServiceSelection.name) {
            ServiceSelectionScreen(
                onNext = { navController.navigate(FeedbackScreen.DetailedFeedback.name) },
                onBack = { navController.popBackStack() },
                onSkip = {
                    viewModel.submitFeedback()
                    navController.navigate(FeedbackScreen.ThankYou.name)
                },
                viewModel = viewModel
            )
        }
        composable(route = FeedbackScreen.DetailedFeedback.name) {
            DetailedFeedbackScreen(
                onNext = { navController.navigate(FeedbackScreen.Comment.name) },
                onBack = { navController.popBackStack() },
                onSkip = {
                    viewModel.submitFeedback()
                    navController.navigate(FeedbackScreen.ThankYou.name)
                },
                viewModel = viewModel
            )
        }
        composable(route = FeedbackScreen.Comment.name) {
            CommentScreen(
                onNext = { navController.navigate(FeedbackScreen.ThankYou.name) },
                onBack = { navController.popBackStack() },
                onSkip = {
                    viewModel.submitFeedback()
                    navController.navigate(FeedbackScreen.ThankYou.name)
                },
                viewModel = viewModel
            )
        }
        composable(route = FeedbackScreen.ThankYou.name) {
            ThankYouScreen(
                onFinish = {
                    navController.popBackStack(FeedbackScreen.NPS.name, inclusive = false)
                },
                viewModel = viewModel
            )
        }
    }
}
