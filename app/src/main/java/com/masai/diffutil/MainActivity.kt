package com.masai.diffutil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var studentList = ArrayList<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buildData()
        setAdapter()
    }

    private fun buildData() {
        for (i in 1..100) {
            val student = Student("Lloyd-$i", i, 26, "Jayanagar 4th Block-$i Bangalore")
            studentList.add(student)
        }
    }

    private fun setAdapter() {
        val linearLayoutManager = LinearLayoutManager(this)
        val studentAdapter = StudentAdapter()
        recyclerView.apply {
            adapter = studentAdapter
            layoutManager = linearLayoutManager
            studentAdapter.updateData(studentList)
        }

    }
}