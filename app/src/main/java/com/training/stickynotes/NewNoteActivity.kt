package com.training.stickynotes

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.new_note.*

class NewNoteActivity : AppCompatActivity() {
    var selectedId: Int = -1
   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_note)
       val id:Int? = intent.getIntExtra("serialNo",-1)
       if (id != null && id != -1) {
           selectedId = id
           viewSelectedStickyNotes()
       }else{
           selectedId = -1
       }
    }
    fun viewSelectedStickyNotes(){
        val helper: DBHelper= DBHelper(this)
        val stickyNote: List<StickyNoteModelClass> = helper.viewSelectedStickyNotes(selectedId)
        val arrTitle = MutableList<String>(stickyNote.size){"null"}
        val arrContent = MutableList<String>(stickyNote.size){"null"}
        val arrSerialNo = MutableList<String>(stickyNote.size){"0"}
        var index = 0
        for(e in stickyNote){
            arrSerialNo[index] = e.serialNo.toString()
            arrTitle[index] = e.title
            arrContent[index] = e.notes
            index++
        }
        editTxt_title.setText(arrTitle[0])
        editTxt_content.setText(arrContent[0])

        print("stick size is ${stickyNote.size}")
        }


    override fun onBackPressed() {
        //Disabling device back
    }

    fun saveStickyNote(view: View){

        val name = editTxt_title.text.toString()
        val content = editTxt_content.text.toString()
        val databaseHandler: DBHelper= DBHelper(this)
        if(name.trim()!="" && content.trim()!=""){

            if(selectedId == -1){
                var id = databaseHandler.getDBCount()+1
                val  status = databaseHandler.addNewNote(StickyNoteModelClass(Integer.parseInt(id.toString()),name, content))
                if(status > -1){
                    val newIntent = Intent(this,MainActivity::class.java)
                    startActivity(newIntent)
                }
            }else{
                val status = databaseHandler.updateStickyNote(StickyNoteModelClass(Integer.parseInt(selectedId.toString()),name, content))
                if(status > -1){
                    val newIntent = Intent(this,MainActivity::class.java)
                    startActivity(newIntent)
                }
            }



        }
    }

    fun discardStickyNote(view: View){
        val newIntent = Intent(this,MainActivity::class.java)
        startActivity(newIntent)
    }
}