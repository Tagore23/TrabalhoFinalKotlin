package com.example.trabalhofinalkotlin.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trabalhofinalkotlin.model.entity.Receitas

@Dao
interface ReceitasDao {
    @Insert
    suspend fun inserir(receitas: Receitas)

    @Query("SELECT * FROM receitas WHERE usuarioId = :usuarioId")
    suspend fun buscarReceitasPorUsuario(usuarioId: Int): List<Receitas>

    @Query("UPDATE receitas SET titulo = :titulo, descricao = :descricao WHERE id = :id")
    suspend fun atualizarReceita(id: Int, titulo: String, descricao: String)

    @Query("DELETE FROM receitas WHERE id = :id")
    suspend fun excluirReceita(id: Int)
}
