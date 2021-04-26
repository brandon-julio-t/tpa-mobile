package edu.bluejack20_2.braven.services

import android.content.Context
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import javax.inject.Inject


class VolleyFirebaseMessagingService @Inject constructor(@ApplicationContext private val context: Context) {
    fun sendNotificationTo(title: String, body: String, token: String) {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings { minimumFetchIntervalInSeconds = 3600 }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.fetchAndActivate().addOnSuccessListener {
            val queue = Volley.newRequestQueue(context)
            val url = remoteConfig.getString("fcmBackendURL")
            Log.wtf("hehe", url)

            val request = object : StringRequest(
                Method.POST,
                url,
                { resp -> Log.wtf("hehe", "resp: $resp") },
                null
            ) {
                override fun getBodyContentType() = "application/json; charset=utf-8"
                override fun getBody() = JSONObject(
                    mapOf(
                        "title" to title,
                        "body" to body,
                        "token" to token
                    )
                ).toString().toByteArray()
            }.setRetryPolicy( // fix sending data twice https://stackoverflow.com/a/27873079/10644595
                DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
            )

            queue.add(request)
        }
    }
}