package com.example.zenithpaw.ui.uiscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.zenithpaw.R
import com.example.zenithpaw.ui.uievents.UserUiEvent
import com.example.zenithpaw.ui.uiscreens.pixelcomposables.AppButton
import com.example.zenithpaw.ui.user.UserUiState

@Composable
fun RegisterUserDialog(
    state: UserUiState, onEvent: (UserUiEvent) -> Unit, modifier: Modifier
){
    Dialog(
        onDismissRequest = {
            onEvent(UserUiEvent.OnHideRegisterDialogClicked)
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = (-6).dp, y = (-6).dp)
                .background(MaterialTheme.colorScheme.surface, shape = RectangleShape)
                .border(width = 6.dp, color = Color.Black, shape = RectangleShape)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        painterResource(R.drawable.user_solid_full),
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Create Account", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                }
                IconButton(onClick = { onEvent(UserUiEvent.OnHideRegisterDialogClicked) }) {
                    Icon(painterResource(R.drawable.xmark_solid_full), contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurface)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)){
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
            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                text = "Create Account",
                onClick = {
                    //Create account
                    onEvent(UserUiEvent.OnCreateAccountClicked(state.name, state.email))
                    //Hide the dialog
                    onEvent(UserUiEvent.OnHideRegisterDialogClicked)
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                fontSize = 18.sp,
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}