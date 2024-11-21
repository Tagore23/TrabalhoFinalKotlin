package com.example.trabalhofinalkotlin.model.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.trabalhofinalkotlin.model.entity.Receitas
//import com.example.trabalhofinalkotlin.model.entity.UsuarioReceita

@Dao
interface ReceitasDao {

    @Insert
    suspend fun inserir(receitas: Receitas)

    // Atualizar uma receita existente
    @Update
    suspend fun atualizar(receitas: Receitas)

    // Excluir uma receita
    @Delete
    suspend fun excluir(receitas: Receitas)

    // Buscar todas as receitas de uma categoria
    @Query("SELECT * FROM receitas WHERE categoria = :categoria")
    suspend fun buscarPorCategoria(categoria: String): List<Receitas>


    /*@Transaction
    @Query("SELECT * FROM usuarios WHERE usuId = :usuarioId")
    suspend fun buscarReceitasPorUsuario(usuarioId: Int): UsuarioReceita*/

    // Buscar uma receita pelo ID
    @Query("SELECT * FROM receitas WHERE id = :id")
    suspend fun buscarPorId(id: Int): Receitas?

}