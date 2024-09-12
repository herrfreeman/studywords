package ru.blackmesa.studywords.ui.words


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.blackmesa.studywords.R
import ru.blackmesa.studywords.data.models.WordData
import ru.blackmesa.studywords.databinding.FragmentWordsBinding
import ru.blackmesa.studywords.ui.study.StudyFragment

class WordsFragment : Fragment() {

    companion object {
        fun newInstance() = WordsFragment()

        const val DICTIONARY_ID_ARG = "DICTIONARY_ID_ARG"
        const val DICTIONARY_NAME_ARG = "DICTIONARY_NAME_ARG"
        fun createArgs(dictId: Int, dictName: String): Bundle =
            bundleOf(DICTIONARY_ID_ARG to dictId, DICTIONARY_NAME_ARG to dictName)
    }

    private var _binding: FragmentWordsBinding? = null
    private val binding: FragmentWordsBinding get() = _binding!!
    private val viewModel: WordsViewModel by viewModel {
        parametersOf(
            requireArguments().getInt(DICTIONARY_ID_ARG),
            requireArguments().getString(DICTIONARY_NAME_ARG),
        )
    }
    private val adapter = WordsRVAdapter {
        Toast.makeText(
            requireContext(),
            "${it.word} - ${it.translate} : ${it.status}",
            Toast.LENGTH_SHORT
        ).show()
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
        _binding = FragmentWordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.title = viewModel.getDictName()
        binding.wordsRecyclerView.adapter = adapter
        viewModel.observeContent().observe(viewLifecycleOwner) {
            renderContent(it)
        }

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.clearProgress -> {
                    viewModel.clearProgress(adapter.words)
                    true
                }

                else -> false
            }
        }

        binding.studyButton.setOnClickListener {
            val currentTimestamp = System.currentTimeMillis() / 1000
            val wordsToStudy = adapter.words
                .filter { it.status < 12 && (it.repeatdate == 0L || it.repeatdate <= currentTimestamp) }
                .shuffled()
                .sortedBy { if (it.repeatdate > 0) 1 else 2 }
                .take(10)

            if (wordsToStudy.isEmpty()) {
                // TODO Следующее повтороение тагдато / все слова изучегы
            } else {
                findNavController().navigate(
                    R.id.action_wordsFragment_to_studyFragment,
                    StudyFragment.createArgs(wordsToStudy)
                )
            }
        }

    }

    private fun renderContent(words: List<WordData>) {
        if (adapter.words != words) {
            adapter.words.clear()
            adapter.words.addAll(words)
        }
        adapter.notifyDataSetChanged()
    }


}