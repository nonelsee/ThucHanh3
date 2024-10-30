package com.example.thuchanh3

import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(navListener)

        // Hiển thị fragment mặc định
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CourseListFragment())
                .commit()
        }
        val fabAddCourse: FloatingActionButton = findViewById(R.id.fab_add_course)
        fabAddCourse.setOnClickListener {
            showAddCourseDialog()
        }
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val selectedFragment: Fragment = when (item.itemId) {
            R.id.nav_courses -> CourseListFragment()
            R.id.nav_info -> CourseInfoFragment()
            R.id.nav_search -> CourseSearchFragment()
            else -> CourseListFragment()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, selectedFragment)
            .commit()
        true
    }
    private fun showAddCourseDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialod_add_course, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Thêm khóa học mới")
            .setPositiveButton("Thêm") { dialog, _ ->
                val courseName = dialogView.findViewById<EditText>(R.id.et_course_name).text.toString()
                val startDate = dialogView.findViewById<EditText>(R.id.et_start_date).text.toString()
                val department = dialogView.findViewById<EditText>(R.id.et_department).text.toString()
                val isActive = dialogView.findViewById<CheckBox>(R.id.cb_is_active).isChecked

                if (courseName.isNotEmpty() && startDate.isNotEmpty() && department.isNotEmpty()) {
                    val newCourse = Course(name = courseName, startDate = startDate, department = department, isActive = isActive)
                    val databaseHelper = DatabaseHelper(this)
                    databaseHelper.addCourse(newCourse)
                    Toast.makeText(this, "Khóa học đã được thêm", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)

        dialogBuilder.create().show()
    }
}