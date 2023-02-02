package com.example.drcpractical

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var totalAmount = 50000

    private val _totalBalance = MutableLiveData<String>()
    val totalBalance: LiveData<String> by this::_totalBalance

    private val _totalNotes = MutableLiveData<String>()
    val totalNotes: LiveData<String> by this::_totalNotes

    private val _lastTxn = MutableLiveData("")
    val lastTxn: LiveData<String> by this::_lastTxn

    init {
        getTotalBalance()
        getTotalNotes()
    }

    fun getTotalBalance() = viewModelScope.launch {
        _totalBalance.value = totalAmount.toString()
    }

    fun getTotalNotes() = viewModelScope.launch {
        getTxnDetails(totalAmount) {
            _totalNotes.value = it
        }
    }

    fun withdrawMoney(amount: Int) = viewModelScope.launch {
        if (amount > totalAmount || amount <= 0 || amount % 10 != 0)
            return@launch

        totalAmount -= amount

        getTotalBalance()

        getTotalNotes()

        getTxnDetails(amount) {
            _lastTxn.value = it
        }
    }

    fun getTxnDetails(amount: Int, callback: (String) -> Unit) = viewModelScope.launch {
        val txnDetails = StringBuilder()

        val denominations = listOf(2000, 500, 200, 100, 50, 20, 10)

        var temp = amount

        denominations.forEach {
            val notes = temp / it

            if (notes > 0) {
                temp %= it
                txnDetails.append("%d * %d = %d".format(it, notes, it * notes))
                txnDetails.append("\n")
            }
        }

        txnDetails.append("\n")
        txnDetails.append("Total : $amount")

        callback.invoke(txnDetails.toString())
    }

}