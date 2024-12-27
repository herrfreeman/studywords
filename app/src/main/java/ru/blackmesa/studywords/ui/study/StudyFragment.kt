package ru.blackmesa.studywords.ui.study


import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.blackmesa.studywords.data.models.StudyList
import ru.blackmesa.studywords.data.models.WordData
import ru.blackmesa.studywords.databinding.FragmentStudyBinding


class StudyFragment : Fragment() {

    companion object {
        fun newInstance() = StudyFragment()

        const val WORDLIST_ARG = "DICTIONARY_ID_ARG"
        const val STUDY_MODE = "STUDY_MODE"

        fun createArgs(wordList: List<WordData>, studyMode: Int): Bundle =
            bundleOf(WORDLIST_ARG to StudyList(wordList), STUDY_MODE to studyMode)

    }

    private var _binding: FragmentStudyBinding? = null
    private val binding: FragmentStudyBinding get() = _binding!!
    private val textToSpeech: TextToSpeech by inject()
    private val viewModel: StudyViewModel by viewModel {
        val wordArg: StudyList? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(WORDLIST_ARG, StudyList::class.java)
        } else {
            requireArguments().getSerializable(WORDLIST_ARG) as? StudyList
        }
        parametersOf(
            wordArg?.words ?: emptyList<WordData>(),
            requireArguments().getInt("STUDY_MODE")
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
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.okButton.setOnClickListener { viewModel.showAnswer() }
        binding.yesButton.setOnClickListener { viewModel.gotResult(true) }
        binding.yesButton.setOnLongClickListener {
            viewModel.setFullyStudied()
            true
        }
        binding.noButton.setOnClickListener { viewModel.gotResult(false) }

    }

    private fun setSpeakListener(source: View, text: CharSequence) {
        source.setOnClickListener {
            textToSpeech?.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                this.hashCode().toString()
            )
        }
    }

    private fun renderState(state: StudyState) {
        when (state) {
            is StudyState.Question -> {
                binding.answer.setOnClickListener(null)
                binding.word.text = if (viewModel.wordInStraitStage(state.word)) {
                    setSpeakListener(binding.word, state.word.word)
                    state.word.word
                } else {
                    binding.word.setOnClickListener(null)
                    state.word.translate
                }
                binding.answer.text = "?"
                binding.okButton.isVisible = true
                binding.yesButton.isVisible = false
                binding.noButton.isVisible = false
                binding.topAppBar.title = state.wordsLeft.toString()
            }

            is StudyState.Answer -> {
                if (viewModel.wordInStraitStage(state.word)) {
                    setSpeakListener(binding.word, state.word.word)
                    binding.answer.setOnClickListener(null)
                    binding.word.text = state.word.word
                    binding.answer.text = state.word.translate
                } else {
                    setSpeakListener(binding.answer, state.word.word)
                    binding.word.setOnClickListener(null)
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