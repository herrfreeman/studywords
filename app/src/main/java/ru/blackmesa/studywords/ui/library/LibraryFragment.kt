package ru.blackmesa.studywords.ui.library


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.blackmesa.studywords.R
import ru.blackmesa.studywords.data.models.UpdateResult
import ru.blackmesa.studywords.databinding.FragmentLibraryBinding

class LibraryFragment : Fragment() {

    companion object {
        fun newInstance() = LibraryFragment()
    }

    private var _binding: FragmentLibraryBinding? = null
    private val binding: FragmentLibraryBinding get() = _binding!!
    private val viewModel: LibraryViewModel by viewModel()

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
        viewModel.startUpdate()

        binding.statusIcon.setOnClickListener { viewModel.startUpdate() }

    }

    private fun renderUpdateStatus(it: UpdateResult) {
        when (it) {
            is UpdateResult.Synchronized -> binding.statusIcon.setImageResource(R.drawable.ic_synchronized)
            is UpdateResult.Error -> Log.d("STUDY_WORDS_DEBUG", "got error: ${it.message}")
            is UpdateResult.GotNewData -> Log.d("STUDY_WORDS_DEBUG", "got new data")
            is UpdateResult.NoConnection -> binding.statusIcon.setImageResource(R.drawable.ic_flightmode)
            is UpdateResult.NotSignedIn -> Log.d("STUDY_WORDS_DEBUG", "not signed in")
        }

    }


}