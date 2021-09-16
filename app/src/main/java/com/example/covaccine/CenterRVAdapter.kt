package com.example.covaccine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CenterRVAdapter (private val centerList : List<CenterRVModel>): RecyclerView.Adapter<CenterRVAdapter.CenterRVViewHolder>(){

    class CenterRVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val centerName : TextView = itemView.findViewById(R.id.idTVCenterName)
        val centerAddress : TextView = itemView.findViewById(R.id.idTVCenterAddress)
        val centerTiming : TextView = itemView.findViewById(R.id.idTVCenterTimings)
        val vaccineName : TextView = itemView.findViewById(R.id.idTVVaccineName)
        val vaccineFees : TextView = itemView.findViewById(R.id.idTVFeeType)
        val ageLimit : TextView = itemView.findViewById(R.id.idTVAgeLimit)
        val avaliabilityTv : TextView = itemView.findViewById(R.id.idTVAvaliablity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterRVViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.center_rv_item, parent, false)
        return CenterRVViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CenterRVViewHolder, position: Int) {
        val center = centerList[position]
        holder.centerName.text = center.centerName
        holder.centerAddress.text = center.centerAddress
        holder.centerTiming.text = ("From : "+center.centerFromTime+" To : "+center.centerToTime)
        holder.vaccineName.text = center.vaccineName
        holder.vaccineFees.text = center.fee_type
        holder.ageLimit.text = ("Age Limit "+center.ageLimit.toString())
        holder.avaliabilityTv.text = ("Availability"+center.avaliableCapacity.toString())
    }

    override fun getItemCount(): Int {
        return centerList.size
    }
}