package com.rm.todocomposemvvm.data.room.entity

import androidx.compose.ui.graphics.Color
import com.rm.todocomposemvvm.ui.theme.HighPriorityColor
import com.rm.todocomposemvvm.ui.theme.LowPriorityColor
import com.rm.todocomposemvvm.ui.theme.MediumPriorityColor
import com.rm.todocomposemvvm.ui.theme.NonePriorityColor

enum class Priority(val color: Color) {
    LOW(LowPriorityColor),
    MEDIUM(MediumPriorityColor),
    HIGH(HighPriorityColor),
    NONE(NonePriorityColor)
}