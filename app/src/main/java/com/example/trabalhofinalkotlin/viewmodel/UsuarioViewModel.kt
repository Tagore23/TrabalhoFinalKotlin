package com.example.trabalhofinalkotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trabalhofinalkotlin.model.dao.UsuarioDao
import com.example.trabalhofinalkotlin.model.entity.Usuario
import com.example.mvvm2.model.Validacao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsuarioViewModel(private val usuarioDao: UsuarioDao) : ViewModel() {

    fun registrarUsuario(nome: String, email: String, senha: String, confirmacaoSenha: String, onResult: (Boolean, String?) -> Unit) {
        // Verificando campos vazios
        if (Validacao.haCamposEmBranco(nome, email, senha, confirmacaoSenha)) {
            onResult(false, "Preencha todos os campos.")
            return
        }
        // Validando o email
        if (!Validacao.isEmailValido(email)) {
            onResult(false, "Email inválido.")
            return
        }
        // Validando a senha
        if (!Validacao.isSenhaValida(senha)) {
            onResult(false, "A senha deve ter pelo menos 6 caracteres.")
            return
        }
        // Validando se as senhas coincidem
        if (!Validacao.senhasCoincidem(senha, confirmacaoSenha)) {
            onResult(false, "As senhas não coincidem.")
            return
        }

        // Criando o usuário
        val usuario = Usuario(usuId = 0, nome = nome, email = email, senha = senha)

        // Inserindo no banco
        viewModelScope.launch(Dispatchers.IO) {
            try {
                usuarioDao.inserir(usuario)
                onResult(true, "Cadastro realizado com sucesso!")  // Sucesso
            } catch (e: Exception) {
                onResult(false, "Erro ao cadastrar usuário. Tente novamente.")  // Falha
            }
        }
    }


    fun loginUsuario(email: String, senha: String, onResult: (Usuario?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val usuario = usuarioDao.login(email, senha)
            onResult(usuario)
        }
    }
}

