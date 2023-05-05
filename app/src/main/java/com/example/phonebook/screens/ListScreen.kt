package com.example.phonebook.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.phonebook.domain.model.ContactModel
import com.example.phonebook.domain.model.TagModel
import com.example.phonebook.routing.Screen
import com.example.phonebook.ui.components.AppDrawer
import com.example.phonebook.ui.components.Contact
import com.example.phonebook.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun ListScreen(viewModel: MainViewModel) {
    val contacts by viewModel.contactsNotInTrash.observeAsState(listOf())
    val tags by viewModel.tags.observeAsState(listOf())
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "PhoneBook",
                        color = MaterialTheme.colors.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch { scaffoldState.drawerState.open() }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Drawer Button"
                        )
                    }
                }
            )
        },
        drawerContent = {
            AppDrawer(
                currentScreen = Screen.List,
                closeDrawerAction = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onCreateNewContactClick() },
                contentColor = MaterialTheme.colors.background,
                content = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Note Button"
                    )
                }
            )
        }
    ) {
        if (contacts.isNotEmpty()) {
            ContactsList(
                tags = tags,
                contacts = contacts,
                onContactClick = { viewModel.onContactClick(it) }
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun ContactsList(
    tags: List<TagModel>,
    contacts: List<ContactModel>,
    onContactClick: (ContactModel) -> Unit
) {
    val tabs = listOf(TagModel(0, "All")).plus(tags)
    var tabIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEach { tab ->
                Tab(text = { Text(tab.name) },
                    selected = tabIndex == tab.id.toInt(),
                    onClick = { tabIndex = tab.id.toInt() }
                )
            }
        }
        val filteredContacts = if (tabIndex <= 0) contacts else contacts.filter { it.tag.id.toInt() == tabIndex }
        LazyColumn {
            items(
                count = filteredContacts.size,
            ) { index ->
                val contact = filteredContacts.sortedByDescending { it.id }[index]
                Contact(
                    contact = contact,
                    onContactClick = onContactClick,
                    isSelected = false
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun ContactsListPreview() {
    ContactsList(
        tags = listOf(
            TagModel(1, "Mobile"),
            TagModel(2, "Home"),
            TagModel(3, "Work")
        ),
        contacts = listOf(
            ContactModel(1, "John", "Smith", "0956090108"),
            ContactModel(2, "John", "Smith", "0956090108"),
            ContactModel(3, "John", "Smith", "0956090108")
        ),
        onContactClick = {}
    )
}