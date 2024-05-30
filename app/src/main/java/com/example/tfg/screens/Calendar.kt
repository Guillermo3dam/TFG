package com.example.tfg.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.tfg.R
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale



@Composable
fun CalendarScreen(NavController: NavController){

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
    ) {

            ConstraintLayout (modifier = Modifier.fillMaxSize()){

                val(calendar, floatinButton) = createRefs() //Identificadores
                val GuiaHorizontal = createGuidelineFromBottom(0.12f)
                val GuiaVertical = createGuidelineFromEnd(0.04f)
                Box(modifier = Modifier.constrainAs(calendar){

                }){
                   Calendar()
               }
                Box(
                    modifier = Modifier
                        .size(55.dp)
                        .clip(CircleShape)
                        .constrainAs(floatinButton){
                            end.linkTo(GuiaVertical)
                            bottom.linkTo(GuiaHorizontal)
                        }
                ){
                    FloatingButton()
                }
            }
    }
}



@Composable
fun Calendar() {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val daysOfWeek = remember { daysOfWeek() }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )
/*
    Column {
        var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
        HorizontalCalendar(
            state = state,
            dayContent = { day ->
                Day(day, isSelected = selectedDate == day.date) { day ->
                    selectedDate = if (selectedDate == day.date) null else day.date
                }
            },
            monthHeader = { month ->
                MonthHeader(month)
            }
        )
    }
}

@Composable
fun MonthHeader(month: CalendarMonth) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        val monthName = getMonthName(month.yearMonth.monthValue)
        val year = month.yearMonth.year
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.left_arrow),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .size(30.dp)
                    .padding(8.dp)
            )
            Text(
                text = "$monthName  $year",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 16.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.right_arrow),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .size(30.dp)
                    .padding(8.dp)
            )
        }
        val daysOfWeek = month.weekDays.first().map { it.date.dayOfWeek }
        DaysOfWeekTitle(daysOfWeek = daysOfWeek)
    }*/
}



private fun getMonthName(monthValue: Int): String {
    val monthNames = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
    return monthNames.getOrElse(monthValue - 1) { "" }
}


@Composable
fun Day(day: CalendarDay, isSelected: Boolean, onClick: (CalendarDay) -> Unit) {

    val currentDate = LocalDate.now()
    val currentDay = currentDate.dayOfMonth
    val currentMonth = currentDate.monthValue

    val isCurrentMonth = day.date.monthValue == currentMonth
    val isCurrentDay = day.date.dayOfMonth == currentDay && isCurrentMonth

    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(
                color = Color.Transparent,
                shape = CircleShape
            )
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(color = if (isSelected)  Color(0xFF57B262) else Color.Transparent)
                .clickable(
                    enabled = day.position == DayPosition.MonthDate,
                    onClick = {
                        onClick(day)
                    }
                )
                .border(
                    width = 1.dp,
                    color = if (isCurrentDay)  Color(0xFF57B262) else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                color = if (day.position == DayPosition.MonthDate) Color.Black else Color.White
            )
        }
    }
}




@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                color = Color.Black,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                style = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun FloatingButton() {
        FloatingActionButton(
            onClick = { },
            containerColor =  Color(0xFF57B262),
            contentColor = Color.Transparent
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White
            )
        }
}

