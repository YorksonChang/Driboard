package com.sktbd.driboard.ui.fragment

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sktbd.driboard.data.db.AppDatabase
import com.sktbd.driboard.data.model.DraftEntity
import com.sktbd.driboard.databinding.DraftListFragmentBinding
import com.sktbd.driboard.ui.adapter.DraftList_RVAdapter
import com.sktbd.driboard.ui.factory.DraftListViewModelFactory
import com.sktbd.driboard.ui.viewmodel.DraftListViewModel


class DraftListFragment : Fragment (), SwipeRefreshLayout.OnRefreshListener  {
    private lateinit var binding: DraftListFragmentBinding
    private lateinit var rvAdapter: DraftList_RVAdapter
    private lateinit var draftListViewModel: DraftListViewModel
    private lateinit var draftListViewModelFactory: DraftListViewModelFactory
    companion object {
        fun newInstance() = ShotBoardFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        draftListViewModelFactory = DraftListViewModelFactory(activity!!.applicationContext)
        draftListViewModel = ViewModelProvider(this, draftListViewModelFactory).get(DraftListViewModel::class.java)
        binding = DraftListFragmentBinding.inflate(inflater,container,false)
        draftListViewModel.addData(DraftEntity(4,"DFSD","dasfadsfas","asdfa","asdfasdfas","asdfasf"))


        draftListViewModel.apply {
            draftListViewModel.getData()
            alMutableLiveData.observe(viewLifecycleOwner,androidx.lifecycle.Observer { list ->
                if(list!==null){
                    rvAdapter= DraftList_RVAdapter(list, draftListViewModel)
//                    rvAdapter.setOnItemClickListener(object : OnItemClickListener {
//                        override fun onclick(v: View, position: Int) {
//                            Log.i("CLICK", list[position].id.toString())
//                            this@ShotBoardFragment.findNavController().navigate(ShotBoardFragmentDirections.actionShotBoardFragmentToShotDetailFragment(list[position].id))
//                        }
//                    })
                    binding.rvDraftList.layoutManager= LinearLayoutManager(activity)
                    binding.rvDraftList.adapter = rvAdapter
                }
            })
        }

        binding.swipeContainerDraftList.setOnRefreshListener(this)

        return binding.root
    }

    override fun onRefresh() {
        draftListViewModel.clear()
        draftListViewModel.getData()
        binding.swipeContainerDraftList.isRefreshing = false
    }

}
