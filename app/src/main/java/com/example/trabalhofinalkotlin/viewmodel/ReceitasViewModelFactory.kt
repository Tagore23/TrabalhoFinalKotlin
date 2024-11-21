package com.example.trabalhofinalkotlin.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trabalhofinalkotlin.model.database.dao.ReceitasDao

class ReceitasViewModelFactory(private val receitasDao: ReceitasDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReceitasViewModel::class.java)) {
            return ReceitasViewModel(receitasDao) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}
