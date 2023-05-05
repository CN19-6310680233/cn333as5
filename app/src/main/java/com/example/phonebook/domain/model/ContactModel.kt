package com.example.phonebook.domain.model

const val NEW_CONTACT_ID = -1L

data class ContactModel(
    val id: Long = NEW_CONTACT_ID, // This value is used for new notes
    
    val firstName: String = "",
    val lastName: String = "",
    val number: String = "",

    val tag: TagModel = TagModel.DEFAULT,
    val isInTrash: Boolean = false,
)