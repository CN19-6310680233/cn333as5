package com.example.phonebook.database.utils

import com.example.phonebook.database.models.ContactDbModel
import com.example.phonebook.database.models.TagDbModel
import com.example.phonebook.domain.model.*

class DbMapper {
    // Create list of NoteModels by pairing each note with a color
    fun mapContacts(
        contactDbModels: List<ContactDbModel>,
        tagDbModels: Map<Long, TagDbModel>
    ): List<ContactModel> = contactDbModels.map {
        val tagDbModel = tagDbModels[it.tagId]
            ?: throw RuntimeException("Tag for tagId: ${it.tagId} was not found. Make sure that all tags are passed to this method")

        mapContact(it, tagDbModel)
    }

    // convert NoteDbModel to NoteModel
    fun mapContact(contactDbModel: ContactDbModel, tagDbModel: TagDbModel): ContactModel {
        val tag = mapTag(tagDbModel)
        return with(contactDbModel) { ContactModel(id, firstName, lastName, number, tag) }
    }

    // convert list of ColorDdModels to list of ColorModels
    fun mapTags(tagDbModels: List<TagDbModel>): List<TagModel> =
        tagDbModels.map { mapTag(it) }

    // convert ColorDbModel to ColorModel
    fun mapTag(tagDbModel: TagDbModel): TagModel =
        with(tagDbModel) { TagModel(id, name) }

    // convert NoteModel back to NoteDbModel
    fun mapDbContact(contact: ContactModel): ContactDbModel =
        with(contact) {
            if (id == NEW_CONTACT_ID)
                ContactDbModel(
                    firstName = firstName,
                    lastName = lastName,
                    number = number,
                    tagId = tag.id,
                    isInTrash = false
                )
            else
                ContactDbModel(id, firstName, lastName, number, tag.id, false)
        }
}