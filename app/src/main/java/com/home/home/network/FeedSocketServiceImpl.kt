package com.home.home.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.home.home.domain.models.Feed
import com.home.home.domain.models.SocketState
import okhttp3.*
import java.util.concurrent.TimeUnit

class FeedSocketServiceImpl : WebSocketListener(),
    FeedSocketService {

    companion object {
        const val SOCKET_URL = "ws://superdo-groceries.herokuapp.com/receive"
    }

    private var socket: WebSocket? = null

    private val client: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .build()

    private val request: Request = Request.Builder()
        .url(SOCKET_URL)
        .build()

    private var socketState: SocketState = SocketState.DISCONNECTED

    private var feedLiveData = MutableLiveData<Feed>()

    override fun connect() {
        if (socketState == SocketState.DISCONNECTED) {
            socketState = SocketState.CONNECTING
            socket = client.newWebSocket(request, this)
        }
    }

    override fun disconnect() {
        client.dispatcher.executorService.shutdown()
        client.connectionPool.evictAll()
    }

    override fun getFeeds(): LiveData<Feed> {
        return feedLiveData
    }

//    start Socket methods

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        socketState = SocketState.DISCONNECTED
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        socketState = SocketState.DISCONNECTED
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)

        val feed: Feed? = try {
            Gson().fromJson(text, Feed::class.java)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }

        feed?.let {
            feedLiveData.postValue(it)
        }
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        socketState = SocketState.CONNECTED
    }

//    ends Socket methods
}