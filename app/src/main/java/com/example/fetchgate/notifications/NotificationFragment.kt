package com.example.fetchgate.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fetchgate.R
import com.example.fetchgate.adapter.NotificationsAdapter
import com.example.fetchgate.bol
import com.example.fetchgate.databinding.FragmentNotificationsBinding
import com.example.fetchgate.db.ItemDatabase
import com.example.fetchgate.message1
import com.example.fetchgate.network.Notifications
import com.example.fetchgate.notificationTitle
import com.example.fetchgate.utils.SwipeToDeleteCallback
import com.google.android.material.snackbar.Snackbar


class NotificationFragment : Fragment() {


    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var notificationsRecyclerViewAdapter: NotificationsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentNotificationsBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val removedItem = notificationsRecyclerViewAdapter.notifications[position]

              //  notificationsRecyclerViewAdapter.removeItem(position)
                notificationViewModel.deleteNotification(removedItem)


                val snackBar = Snackbar.make(
                    view,
                    "Item Removed!",
                    Snackbar.LENGTH_LONG
                ).setAction("Undo"){
                    notificationViewModel.addNotification(removedItem)

//                    notificationsRecyclerViewAdapter.notifications.add(
//                        notificationsRecyclerViewAdapter.removedPosition,
//                        notificationsRecyclerViewAdapter.removedItem
//                    )
                   // notificationsRecyclerViewAdapter.notifyItemInserted(notificationsRecyclerViewAdapter.removedPosition)
                }
                snackBar.show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerviewNotifications)
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
                notificationsRecyclerViewAdapter.updateList(it)
            }
        }
        return addViewModel
    }

//    private fun swipeToDelete() {
//        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
//            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
//            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
//        ) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                return true
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//               // val position = viewHolder.absoluteAdapterPosition
//                val notificationViewModel = initViewModel()
//
//                notificationsRecyclerViewAdapter.removeItem(viewHolder.absoluteAdapterPosition,viewHolder)
//
//             //   notificationViewModel.deleteNotification(notificationsRecyclerViewAdapter.removedItem)
//
//
//
////                Snackbar.make(
////                    viewHolder.itemView,
////                    " removed",
////                    Snackbar.LENGTH_LONG
////                ).setAction(
////                    "Undo"
////                ) {
////
////                    notificationsRecyclerViewAdapter.notifications.add(
////                        notificationsRecyclerViewAdapter.removedPosition,
////                        notificationsRecyclerViewAdapter.removedItem
////                    )
////                   // notificationViewModel.addNotification(notificationsRecyclerViewAdapter.removedItem)
////                    notificationsRecyclerViewAdapter.notifyItemInserted(
////                        notificationsRecyclerViewAdapter.removedPosition
////                    )
////                   // notificationViewModel.addNotification(notificationsRecyclerViewAdapter.removedItem)
////
////                }.show()
//
//
//            }
//
//        }).attachToRecyclerView(binding.recyclerviewNotifications)
//    }
}