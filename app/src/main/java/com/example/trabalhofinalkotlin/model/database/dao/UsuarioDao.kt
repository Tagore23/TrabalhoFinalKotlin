package com.example.trabalhofinalkotlin.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trabalhofinalkotlin.model.entity.Usuario

@Dao
interface UsuarioDao {
    @Insert
    suspend fun inserir(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE email = :email AND senha = :senha")
    suspend fun login(email: String, senha: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE usuId = :id")
    suspend fun buscarPorId(id: Int): Usuario?
}
