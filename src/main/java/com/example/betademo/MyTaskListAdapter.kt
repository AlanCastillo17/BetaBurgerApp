package com.example.betademo

import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class MyTaskListAdapter(context : AppCompatActivity,
                        val info: Bundle):RecyclerView.Adapter<MyTaskListAdapter.MyViewHolder>()
{
                            class MyViewHolder(val layout: View):RecyclerView.ViewHolder(layout)
    private var context : AppCompatActivity= context
    var MyTaskTitles: ArrayList<String> = info.getStringArrayList("title") as ArrayList<String>
    var MyTaskPlace: ArrayList<String> = info.getStringArrayList("places") as ArrayList<String>
    var MyTaskTimes: ArrayList<String> = info.getStringArrayList("time") as ArrayList<String>
    var MyTaskIds: ArrayList<String> = info.getStringArrayList("Ids") as ArrayList<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.tast_list_items,parent,false)
        return MyViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var textViewTask = holder.layout.findViewById<TextView>(R.id.textViewTask)
        textViewTask.text = MyTaskTitles[position]
        var textViewTime = holder.layout.findViewById<TextView>(R.id.textViewTime)
        textViewTime.text = MyTaskTimes[position]
        var textViewPlace = holder.layout.findViewById<TextView>(R.id.textViewPlace)
        textViewPlace.text = MyTaskPlace[position]

        holder.layout.setOnClickListener{
            Toast.makeText(holder.itemView.context,textViewTask.text,Toast.LENGTH_LONG).show()
            val datos= Bundle()
            datos.putString("tarea", textViewTask.text as String)
            datos.putString("hora", textViewTime.text as String)
            datos.putString("lugar", MyTaskPlace[position])
            datos.putString("id", MyTaskIds[position])
            context.getSupportFragmentManager()?.beginTransaction()
                ?.setReorderingAllowed(true)
                ?.replace(R.id.fcv,DetailFragment::class.java,datos,"detail")
                ?.addToBackStack("")
                ?.commit()
        }
    }

    override fun getItemCount(): Int {
        return MyTaskTitles.size
    }
}