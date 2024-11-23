package com.example.trabalhofinalkotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trabalhofinalkotlin.model.database.AppDataBase
import com.example.trabalhofinalkotlin.model.entity.Receitas
import com.example.trabalhofinalkotlin.model.database.dao.ReceitasDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReceitasViewModel(private val receitasDao: ReceitasDao) : ViewModel() {
    val receitasLiveData = MutableLiveData<List<Receitas>>()

    fun adicionarReceita(titulo: String, descricao: String, categoria: String, usuarioId: Int) {
        val receita = Receitas(id = 0, titulo = titulo, descricao = descricao, categoria = categoria, usuarioId = usuarioId)
        viewModelScope.launch(Dispatchers.IO) {
            receitasDao.inserir(receita)
            buscarReceitasPorUsuario(usuarioId) // Atualiza a lista de receitas
        }
    }

    fun editarReceita(id: Int, titulo: String, descricao: String, usuarioId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            receitasDao.atualizarReceita(id, titulo, descricao)
            buscarReceitasPorUsuario(usuarioId) // Atualiza a lista de receitas
        }
    }

    fun excluirReceita(id: Int, usuarioId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            receitasDao.excluirReceita(id)
            buscarReceitasPorUsuario(usuarioId) // Atualiza a lista após exclusão
        }
    }

    fun buscarReceitasPorUsuario(usuarioId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val receitas = receitasDao.buscarReceitasPorUsuario(usuarioId)
            receitasLiveData.postValue(receitas)
        }
    }
}


