package com.example.freecodecampsqlite

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Adapter
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var db : SQLiteDatabase
    lateinit var rs : Cursor
    lateinit var adapter: SimpleCursorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var myHelper = MyHelper(applicationContext)
         db = myHelper.readableDatabase
        rs = db.rawQuery("SELECT * FROM ACTABLE ORDER BY NAME ", null)
        if (rs.moveToFirst()){
            etName.setText(rs.getString(1))
            etMeaning.setText(rs.getString(2))
        }

        buttonPrev.setOnClickListener{
            if (rs.moveToPrevious()){
                etName.setText(rs.getString(1))
                etMeaning.setText(rs.getString(2))
            }else if (rs.moveToLast()){
                etName.setText(rs.getString(1))
                etMeaning.setText(rs.getString(2))
            }else{
                Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
            }
        }

        buttonNext.setOnClickListener{
            if (rs.moveToNext()){
                etName.setText(rs.getString(1))
                etMeaning.setText(rs.getString(2))
            }else if (rs.moveToFirst()){
                etName.setText(rs.getString(1))
                etMeaning.setText(rs.getString(2))
            }else{
                Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
            }
        }

        buttonInsert.setOnClickListener{
             var cv  = ContentValues()

            cv.put("NAME", etName.text.toString())
            cv.put("MEANING", etMeaning.text.toString())
            db.insert("ACTABLE", null , cv)

            rs.requery()
        }

        buttonClear.setOnClickListener{
            etName.text.clear()
            etMeaning.text.clear()
        }

        buttonUpdate.setOnClickListener{
            var cv = ContentValues()

            cv.put("NAME", etName.text.toString())
            cv.put("MEANING", etMeaning.text.toString())
            db.update("ACTABLE", cv, "_id = ?", arrayOf(rs.getString(0)))
            rs.requery()

            Toast.makeText(this, "record updated", Toast.LENGTH_SHORT).show()
        }

        buttonDel.setOnClickListener{

            db.delete("ACTABLE", "_id = ?", arrayOf(rs.getString(0)))
            rs.requery()
        }

        searchViewAll.isIconified = false
        searchViewAll.queryHint = "Search among ${rs.count} Records"

         adapter = SimpleCursorAdapter(applicationContext, android.R.layout.simple_expandable_list_item_2, rs, arrayOf("NAME", "MEANING"),
            intArrayOf(android.R.id.text1, android.R.id.text2), 0)

        lvViewAll.adapter = adapter
        registerForContextMenu(lvViewAll)

        BtnViewAll.setOnClickListener{
            searchViewAll.visibility = View.VISIBLE
            lvViewAll.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
        }

        searchViewAll.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                rs = db.rawQuery("SELECT * FROM ACTABLE WHERE NAME LIKE '%${p0}%' OR MEANING LIKE '%${p0}%'", null)
                adapter.changeCursor(rs)
                return false
            }
        })
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.add(101,11,1,"DELETE") // .add( order no. , id no. , text )
        menu?.setHeaderTitle("Removing Delete")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        if (item.itemId == 11){
            db.delete("ACTABLE", "_id = ?", arrayOf(rs.getString(0)))
            rs.requery()
            adapter.notifyDataSetChanged()
            searchViewAll.queryHint = "Search among ${rs.count} Records"
        }
        return super.onContextItemSelected(item)
    }
}