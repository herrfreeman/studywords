package ru.blackmesa.studywords.ui.words


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.blackmesa.studywords.R
import ru.blackmesa.studywords.data.models.WordData
import ru.blackmesa.studywords.databinding.FragmentWordsBinding
import ru.blackmesa.studywords.ui.study.StudyFragment
import ru.blackmesa.studywords.ui.study.StudyMode

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
    private var clearConfirmDialog: MaterialAlertDialogBuilder? = null
    private var nothingStudyDialog: MaterialAlertDialogBuilder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = requireContext()
        clearConfirmDialog = MaterialAlertDialogBuilder(context)
            .setTitle(context.getString(R.string.clear_confirm))
            .setNegativeButton(context.getString(R.string.no_button)) { dialog, which ->
            }.setPositiveButton(context.getString(R.string.yes_button)) { dialog, which ->
                viewModel.clearProgress(adapter.words)
            }
        nothingStudyDialog = MaterialAlertDialogBuilder(context)
            .setNeutralButton("ОК") { dialog, which -> }
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
                R.id.checkAllDone -> {
                    findNavController().navigate(
                        R.id.action_wordsFragment_to_studyFragment,
                        StudyFragment.createArgs(viewModel.getDoneWords(), StudyMode.CHECK_ONCE)
                    )
                    true
                }
                R.id.clearProgress -> {
                    clearConfirmDialog?.show()
                    true
                }
                else -> false
            }
        }

        binding.studyButton.setOnClickListener {
            val currentTimestamp = System.currentTimeMillis() / 1000
            val wordsToStudy = viewModel.getWordsToStudy()
            val secInHour = 60 * 60
            val secInDay = secInHour * 24

            if (wordsToStudy.isEmpty()) {
                val context = requireContext()
                var message = context.getString(R.string.nothing_study)
                viewModel.getNextRepeatTimestamp()?.let { nextRepeat ->
                    message += "\n" + context.getString(R.string.next_repeat) + if (nextRepeat - currentTimestamp > secInDay) {
                        ((nextRepeat - currentTimestamp) / secInDay).toInt()
                            .toString() + context.getString(R.string.days)
                    } else {
                        ((nextRepeat - currentTimestamp) / secInHour).toInt()
                            .toString() + context.getString(R.string.hours)
                    }
                }
                nothingStudyDialog?.setMessage(message)?.show()
            } else {
                findNavController().navigate(
                    R.id.action_wordsFragment_to_studyFragment,
                    StudyFragment.createArgs(wordsToStudy, StudyMode.COMMON)
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