package com.example.tfg.models.viewmodels

import com.example.tfg.models.classes.Reminder

sealed class ReminderState {
    class Success(val data: MutableList<Reminder>) : ReminderState()
    class Failure(val message: String) : ReminderState()
    object Loading : ReminderState()
    object Empty : ReminderState()
}