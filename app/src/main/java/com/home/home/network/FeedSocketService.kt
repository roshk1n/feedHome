package com.home.home.network

import androidx.lifecycle.LiveData
import com.home.home.domain.liveData.Event
import com.home.home.domain.models.Feed

interface FeedSocketService  {

    fun connect()

    fun disconnect()

    fun getFeeds(): LiveData<Feed>
}