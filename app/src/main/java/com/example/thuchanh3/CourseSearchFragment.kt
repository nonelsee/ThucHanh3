package com.example.thuchanh3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CourseSearchFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var courseListAdapter: CourseListAdapter
    private lateinit var radioGroup: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_course_search, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_search_results)
        recyclerView.layoutManager = LinearLayoutManager(context)

        databaseHelper = DatabaseHelper(requireContext())

        // RadioGroup để chọn trạng thái khóa học
        radioGroup = view.findViewById(R.id.radio_group_status)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_active -> showCoursesByStatus(true)
                R.id.radio_inactive -> showCoursesByStatus(false)
            }
        }

        return view
    }

    private fun showCoursesByStatus(isActive: Boolean) {
        val filteredCourses = databaseHelper.getCoursesByStatus(isActive)
        courseListAdapter = CourseListAdapter(filteredCourses)
        view?.findViewById<RecyclerView>(R.id.recycler_view_search_results)?.adapter = courseListAdapter
    }
}
