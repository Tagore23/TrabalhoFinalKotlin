package com.example.mvvm2.model

import android.util.Patterns

abstract class Validacao {

    companion object {

        private var id = 0

        // Método para gerar ID incremental
        fun getId(): Int {
            return id++
        }

        // Valida se algum campo está em branco
        fun haCamposEmBranco(vararg campos: String): Boolean {
            return campos.any { it.isBlank() }
        }

        // Valida se o email tem o formato correto
        fun isEmailValido(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        // Valida se a senha tem pelo menos 6 caracteres
        fun isSenhaValida(senha: String): Boolean {
            return senha.length >= 6
        }

        // Valida se o nome de usuário tem pelo menos 3 caracteres
        fun isNomeUsuarioValido(nome: String): Boolean {
            return nome.length >= 3
        }

        // Valida se a senha e a confirmação de senha são iguais
        fun senhasCoincidem(senha: String, confirmacaoSenha: String): Boolean {
            return senha == confirmacaoSenha
        }
    }
}
