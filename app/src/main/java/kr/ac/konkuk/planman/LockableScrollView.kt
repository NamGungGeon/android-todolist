package kr.ac.konkuk.planman

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView

internal class LockableScrollView(context: Context, attributeSet: AttributeSet) : ScrollView(context, attributeSet) {
    // true if we can scroll (not locked)
    // false if we cannot scroll (locked)
    var isScrollable = true
        private set

    fun setScrollingEnabled(enabled: Boolean) {
        isScrollable = enabled
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return when (ev.action) {
            MotionEvent.ACTION_DOWN ->                 // if we can scroll pass the event to the superclass
                isScrollable && super.onTouchEvent(ev)
            else -> super.onTouchEvent(ev)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        return isScrollable && super.onInterceptTouchEvent(ev)
    }
}