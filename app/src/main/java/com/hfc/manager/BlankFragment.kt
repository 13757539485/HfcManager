package com.hfc.manager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hfc.manager.databinding.FragmentBlankBinding

class BlankFragment : Fragment() {
    private val TAG = "yuli"

    private val binding by lazy { FragmentBlankBinding.inflate(layoutInflater) }

    companion object {
        fun newInstance() = BlankFragment()
    }

    private val viewModel by lazy { ViewModelProvider(requireActivity())[BlankViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 恢复数据
        if (viewModel.getData() != null) {
            Log.e(
                TAG,
                "onViewCreated second $savedInstanceState"
            )
            binding.showContent.text = viewModel.getData()
        } else {
            Log.e(
                TAG,
                "onViewCreated first $savedInstanceState"
            )
//            viewModel.setData("123456")
            binding.showContent.text = viewModel.getData()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState() called with: outState = $outState")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(TAG, "onViewStateRestored() called with: savedInstanceState = $savedInstanceState")
    }
}