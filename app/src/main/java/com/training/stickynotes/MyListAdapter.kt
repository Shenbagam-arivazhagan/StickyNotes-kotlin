package com.training.stickynotes


import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MyListAdapter(private val context: Activity, private var title: MutableList<String>, private var content: MutableList<String>,private var serialNo: MutableList<String>)
    : ArrayAdapter<String>(context, R.layout.stickynote_list, serialNo) {



    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.stickynote_list, null, true)

        val titleText = rowView.findViewById(R.id.tv_title) as TextView
        val contentText = rowView.findViewById(R.id.tv_content) as TextView
        val btnDelete = rowView.findViewById(R.id.deleteImageButton) as Button

        btnDelete.setOnClickListener() {
            val databaseHandler: DBHelper= DBHelper(getContext())
            val status = databaseHandler.deleteNote(Integer.parseInt(serialNo[position]))
            if(status > -1){
                title.removeAt(position)
                content.removeAt(position)
                serialNo.removeAt(position)

                notifyDataSetChanged()
            }else{
                println("record not deleted ")
            }
        }

            titleText.text = title[position]
            contentText.text = content[position]
            return rowView
        }


}