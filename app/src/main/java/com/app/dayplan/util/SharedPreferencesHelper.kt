package com.app.dayplan.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SharedPreferencesHelper {

    private const val FILE_NAME = "encrypted_settings"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_REFRESH_TOKEN = "refresh_token"
    private const val GRANTED_LOCATION = "granted_location"
    private const val LATITUDE_LOCATION = "latitude"
    private const val LONGITUDE_LOCATION = "longitude"

    private const val DEFAULT_LATITUDE = "37.541"
    private const val DEFAULT_LONGITUDE = "126.986"

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        val masterKeyAlias = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            FILE_NAME,
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    fun saveLocationPermissionStatus(grantedLocation: GrantedLocation) {
        sharedPreferences.edit().putString(GRANTED_LOCATION, grantedLocation.toString()).apply()
    }

    val grantedLocation: String
        get() = sharedPreferences.getString(GRANTED_LOCATION, null) ?: GrantedLocation.DENIED.toString()

    var accessToken: String
        get() = sharedPreferences.getString(KEY_ACCESS_TOKEN, null) ?: ""
        set(value) {
            sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, value).apply()
        }

    var refreshToken: String
        get() = sharedPreferences.getString(KEY_REFRESH_TOKEN, null) ?: ""
        set(value) {
            sharedPreferences.edit().putString(KEY_REFRESH_TOKEN, value).apply()
        }

    var latitude: String
        get() = sharedPreferences.getString(LATITUDE_LOCATION, null) ?: DEFAULT_LATITUDE
        set(value) {
            sharedPreferences.edit().putString(LATITUDE_LOCATION, value).apply()
        }

    var longitude: String
        get() = sharedPreferences.getString(LONGITUDE_LOCATION, null) ?: DEFAULT_LONGITUDE
        set(value) {
            sharedPreferences.edit().putString(LONGITUDE_LOCATION, value).apply()
        }

}