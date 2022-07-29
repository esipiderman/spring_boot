package com.example.springboot.mainScreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.springboot.databinding.ItemRecyclerMainBinding
import com.example.springboot.model.local.student.Student

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

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(it: List<Student>) {
        data.clear()
        data.addAll(it)
        notifyDataSetChanged()
    }

    interface StudentEvent{
        fun onItemClicked(student: Student, position: Int)
        fun onItemLongClicked(student: Student, position: Int)
    }
}
