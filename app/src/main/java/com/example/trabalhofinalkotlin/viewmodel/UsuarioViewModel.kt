package com.example.trabalhofinalkotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.trabalhofinalkotlin.model.database.AppDataBase
import com.example.trabalhofinalkotlin.model.dao.UsuarioDao
import com.example.trabalhofinalkotlin.model.entity.Usuario
import com.example.mvvm2.model.Validacao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val usuarioDao: UsuarioDao = AppDataBase.getDatabase(application).usuarioDao()


    fun registrarUsuario(nome: String, email: String, senha: String, confirmacaoSenha: String) {

        if (Validacao.haCamposEmBranco(nome, email, senha, confirmacaoSenha)) {

            return
        }

        if (!Validacao.isEmailValido(email)) {

            return
        }

        if (!Validacao.isSenhaValida(senha)) {

            return
        }

        if (!Validacao.senhasCoincidem(senha, confirmacaoSenha)) {

            return
        }

        val usuario = Usuario(usuId = 0, nome = nome, email = email, senha = senha)

        viewModelScope.launch(Dispatchers.IO) {
            usuarioDao.inserir(usuario)
        }
    }


    fun loginUsuario(email: String, senha: String, onResult: (Usuario?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val usuario = usuarioDao.login(email, senha)
            onResult(usuario)
        }
    }


    fun buscarUsuarioPorId(id: Int, onResult: (Usuario?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val usuario = usuarioDao.buscarPorId(id)
            onResult(usuario)
        }
    }
}