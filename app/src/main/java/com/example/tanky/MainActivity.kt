package com.example.tanky

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.koushikdutta.async.http.AsyncHttpClient
import com.koushikdutta.async.http.WebSocket

class MainActivity : AppCompatActivity() {
    private var socket: WebSocket? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val forward = findViewById<Button>(R.id.MoveForward)
        forward.setOnClickListener{
            socket?.send("""
                        {
                        "cmd":"request/move/forward",
                        "speed":100
                        }
                    """.trimIndent())
        }

        AsyncHttpClient.getDefaultInstance()
            .websocket("ws://tank-game-server.herokuapp.com", "ws") {ex, webSocket ->
                socket = webSocket
                webSocket.stringCallback = WebSocket.StringCallback { s: String? ->
                    Log.i("ws", s)
                }
            }
    }
}
