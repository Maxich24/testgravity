package com.example.testgravity

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


val reqUrl = "https://efs5i1ube5.execute-api.eu-central-1.amazonaws.com/"
var url = ""
lateinit var linkSave: SharedPreferences

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showFragment(LoadingFragment())

        linkSave = getSharedPreferences("save", 0)
        val home = linkSave.getString("home", "")
        if (home != null) {
            if (home.contains("http")) {
                url = home
                showFragment(WebFragment())
            }else{
                request()
            }
        }
    }

    fun request(){
        val retrofit = Retrofit.Builder()
            .baseUrl(reqUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val apiService: APIService = retrofit.create(APIService::class.java)
        apiService.getData()?.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                val resp = response.body()

                val json = resp?.let { JSONObject(it) }
                try {
                    val link = json!!.getString("link")
                    val home = json.getString("home")
                    linkSave.edit()
                        .putString("home", home)
                        .apply();

                    url = link

                    showFragment(WebFragment())

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {}
        })
    }

    fun getUrl(): String {
        return url
    }

    fun showFragment(fragment: Fragment){
        val fram = supportFragmentManager.beginTransaction()
        fram.replace(R.id.fragment_main,fragment)
        fram.commit()
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}