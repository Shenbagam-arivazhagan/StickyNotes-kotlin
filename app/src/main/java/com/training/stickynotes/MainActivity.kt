package com.training.stickynotes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Integer.parseInt

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        btnNewNote.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
             //       .setAction("Action", null).show()
            val newIntent = Intent(this,NewNoteActivity::class.java)
            startActivity(newIntent)
        }
    }


    override fun onPostResume() {
        super.onPostResume()
        viewStickyNotes();
    }


    fun viewStickyNotes(){
        val helper: DBHelper= DBHelper(this)
        val stickyNote: List<StickyNoteModelClass> = helper.viewStickyNotes()
        val arrTitle = MutableList<String>(stickyNote.size){"null"}
        val arrContent = MutableList<String>(stickyNote.size){"null"}
        val arrSerialNo = MutableList<String>(stickyNote.size){"0"}
        var index = 0

        if(stickyNote.size > 0){
            for(e in stickyNote){
                    arrSerialNo[index] = e.serialNo.toString()
                    arrTitle[index] = e.title
                    arrContent[index] = when (e.notes.length) {
                        in 0..30 ->  e.notes
                        else -> e.notes.substring(0..30)
                    } as String
                    index++
            }

            val myListAdapter = MyListAdapter(this,arrTitle,arrContent,arrSerialNo)
            list_notes.adapter = myListAdapter

            list_notes.setOnItemClickListener { parent, view, position, id ->
                val element = parent.getItemAtPosition(position); // The item that was clicked
                val newIntent = Intent(this,NewNoteActivity::class.java).putExtra("serialNo",parseInt(element.toString()))
                startActivity(newIntent)
            }
        }else{
            txt_emptyNote.setVisibility(View.VISIBLE)
        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
