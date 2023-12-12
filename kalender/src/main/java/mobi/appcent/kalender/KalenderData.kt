package mobi.appcent.kalender

import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class KalenderData {
    private val selectedDate: Calendar = Calendar.getInstance()
    val kalenderValues = MutableStateFlow<KalenderValues?>(null)

    init {
        kalenderValues.value = KalenderValues()
        setCalenderValues()
        yearFromDate()
        monthFromDate()
    }

    private fun yearFromDate() {
        kalenderValues.value = kalenderValues.value?.copy(year = SimpleDateFormat("yyyy", Locale.getDefault()).format(selectedDate.time ?: Date()))
    }

    private fun monthFromDate() {
        kalenderValues.value = kalenderValues.value?.copy(month = SimpleDateFormat("MMMM", Locale.getDefault()).format(selectedDate.time ?: Date()))
    }

    fun nextMonthAction() {
        selectedDate.add(Calendar.MONTH, 1)
        setCalenderValues()
        monthFromDate()
        yearFromDate()
    }

    fun previousMonthAction() {
        selectedDate.add(Calendar.MONTH, -1)
        setCalenderValues()
        monthFromDate()
        yearFromDate()
    }

    private fun setCalenderValues(){
        kalenderValues.value?.days?.clear()
        val year = selectedDate.get(Calendar.YEAR)
        val month = selectedDate.get(Calendar.MONTH)
        val currentMonthDayCount = getDaysInMonth(year, month)
        val firstDayOfMonth = selectedDate.clone() as Calendar
        firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1)
        val prevWeekDayCount =
            if (firstDayOfMonth.get(Calendar.DAY_OF_WEEK) == 1) 7 else firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1

        setPrevMonthDate(prevWeekDayCount)
        setCurrentMonthDate(currentMonthDayCount)
        setNextMonthDate(currentMonthDayCount, prevWeekDayCount)
    }

    private fun setPrevMonthDate(prevWeekDayCount: Int) {
        val prevMonthCalendar = selectedDate.clone() as Calendar
        prevMonthCalendar.add(Calendar.MONTH, -1)
        val prevMonthDays = getDaysInMonth(
            prevMonthCalendar.get(Calendar.YEAR),
            prevMonthCalendar.get(Calendar.MONTH)
        )

        for (prevWeekDay in 1 until prevWeekDayCount) {
            val dateOnSelected = selectedDate.clone() as Calendar
            dateOnSelected.set(
                dateOnSelected.get(Calendar.YEAR),
                dateOnSelected.get(Calendar.MONTH) - 1,
                prevMonthDays - prevWeekDayCount + prevWeekDay + 1
            )
            kalenderValues.value?.days?.add(
                KalenderDateModel(
                    day = prevMonthDays - prevWeekDayCount + prevWeekDay + 1
                )
            )
        }
    }

    private fun setCurrentMonthDate(currentMonthDayCount: Int) {
        for (currentMonthDay in 1..currentMonthDayCount) {
            selectedDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), currentMonthDay)
            kalenderValues.value?.days?.add(
                KalenderDateModel(
                    day = currentMonthDay,
                    isActive = true,
                )
            )
        }
    }

    private fun setNextMonthDate(currentMonthDayCount: Int, prevWeekDayCount: Int) {
        val nextMonthDayCount = 42 - (prevWeekDayCount - 1) - currentMonthDayCount
        for (nextMonthDay in 1..nextMonthDayCount) {
            val dateOnSelected = selectedDate.clone() as Calendar
            dateOnSelected.set(
                dateOnSelected.get(Calendar.YEAR),
                dateOnSelected.get(Calendar.MONTH) + 1,
                nextMonthDay
            )
            kalenderValues.value?.days?.add(
                KalenderDateModel(
                    day = nextMonthDay
                )
            )
        }
    }

    private fun getDaysInMonth(year: Int?, month: Int?): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year ?: 0, month ?: 0, 1)
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
}