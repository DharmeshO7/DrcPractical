package com.example.drcpractical

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.drcpractical.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.lifecycleOwner = this

        setBinders()

        listeners()
    }

    private fun listeners() {
        mBinding.btnWithdraw.setOnClickListener {
            val withdrawAmount = mBinding.etWithdraw.text.toString()
            val amount = if (withdrawAmount.isEmpty()) 0 else withdrawAmount.toInt()
            mainViewModel.withdrawMoney(amount)
            mBinding.etWithdraw.text?.clear()
        }
    }

    private fun setBinders() {
        mainViewModel.totalBalance.observe(this) {
            mBinding.balance = it
        }

        mainViewModel.totalNotes.observe(this) {
            mBinding.notes = it
        }

        mainViewModel.lastTxn.observe(this) {
            mBinding.txn = it
        }
    }
}