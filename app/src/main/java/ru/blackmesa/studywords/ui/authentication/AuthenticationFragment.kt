package ru.blackmesa.studywords.ui.authentication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.blackmesa.studywords.data.models.AuthState
import ru.blackmesa.studywords.databinding.FragmentAuthenticationBinding

class AuthenticationFragment : Fragment() {

    companion object {
        fun newInstance() = AuthenticationFragment()
        const val ERROR_MESSAGE = "ERROR_MESSAGE"

        fun createArgs(errorMessage: String): Bundle =
            bundleOf(ERROR_MESSAGE to errorMessage)
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

        binding.errorMessage.text = arguments?.getString(ERROR_MESSAGE).orEmpty()

        binding.connectButton.setOnClickListener {
            hideKeyboard(binding.userText)
            hideKeyboard(binding.passwordText)

            viewModel.singIn(
                binding.userText.text.toString(),
                binding.passwordText.text.toString()
            )
        }

        binding.createButton.setOnClickListener {
            hideKeyboard(binding.userText)
            hideKeyboard(binding.passwordText)

            viewModel.createUser(
                binding.userText.text.toString()
            )
        }

        viewModel.observeAuthState().observe(viewLifecycleOwner) { renderState(it) }


    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    private fun renderState(state: AuthState) {
        binding.errorMessage.text = ""
        binding.userText.setText(state.credentials.userName)
        binding.passwordText.setText(state.credentials.password)

        binding.progressBar.isVisible = false
        binding.confirmErrorMessage.isVisible = false
        binding.confirmLayout.isVisible = false
        binding.confirmButton.isVisible = false
        binding.errorMessage.isVisible = false
        binding.restoreButton.isVisible = false

        when (state) {
            is AuthState.NoInternet -> {
                binding.errorMessage.text = "No internet connection"
                binding.errorMessage.isVisible = true
            }
            is AuthState.NotConnected -> {
                Log.d("STUDY_WORDS_DEBUG", "NotConnected")
            }
            is AuthState.NotConnectedLoading -> {
                binding.progressBar.isVisible = true
            }
            is AuthState.OtherError -> {
                binding.errorMessage.isVisible = true
                binding.errorMessage.text = state.errorMessage
            }
            is AuthState.PasswordError -> {
                binding.errorMessage.isVisible = true
                binding.errorMessage.text = "Wrong password"
            }
            is AuthState.PasswordErrorLoading -> {
                binding.progressBar.isVisible = true
                binding.errorMessage.text = "Wrong password"
            }
            is AuthState.Success -> Log.d("STUDY_WORDS_DEBUG", "Success")
            is AuthState.Confirmation -> {
                val confirmError = state.errorMessage
                if (confirmError.isNotEmpty()) {
                    binding.confirmErrorMessage.isVisible = true
                    binding.confirmErrorMessage.setText(confirmError)
                }
                binding.confirmLayout.isVisible = true
                binding.confirmButton.isVisible = true
            }
            is AuthState.ConfirmationLoading -> {
                binding.progressBar.isVisible = true
                val confirmError = state.errorMessage
                if (confirmError.isNotEmpty()) {
                    binding.confirmErrorMessage.isVisible = true
                    binding.confirmErrorMessage.setText(confirmError)
                }
                binding.confirmLayout.isVisible = true
                binding.confirmButton.isVisible = true
            }
        }
    }
}