package com.example.phonebook.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "number") val number: String,

    @ColumnInfo(name = "tag_id") val tagId: Long,
    @ColumnInfo(name = "in_trash") val isInTrash: Boolean
) {
    companion object {
        val DEFAULT_CONTACTS = listOf(
            ContactDbModel(1, "Siriwat", "Janke", "0956091008", 1, false),
            ContactDbModel(2, "Siriwat", "Janke", "0956091008", 2, false),
            ContactDbModel(3, "Siriwat", "Janke", "0956091008", 1, false),
            ContactDbModel(4, "Siriwat", "Janke", "0956091008", 1, false),
            ContactDbModel(5, "Siriwat", "Janke", "0956091008", 3, false),
            ContactDbModel(6, "Siriwat", "Janke", "0956091008", 2, false),
            ContactDbModel(7, "Siriwat", "Janke", "0956091008", 2, false),
            ContactDbModel(8, "Siriwat", "Janke", "0956091008", 2, false),
            ContactDbModel(9, "Siriwat", "Janke", "0956091008", 3, false),
            ContactDbModel(10, "Siriwat", "Janke", "0956091008", 3, false),
        )
    }
}
