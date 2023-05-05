package com.example.phonebook.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.example.phonebook.routing.PhoneBookRouter
import com.example.phonebook.routing.Screen
import com.example.phonebook.viewmodel.MainViewModel
import com.example.phonebook.domain.model.ContactModel
import com.example.phonebook.domain.model.NEW_CONTACT_ID
import com.example.phonebook.domain.model.TagModel
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun UpdateScreen(viewModel: MainViewModel) {
    val contactEntry by viewModel.contactEntry.observeAsState(ContactModel())

    val tagsEntry: List<TagModel> by viewModel.tags.observeAsState(listOf())

    val bottomDrawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)

    val coroutineScope = rememberCoroutineScope()

    val moveContactToTrashDialogShownState = rememberSaveable { mutableStateOf(false) }

    BackHandler {
        if (bottomDrawerState.isOpen) {
            coroutineScope.launch { bottomDrawerState.close() }
        } else {
            PhoneBookRouter.navigateTo(Screen.Update)
        }
    }

    Scaffold(
        topBar = {
            val isEditingMode: Boolean = contactEntry.id != NEW_CONTACT_ID
            SaveContactTopAppBar(
                isEditingMode = isEditingMode,
                onBackClick = { PhoneBookRouter.navigateTo(Screen.List) },
                onSaveContactClick = { viewModel.saveContact(contactEntry) },
                onDeleteContactClick = {
                    moveContactToTrashDialogShownState.value = true
                }
            )
        }
    ) {
        SaveContactContent(
            contact = contactEntry,
            tags = tagsEntry,
            onContactChange = { updateContactEntry ->
                viewModel.onContactEntryChange(updateContactEntry)
            }
        )

        if (moveContactToTrashDialogShownState.value) {
            AlertDialog(
                onDismissRequest = {
                    moveContactToTrashDialogShownState.value = false
                },
                title = {
                    Text("Move contact to the trash?")
                },
                text = {
                    Text(
                        "Are you sure you want to " +
                                "move this contact to the trash?"
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.moveContactToTrash(contactEntry)
                    }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        moveContactToTrashDialogShownState.value = false
                    }) {
                        Text("Dismiss")
                    }
                }
            )
        }
    }
}

@Composable
fun SaveContactTopAppBar(
    isEditingMode: Boolean,
    onBackClick: () -> Unit,
    onSaveContactClick: () -> Unit,
    onDeleteContactClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Contact",
                color = MaterialTheme.colors.onPrimary
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Button",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
        actions = {
            IconButton(onClick = onSaveContactClick) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save Button",
                    tint = MaterialTheme.colors.onPrimary
                )
            }

            if (isEditingMode) {
                IconButton(onClick = onDeleteContactClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Button",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    )
}

@Composable
private fun SaveContactContent(
    contact: ContactModel,
    tags: List<TagModel>,
    onContactChange: (ContactModel) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        ContentTextField(
            label = "First Name",
            text = contact.firstName,
            onTextChange = { newFirstName ->
                onContactChange.invoke(contact.copy(firstName = newFirstName))
            }
        )

        ContentTextField(
            label = "Last Name",
            text = contact.lastName,
            onTextChange = { newLastName ->
                onContactChange.invoke(contact.copy(lastName = newLastName))
            }
        )

        ContentTextField(
            label = "Phone Number",
            text = contact.number,
            onTextChange = { newNumber ->
                onContactChange.invoke(contact.copy(number = newNumber))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // Dropdown
        var expanded by remember { mutableStateOf(false) }
        val selectedId: Long = contact.tag.id
        val onOpen = { expanded = true }
        val onClose = { expanded = false }
        ContentDropdown(
            tags.find { t -> t.id == selectedId }?.name ?: "N/A",
            expanded,
            onOpen,
            onClose
        ) {
            tags.forEach { s ->
                DropdownMenuItem(
                    onClick = {
                        onContactChange.invoke(contact.copy(tag = contact.tag.copy(id = s.id)))
                        onClose()
                    }
                ) {
                    Text(text = s.name)
                }
            }
        }
    }
}

@Composable
private fun ContentDropdown(
    defaultSelected: String,
    expanded: Boolean,
    onOpen: () -> Unit,
    onClose: () -> Unit,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    properties: PopupProperties = PopupProperties(focusable = true),
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        Card(
            modifier = Modifier.padding(8.dp, top = 16.dp),
            elevation = 4.dp,
        ) {
            Text(
                defaultSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onOpen)
                    .padding(16.dp)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onClose,
            modifier = Modifier.fillMaxWidth(),
            offset = offset,
            properties = properties,
            content = content
        )
    }
}

@Composable
private fun ContentTextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    onTextChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions()
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface
        ),
        keyboardOptions = keyboardOptions
    )
}
