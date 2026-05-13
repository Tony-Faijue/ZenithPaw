package com.example.zenithpaw.roomdatabase

import androidx.room.TypeConverter
import com.example.zenithpaw.roomdatabase.pet.PetState
import com.example.zenithpaw.roomdatabase.pet.PetType
import com.example.zenithpaw.roomdatabase.task.TaskState

class Converters {

    @TypeConverter
    fun toPetState(state: String): PetState {
        return PetState.valueOf(state)
    }

    @TypeConverter
    fun fromPetState(state: PetState): String{
        return state.name
    }

    @TypeConverter
    fun toTaskState(state: String): TaskState {
        return TaskState.valueOf(state)
    }

    @TypeConverter
    fun fromTaskState(state: TaskState): String{
        return state.name
    }

    @TypeConverter
    fun toPetType(species: String): PetType {
        return PetType.valueOf(species)
    }

    @TypeConverter
    fun fromPetType(species: PetType): String{
        return species.name
    }
}