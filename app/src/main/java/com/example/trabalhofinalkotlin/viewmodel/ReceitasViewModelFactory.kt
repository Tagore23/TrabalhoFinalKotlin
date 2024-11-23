package com.example.trabalhofinalkotlin.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trabalhofinalkotlin.model.database.dao.ReceitasDao

class ReceitasViewModelFactory(
    private val receitasDao: ReceitasDao,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReceitasViewModel::class.java)) {
            return ReceitasViewModel(receitasDao, context) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}
