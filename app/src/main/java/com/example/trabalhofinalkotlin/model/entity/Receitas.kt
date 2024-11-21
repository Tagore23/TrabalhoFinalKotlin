package com.example.trabalhofinalkotlin.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "receitas")
data class Receitas(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val titulo: String,
    val descricao: String,
    val categoria: String,
    val dataCriacao: Long = System.currentTimeMillis(),
    val usuarioId: Int
)
