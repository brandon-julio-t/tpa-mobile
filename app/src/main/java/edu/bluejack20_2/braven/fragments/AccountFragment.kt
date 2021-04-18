package edu.bluejack20_2.braven.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import edu.bluejack20_2.braven.databinding.FragmentAccountBinding
import edu.bluejack20_2.braven.services.AuthenticationService
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var authenticationService: AuthenticationService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = authenticationService.getUser()
        binding.email.text = user?.email
        binding.posterDisplayName.text = user?.displayName
        binding.fullName.text = user?.displayName
        binding.dateOfBirth.text = "not set"
        binding.photoUrl.text = user?.photoUrl.toString()
        binding.logout.setOnClickListener { authenticationService.logout(it) }
    }
}