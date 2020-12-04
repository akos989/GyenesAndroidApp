package hu.gyenes.paintball.app.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import hu.gyenes.paintball.app.model.DTO.UserLoginResponse
import hu.gyenes.paintball.app.repository.UserRepository
import hu.gyenes.paintball.app.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    val userLogin: MutableLiveData<Resource<UserLoginResponse>> = MutableLiveData()

    fun login(email: String, password: String) = viewModelScope.launch {
        userLogin.postValue(Resource.Loading())
        val response = userRepository.login(email, password)
        userLogin.postValue(handleLoginResponse(response))
    }

    private fun handleLoginResponse(response: Response<UserLoginResponse>): Resource<UserLoginResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}

class UserViewModelProviderFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(userRepository) as T
    }
}