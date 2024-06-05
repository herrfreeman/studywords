package ru.blackmesa.studywords.ui.study


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.blackmesa.studywords.data.models.StudyList
import ru.blackmesa.studywords.data.models.WordData
import ru.blackmesa.studywords.databinding.FragmentStudyBinding

class StudyFragment : Fragment() {

    companion object {
        fun newInstance() = StudyFragment()

        const val WORDLIST_ARG = "DICTIONARY_ID_ARG"
        fun createArgs(wordList: List<WordData>): Bundle =
            bundleOf(WORDLIST_ARG to StudyList(wordList))

        val STRIGHT_STAGE = listOf(0,1,4,5,8,9)
    }

    private var _binding: FragmentStudyBinding? = null
    private val binding: FragmentStudyBinding get() = _binding!!
    private val viewModel: StudyViewModel by viewModel {
        parametersOf(
            requireArguments().getSerializable(WORDLIST_ARG, StudyList::class.java)
                ?.words ?: emptyList<WordData>()
        )
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
        _binding = FragmentStudyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) { renderState(it) }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.okButton.setOnClickListener { viewModel.showAnswer() }
        binding.yesButton.setOnClickListener { viewModel.gotResult(true) }
        binding.noButton.setOnClickListener { viewModel.gotResult(false) }

    }

    private fun renderState(state: StudyState) {
        when (state) {
            is StudyState.Question -> {

                binding.word.text = if (state.word.status in STRIGHT_STAGE) {
                    state.word.word
                } else {
                    state.word.translate
                }
                binding.answer.text = "?"
                binding.okButton.isVisible = true
                binding.yesButton.isVisible = false
                binding.noButton.isVisible = false
                binding.statusCaption.text = state.wordsLeft.toString()
            }

            is StudyState.Answer -> {
                if (state.word.status in STRIGHT_STAGE) {
                    binding.word.text = state.word.word
                    binding.answer.text = state.word.translate
                } else {
                    binding.word.text = state.word.translate
                    binding.answer.text = state.word.word
                }
                binding.okButton.isVisible = false
                binding.yesButton.isVisible = true
                binding.noButton.isVisible = true
            }

            StudyState.Finish -> findNavController().popBackStack()
        }

    }


}