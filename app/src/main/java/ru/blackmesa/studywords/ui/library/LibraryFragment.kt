package ru.blackmesa.studywords.ui.library


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.blackmesa.studywords.R
import ru.blackmesa.studywords.data.models.UpdateResult
import ru.blackmesa.studywords.databinding.FragmentLibraryBinding
import ru.blackmesa.studywords.ui.authentication.AuthenticationFragment
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

        // TODO: Use the ViewModel
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

        viewModel.observeUpdateState().observe(viewLifecycleOwner) {
            renderUpdateStatus(it)
        }
        viewModel.observeDictionary().observe(viewLifecycleOwner) {
            adapter.library.clear()
            adapter.library.addAll(it)
            adapter.notifyDataSetChanged()
        }
        viewModel.startUpdate()

        binding.libraryRecyclerView.adapter = adapter

        binding.topAppBar.setNavigationOnClickListener {
            binding.topAppBar.setNavigationIcon(R.drawable.ic_updateholder)
            viewModel.startUpdate()
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
        viewModel.loadLibrary()
        //binding.libraryRecyclerView.invalidate()
    }

    private fun renderUpdateStatus(it: UpdateResult) {
        when (it) {
            is UpdateResult.Synchronized -> binding.topAppBar.setNavigationIcon(R.drawable.ic_synchronized)
            is UpdateResult.Error -> {
                findNavController().navigate(R.id.action_libraryFragment_to_authenticationFragment,
                    AuthenticationFragment.createArgs(it.message))
            }
            is UpdateResult.LibraryUpdated -> {
                adapter.library.clear()
                adapter.library.addAll(it.library)
                adapter.notifyDataSetChanged()
                //binding.libraryRecyclerView.invalidate()
                binding.topAppBar.setNavigationIcon(R.drawable.ic_bolt)
            }
            is UpdateResult.NoConnection -> binding.topAppBar.setNavigationIcon(R.drawable.ic_flightmode)
            is UpdateResult.NotSignedIn -> findNavController().navigate(R.id.action_libraryFragment_to_authenticationFragment)
        }

    }


}