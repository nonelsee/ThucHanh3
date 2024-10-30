package com.example.thuchanh3

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.DocumentsContract.Document.COLUMN_SUMMARY

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "courses.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_COURSE = "courses"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_START_DATE = "start_date"
        private const val COLUMN_DEPARTMENT = "department"
        private const val COLUMN_IS_ACTIVE = "is_active"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_COURSE ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NAME TEXT, "
                + "$COLUMN_START_DATE TEXT, "
                + "$COLUMN_DEPARTMENT TEXT, "
                + "$COLUMN_IS_ACTIVE INTEGER)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COURSE")
        onCreate(db)
    }

    // Thêm khóa học mới
    fun addCourse(course: Course): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, course.name)
            put(COLUMN_START_DATE, course.startDate)
            put(COLUMN_DEPARTMENT, course.department)
            put(COLUMN_IS_ACTIVE, if (course.isActive) 1 else 0)
        }
        val id = db.insert(TABLE_COURSE, null, values)
        db.close()
        return id
    }

    // Lấy tất cả các khóa học
    fun getAllCourses(): List<Course> {
        val courseList = mutableListOf<Course>()
        val selectQuery = "SELECT * FROM $TABLE_COURSE"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val course = Course(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    startDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE)),
                    department = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEPARTMENT)),
                    isActive = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ACTIVE)) == 1
                )
                courseList.add(course)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return courseList
    }

    // Đếm số lượng khóa học theo tháng và trạng thái
    fun getCourseCountByMonth(): Map<Int, Int> {
        val countByMonth = mutableMapOf<Int, Int>()
        val selectQuery = "SELECT strftime('%m', $COLUMN_START_DATE) AS month, COUNT(*) as total FROM $TABLE_COURSE GROUP BY month ORDER BY month"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val month = cursor.getInt(cursor.getColumnIndexOrThrow("month"))
                val count = cursor.getInt(cursor.getColumnIndexOrThrow("total"))
                countByMonth[month] = count
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return countByMonth
    }
    fun getCoursesByStatus(isActive: Boolean): List<Course> {
        val courseList = mutableListOf<Course>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_COURSE WHERE $COLUMN_IS_ACTIVE=?", arrayOf(if (isActive) "1" else "0"))

        if (cursor.moveToFirst()) {
            do {
                val course = Course(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    startDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE)),
                    department = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEPARTMENT)),
                    isActive = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ACTIVE)) == 1
                )
                courseList.add(course)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return courseList
    }

    fun getCourseCountByStatus(isActive: Boolean): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_COURSE WHERE $COLUMN_IS_ACTIVE=?", arrayOf(if (isActive) "1" else "0"))
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return count
    }
    fun getCourseById(courseId: Int): Course? {
        val db = this.readableDatabase
        val cursor = db.query(
            "courses",
            arrayOf("id", "name", "start_date", "department", "is_active"),  // Không truy vấn cột summary
            "id = ?",
            arrayOf(courseId.toString()),
            null,
            null,
            null
        )

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val startDate = cursor.getString(cursor.getColumnIndexOrThrow("start_date"))
                val department = cursor.getString(cursor.getColumnIndexOrThrow("department"))
                val isActive = cursor.getInt(cursor.getColumnIndexOrThrow("is_active")) == 1
                cursor.close()
                return Course(id, name, startDate, department, isActive)
            }
        }
        cursor?.close()
        return null
    }


}


