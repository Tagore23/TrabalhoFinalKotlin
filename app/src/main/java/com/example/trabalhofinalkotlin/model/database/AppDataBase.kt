package com.example.trabalhofinalkotlin.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.trabalhofinalkotlin.model.dao.UsuarioDao
import com.example.trabalhofinalkotlin.model.database.dao.ReceitasDao
import com.example.trabalhofinalkotlin.model.entity.Receitas
import com.example.trabalhofinalkotlin.model.entity.Usuario

@Database(entities = [Usuario::class, Receitas::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun receitasDao(): ReceitasDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val tempInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_database"
                ).build()
                INSTANCE = tempInstance
                tempInstance
            }
        }
    }
}
