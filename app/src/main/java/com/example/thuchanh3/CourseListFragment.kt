package com.example.thuchanh3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CourseListFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var courseListAdapter: CourseListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_course_list, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_courses)
        recyclerView.layoutManager = LinearLayoutManager(context)

        databaseHelper = DatabaseHelper(requireContext())

        // Lấy danh sách khóa học từ SQLite và hiển thị trong RecyclerView
        val courseList = databaseHelper.getAllCourses()
        courseListAdapter = CourseListAdapter(courseList)
        recyclerView.adapter = courseListAdapter

        return view
    }

    override fun onResume() {
        super.onResume()
        refreshCourseList()
    }

    private fun refreshCourseList() {
        // Lấy lại danh sách khóa học khi Fragment được hiển thị lại
        val updatedList = databaseHelper.getAllCourses()
        courseListAdapter.updateData(updatedList)
    }
}
