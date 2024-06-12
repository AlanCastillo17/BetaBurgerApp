package com.example.betademo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class DetailFragment : Fragment()  {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmento = inflater.inflate(R.layout.fragment_detail,container,false)

        var tarea=requireArguments().getString("tarea")
        var hora=requireArguments().getString("hora")
        var lugar=requireArguments().getString("lugar")
        var id=requireArguments().getString("id")

        var tvTarea:TextView=fragmento.findViewById(R.id.tvTarea)
        var tvHora:TextView=fragmento.findViewById(R.id.tvHora)
        var tvLugar:TextView=fragmento.findViewById(R.id.tvLugar)
        var tvId:TextView=fragmento.findViewById(R.id.tvId)

        tvTarea.text=tarea
        tvHora.text=hora
        tvLugar.text=lugar
        tvId.text=id

        val btnEdit: Button = fragmento.findViewById(R.id.btnEditFD)
        btnEdit.setOnClickListener {
            val principal = Intent(inflater.context,NewTaskActivity::class.java)
            principal.putExtra("tarea",tvTarea.text as String)
            principal.putExtra("hora",tvHora.text as String)
            principal.putExtra("lugar",tvLugar.text as String)
            principal.putExtra("id",tvId.text as String)
            startActivity(principal)
        }
        return fragmento
    }
}