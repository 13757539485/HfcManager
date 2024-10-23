package com.hfc.manager

import androidx.lifecycle.ViewModel

class BlankViewModel : ViewModel() {
    private var data: String? = null

    fun getData(): String? {
        return data
    }

    fun setData(data: String?) {
        this.data = data
    }
}