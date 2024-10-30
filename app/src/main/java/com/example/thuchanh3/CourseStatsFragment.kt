package com.example.thuchanh3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class CourseStatsFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_course_stats, container, false)

        databaseHelper = DatabaseHelper(requireContext())

        val totalActiveCourses = databaseHelper.getCourseCountByStatus(true)
        val totalInactiveCourses = databaseHelper.getCourseCountByStatus(false)

        view.findViewById<TextView>(R.id.tv_active_course_count).text = "Đã kích hoạt: $totalActiveCourses"
        view.findViewById<TextView>(R.id.tv_inactive_course_count).text = "Chưa kích hoạt: $totalInactiveCourses"

        return view
    }
}
