package com.example.springboot.recyclerMain

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.springboot.databinding.ItemRecyclerMainBinding

class RecyclerAdapter(val data : ArrayList<Student>, val studentEvent: StudentEvent) : RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>() {

    inner class RecyclerViewHolder(private val binding: ItemRecyclerMainBinding) : RecyclerView.ViewHolder(binding.root){

        fun bindData(position: Int){

            binding.txtFirstLetter.text = data[position].name[0].uppercaseChar().toString()
            binding.txtNameMain.text = data[position].name
            binding.txtJobMain.text = data[position].course
            binding.txtNumberMain.text = data[position].score.toString()

            itemView.setOnClickListener {
                studentEvent.onItemClicked(data[position], position)
            }

            itemView.setOnLongClickListener {
                studentEvent.onItemLongClicked(data[position], position)
                true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = ItemRecyclerMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun removeItem(student: Student, position: Int){
        data.remove(student)
        notifyItemRemoved(position)
    }

    fun addItem(student: Student){
        data.add(0, student)
        notifyItemInserted(0)
    }

    fun updateItem(student: Student, position: Int){
        data.set(position, student)
        notifyItemChanged(position)
    }

    interface StudentEvent{
        fun onItemClicked(student: Student, position: Int)
        fun onItemLongClicked(student: Student, position: Int)
    }
}
