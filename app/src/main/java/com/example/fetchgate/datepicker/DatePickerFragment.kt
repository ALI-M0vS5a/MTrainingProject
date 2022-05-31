package com.example.fetchgate.datepicker


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fetchgate.databinding.FragmentDatePickerBinding
import com.google.android.material.datepicker.MaterialDatePicker
import zion830.com.range_picker_dialog.TimeRangePickerDialog
import java.text.SimpleDateFormat
import java.util.*


class DatePickerFragment : Fragment() {
    private lateinit var binding: FragmentDatePickerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDatePickerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pickDate.setOnClickListener {

            dateRangePicker()
        }
        binding.pickTime.setOnClickListener {
            timeRangePicker()
        }
        binding.deepLinkNavigation.setOnClickListener {

            findNavController().navigate(
                DatePickerFragmentDirections.actionDatePickerFragmentToOverviewFragment(
                    true
                )
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun timeRangePicker() {
        TimeRangePickerDialog.Builder()
            .setTimeRange(10, 20, 16, 40)
            .setOnTimeRangeSelectedListener { timeRange ->
                val startHour = timeRange.startHour
                val endHour = timeRange.endHour
                val startMinute = timeRange.startMinute
                val endMinute = timeRange.endMinute
                binding.timeText.setText("$startHour:$startMinute ----> $endHour:$endMinute")
            }
            .build()
            .show(requireActivity().supportFragmentManager)
    }

    @SuppressLint("SetTextI18n")
    private fun dateRangePicker() {
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .build()
        dateRangePicker.addOnPositiveButtonClickListener {

            val myFormat = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            val startDate: Long = it.first
            val endDate: Long = it.second
            binding.dateText.setText(sdf.format(startDate) + "---->" + sdf.format(endDate))
        }
        dateRangePicker.show(requireActivity().supportFragmentManager, "DatePickerDialog")
    }
}