package ru.blackmesa.studywords.ui.authentication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.blackmesa.studywords.R
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
            //Hide keyboard when clear button clicked

            hideKeyboard(binding.userText)
            hideKeyboard(binding.passwordText)

            viewModel.singIn(
                binding.userText.text.toString(),
                binding.passwordText.text.toString()
            )
        }

        viewModel.observeCredentials().observe(viewLifecycleOwner) {
            binding.userText.setText(it.userName)
            binding.passwordText.setText(it.password)
        }

        viewModel.observeAuthState().observe(viewLifecycleOwner) {
            binding.connectButton.text = "CONNECT"
            binding.errorMessage.text = ""
            when (it) {
                is AuthState.Success -> findNavController().navigate(
                    R.id.action_authenticationFragment_to_libraryFragment)
                is AuthState.Connecting -> binding.connectButton.text = "CONNECTING..."
                is AuthState.Error -> binding.errorMessage.text = it.errorMessage
                is AuthState.NoConnection -> binding.errorMessage.text = "No internet connection"
            }
        }


    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }
}