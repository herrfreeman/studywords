package ru.blackmesa.studywords.ui.authentication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
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
            findNavController().navigate(R.id.action_authenticationFragment_to_createAccountFragment)
        }

        binding.restoreButton.setOnClickListener {
            findNavController().navigate(R.id.action_authenticationFragment_to_restorePasswordFragment)
        }

        binding.policyLink.setOnClickListener {
            val openLinkIntent = Intent(Intent.ACTION_VIEW)
            openLinkIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            openLinkIntent.data =
                Uri.parse(requireActivity().getString(R.string.privacy_policy_link))
            requireContext().startActivity(openLinkIntent)
        }

        viewModel.observeAuthState().observe(viewLifecycleOwner) { renderState(it) }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                setButtonsEnable()

            }
        }
        binding.userName.addTextChangedListener(textWatcher)
        binding.password.addTextChangedListener(textWatcher)
    }

    private fun setButtonsEnable() {
        binding.loginButton.isEnabled =
            !binding.userName.text.isNullOrEmpty() && !binding.password.text.isNullOrEmpty()
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
            }

            is AuthState.DefaultLoading -> {
                binding.progressBar.isVisible = true
            }

            is AuthState.ConfirmationRequest -> {
                throw IllegalStateException("CreateConfirmation on auth frame")
            }

            is AuthState.ConfirmationLoading -> {
                throw IllegalStateException("CreateConfirmationLoading on auth frame")
            }

            is AuthState.Success -> {
                binding.progressBar.isVisible = false
                findNavController().popBackStack()
            }

        }
        setButtonsEnable()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.userName.removeTextChangedListener(textWatcher)
        binding.password.removeTextChangedListener(textWatcher)
        _binding = null
    }

}