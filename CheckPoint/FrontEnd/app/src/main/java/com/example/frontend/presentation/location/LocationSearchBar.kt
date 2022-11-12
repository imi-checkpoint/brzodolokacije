package com.example.frontend.presentation.location

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LocationSearchBar(
    searchText : String,
    onChange: (String) -> Unit = {}
)
{
    var searchTextChange by remember {
        mutableStateOf(searchText)
    }

    val trailingIconView = @Composable {
        IconButton(onClick = {
            onChange("") //isprazni search text
        }) {
            Icon(
                Icons.Default.Clear,
                contentDescription ="",
                tint = Color.Black
            )
        }
    }
    
    OutlinedTextField(
        value = searchText,
        trailingIcon = if(searchText.isNotBlank()) trailingIconView else null,
        onValueChange = {
            onChange(it)
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text("Search locations")
        }
    )

}