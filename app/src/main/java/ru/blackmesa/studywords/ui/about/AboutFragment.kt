package ru.blackmesa.studywords.ui.about


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.blackmesa.studywords.BuildConfig
import ru.blackmesa.studywords.R
import ru.blackmesa.studywords.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    companion object {
        fun newInstance() = AboutFragment()
    }

    private var _binding: FragmentAboutBinding? = null
    private val binding: FragmentAboutBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val context = requireContext()
        binding.appname.text = "${context.getString(R.string.app_name)} v. ${BuildConfig.VERSION_NAME}"

        binding.shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_link))
            shareIntent.type = "text/*"
            context.startActivity(shareIntent)
        }

        binding.supportButton.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.support_mail)))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.support_subject))
            context.startActivity(emailIntent)
        }

        binding.manualLink.setOnClickListener {
            val openLinkIntent = Intent(Intent.ACTION_VIEW)
            openLinkIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            openLinkIntent.data =
                Uri.parse(requireActivity().getString(R.string.manual_link))
            requireContext().startActivity(openLinkIntent)
        }

        binding.policyLink.setOnClickListener {
            val openLinkIntent = Intent(Intent.ACTION_VIEW)
            openLinkIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            openLinkIntent.data =
                Uri.parse(requireActivity().getString(R.string.privacy_policy_link))
            requireContext().startActivity(openLinkIntent)
        }


    }



}