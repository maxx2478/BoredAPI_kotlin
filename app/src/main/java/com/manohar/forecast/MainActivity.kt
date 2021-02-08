package com.manohar.forecast

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.ref.WeakReference
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
       myTask(this).execute(url)
    }


      private class myTask internal constructor(context: MainActivity) : AsyncTask<String, String, String>() {

          private val activityReference: WeakReference<MainActivity> = WeakReference(context)

          override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String {
            try {
                val url = URL(params[0])
                val urlConnect = url.openConnection() as HttpURLConnection
                urlConnect.connectTimeout = 7000

                var inputstream = activityReference.get()!!.ConvertStreamintoString(urlConnect.inputStream)
                publishProgress(inputstream)
            }
            catch (e:Exception){}

            return "inputstream"
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
            var json= JSONObject(values[0])
            //val query=json.getJSONObject("query") //node which divides into children
            //val results=query.getJSONObject("results") //node which divides into children
           // val channel=results.getJSONObject("channel") //node which divides into children
           // val astronomy=channel.getJSONObject("astronomy") //node which divides into children
            var activity=json.getString("activity")
            activityReference.get()!!.activitytext!!.setText("Why not try:\n"+ activity)
            var peoplenum=json.getString("participants")
            if (peoplenum.equals("1"))
                activityReference.get()!!.peopletext!!.setText("Participants Needed:\n"+"Just You")
            else
                activityReference.get()!!.peopletext!!.setText("Participants Needed:\n"+peoplenum)



        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)



        }

    }

     fun ConvertStreamintoString(inputStream: InputStream?): String {
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