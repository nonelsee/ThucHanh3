package com.example.thuchanh3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class CourseListAdapter(private var courseList: List<Course>) :
    RecyclerView.Adapter<CourseListAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val courseName: TextView = itemView.findViewById(R.id.tv_course_name)
        val courseStartDate: TextView = itemView.findViewById(R.id.tv_course_start_date)
        val courseDepartment: TextView = itemView.findViewById(R.id.tv_course_department)
        val isActive: CheckBox = itemView.findViewById(R.id.cb_is_active)

        init {
            itemView.setOnClickListener(this) // Đăng ký sự kiện click
        }

        // Xử lý sự kiện khi người dùng nhấn vào item
        override fun onClick(v: View?) {
            val course = courseList[adapterPosition]
            // Điều hướng tới CourseInfoFragment
            val fragment = CourseInfoFragment.newInstance(course.id)
            (itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseList[position]
        holder.courseName.text = course.name
        holder.courseStartDate.text = course.startDate
        holder.courseDepartment.text = course.department
        holder.isActive.isChecked = course.isActive
    }

    override fun getItemCount(): Int = courseList.size

    // Hàm để cập nhật lại danh sách khóa học khi dữ liệu thay đổi
    fun updateData(newCourses: List<Course>) {
        courseList = newCourses
        notifyDataSetChanged()
    }
}

