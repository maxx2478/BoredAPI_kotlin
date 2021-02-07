package com.manohar.forecast

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {


    var button:Button?=null
    var activitytext:TextView?=null
    var peopletext:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       var url:String = "https://www.boredapi.com/api/activity"
        button = findViewById(R.id.getdata)
        activitytext = findViewById(R.id.activity)
        peopletext = findViewById(R.id.people)

        button!!.setOnClickListener(View.OnClickListener { getdata(url) })

    }

    fun getdata(url:String)
    {
       myTask().execute(url)
    }


    inner class myTask: AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                val url = URL(params[0])
                val urlConnect = url.openConnection() as HttpURLConnection
                urlConnect.connectTimeout = 7000
                var inputstream = ConvertStreamintoString(urlConnect.inputStream)
                publishProgress(inputstream)
            }
            catch (e:Exception){}

            return "inputstream"
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
            var json= JSONObject(values[0])
            var activity=json.getString("activity")
            activitytext!!.setText("Why not try:\n"+ activity)
            var peoplenum=json.getString("participants")
            if (peoplenum.equals("1"))
                peopletext!!.setText("Participants Needed:\n"+"Only You dude")
            else
                peopletext!!.setText("Participants Needed:\n"+peoplenum)



        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)



        }

    }

    private fun ConvertStreamintoString(inputStream: InputStream?): String {
        val bufferReader= BufferedReader(InputStreamReader(inputStream))
        var line:String
        var AllString:String=""

        try {
            do{
                line=bufferReader.readLine()
                if(line!=null){
                    AllString+=line
                }
            } while (line!=null)
            inputStream!!.close()
        }catch (ex:Exception){}

        return AllString
    }



}