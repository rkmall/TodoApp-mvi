package com.rm.todocomposemvvm.ui.base

/**
 * A [ViewState] flows from ViewModel to Composable.
 * It describes what screen state should be displayed right now.
 */
interface ViewState

/**
 * A [ViewEvent] flows from Composable to ViewModel.
 * When a user takes action via UI, [ViewEvent] represents the user's Intent.
 * As a result of user action, it either results in the [ViewState] change
 * or invokes [ViewSideEffect] to launch.
 * It is equivalent to ViewAction or MVI Intent.
 */
interface ViewEvent

/**
 * A [ViewSideEffect] flows from ViewModel to Composable.
 * It represents the side-effect that happens as a result of [ViewEvent].
 * It is commonly used for sending navigation updates to UI, show snackbar
 * or show other pieces of UI that is not tied to [ViewState].
 */
interface ViewSideEffect