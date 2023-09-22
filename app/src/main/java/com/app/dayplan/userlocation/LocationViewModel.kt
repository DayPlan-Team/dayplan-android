package com.app.dayplan.userlocation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.dayplan.api.auth.ApiAuthClient
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {
    private val _cityCode = MutableLiveData<CityResponse?>()
    var cityCodeState: MutableState<CityResponse?> = mutableStateOf(null)
        private set

    fun fetchCityCode(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val response = ApiAuthClient.locationGeocodeService.getAddressCodeByGeocode(latitude, longitude)

            Log.i("response = ", response.toString())

            if (response.isSuccessful) {
                val name = response.body()?.city ?: CityResponse.DEFAULT_NAME
                val code = response.body()?.cityCode ?: CityResponse.DEFAULT_CODE

                Log.i("name, code =", "$name, $code")
                _cityCode.value = CityResponse(
                    name = name,
                    code = code,
                )
                cityCodeState.value = _cityCode.value
            }
        }
    }
}