package com.example.tanky

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.koushikdutta.async.http.AsyncHttpClient
import com.koushikdutta.async.http.WebSocket

class MainActivity : AppCompatActivity() {
    private var socket: WebSocket? = null
    var clicks = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val forward = findViewById<Button>(R.id.MoveForward)
        forward.setOnClickListener{
            clicks = clicks + 1
            var speed = 30 * clicks
            if(speed >= 120){
                speed = 150
            }
            socket?.send("""
                        {
                        "cmd":"request/move/forward",
                        "speed": ${speed}
                        }
                        """.trimIndent())
        }

        val right = findViewById<Button>(R.id.MoveRight)
        right.setOnClickListener{
            socket?.send("""
                        {
                        "cmd":"request/move/turn-right",
                        "speed":50
                        }
                    """.trimIndent())
        }

        val left = findViewById<Button>(R.id.MoveLeft)
        left.setOnClickListener{
            socket?.send("""
                        {
                        "cmd":"request/move/turn-left",
                        "speed":50
                        }
                    """.trimIndent())
        }

        val back = findViewById<Button>(R.id.MoveBackwards)
        back.setOnClickListener{
            socket?.send("""
                        {
                        "cmd":"request/move/backward",
                        "speed":50
                        }
                    """.trimIndent())
        }

        val stop = findViewById<Button>(R.id.MoveStop)
        stop.setOnClickListener{
            clicks = 0
            socket?.send("""
                        {
                        "cmd":"request/move/stop",
                        "speed":0
                        }
                    """.trimIndent())
        }

        AsyncHttpClient.getDefaultInstance()
            .websocket("ws://tank-game-server.herokuapp.com", "ws") {ex, webSocket ->
                socket = webSocket
                if(ex != null){
                    Log.i("error", ex.toString())
                }
                webSocket.stringCallback = WebSocket.StringCallback { s: String? ->
                    Log.i("ws", s)
                }
            }
    }
}
