package com.example.user.myapplication

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var status = ""
    var blankNumber = ""
    var carrierName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun checkClick(view: View){
        // Логика скрытия клавиатуры
        val view: View =if (currentFocus == null) View (this) else currentFocus
        val inputMethodManager=getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken,0)

        if(editText.text.isEmpty()){
            val textString ="Edit text is empty"
            val toastMe= Toast.makeText(this, textString, Toast.LENGTH_SHORT)
            toastMe.show()
        } else{
            requestDatabase()


        }

    }

    fun requestDatabase(){
        var taxnum = editText.text.toString().toUpperCase()
        val referance= FirebaseDatabase.getInstance().reference
        val myQuery = referance.orderByChild("VehicleNumber").equalTo(taxnum)

        val postListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                if (p0?.children?.count() != 0) {
                    val textString = p0?.children?.first()?.children?.elementAt(11)?.value as String

                    this@MainActivity.status = p0?.children?.first()?.children?.elementAt(11)?.value.toString()
                    this@MainActivity.blankNumber = p0?.children?.first()?.children?.elementAt(0)?.value as String
                    this@MainActivity.carrierName = p0?.children?.first()?.children?.elementAt(3)?.value as String

                    val toastMe = Toast.makeText(this@MainActivity, status, Toast.LENGTH_SHORT)
                    toastMe.show()

                    button2.text = status

                    when (status) {
                        "Acting" -> button2.setBackgroundResource(R.color.colorPrimaryDark)
                        "Canceled" -> button2.setBackgroundResource(R.color.colorAccent)
                     else -> button2.setBackgroundResource(R.color.colorAccent)
                    }
            } else {
                    val toastMe = Toast.makeText(this@MainActivity, "is Empty", Toast.LENGTH_SHORT)
                    toastMe.show()
                }

            }


            override fun onCancelled(p0: DatabaseError?) {

            }

        }

        myQuery.addValueEventListener(postListener)
    }
}
