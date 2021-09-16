package com.example.covaccine

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.textclassifier.TextLanguage
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var searchButton : Button
    lateinit var pinCodeEdt : EditText
    lateinit var centerRV : RecyclerView
    lateinit var progressBar : ProgressBar
    lateinit var centerList : List<CenterRVModel>
    lateinit var centerRVAdapter: CenterRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton = findViewById(R.id.idBtnSearch)
        pinCodeEdt = findViewById(R.id.idEdtPinCode)
        centerRV = findViewById(R.id.idRvCenters)
        progressBar = findViewById(R.id.idPbLoading)
        centerList = ArrayList<CenterRVModel>()

        searchButton.setOnClickListener {
            val pinCode = pinCodeEdt.text.toString()

            if(pinCode.length != 6){
                Toast.makeText(this,"Please enter a valid pin code",Toast.LENGTH_SHORT).show()
            }else{
                (centerList as ArrayList<CenterRVModel>).clear()
                val c = Calendar.getInstance()

                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        progressBar.visibility = View.VISIBLE

                        val dateStr: String = """$dayOfMonth - ${monthOfYear + 1} - $year"""
                        getAppointments(pinCode, dateStr)
                    },
                    year,
                    month,
                    day
                )
                dpd.show()
            }
        }
    }
    private fun getAppointments(pinCode : String, date : String){

        val url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=" + pinCode + "&date=" + date
        val queue = Volley.newRequestQueue(this,)
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            Log.e("TAG", "SUCCESS RESPONSE IS $response")
            progressBar.visibility = View.GONE
            try {
                val centerArray = response.getJSONArray("centers")
                if (centerArray.length() == 0){
                    Toast.makeText(this,"No Vaccine Center Available At This Date",Toast.LENGTH_SHORT).show()

                }
                for (i in 0 until centerArray.length()){
                    val centerObj = centerArray.getJSONObject(i)
                    val centerName : String = centerObj.getString("name")
                    val centerAddress : String = centerObj.getString("address")
                    val centerFromTiming : String = centerObj.getString("from")
                    val centerToTiming : String = centerObj.getString("to")
                    val fee_type : String = centerObj.getString("fee_type")
                    val sessionObj = centerObj.getJSONArray("sessions").getJSONObject(0)
                    val availableCapacity : Int = sessionObj.getInt("available_capacity")
                    val ageLimit : Int = sessionObj.getInt("min_age_limit")
                    val vaccineName : String = sessionObj.getString("vaccine")

                    val center = CenterRVModel(
                        centerName,centerAddress,centerFromTiming,centerToTiming,fee_type,ageLimit,vaccineName,availableCapacity
                    )
                    centerList = centerList+center
                }
                centerRVAdapter = CenterRVAdapter(centerList)
                centerRV.layoutManager = LinearLayoutManager(this)
                centerRV.adapter = centerRVAdapter
                centerRVAdapter.notifyDataSetChanged()


            }catch (e:JSONException){
                progressBar.visibility = View.GONE
                e.printStackTrace()
            }

        },{ _ ->
            progressBar.visibility = View.GONE
            Toast.makeText(this,"Failed to get the data",Toast.LENGTH_SHORT).show()
        })
        queue.add(request)

    }
}