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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.blackmesa.studywords.R
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
    private var complainSuccessfulDialog: MaterialAlertDialogBuilder? = null
    private var complainErrorDialog: MaterialAlertDialogBuilder? = null

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

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.complainMenu -> {
                    viewModel.complain()
                    true
                }
                else -> false
            }
        }

        val context = requireContext()
        complainSuccessfulDialog = MaterialAlertDialogBuilder(context)
            .setTitle(context.getString(R.string.complain_success_message))
            .setNeutralButton("ОК") { dialog, which -> }
        complainErrorDialog = MaterialAlertDialogBuilder(context)
            .setTitle(context.getString(R.string.complain_error_message))
            .setNeutralButton("ОК") { dialog, which -> }

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
            is StudyState.ComplainError -> {
                renderWord(state.word, state.stage, state.wordsLeft)
                binding.hideLayout.isVisible = false
                binding.progressBar.isVisible = false
                complainErrorDialog?.show()
            }
            is StudyState.ComplainSuccess -> {
                renderWord(state.word, state.stage, state.wordsLeft)
                binding.hideLayout.isVisible = false
                binding.progressBar.isVisible = false
                complainSuccessfulDialog?.show()
            }
            is StudyState.Loading -> {
                renderWord(state.word, state.stage, state.wordsLeft)
                binding.hideLayout.isVisible = true
                binding.progressBar.isVisible = true
            }
            is StudyState.Study -> {
                renderWord(state.word, state.stage, state.wordsLeft)
                binding.hideLayout.isVisible = false
                binding.progressBar.isVisible = false
            }
            StudyState.Finish -> findNavController().popBackStack()
        }

    }

    private fun renderWord(word: WordData, stage: Int, wordsLeft: Int) {
        when (stage) {
            StudyState.STAGE_QUESTION -> {
                binding.answer.setOnClickListener(null)
                binding.word.text = if (viewModel.wordInStraitStage(word)) {
                    setSpeakListener(binding.word, word.word)
                    word.word
                } else {
                    binding.word.setOnClickListener(null)
                    word.translate
                }
                binding.answer.text = "?"
                binding.okButton.isVisible = true
                binding.yesButton.isVisible = false
                binding.noButton.isVisible = false
                binding.topAppBar.title = wordsLeft.toString()
            }
            StudyState.STAGE_ANSWER -> {
                if (viewModel.wordInStraitStage(word)) {
                    setSpeakListener(binding.word, word.word)
                    binding.answer.setOnClickListener(null)
                    binding.word.text = word.word
                    binding.answer.text = word.translate
                } else {
                    setSpeakListener(binding.answer, word.word)
                    binding.word.setOnClickListener(null)
                    binding.word.text = word.translate
                    binding.answer.text = word.word
                }
                binding.okButton.isVisible = false
                binding.yesButton.isVisible = true
                binding.noButton.isVisible = true
            }
        }
    }


}