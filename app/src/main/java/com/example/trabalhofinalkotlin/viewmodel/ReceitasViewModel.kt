package com.example.trabalhofinalkotlin.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trabalhofinalkotlin.model.database.dao.ReceitasDao
import com.example.trabalhofinalkotlin.model.entity.Receitas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReceitasViewModel(private val receitasDao: ReceitasDao, private val context: Context) : ViewModel() {

    private val _receitasLiveData = MutableLiveData<List<Receitas>>()
    val receitasLiveData: LiveData<List<Receitas>> get() = _receitasLiveData

    var usuarioLogadoId: Int = 0

    fun buscarReceitasPorUsuario(usuarioId: Int) {
        usuarioLogadoId = usuarioId
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val receitas = receitasDao.buscarReceitasPorUsuario(usuarioId)
                _receitasLiveData.postValue(receitas)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Erro ao buscar receitas: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun adicionarReceita(titulo: String, descricao: String, categoria: String, usuarioId: Int) {
        if (titulo.isEmpty() || descricao.isEmpty() || categoria.isEmpty()) {
            Toast.makeText(context, "Preencha todos os campos antes de cadastrar!", Toast.LENGTH_SHORT).show()
            return
        }

        val receita = Receitas(
            id = 0,
            titulo = titulo,
            descricao = descricao,
            categoria = categoria,
            usuarioId = usuarioId
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                receitasDao.inserir(receita)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Receita adicionada com sucesso!", Toast.LENGTH_SHORT).show()
                }
                buscarReceitasPorUsuario(usuarioId)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Erro ao adicionar receita: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
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
}


