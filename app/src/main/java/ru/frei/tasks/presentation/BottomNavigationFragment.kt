package ru.frei.tasks.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.frei.tasks.NavigationDirections
import ru.frei.tasks.R
import ru.frei.tasks.ui.main.BottomNavigationDrawer
import ru.frei.tasks.ui.theme.AppTheme

@AndroidEntryPoint
class BottomNavigationFragment : BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<MainViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    val lists by viewModel.getListsLiveData().observeAsState(emptyList())

                    BottomNavigationDrawer(
                        lists = lists,
                        onNavigateToMain = { listId ->
                            findNavController().navigate(
                                NavigationDirections.actionGlobalMainFragment()
                                    .setListId(listId)
                            )
                        },
                        onNavigateToAddList = {
                            findNavController().navigate(
                                R.id.addListFragment,
                                Bundle().apply { putLong("listId", 0L) }
                            )
                        },
                        onDismiss = { dismiss() }
                    )
                }
            }
        }
    }
}