package ru.blackmesa.studywords.ui.library


import android.os.Bundle
import android.util.Log
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
    private val adapter = LibraryRVAdapter(
        object : LibraryRVAdapter.ItemClickListener {
            override fun onItemClick(item: DictData) {
                findNavController().navigate(
                    R.id.action_libraryFragment_to_wordsFragment,
                    WordsFragment.createArgs(
                        dictId = item.id,
                        dictName = item.name,
                    )
                )
            }

            override fun onDownloadClick(item: DictData) {
                viewModel.downloadDictionary(item)
            }
        }
    )

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
        binding.libraryRecyclerView.adapter = adapter

        binding.repatUpdate.setOnClickListener { viewModel.updateLibrary() }
        binding.dontUpdate.setOnClickListener { viewModel.updateLibrary(true) }

        viewModel.observeLibraryState().observe(viewLifecycleOwner) {
            renderLibraryState(it)
        }

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

                R.id.about -> {
                    findNavController().navigate(R.id.action_libraryFragment_to_aboutFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("STUDY_WORDS", "On resume update")
        viewModel.updateLibrary()
    }

    private fun renderLibraryState(libraryState: LibraryState) {
        when (libraryState) {
            is LibraryState.Loading -> {
                Log.d("STUDY_WORDS", "Render Loading state")
                binding.errorLayout.isVisible = false
                binding.progressBar.isVisible = true
                binding.hideLayout.isVisible = true
                binding.libraryRecyclerView.isVisible = true
                binding.topAppBar.setNavigationIcon(R.drawable.ic_updateholder)
            }

            is LibraryState.LibraryCurrent -> {
                Log.d("STUDY_WORDS", "Render LibraryCurrent state")
                binding.errorLayout.isVisible = false
                binding.progressBar.isVisible = false
                binding.hideLayout.isVisible = false
                binding.libraryRecyclerView.isVisible = true
                binding.topAppBar.setNavigationIcon(R.drawable.ic_synchronized)
                showLibrary(libraryState.library)
            }

            is LibraryState.LibraryUpdated -> {
                Log.d("STUDY_WORDS", "Render LibraryUpdated state")
                binding.errorLayout.isVisible = false
                binding.progressBar.isVisible = false
                binding.hideLayout.isVisible = false
                binding.libraryRecyclerView.isVisible = true
                binding.topAppBar.setNavigationIcon(R.drawable.ic_bolt)
                showLibrary(libraryState.library)
            }

            is LibraryState.NotAuthorized -> {
                Log.d("STUDY_WORDS", "Render NotAuthorized state")
                binding.errorLayout.isVisible = false
                binding.progressBar.isVisible = false
                binding.hideLayout.isVisible = true
                binding.libraryRecyclerView.isVisible = false
                viewModel.setLoadingState()
                findNavController().navigate(R.id.action_libraryFragment_to_authenticationFragment)
            }

            is LibraryState.NoConnection -> {
                Log.d("STUDY_WORDS", "Render NoConnection state")
                binding.errorLayout.isVisible = false
                binding.progressBar.isVisible = false
                binding.hideLayout.isVisible = false
                binding.libraryRecyclerView.isVisible = true
                showLibrary(libraryState.library)
                binding.topAppBar.setNavigationIcon(R.drawable.ic_flightmode)
            }

            is LibraryState.UpdateError -> {
                Log.d("STUDY_WORDS", "Render UpdateError state")
                binding.errorMessage.text = libraryState.error
                //Toast.makeText(requireContext(), libraryState.error, Toast.LENGTH_LONG).show()
                binding.errorLayout.isVisible = true
                binding.progressBar.isVisible = false
                binding.hideLayout.isVisible = false
                binding.libraryRecyclerView.isVisible = true
                binding.topAppBar.setNavigationIcon(R.drawable.ic_synchronized)
                showLibrary(libraryState.library)
            }
        }
    }

    private fun showLibrary(dictData: List<DictData>) {
        Log.d("STUDY_WORDS", "Show lobrary couint: ${dictData.size}")
        adapter.library.clear()
        adapter.library.addAll(dictData)
        adapter.notifyDataSetChanged()
    }

}