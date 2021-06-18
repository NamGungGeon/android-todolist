package kr.ac.konkuk.planman

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
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
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*
import kotlin.collections.ArrayList


class CalendarTodoFragment : Fragment() {

    private lateinit var binding: FragmentCalendarTodoBinding


    val daysOfWeek = daysOfWeekFromLocale()
    val MAX_COUNT_TODO_PER_DAY = 2

    lateinit var todoList: ArrayList<MyData2>

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

        todoList = DB(requireContext()).readMyData()
        initCalendar()
    }

    private fun setCalendarMonth(month: YearMonth = YearMonth.now()) {
        showTodoAsList(ArrayList<MyData2>())
        binding.apply {

            //calendar header
            val currentMonth = month
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

            prevMonthBtn.setOnClickListener {
                setCalendarMonth(month.minusMonths(1))
            }
            nextMonthBtn.setOnClickListener {
                setCalendarMonth(month.plusMonths(1))
            }
        }
    }

    private fun initCalendar() {
        binding.exFiveRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }

        binding.apply {
            //calendar body
            calendarView.dayBinder = object : DayBinder<DayViewContainer> {
                // Called only when a new container is needed.
                override fun create(view: View) = DayViewContainer(view)

                // Called every time we need to reuse a container.
                override fun bind(container: DayViewContainer, day: CalendarDay) {
                    container.day = day
                    val dayText = container.binding.dayText
                    dayText.text = day.date.dayOfMonth.toString()

                    val layout = container.binding.exFiveDayLayout

                    if (day.owner == DayOwner.THIS_MONTH)
                        container.binding.apply {

                            val dayTodoItems =
                                SimpleListRecyclerViewAdapter.SimpleListItem.Builder()
                            val dayTodoList = getDayTodoList(day.date)

                            val onDaySelected = fun() {
                                //show selected day's todoList
                                showTodoAsList(dayTodoList, day.date)
                            }
                            dayTodoList.map { todo ->
                                when {
                                    dayTodoItems.list.size < MAX_COUNT_TODO_PER_DAY -> {
                                        dayTodoItems.append(todo.title!!, onDaySelected) { holder ->
                                            holder.labelView.apply {
                                                setLines(1)
                                                textSize = 6f
                                                setTextColor(resources.getColor(R.color.white))
                                                (this.layoutParams as LinearLayout.LayoutParams).setMargins(
                                                    0,
                                                    0,
                                                    0,
                                                    4
                                                )
                                                setBackgroundColor(resources.getColor(R.color.red_800))
                                            }
                                        }
                                    }
                                    dayTodoItems.list.size == MAX_COUNT_TODO_PER_DAY -> {
                                        dayTodoItems.append(
                                            "+${dayTodoList.size - MAX_COUNT_TODO_PER_DAY}",
                                            onDaySelected
                                        ) { holder ->
                                            holder.labelView.apply {
                                                textSize = 8f
                                                textAlignment = TextView.TEXT_ALIGNMENT_TEXT_END
                                                (layoutParams as LinearLayout.LayoutParams).setMargins(
                                                    0,
                                                    0,
                                                    0,
                                                    4
                                                )
                                                setTextColor(resources.getColor(R.color.black))
                                            }
                                        }
                                    }
                                    else -> {
                                    }
                                }
                            }

                            dayTodoListRecyclerView.adapter =
                                SimpleListRecyclerViewAdapter(dayTodoItems.build())
                            layout.setOnClickListener {
                                onDaySelected()
                            }
                        }

                    if (day.date.isEqual(LocalDate.now())) {
                        layout.background =
                            resources.getDrawable(R.drawable.calendar_today_background)
                    } else {
                        layout.background =
                            resources.getDrawable(R.drawable.calendar_day_background)
                    }
                    if (day.owner == DayOwner.THIS_MONTH) {
                        dayText.setTextColorRes(R.color.black)
                    } else {
                        dayText.setTextColorRes(R.color.example_5_text_grey)
                    }
                }
            }

            setCalendarMonth()
        }
    }

    private fun getDayTodoList(time: LocalDate): ArrayList<MyData2> {
        //with dummy data
        val dayTodoList = ArrayList<MyData2>()
        todoList.map { todo ->
            val notifyDateTime = todo.notification.notifyDateTime ?: return@map
            val todoLocalDate =
                LocalDate.parse(notifyDateTime, DateTimeFormatter.ofPattern("yyyy-M-d-H-m"))
            if (todoLocalDate.year == time.year && todoLocalDate.month == time.month && todoLocalDate.dayOfMonth == time.dayOfMonth)
                dayTodoList.add(todo)
        }
        return dayTodoList
    }

    private fun showTodoAsList(dayTodoList: ArrayList<MyData2>, date: LocalDate? = null) {
        val fragment: ListTodoFragment =
            childFragmentManager.findFragmentById(R.id.list_todo_fragment) as ListTodoFragment
        fragment.setCustomData(dayTodoList)
        fragment.view?.post {
            if (date != null) {
                binding.selectedDayText.text =
                    "${date.year}년 ${date.monthValue}월 ${date.dayOfMonth}일의 할 일"
                binding.selectedDayText.visibility = View.VISIBLE
                binding.calendarLayoutScrollView.fullScroll(View.FOCUS_DOWN)
            } else {
                binding.selectedDayText.visibility = View.GONE
                binding.calendarLayoutScrollView.fullScroll(View.FOCUS_UP)
            }

        }
    }
}

class DayViewContainer(view: View) : ViewContainer(view) {
    lateinit var day: CalendarDay // Will be set when this container is bound.
    val binding = CalendarDayLayoutBinding.bind(view)
}