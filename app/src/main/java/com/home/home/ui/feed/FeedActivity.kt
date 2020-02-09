package com.home.home.ui.feed

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.home.home.R
import com.home.home.ui.helpers.SpaceItemDecoration
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {

    private var itemsAdapter: ItemsAdapter = ItemsAdapter()

    private lateinit var viewModel: FeedActivityViewModel

    private val weightFilterTextWatcher = object : TextWatcher {
        override fun afterTextChanged(value: Editable?) {
            viewModel.setFilter(FieldType.WEIGHT, FilterRule.LESS_THAN, value.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        setEventsRecycler()

        viewModel = ViewModelProvider(this).get(FeedActivityViewModel::class.java)

        viewModel.connect()

        viewModel.getFeedsLiveData().observe(this, Observer {
            it.get()?.let { item ->
                itemsAdapter.addItem(item)
            }
        })

        startBtn.setOnClickListener {
            viewModel.startListening()
        }

        stopBtn.setOnClickListener {
            viewModel.stopListening()
        }

        weightFilterEt.addTextChangedListener(weightFilterTextWatcher)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.disconnect()
        weightFilterEt.removeTextChangedListener(weightFilterTextWatcher)
    }

    private fun setEventsRecycler() {
        val spacing = resources.getDimension(R.dimen.default_margin).toInt()
        feedsRv.addItemDecoration(SpaceItemDecoration(spacing))
        feedsRv.adapter = itemsAdapter
    }
}
