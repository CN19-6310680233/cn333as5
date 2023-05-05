package com.example.phonebook.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.phonebook.domain.model.TagModel

@Composable
fun Tag(
    tag: TagModel,
) {
    val icons: List<ImageVector> = listOf(
        Icons.Rounded.AccountCircle,
        Icons.Rounded.Phone,
        Icons.Rounded.Home,
        Icons.Rounded.Place
    )
    Icon(
        icons[tag.id.toInt()],
        contentDescription = tag.name
    )
}