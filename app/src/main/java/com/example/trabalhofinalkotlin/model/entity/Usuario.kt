package com.example.trabalhofinalkotlin.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val usuId: Int,
    val nome: String,
    val email: String,
    val senha: String
)
