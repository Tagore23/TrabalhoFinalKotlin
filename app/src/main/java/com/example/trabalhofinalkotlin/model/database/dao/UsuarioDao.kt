package com.example.trabalhofinalkotlin.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trabalhofinalkotlin.model.entity.Usuario

@Dao
interface UsuarioDao {

    // Inserir um novo usuário
    @Insert
    suspend fun inserir(usuario: Usuario)

    // Buscar todos os usuários
    @Query("SELECT * FROM usuarios")
    suspend fun buscarTodos(): List<Usuario>

    // Buscar um usuário pelo email e senha (para login)
    @Query("SELECT * FROM usuarios WHERE email = :email AND senha = :senha")
    suspend fun login(email: String, senha: String): Usuario?

    // Buscar um usuário pelo ID (exemplo)
    @Query("SELECT * FROM usuarios WHERE usuId = :id")
    suspend fun buscarPorId(id: Int): Usuario?
}
