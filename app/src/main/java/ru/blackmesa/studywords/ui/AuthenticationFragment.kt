package ru.blackmesa.studywords.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.blackmesa.studywords.data.network.AuthRequest
import ru.blackmesa.studywords.databinding.FragmentAuthenticationBinding

class AuthenticationFragment : Fragment() {

    companion object {
        fun newInstance() = AuthenticationFragment()
    }

    private var _binding: FragmentAuthenticationBinding? = null
    private val binding: FragmentAuthenticationBinding get() = _binding!!
    private val viewModel: AuthenticationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.message.observe(viewLifecycleOwner) {
            binding.result.setText(it)
        }

        binding.connectButton.setOnClickListener {
            Log.d("STUDY_WORDS", "Connect clicked")
            viewModel.auth(
                AuthRequest(
                    binding.userText.text.toString(),
                    binding.passwordText.text.toString()
                )
            )
        }
    }
}