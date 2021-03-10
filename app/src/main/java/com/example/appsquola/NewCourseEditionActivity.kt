package com.example.appsquola

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appsquola.model.CourseEdition
import com.example.appsquola.services.CourseService
import kotlinx.android.synthetic.main.activity_newcourseeditionactivity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class NewCourseEditionActivity : AppCompatActivity() {
    var id: Long? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newcourseeditionactivity)

        val bundle: Bundle? = intent.extras
        if (bundle?.containsKey("courseId")!!) {
            id = intent.getLongExtra("courseId", 0)

        }
        insertNewEdition.setOnClickListener {
            val code = editionCode.text.toString()
            val start = dateStringfromDatePicker(startEditionDate.year, startEditionDate.month, startEditionDate.dayOfMonth)
            val end = dateStringfromDatePicker(endEditionDate.year, endEditionDate.month, endEditionDate.dayOfMonth)
            val edition = CourseEdition(0,code,start,end,id!!,"")
            val courseService = ServiceBuilder.buildService(CourseService::class.java)
            val requestCall = courseService.addEdition(edition, id!!);

            requestCall.enqueue(object: Callback<CourseEdition> {
                override fun onResponse(call: Call<CourseEdition>, response: Response<CourseEdition>) {
                    if (response.isSuccessful) {

                        var newlyCreatedEdition = response.body() // Use it or ignore it
                        Toast.makeText(this@NewCourseEditionActivity, "Successfully Added Edition with id  ${newlyCreatedEdition!!.id}", Toast.LENGTH_LONG).show()
                        finish() // Move back to CourseDetailsActivity
                    } else {
                        Toast.makeText(this@NewCourseEditionActivity, "Failed to add item " + response.errorBody() + "   "
                            + response.code(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CourseEdition>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(this@NewCourseEditionActivity, "Error: Failed to add item", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
//    fun dateStringfromDatePicker(year: Int, month: Int, day: Int): String {
//        val stringDay = if(day <= 9) {
//                            "0$day"
//                        } else {
//                            "$day"
//                        }
//        val stringMonth = if(day <= 9) {
//                            "0$month"
//                          } else {
//                            "$month"
//                          }
//        return "$year-$stringMonth-$stringDay"
//    }

    fun dateStringfromDatePicker(year: Int, month: Int, day: Int): String {

        return LocalDate.of(year, month, day).toString()
    }
}


