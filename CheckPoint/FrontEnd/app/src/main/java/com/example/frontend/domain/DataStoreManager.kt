package com.example.frontend.domain

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.settingsDataStore : DataStore<Preferences> by preferencesDataStore(name = "MyDataStore");

object DataStoreManager {
    suspend fun saveValue(context: Context, key : String, value : String)
    {
        val wrappedKey = stringPreferencesKey(key);
        context.settingsDataStore.edit {
            it[wrappedKey] = value
        }
    }

    suspend fun saveValue(context: Context, key : String, value : Int)
    {
        val wrappedKey = intPreferencesKey(key);
        context.settingsDataStore.edit {
            it[wrappedKey] = value
        }
    }

    suspend fun getStringValue(context: Context, key : String, default : String = "") : String
    {
        val wrappedKey = stringPreferencesKey(key);
        val valueFlow : Flow<String> = context.settingsDataStore.data.map {
            it[wrappedKey] ?: default
        }
        return valueFlow.first()
    }

    suspend fun getIntValue(context: Context, key : String, default: Int = 0) : Int
    {
        val wrappedKey = intPreferencesKey(key);
        val valueFlow : Flow<Int> = context.settingsDataStore.data.map {
            it[wrappedKey] ?: default
        }
        return valueFlow.first()
    }
}