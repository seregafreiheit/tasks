package ru.frei.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.frei.tasks.data.ListsEntry
import ru.frei.tasks.databinding.FragmentBottomNavigationDrawerBinding
import ru.frei.tasks.databinding.ListItemBinding

class BottomNavigationFragment : BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<MainViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val database = (activity?.application as App).database
                return MainViewModel(database) as T
            }
        }
    }
    private var _binding: FragmentBottomNavigationDrawerBinding? = null
    private val binding get() = _binding!!

    private val navController: NavController by lazy {
        findNavController(
            requireActivity(),
            R.id.nav_host_fragment
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomNavigationDrawerBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ItemAdapter { listEntry ->
            navigateToMainFragment(listEntry.id.toInt())
        }

        binding.apply {
            listOfLists.adapter = adapter
            buttonAddList.setOnClickListener {
                navigateToAddListFragment()
            }
        }

        viewModel.lists.observe(viewLifecycleOwner) {
            adapter.setLists(it)
        }
    }

    private fun navigateToMainFragment(listId: Int) {
        navController.navigate(
            NavigationDirections
                .actionGlobalMainFragment().setListId(listId)
        )
        dismiss()
    }

    private fun navigateToAddListFragment() {
        val bundle = Bundle().apply {
            putLong("listId", 0L)
        }
        navController.navigate(R.id.addListFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private inner class ItemAdapter(
        private val onItemClick: (ListsEntry) -> Unit
    ) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

        private var lists: List<ListsEntry>? = null
        fun setLists(lists: List<ListsEntry>?) {
            this.lists = lists
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                ListItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            lists?.get(position)?.let { item ->
                holder.binding.apply {
                    taskView.text = item.list
                    root.setOnClickListener { onItemClick(item) }
                }
            }
        }

        override fun getItemCount(): Int = lists?.size ?: 0

        inner class ViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)
    }
}
