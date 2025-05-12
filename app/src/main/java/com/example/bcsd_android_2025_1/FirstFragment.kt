package com.example.bcsd_android_2025_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bcsd_android_2025_1.databinding.FragmentFirstBinding

class FirstFragment : Fragment(R.layout.fragment_first) {

    private var count = 0
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.setFragmentResultListener("random_result_key", this) { _, bundle ->
            val randomValue = bundle.getInt("random_value", 0)
            binding.Count.text = randomValue.toString()
            count = randomValue
        }

        binding.CountButton.setOnClickListener {
            count++
            binding.Count.text = count.toString()
        }

        binding.ToastButton.setOnClickListener {
            showAlertDialog()
        }

        binding.RandomButton.setOnClickListener {
            val secondFragment = SecondFragment.newInstance(count)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, secondFragment, "SecondFragment")
                .addToBackStack(null)
                .commit()
        }
    }

    private fun showAlertDialog() {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("알림")
            .setMessage("원하는 동작을 선택하세요.")
            .setPositiveButton("positive") { dialog, _ ->
                count = 0
                binding.Count.text = count.toString()
                dialog.dismiss()
            }
            .setNeutralButton("neutral") { dialog, _ ->
                Toast.makeText(requireContext(), getString(R.string.toast_message), Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("negative") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
