package com.example.zenithpaw.ui.uiscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.zenithpaw.R
import com.example.zenithpaw.ui.uievents.UserUiEvent
import com.example.zenithpaw.ui.user.UserUiState

@Composable
fun RegisterUserDialog(
    state: UserUiState, onEvent: (UserUiEvent) -> Unit, modifier: Modifier
){
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(UserUiEvent.OnHideRegisterDialogClicked)
        },
        confirmButton = {
            Button(onClick = {
                //Create account
                onEvent(UserUiEvent.OnCreateAccountClicked(state.name, state.email))
                //Hide the dialog
                onEvent(UserUiEvent.OnHideRegisterDialogClicked)
            }) {Text("Create Account")}
        },
        dismissButton = {
            IconButton(onClick = {
                onEvent(UserUiEvent.OnHideRegisterDialogClicked)
            }) { Icon(painterResource(R.drawable.xmark_solid_full), contentDescription = "Close") }
        },
        icon = {Icon(
            painterResource(R.drawable.user_solid_full),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp),
            tint = MaterialTheme.colorScheme.primary
        )},
        title = { Text("Create Account")},
        text = {
            Column (verticalArrangement = Arrangement.spacedBy(8.dp))
            {
                TextField(
                    value = state.name,
                    onValueChange = {
                        onEvent(UserUiEvent.OnNameChange(it))
                    },
                    label = { Text("Name")},
                    placeholder = { Text("Your Name")}
                )
                TextField(
                    value = state.email,
                    onValueChange = {
                        onEvent(UserUiEvent.OnEmailChange(it))
                    },
                    label = { Text("Email")},
                    placeholder = { Text("Your Email")},
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Unspecified,
                        autoCorrectEnabled = false,
                    )
                )
            }
        }
    )
}