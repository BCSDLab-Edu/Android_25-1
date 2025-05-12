package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import com.example.bcsd_android_2025_1.databinding.FragmentSecondBinding


class SecondFragment : Fragment(R.layout.fragment_second) {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FirstFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val count = arguments?.getInt("count_value", 0) ?: 0
        val randomValue = (0 until count + 1).random()

        binding.Count.text = randomValue.toString()
        val resultBundle = Bundle().apply {
            putInt("random_value", randomValue)
        }
        parentFragmentManager.setFragmentResult("random_result_key", resultBundle)
    }

    companion object {
        fun newInstance(countValue: Int): SecondFragment {
            return SecondFragment().apply {
                arguments = Bundle().apply { putInt("count_value", countValue) }
            }
        }
    }
}
