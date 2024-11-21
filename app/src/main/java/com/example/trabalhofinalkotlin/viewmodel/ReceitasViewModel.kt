package com.example.trabalhofinalkotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.trabalhofinalkotlin.model.database.AppDataBase
import com.example.trabalhofinalkotlin.model.database.dao.ReceitasDao
import com.example.trabalhofinalkotlin.model.entity.Receitas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReceitasViewModel(application: Application) : AndroidViewModel(application) {

    private val receitasDao: ReceitasDao = AppDataBase.getDatabase(application).receitasDao()

    private val _receitasLiveData = MutableLiveData<List<Receitas>>()
    val receitasLiveData: LiveData<List<Receitas>> get() = _receitasLiveData


    fun buscarReceitasPorCategoria(categoria: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val receitas = receitasDao.buscarPorCategoria(categoria)
            _receitasLiveData.postValue(receitas)
        }
    }


    fun adicionarReceita(titulo: String, descricao: String, categoria: String, usuarioId: Int) {
        val receita = Receitas(
            id = 0,
            titulo = titulo,
            descricao = descricao,
            categoria = categoria,
            usuarioId = usuarioId
        )
        viewModelScope.launch(Dispatchers.IO) {
            receitasDao.inserir(receita)
        }
    }


    fun editarReceita(id: Int, titulo: String, descricao: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val receita = receitasDao.buscarPorId(id)
            receita?.let {

                val receitaAtualizada = it.copy(titulo = titulo, descricao = descricao)
                receitasDao.atualizar(receitaAtualizada)
            }
        }
    }


    fun excluirReceita(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val receita = receitasDao.buscarPorId(id)
            receita?.let {
                receitasDao.excluir(it)
            }
        }
    }


    fun buscarReceitasPorUsuario(usuarioId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
           // val usuarioReceita = receitasDao.buscarReceitasPorUsuario(usuarioId)
           // _receitasLiveData.postValue(usuarioReceita.receitas)
        }
    }
}
