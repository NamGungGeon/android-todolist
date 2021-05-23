package kr.ac.konkuk.planman

import java.time.YearMonth


import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import java.time.DayOfWeek
import java.time.temporal.WeekFields
import java.util.*


fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInVisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun dpToPx(dp: Int, context: Context): Int =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
        context.resources.displayMetrics
    ).toInt()

internal fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return context.layoutInflater.inflate(layoutRes, this, attachToRoot)
}

internal val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)

internal val Context.inputMethodManager
    get() = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

internal inline fun Boolean?.orFalse(): Boolean = this ?: false

internal fun Context.getDrawableCompat(@DrawableRes drawable: Int) = ContextCompat.getDrawable(this, drawable)

internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

internal fun TextView.setTextColorRes(@ColorRes color: Int) = setTextColor(context.getColorCompat(color))

fun daysOfWeekFromLocale(): Array<DayOfWeek> {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var daysOfWeek = DayOfWeek.values()
    // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
    // Only necessary if firstDayOfWeek != DayOfWeek.MONDAY which has ordinal 0.
    if (firstDayOfWeek != DayOfWeek.MONDAY) {
        val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
        val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
        daysOfWeek = rhs + lhs
    }
    return daysOfWeek
}

fun GradientDrawable.setCornerRadius(
    topLeft: Float = 0F,
    topRight: Float = 0F,
    bottomRight: Float = 0F,
    bottomLeft: Float = 0F
) {
    cornerRadii = arrayOf(
        topLeft, topLeft,
        topRight, topRight,
        bottomRight, bottomRight,
        bottomLeft, bottomLeft
    ).toFloatArray()
}


private typealias Airport = Flight.Airport

fun generateFlights(): List<Flight> {
    val list = mutableListOf<Flight>()
    val currentMonth = YearMonth.now()

    val currentMonth17 = currentMonth.atDay(17)
    list.add(Flight(currentMonth17.atTime(14, 0), Airport("Lagos", "LOS"), Airport("Abuja", "ABV"), R.color.brown_700))
    list.add(Flight(currentMonth17.atTime(21, 30), Airport("Enugu", "ENU"), Airport("Owerri", "QOW"), R.color.blue_grey_700))

    val currentMonth22 = currentMonth.atDay(22)
    list.add(Flight(currentMonth22.atTime(13, 20), Airport("Ibadan", "IBA"), Airport("Benin", "BNI"), R.color.blue_800))
    list.add(Flight(currentMonth22.atTime(17, 40), Airport("Sokoto", "SKO"), Airport("Ilorin", "ILR"), R.color.red_800))

    list.add(
        Flight(
            currentMonth.atDay(3).atTime(20, 0),
            Airport("Makurdi", "MDI"),
            Airport("Calabar", "CBQ"),
            R.color.teal_700
        )
    )

    list.add(
        Flight(
            currentMonth.atDay(12).atTime(18, 15),
            Airport("Kaduna", "KAD"),
            Airport("Jos", "JOS"),
            R.color.cyan_700
        )
    )

    val nextMonth13 = currentMonth.plusMonths(1).atDay(13)
    list.add(Flight(nextMonth13.atTime(7, 30), Airport("Kano", "KAN"), Airport("Akure", "AKR"), R.color.pink_700))
    list.add(Flight(nextMonth13.atTime(10, 50), Airport("Minna", "MXJ"), Airport("Zaria", "ZAR"), R.color.green_700))

    list.add(
        Flight(
            currentMonth.minusMonths(1).atDay(9).atTime(20, 15),
            Airport("Asaba", "ABB"),
            Airport("Port Harcourt", "PHC"),
            R.color.orange_800
        )
    )

    return list
}