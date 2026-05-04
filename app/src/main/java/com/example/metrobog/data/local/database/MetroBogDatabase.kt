package com.example.metrobog.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.metrobog.data.local.dao.RouteDao
import com.example.metrobog.data.local.dao.StopDao
import com.example.metrobog.data.local.dao.StopTimeDao
import com.example.metrobog.data.local.entities.RouteEntity
import com.example.metrobog.data.local.entities.StopEntity
import com.example.metrobog.data.local.entities.StopTimeEntity
import com.example.metrobog.data.local.entities.TripEntity

@Database(
    entities = [
        StopEntity::class,
        RouteEntity::class,
        TripEntity::class,
        StopTimeEntity:: class
    ],
    version = 1,
    exportSchema = false
)
abstract class MetroBogDatabase : RoomDatabase(){
    abstract fun stopDao() : StopDao
    abstract fun routeDao(): RouteDao
    abstract fun stopTimeDao(): StopTimeDao

    companion object{
        @Volatile
        private var INSTANCE: MetroBogDatabase? = null

        fun getInstance(context: Context): MetroBogDatabase =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MetroBogDatabase::class.java,
                    "metrobog.db"
                ).build().also { INSTANCE = it }

            }
    }
}