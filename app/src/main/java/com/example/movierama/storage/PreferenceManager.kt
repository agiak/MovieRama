package com.example.movierama.storage

import android.content.Context
import com.orhanobut.hawk.Hawk
import javax.inject.Inject

class PreferenceManager @Inject constructor(context: Context) {

    companion object{
        const val FAVOURITE_MOVIES_KEY = "favourite_movies_key"
    }

    init {
        Hawk.init(context).build()
    }

    fun<T> get(key: String, defaultValue: T): T{
        return Hawk.get(key, defaultValue)
    }

    fun<T> put(key: String, defaultValue: T){
         Hawk.put(key, defaultValue)
    }

    fun delete(key: String){
        Hawk.delete(key)
    }

    fun deleteAll(){
        Hawk.deleteAll()
    }

    fun contains(key: String): Boolean{
        return Hawk.contains(key)
    }
}