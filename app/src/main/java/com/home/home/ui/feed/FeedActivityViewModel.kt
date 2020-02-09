package com.home.home.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.home.home.network.FeedSocketService
import com.home.home.network.FeedSocketServiceImpl
import com.home.home.domain.liveData.Event
import com.home.home.domain.models.Feed

class FeedActivityViewModel : ViewModel() {

    private val feedSocketService: FeedSocketService =
        FeedSocketServiceImpl()

    private val feedsLiveData: MediatorLiveData<Event<Feed>> = MediatorLiveData()

    private var isListening = false

    init {
        feedsLiveData.addSource(feedSocketService.getFeeds()) {
            if (isListening && validate(it.weight)) {
                feedsLiveData.postValue(Event(it))
            }
        }
    }

    // workaround see Filter object unfinished
    var filterValue: String? = null

    fun getFeedsLiveData(): LiveData<Event<Feed>> = feedsLiveData

    fun connect() {
        feedSocketService.connect()
    }

    fun disconnect() {
        feedSocketService.disconnect()
    }

    fun setFilter(fieldType: FieldType, filter: FilterRule, value: String) {
        filterValue = value
    }

    fun startListening() {
        isListening = true
    }

    fun stopListening() {
        isListening = false
    }

    // bad idea))) leak of time)
    private fun validate(weight: String): Boolean {
        var isValid = false
        if (filterValue.isNullOrEmpty()) {
            isValid = true
        } else {
            filterValue?.let {
                isValid = try {
                    val filterNumber = it.toDouble()
                    val weightValue = weight.split("kg")[0].toDouble()
                    filterNumber < weightValue

                } catch (e: NumberFormatException) {
                    true
                }
            }
        }
        return isValid
    }
}