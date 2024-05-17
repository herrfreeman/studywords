package ru.blackmesa.studywords.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import ru.blackmesa.studywords.R
import ru.blackmesa.studywords.data.network.AuthRequest
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthenticationFragment : Fragment() {

//    companion object {
//        fun newInstance() = AuthenticationFragment()
//    }

    private val viewModel: AuthenticationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_authentication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view.findViewById<Button>(R.id.connectButton)
        val user = view.findViewById<TextInputEditText>(R.id.user_text)
        val password = view.findViewById<TextInputEditText>(R.id.password_text)
        val result = view.findViewById<TextView>(R.id.result)

        viewModel.message.observe(viewLifecycleOwner) {
            result.setText(it)
        }

        button.setOnClickListener {
            Log.d("STUDY_WORDS", "Connect clicked")
            viewModel.auth(AuthRequest(user.text.toString(), password.text.toString()))
        }
    }
}