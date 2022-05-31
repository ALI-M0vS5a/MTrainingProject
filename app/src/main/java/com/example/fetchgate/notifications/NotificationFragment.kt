package com.example.fetchgate.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fetchgate.adapter.NotificationsAdapter
import com.example.fetchgate.databinding.FragmentNotificationsBinding
import com.example.fetchgate.db.ItemDatabase
import com.example.fetchgate.network.Notifications
import com.example.fetchgate.utils.SwipeToDeleteCallback
import com.example.fetchgate.utils.bol
import com.example.fetchgate.utils.message1
import com.example.fetchgate.utils.notificationTitle
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList


class NotificationFragment : Fragment() {


    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var notificationsRecyclerViewAdapter: NotificationsAdapter
    private lateinit var notificationsList: List<Notifications>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentNotificationsBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationsList = arrayListOf()

        val notificationViewModel = initViewModel()
        initRecyclerView()
        val notification =
            notificationTitle?.let { message1?.let { it1 -> Notifications(0, it, it1) } }
        if (notification != null) {
            if (bol) {
                notificationViewModel.addNotification(notification)
                bol = false
            }
        }
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val removedItem = notificationsRecyclerViewAdapter.notifications[position]

                notificationViewModel.deleteNotification(removedItem)


                val snackBar = Snackbar.make(
                    view,
                    "Item Removed!",
                    Snackbar.LENGTH_LONG
                ).setAction("Undo") {
                    notificationViewModel.addNotification(removedItem)

                }
                snackBar.show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerviewNotifications)

        binding.edtSearch.doOnTextChanged { text, _, _, _ ->
            val query = text.toString().lowercase(Locale.getDefault())
            filterWithQuery(query)

        }

    }

    private fun initRecyclerView() {
        notificationsRecyclerViewAdapter = NotificationsAdapter()
        binding.recyclerviewNotifications.adapter = notificationsRecyclerViewAdapter
        binding.recyclerviewNotifications.layoutManager = LinearLayoutManager(this.context)

        binding.recyclerviewNotifications.apply {
            val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
        }

        binding.lifecycleOwner = this
    }

    private fun initViewModel(): NotificationViewModel {
        val application = requireNotNull(this.activity).application
        ItemDatabase.getInstance(application).itemDao
        val viewModelFactory = NotificationViewModelFactory(application)
        val addViewModel =
            ViewModelProvider(this, viewModelFactory)[NotificationViewModel::class.java]

        addViewModel.allNotifications.observe(
            viewLifecycleOwner
        ) { list ->
            list?.let {
                notificationsList = it
                notificationsRecyclerViewAdapter.updateList(it)
            }
        }
        return addViewModel
    }

    private fun filterWithQuery(query: String) {
        if (query.isNotEmpty()) {
            val filteredList: List<Notifications> = onFilterChanged(query)
            notificationsRecyclerViewAdapter.updateList(filteredList)
        }else if(query.isEmpty()){
            notificationsRecyclerViewAdapter.updateList(notificationsList)
        }

    }

    private fun onFilterChanged(filterQuery: String): List<Notifications> {
        val filteredList = ArrayList<Notifications>()
        for (currentNotification in notificationsList) {
            if (currentNotification.notificationTitle.lowercase(Locale.getDefault())
                    .contains(filterQuery)
            ) {
                filteredList.add(currentNotification)
            }

        }
        return filteredList

    }
}