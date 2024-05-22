package ru.blackmesa.studywords.ui.words


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.blackmesa.studywords.R
import ru.blackmesa.studywords.data.models.UpdateResult
import ru.blackmesa.studywords.data.models.Word
import ru.blackmesa.studywords.data.models.WordWithTranslate
import ru.blackmesa.studywords.databinding.FragmentLibraryBinding
import ru.blackmesa.studywords.databinding.FragmentWordsBinding
import ru.blackmesa.studywords.ui.authentication.AuthenticationFragment
import ru.blackmesa.studywords.ui.study.StudyFragment

class WordsFragment : Fragment() {

    companion object {
        fun newInstance() = WordsFragment()

        const val DICTIONARY_ID_ARG = "DICTIONARY_ID_ARG"
        fun createArgs(dictionaryId: Int): Bundle =
            bundleOf(DICTIONARY_ID_ARG to dictionaryId)
    }

    private var _binding: FragmentWordsBinding? = null
    private val binding: FragmentWordsBinding get() = _binding!!
    private val viewModel: WordsViewModel by viewModel {
        parametersOf(requireArguments().getInt(DICTIONARY_ID_ARG))
    }
    private val adapter = WordsRVAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.wordsRecyclerView.adapter = adapter
        viewModel.observeContent().observe(viewLifecycleOwner) {
            renderContent(it)
        }
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.studyButton.setOnClickListener {
            findNavController().navigate(R.id.action_wordsFragment_to_studyFragment,
                StudyFragment.createArgs(adapter.words)
            )
        }

    }

    private fun renderContent(words: List<WordWithTranslate>) {
        adapter.words.clear()
        adapter.words.addAll(words)
        adapter.notifyDataSetChanged()
    }


}