package ru.blackmesa.studywords.ui.library


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.blackmesa.studywords.BuildConfig
import ru.blackmesa.studywords.R
import ru.blackmesa.studywords.data.models.DictData
import ru.blackmesa.studywords.databinding.FragmentLibraryBinding
import ru.blackmesa.studywords.ui.words.WordsFragment

class LibraryFragment : Fragment() {

    companion object {
        fun newInstance() = LibraryFragment()
    }

    private var _binding: FragmentLibraryBinding? = null
    private val binding: FragmentLibraryBinding get() = _binding!!
    private val viewModel: LibraryViewModel by viewModel()
    private val adapter = LibraryRVAdapter {
        findNavController().navigate(R.id.action_libraryFragment_to_wordsFragment,
            WordsFragment.createArgs(it.id))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.stopUpdate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.title = BuildConfig.BUILD_NAME

        viewModel.observeLibraryState().observe(viewLifecycleOwner) {
            renderLibraryState(it)
        }

        binding.libraryRecyclerView.adapter = adapter

        binding.topAppBar.setNavigationOnClickListener {
            binding.topAppBar.setNavigationIcon(R.drawable.ic_syncing)
            viewModel.updateLibrary()
            true
        }

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.wipeData -> {
                    viewModel.wipeAllLocalData()
                    true
                }
                R.id.signOut -> {
                    viewModel.signOut()
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadLocalLibrary()
        viewModel.updateLibrary()

        //binding.libraryRecyclerView.invalidate()
    }

    private fun renderLibraryState(libraryState: LibraryState) {
        when (libraryState) {
            is LibraryState.Start -> {
                binding.libraryRecyclerView.isVisible = false
                binding.topAppBar.setNavigationIcon(R.drawable.ic_updateholder)
            }

            is LibraryState.LibraryCurrent -> {
                binding.libraryRecyclerView.isVisible = true
                binding.topAppBar.setNavigationIcon(R.drawable.ic_synchronized)
                showLibrary(libraryState.library)
            }
            is LibraryState.LibraryUpdated -> {
                binding.libraryRecyclerView.isVisible = true
                binding.topAppBar.setNavigationIcon(R.drawable.ic_bolt)
                showLibrary(libraryState.library)
            }
            is LibraryState.NotAuthorized -> {
                viewModel.setDefaultState()
                findNavController().navigate(R.id.action_libraryFragment_to_authenticationFragment)
            }

            is LibraryState.NoConnection -> {
                binding.libraryRecyclerView.isVisible = true
                showLibrary(libraryState.library)
                binding.topAppBar.setNavigationIcon(R.drawable.ic_flightmode)
            }
        }
    }

    private fun showLibrary(dictData: List<DictData>) {
        adapter.library.clear()
        adapter.library.addAll(dictData)
        adapter.notifyDataSetChanged()
    }

}