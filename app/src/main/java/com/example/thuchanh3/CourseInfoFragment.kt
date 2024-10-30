package com.example.thuchanh3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment

class CourseInfoFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private var courseId: Int = -1 // ID của khóa học sẽ được truyền khi mở fragment này

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Nhận dữ liệu từ Bundle (ID của khóa học được truyền vào từ Activity hoặc Fragment khác)
        arguments?.let {
            courseId = it.getInt("COURSE_ID", -1)
        }

        databaseHelper = DatabaseHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_course_info, container, false)

        // Lấy thông tin khóa học từ database và hiển thị
        if (courseId != -1) {
            val course = databaseHelper.getCourseById(courseId)
            course?.let {
                view.findViewById<TextView>(R.id.tv_course_info_name).text = it.name
                view.findViewById<TextView>(R.id.tv_course_info_start_date).text = it.startDate
                view.findViewById<TextView>(R.id.tv_course_info_department).text = it.department
                view.findViewById<CheckBox>(R.id.cb_course_info_active).isChecked = it.isActive
            }
        }

        return view
    }

    companion object {
        // Hàm static để tạo một instance của fragment với tham số
        fun newInstance(courseId: Int): CourseInfoFragment {
            val fragment = CourseInfoFragment()
            val args = Bundle()
            args.putInt("COURSE_ID", courseId)
            fragment.arguments = args
            return fragment
        }
    }
}
