package kr.ac.konkuk.planman

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.Size
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
    val MAX_COUNT_TODO_PER_DAY = 3

    lateinit var todoList: ArrayList<MyData2>
    private var currentCalendarLocalDate: LocalDate = LocalDate.now()
    private var categoryList: ArrayList<CategoryData> = ArrayList()
    private var selectedLocalDate: LocalDate? = null
    private val filterTodoViewModel: FilterTodoViewModel by activityViewModels()

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

        init()
        filterTodoViewModel.selectedCategory.observe(viewLifecycleOwner) {
            init()
        }
        filterTodoViewModel.searchKeyword.observe(viewLifecycleOwner) {
            init()
        }
    }

    private fun init() {
        Thread{
            categoryList = DB(requireContext()).readCategory()

            val selectedCategory = filterTodoViewModel.selectedCategory.value
            val searchKeyword = filterTodoViewModel.searchKeyword.value
            todoList = ArrayList(DB(requireContext()).readMyData().filter { todo ->
                if (searchKeyword?.isNotEmpty() == true)
                    (todo.title != null && todo.title!!.contains(searchKeyword.toString()))
                            || (todo.content != null && todo.content!!.contains(searchKeyword.toString()))
                else
                    if (selectedCategory != null)
                        todo.type != null && todo.type!! == selectedCategory
                    else true
            }.toList())
            activity?.runOnUiThread{
                initCalendar()
            }
        }.start()

    }

    private fun setCalendarMonth(month: YearMonth = YearMonth.now()) {
        showTodoAsList(getMonthTodoList(currentCalendarLocalDate), currentCalendarLocalDate, true)

        binding.apply {

            //calendar header
            val currentMonth = month
            val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
            calendarView.setup(currentMonth, currentMonth, firstDayOfWeek)

            calendarMonth.text = "${currentMonth.year}??? ${currentMonth.monthValue}???"

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
                                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
                            }
                        month.yearMonth
                    }
                }
            }

            prevMonthBtn.setOnClickListener {
                setCalendarMonth(month.minusMonths(1))
                currentCalendarLocalDate = currentCalendarLocalDate.minusMonths(1)
                showTodoAsList(
                    getMonthTodoList(currentCalendarLocalDate),
                    currentCalendarLocalDate,
                    true
                )
            }
            nextMonthBtn.setOnClickListener {
                setCalendarMonth(month.plusMonths(1))
                currentCalendarLocalDate = currentCalendarLocalDate.plusMonths(1)
                showTodoAsList(
                    getMonthTodoList(currentCalendarLocalDate),
                    currentCalendarLocalDate,
                    true
                )
            }
        }
    }

    private fun initCalendar() {
        binding.apply {
            //calendar body
            calendarView.daySize = Size(Integer.MIN_VALUE, 220)
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
                                if (selectedLocalDate == day.date) {
                                    selectedLocalDate = null
                                    showTodoAsList(dayTodoList, day.date, true)
                                    calendarView.notifyCalendarChanged()
                                    return
                                }
                                //show selected day's todoList
                                showTodoAsList(dayTodoList, day.date)
                                selectedLocalDate = day.date
                                calendarView.notifyCalendarChanged()
                            }
                            dayTodoList.map { todo ->
                                when {
                                    dayTodoItems.list.size < MAX_COUNT_TODO_PER_DAY -> {
                                        dayTodoItems.append(todo.title!!, onDaySelected) { holder ->
                                            holder.labelView.apply {
                                                setLines(1)
                                                textSize = 10f
                                                setTextColor(resources.getColor(R.color.white))
                                                (this.layoutParams as LinearLayout.LayoutParams).setMargins(
                                                    0,
                                                    0,
                                                    0,
                                                    4
                                                )
                                                if (todo.type != null) {
                                                    val category = useCateogry(todo.type!!)
                                                    if (category != null) {
                                                        setBackgroundColor(
                                                            resources.getColor(
                                                                CategoryData.Color.useColor(category.textColor)
                                                            )
                                                        )
                                                        setTypeface(
                                                            Typeface.DEFAULT,
                                                            CategoryData.TextStyle.useStyle(category.textStyle)
                                                        )
                                                    } else {
                                                        setBackgroundColor(resources.getColor(R.color.black))
                                                    }
                                                } else {
                                                    setBackgroundColor(resources.getColor(R.color.black))
                                                }
                                            }
                                        }
                                    }
                                    dayTodoItems.list.size == MAX_COUNT_TODO_PER_DAY -> {
                                        dayTodoItems.append(
                                            "+${dayTodoList.size - MAX_COUNT_TODO_PER_DAY}",
                                            onDaySelected
                                        ) { holder ->
                                            holder.labelView.apply {
                                                textSize = 10f
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
                        if (selectedLocalDate != null && day.date.isEqual(selectedLocalDate)) {
                            layout.background =
                                resources.getDrawable(R.drawable.calendar_select_background)
                        } else {
                            layout.background =
                                resources.getDrawable(R.drawable.calendar_day_background)
                        }
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

    private fun useCateogry(type: String): CategoryData? {
        return categoryList.find {
            it.type == type
        }
    }

    private fun getDayTodoList(time: LocalDate): ArrayList<MyData2> {
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

    private fun getMonthTodoList(time: LocalDate): ArrayList<MyData2> {
        val dayTodoList = ArrayList<MyData2>()
        todoList.map { todo ->
            val notifyDateTime = todo.notification.notifyDateTime ?: return@map
            val todoLocalDate =
                LocalDate.parse(notifyDateTime, DateTimeFormatter.ofPattern("yyyy-M-d-H-m"))
            if (todoLocalDate.year == time.year && todoLocalDate.month == time.month)
                dayTodoList.add(todo)
        }
        return dayTodoList
    }

    private fun showTodoAsList(
        todoList: ArrayList<MyData2>,
        date: LocalDate,
        allAboutMonth: Boolean = false
    ) {
        binding.selectedDayTodoCntText.setText("??? ?????? ${todoList.size}??? ????????????")

        val fragment: ListTodoFragment =
            childFragmentManager.findFragmentById(R.id.list_todo_fragment) as ListTodoFragment
        fragment.setCustomData(todoList)
        fragment.view?.post {
            if (!allAboutMonth) {
                binding.selectedDayText.text =
                    "${date.year}??? ${date.monthValue}??? ${date.dayOfMonth}?????? ??? ???"
                if (todoList.isNotEmpty())
                    binding.calendarLayoutScrollView.fullScroll(View.FOCUS_DOWN)
            } else {
                binding.selectedDayText.text =
                    "${date.year}??? ${date.monthValue}?????? ??? ???"
                binding.selectedDayText.text
                binding.selectedDayText.visibility = View.VISIBLE
//                binding.calendarLayoutScrollView.fullScroll(View.FOCUS_UP)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        init()
    }
}

class DayViewContainer(view: View) : ViewContainer(view) {
    lateinit var day: CalendarDay // Will be set when this container is bound.
    val binding = CalendarDayLayoutBinding.bind(view)
}