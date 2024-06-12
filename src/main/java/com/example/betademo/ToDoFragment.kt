package com.example.betademo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.betademo.room_database.ToDoDatabase
import com.example.betademo.room_database.ToDoRepository.ToDoRepository
import com.example.betademo.room_database.viewmodel.ToDoViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ToDoFragment : Fragment() {
    private lateinit var listRecyclerView: RecyclerView
    private lateinit var myAdapter: RecyclerView.Adapter<MyTaskListAdapter.MyViewHolder>
    var myTaskTitles: ArrayList<String> = ArrayList()
    var myTaskTimes: ArrayList<String> = ArrayList()
    var myTaskPlaces: ArrayList<String> = ArrayList()
    var myTaskIds : ArrayList<String> = ArrayList()
    var info: Bundle = Bundle()
    private lateinit var toDoViewModel : ToDoViewModel
    private lateinit var toDoRepository: ToDoRepository

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmento = inflater.inflate(R.layout.fragment_to_do, container,false)

        /*val btnDetail1:Button=fragmento.findViewById(R.id.btn_detail_1)
        val btnDetail2:Button=fragmento.findViewById(R.id.btn_detail_2)
        val btnDetail3:Button=fragmento.findViewById(R.id.btn_detail_3)
        btnDetail1.setOnClickListener(View.OnClickListener
        {
            val datos= Bundle()
            datos.putString("tarea","Ir al supermercado")
            datos.putString("hora","8:37 am")
            datos.putString("Lugar","supermistic")
            activity?.getSupportFragmentManager()?.beginTransaction()
                ?.setReorderingAllowed(true)
                ?.replace(R.id.fcv,DetailFragment::class.java,datos,"detail1")
                ?.addToBackStack("")
                ?.commit()
        })*/

        return fragmento
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*var myTaskTitles: ArrayList<String> = ArrayList()
        var myTaskTimes: ArrayList<String> = ArrayList()
        var myTaskPlaces: ArrayList<String> = ArrayList()
        myTaskTitles.add("Ir al supermercado")
        myTaskTitles.add("Ir a la trabajacion")
        myTaskTitles.add("Tarea 3")
        myTaskTitles.add("Tarea 4")
        myTaskTitles.add("Tarea 5")
        myTaskTitles.add("Tarea 6")
        myTaskTitles.add("Tarea 7")

        myTaskTimes.add("2:30 pm")
        myTaskTimes.add("10:00 pm")
        myTaskTimes.add("10:00 pm")
        myTaskTimes.add("10:00 pm")
        myTaskTimes.add("10:50 pm")
        myTaskTimes.add("10:00 pm")
        myTaskTimes.add("10:20 pm")

        myTaskPlaces.add("super econo")
        myTaskPlaces.add("torre colpatria")
        myTaskPlaces.add("torre 3")
        myTaskPlaces.add("torre 4")
        myTaskPlaces.add("torre 5")
        myTaskPlaces.add("torre 6")
        myTaskPlaces.add("torre 7")

        var info : Bundle = Bundle()
        info.putStringArrayList("title", myTaskTitles)
        info.putStringArrayList("time", myTaskTimes)
        info.putStringArrayList("places", myTaskPlaces)
        listRecyclerView = requireView().findViewById(R.id.recyclerToDoList)
        myAdapter= MyTaskListAdapter(activity as AppCompatActivity,info)
        listRecyclerView.setHasFixedSize(true)
        listRecyclerView.adapter = myAdapter
        listRecyclerView.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))*/

        val fab : View = requireActivity().findViewById(R.id.fabHome)
        fab.setOnClickListener { view->
            val intent = Intent(activity, NewTaskActivity::class.java)
            var recursiveScope = 0
            startActivityForResult(intent, recursiveScope)
        }
        var info : Bundle = Bundle()
        info.putStringArrayList("title", myTaskTitles)
        info.putStringArrayList("time", myTaskTimes)
        info.putStringArrayList("places", myTaskPlaces)
        info.putStringArrayList("Ids", myTaskIds)
        listRecyclerView = requireView().findViewById(R.id.recyclerToDoList)
        myAdapter= MyTaskListAdapter(activity as AppCompatActivity,info)
        listRecyclerView.setHasFixedSize(true)
        listRecyclerView.adapter = myAdapter
        listRecyclerView.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        updateList()
    }

    fun updateList(){
        val db = ToDoDatabase.getDatabase(requireActivity())
        val toDoDAD = db.todoDao()
        /*runBlocking {
            launch {
                var result = toDoDAD.getAllTasks()
                var i=1
                myTaskTitles.clear()
                myTaskTimes.clear()
                myTaskPlaces.clear()
                myTaskIds.clear()

                while (i< result.size){
                    myTaskTitles.add(result[i].title.toString())
                    myTaskTimes.add(result[i].time.toString())
                    myTaskPlaces.add(result[i].place.toString())
                    myTaskIds.add(result[i].id.toString())
                    i++
                }
                myAdapter.notifyDataSetChanged()
            }
        }*/
        toDoRepository = ToDoRepository(toDoDAD)
        toDoViewModel = ToDoViewModel(toDoRepository)
        var result = toDoViewModel.getAllTasks()
        result.invokeOnCompletion {

            var theTask = toDoViewModel.getTheTasks()

            if(theTask!!.size!=0){
                var i=1
                myTaskTitles.clear()
                myTaskTimes.clear()
                myTaskPlaces.clear()
                myTaskIds.clear()

                while (i< theTask.size){
                    myTaskTitles.add(theTask[i].title.toString())
                    myTaskTimes.add(theTask[i].time.toString())
                    myTaskPlaces.add(theTask[i].place.toString())
                    myTaskIds.add(theTask[i].id.toString())
                    i++
                }
                myAdapter.notifyDataSetChanged()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==0){
            if(resultCode==Activity.RESULT_OK){
                updateList()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}