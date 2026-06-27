package com.petfinder.qr.database

import androidx.room.TypeConverter
import com.petfinder.qr.model.PetStatus

/** Room type converters for non-primitive columns. */
class Converters {

    @TypeConverter
    fun fromPetStatus(status: PetStatus): String = status.name

    @TypeConverter
    fun toPetStatus(value: String): PetStatus = PetStatus.valueOf(value)
}
