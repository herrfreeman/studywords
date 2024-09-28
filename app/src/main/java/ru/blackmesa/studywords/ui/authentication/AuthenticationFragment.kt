package ru.blackmesa.studywords.ui.authentication

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
    private var textWatcher: TextWatcher? = null

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

        binding.restoreButton.setOnClickListener {
            hideKeyboard(binding.userName)
            hideKeyboard(binding.password)

            viewModel.restorePassword(
                binding.userName.text.toString(),
                binding.password.text.toString()
            )
        }

        binding.confirmButton.setOnClickListener {
            viewModel.confirm(binding.confirmCode.text.toString())
        }

        viewModel.observeAuthState().observe(viewLifecycleOwner) { renderState(it) }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                binding.loginButton.isEnabled =
                    !binding.userName.text.isNullOrEmpty() && !binding.password.text.isNullOrEmpty()
                binding.createButton.isEnabled = binding.loginButton.isEnabled
                binding.restoreButton.isEnabled = binding.loginButton.isEnabled
            }
        }
        binding.userName.addTextChangedListener(textWatcher)
        binding.password.addTextChangedListener(textWatcher)
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    private fun renderState(state: AuthState) {
        binding.userName.setText(state.credentials.userName)
        binding.password.setText(state.credentials.password)
        binding.errorMessage.text = Html.fromHtml(state.errorMessage, Html.FROM_HTML_MODE_LEGACY)

        when (state) {
            is AuthState.Default -> {
                binding.progressBar.isVisible = false
                binding.confirmationLayout.isVisible = false
                makeMainViewEnabled(true)
            }

            is AuthState.DefaultLoading -> {
                binding.progressBar.isVisible = true
                binding.confirmationLayout.isVisible = false
                makeMainViewEnabled(false)
            }

            is AuthState.CreateConfirmation -> {
                binding.progressBar.isVisible = false
                binding.confirmationLayout.isVisible = true
                makeMainViewEnabled(false)
                makeConfirmViewEnabled(true)
                binding.confirmErrorMessage.text =
                    Html.fromHtml(state.confirmErrorMessage, Html.FROM_HTML_MODE_LEGACY)
                binding.confirmCode.setText(state.confirmCode)
            }

            is AuthState.CreateConfirmationLoading -> {
                binding.progressBar.isVisible = true
                binding.confirmationLayout.isVisible = true
                makeMainViewEnabled(false)
                makeConfirmViewEnabled(false)
                binding.confirmErrorMessage.text =
                    Html.fromHtml(state.confirmErrorMessage, Html.FROM_HTML_MODE_LEGACY)
                binding.confirmCode.setText(state.confirmCode)
            }

            is AuthState.Success -> {
                binding.progressBar.isVisible = false
                binding.confirmationLayout.isVisible = false
                makeMainViewEnabled(false)
                findNavController().popBackStack()
            }

        }
    }

    private fun makeMainViewEnabled(isEnabled: Boolean) {
        binding.mainLayout.isEnabled = isEnabled
        binding.userName.isEnabled = isEnabled
        binding.password.isEnabled = isEnabled

        binding.loginButton.isEnabled = isEnabled
        binding.createButton.isEnabled = isEnabled
        binding.restoreButton.isEnabled = isEnabled
    }

    private fun makeConfirmViewEnabled(isEnabled: Boolean) {
        binding.confirmationLayout.isEnabled = isEnabled
        binding.confirmCode.isEnabled = isEnabled
        binding.confirmButton.isEnabled = isEnabled
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.userName.removeTextChangedListener(textWatcher)
        binding.password.removeTextChangedListener(textWatcher)
        _binding = null
    }

}