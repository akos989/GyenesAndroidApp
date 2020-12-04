package hu.gyenes.paintball.app.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hu.gyenes.paintball.app.R
import hu.gyenes.paintball.app.model.CurrentUser
import hu.gyenes.paintball.app.model.DTO.UserLoginResponse
import hu.gyenes.paintball.app.utils.Resource
import hu.gyenes.paintball.app.view.activiy.MainActivity
import hu.gyenes.paintball.app.view.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(R.layout.fragment_login) {

    lateinit var userViewModel: UserViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = (activity as MainActivity).userViewModel

        btnLogin.setOnClickListener {
            if (validateAll()) {
                userViewModel.login(
                    etLoginEmail.text.toString(),
                    etLoginPassword.text.toString()
                )
            }
        }
        userViewModel.userLogin.observe(viewLifecycleOwner) { response -> handleLoginResponse(response) }
    }

    private fun handleLoginResponse(response: Resource<UserLoginResponse>) {
        when (response) {
            is Resource.Success -> {
                hideSpinner()
                response.data?.let { loginResponse ->
                    CurrentUser.login(loginResponse)
                    findNavController()
                        .navigate(LoginFragmentDirections.actionLoginFragmentToReservationListFragment())
                }
            }
            is Resource.Error -> {
                hideSpinner()
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_LONG).show()
            }
            is Resource.Loading -> showSpinner()
        }
    }

    private fun hideSpinner() {
    }

    private fun showSpinner() {
    }

    private fun validateAll(): Boolean {
        return when {
            etLoginEmail.text.isEmpty() -> {
                etLoginEmail.error = getString(R.string.field_cannot_empty, "Email")
                false
            }
            etLoginPassword.text.isEmpty() -> {
                etLoginPassword.error = getString(R.string.field_cannot_empty, getString(R.string.Password))
                false
            }
            else -> true
        }
    }
}