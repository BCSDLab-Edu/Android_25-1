package com.example.bcsd_android_2025_1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf

class MainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonRandom: Button = view.findViewById(R.id.button_random)
        val buttonCount: Button = view.findViewById(R.id.button_count)
        val buttonToast: Button = view.findViewById(R.id.button_toast)

        val textviewNumber: TextView = view.findViewById(R.id.textview_number)
        val toastMessage = getString(R.string.text_toast_message)

        var count = 0

        childFragmentManager.setFragmentResultListener("requestRandomNumber", viewLifecycleOwner){key, bundle ->
            count = bundle.getInt("keyRandomNumber", 0)
            textviewNumber.text = count.toString()
        }

        buttonToast.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                .setTitle(getString(R.string.text_dialog_title))
                .setMessage(getString(R.string.text_dialog_message))
                .setPositiveButton(getString(R.string.text_positive)) { dialog, _ ->
                    count = 0
                    textviewNumber.text = count.toString()
                }
                .setNeutralButton(getString(R.string.text_neutral)) { dialog, _ ->
                    Toast.makeText(requireActivity(), toastMessage, Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(getString(R.string.text_negative)) { dialog, _ ->
                }
                .show()
        }

        buttonCount.setOnClickListener {
            count++
            textviewNumber.text = count.toString()
        }

        buttonRandom.setOnClickListener {
            val randomFragment = RandomFragment().apply{
                arguments = bundleOf("countNumber" to count)
            }
            childFragmentManager.beginTransaction()
                .replace(R.id.layout_random_fragment, randomFragment)
                .commit()
        }
    }
}