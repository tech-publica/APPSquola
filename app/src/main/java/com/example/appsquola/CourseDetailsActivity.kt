package com.example.appsquola

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appsquola.model.Course
import com.example.appsquola.services.CourseService
import kotlinx.android.synthetic.main.activity_c_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CourseDetailsActivity : AppCompatActivity() {
    lateinit var editionsNumber: TextView
    var id:Long?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c_detail)

        newEdtionBtn.setOnClickListener{
            val intent = Intent(this, NewCourseEditionActivity::class.java)
            intent.putExtra("courseId", id)
            startActivity(intent)
        }
//        val bundle: Bundle? = intent.extras
//
//        if (bundle?.containsKey("courseId")!!) {
//             id = intent.getLongExtra("courseId", 0)
//            if(id != null) {
//                loadDetails(id!!)
//            }
//        }

        saveBtn.setOnClickListener {
            updateCourse()
        }

    }


    override fun onResume() {
        super.onResume()
        val bundle: Bundle? = intent.extras

        if (bundle?.containsKey("courseId")!!) {
            id = intent.getLongExtra("courseId", 0)
            if(id != null) {
                loadDetails(id!!)
            }
        }
    }


    fun loadDetails(id: Long) {
        val courseService = ServiceBuilder.buildService(CourseService::class.java)
        val requestCall = courseService.getCourseById(id)

        requestCall.enqueue(object: Callback<Course> { // Classe interna anonima
            // object: Callback<List<Course>> = il compilatore crea una classe, di cui non conosciamo
            // il nome, che estende l'interfaccia Callback<List<Course>>. Dopo, crea un oggetto di quella
            // classe.
            override fun onResponse(call: Call<Course>, response: Response<Course>) {
                if (response.isSuccessful) {
                    val course = response.body()!!
                    courseIdTxt.setText(course.id.toString())
                    courseTitleTxt.setText(course.title)
                    courseNHoursTxt.setText(course.numHours.toString())
                    courseDescTxt.setText(course.description)
                    courseCostTxt.setText(course.cost.toString())
                    val linearLayoutEdition = LinearLayoutManager(this@CourseDetailsActivity)
                    editionsView.layoutManager = linearLayoutEdition
                    //val adapterEdition = CourseEditionListAdapter(courses.editions.toList(),this@CourseDetailsActivity )
                    val adapterEdition = CourseEditionListAdapter(course.editions.toMutableList(),this@CourseDetailsActivity )
                    editionsView.adapter = adapterEdition
                } else {
                    Toast.makeText(this@CourseDetailsActivity, "Failed to load course", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Course>, t: Throwable) {
                Toast.makeText(this@CourseDetailsActivity, "Load Error Occurred" + t.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }


    fun updateCourse() {
        val courseId = courseIdTxt.text.toString().toLong()
        val courseTitle = courseTitleTxt.text.toString()
        val courseHours = courseNHoursTxt.text.toString().toInt()
        val courseDescription = courseDescTxt.text.toString()
        val courseCost = courseCostTxt.text.toString().toDouble()

        val newCourse = Course(courseId, courseTitle, courseHours, courseDescription, courseCost, mutableSetOf())

        val courseService = ServiceBuilder.buildService(CourseService::class.java)
        val requestCall = courseService.updateCourse(newCourse, id!!)

        requestCall.enqueue(object: Callback<Course> { // Classe interna anonima
            // object: Callback<List<Course>> = il compilatore crea una classe, di cui non conosciamo
            // il nome, che estende l'interfaccia Callback<List<Course>>. Dopo, crea un oggetto di quella
            // classe.
            override fun onResponse(call: Call<Course>, response: Response<Course>) {
                if (response.isSuccessful) {
                    val courses = response.body()!!
                    Toast.makeText(this@CourseDetailsActivity, "Upload ok", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@CourseDetailsActivity, "Failed to update course", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Course>, t: Throwable) {
                Toast.makeText(this@CourseDetailsActivity, "Update Error Occurred" + t.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }
}