package com.apps.fatal.flowsummator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.apps.fatal.flowsummator.databinding.FragmentFirstBinding
import com.apps.fatal.flowsummator.viewmodels.MainFragmentViewModel

class MainFragment : Fragment() {

    private lateinit var viewModel: MainFragmentViewModel

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            val flowsCount = (binding.inputField.text.toString().takeIf { it.isNotEmpty() })?.toInt() ?: 0
            if (flowsCount < 0) return@setOnClickListener

            binding.textviewFirst.text = ""
            binding.inputField.setText("")

            binding.textviewFirst.append("Input value: $flowsCount\n")

            viewModel.collectAdderFlow(flowsCount) { value ->
                binding.textviewFirst.append("$value\n")
                binding.scrollView.post {
                    binding.scrollView.fullScroll(View.FOCUS_DOWN)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}