package com.example.bcsd_android_2025_1

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

class MainFragment : Fragment() {
    private var number = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        number = arguments?.getInt("number") ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val numberText: TextView = view.findViewById(R.id.text_login_number)
        val toastButton: Button = view.findViewById(R.id.button_login_toast)
        val countButton: Button = view.findViewById(R.id.button_login_count)
        val randomButton: Button = view.findViewById(R.id.button_login_random)

        numberText.text = number.toString()

        toastButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.dialog_title))
                .setMessage(getString(R.string.dialog_message))
                .setPositiveButton((R.string.dialog_positive), object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        number = 0
                        view.findViewById<TextView>(R.id.text_login_number).text = number.toString()
                        dialog.dismiss()
                    }
                })
                .setNegativeButton((R.string.dialog_negative), object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        dialog.dismiss()
                    }
                })
                .setNeutralButton((R.string.dialog_neutral), object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        Toast.makeText(requireContext(), getString(R.string.dialog_toastMassage), Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                })
                .create()
                .show()
        }

        countButton.setOnClickListener {
            number++
            numberText.text = number.toString()
        }

        randomButton.setOnClickListener {
            (activity as? MainActivity)?.showRandomFragment(number)
        }
    }
}
