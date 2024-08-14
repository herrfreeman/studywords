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

        //binding.errorMessage.text = arguments?.getString(ERROR_MESSAGE).orEmpty()

        binding.loginButton.setOnClickListener {
            hideKeyboard(binding.userName)
            hideKeyboard(binding.password)

            viewModel.login(
                binding.userName.text.toString(),
                binding.password.text.toString()
            )
        }

        binding.createButton.setOnClickListener {
            hideKeyboard(binding.userName)
            hideKeyboard(binding.password)

            viewModel.createAccount(
                binding.userName.text.toString(),
                binding.password.text.toString()
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
        binding.userName.setText(state.credentials.userName)
        binding.password.setText(state.credentials.password)
        binding.errorMessage.text = state.errorMessage

        when (state) {
            is AuthState.Default -> {
                binding.progressBar.isVisible = false
                binding.confirmCodeLayout.isVisible = false
            }
            is AuthState.DefaultLoading -> {
                binding.progressBar.isVisible = true
                binding.confirmCodeLayout.isVisible = false
            }
            is AuthState.CreateConfirmation -> {
                binding.progressBar.isVisible = false
                binding.confirmCodeLayout.isVisible = true
                binding.confirmErrorMessage.text = state.confirmErrorMessage
                binding.confirmCode.setText(state.confirmCode)
            }
            is AuthState.CreateConfirmationLoading -> {
                binding.progressBar.isVisible = true
                binding.confirmCodeLayout.isVisible = true
                binding.confirmErrorMessage.text = state.confirmErrorMessage
                binding.confirmCode.setText(state.confirmCode)
            }
            is AuthState.Success -> Log.d("STUDY_WORDS_DEBUG", "Success")

        }
    }
}