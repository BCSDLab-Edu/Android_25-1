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

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val numTextView: TextView = view.findViewById(R.id.textview_num)
        val toastButton: Button = view.findViewById(R.id.button_toast)
        val countButton: Button = view.findViewById(R.id.button_count)
        val randomButton: Button = view.findViewById(R.id.button_random)

        val toastMessage = getString(R.string.toast_message)
        var count = 0

        toastButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.dialog_title))
                .setMessage(getString(R.string.dialog_message))
                .setPositiveButton(getString(R.string.text_positive)) { dialog, which ->
                    count = 0
                    numTextView.text = count.toString()
                }
                .setNeutralButton(getString(R.string.text_neutral)) { dialog, which ->
                    context?.let {
                        Toast.makeText(it, toastMessage, Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton(getString(R.string.text_negative)) { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }


        countButton.setOnClickListener {
            count++
            numTextView.text = count.toString()
        }

        parentFragmentManager.setFragmentResultListener("randomResult", this) { _, result ->
            val random = result.getInt("randomNumber")
            count = random
            numTextView.text = random.toString()
        }

        randomButton.setOnClickListener {
            val fragment = Fragment_Random()
            val bundle = Bundle().apply {
                putInt("currentCount", count)
            }
            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.layout_test_fragment, fragment)
                .addToBackStack(null)
                .commit()
        }

        }
    }
