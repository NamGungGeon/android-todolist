package kr.ac.konkuk.planman

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import kr.ac.konkuk.planman.databinding.CalendarDayLayoutBinding
import kr.ac.konkuk.planman.databinding.CalendarHeaderLayoutBinding
import kr.ac.konkuk.planman.databinding.FragmentCalendarTodoBinding
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*


data class Flight(
    val time: LocalDateTime,
    val departure: Airport,
    val destination: Airport,
    @ColorRes val color: Int
) {
    data class Airport(val city: String, val code: String)
}

class CalendarTodoFragment : Fragment() {

    private lateinit var binding: FragmentCalendarTodoBinding
    private val flights = generateFlights().groupBy { it.time.toLocalDate() }


    val daysOfWeek = daysOfWeekFromLocale()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCalendarTodoBinding.bind(view)
        binding.exFiveRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }

        binding.apply {
            calendarView.dayBinder = object : DayBinder<DayViewContainer> {
                // Called only when a new container is needed.
                override fun create(view: View) = DayViewContainer(view)

                // Called every time we need to reuse a container.
                override fun bind(container: DayViewContainer, day: CalendarDay) {
                    container.day = day
                    val textView = container.binding.exFiveDayText
                    val layout = container.binding.exFiveDayLayout
                    textView.text = day.date.dayOfMonth.toString()

                    val flightTopView = container.binding.exFiveDayFlightTop
                    val flightBottomView = container.binding.exFiveDayFlightBottom
                    flightTopView.background = null
                    flightBottomView.background = null

                    if (day.owner == DayOwner.THIS_MONTH) {
                        textView.setTextColorRes(R.color.black)
//                        layout.setBackgroundResource(if (selectedDate == day.date) R.drawable.example_5_selected_bg else 0)

                        val flights = flights[day.date]
                        if (flights != null) {
                            if (flights.count() == 1) {
                                flightBottomView.setBackgroundColor(
                                    view.context.getColorCompat(
                                        flights[0].color
                                    )
                                )
                            } else {
                                flightTopView.setBackgroundColor(view.context.getColorCompat(flights[0].color))
                                flightBottomView.setBackgroundColor(
                                    view.context.getColorCompat(
                                        flights[1].color
                                    )
                                )
                            }
                        }
                    } else {
                        textView.setTextColorRes(R.color.example_5_text_grey)
                        layout.background = null
                    }
                }
            }
            val currentMonth = YearMonth.now()
            val firstMonth = currentMonth.minusMonths(10)
            val lastMonth = currentMonth.plusMonths(10)
            val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
            calendarView.setup(currentMonth, currentMonth, firstDayOfWeek)

            calendarMonth.text = "${currentMonth.year}년 ${currentMonth.monthValue}월"

            class MonthViewContainer(view: View) : ViewContainer(view) {
                val legendLayout = CalendarHeaderLayoutBinding.bind(view).legendLayout.root
            }
            calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                    // Setup each header day text if we have not done that already.
                    if (container.legendLayout.tag == null) {
                        container.legendLayout.tag = month.yearMonth
                        container.legendLayout.children.map { it as TextView }
                            .forEachIndexed { index, tv ->
                                tv.text = daysOfWeek[index].getDisplayName(
                                    TextStyle.SHORT,
                                    Locale.ENGLISH
                                )
                                    .toUpperCase(Locale.ENGLISH)
                                tv.setTextColorRes(R.color.black)
                                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                            }
                        month.yearMonth
                    }
                }
            }
        }
    }
}

class DayViewContainer(view: View) : ViewContainer(view) {
    lateinit var day: CalendarDay // Will be set when this container is bound.
    val binding = CalendarDayLayoutBinding.bind(view)

    // With ViewBinding
    // val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
}