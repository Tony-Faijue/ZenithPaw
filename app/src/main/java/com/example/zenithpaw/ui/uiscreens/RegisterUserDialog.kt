package com.example.zenithpaw.ui.uiscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.zenithpaw.R
import com.example.zenithpaw.ui.uievents.UserUiEvent
import com.example.zenithpaw.ui.uiscreens.pixelcomposables.AppButton
import com.example.zenithpaw.ui.uiscreens.pixelcomposables.AppTextField
import com.example.zenithpaw.ui.user.UserUiState

@Composable
fun RegisterUserDialog(
    state: UserUiState, onEvent: (UserUiEvent) -> Unit, modifier: Modifier
){
    Dialog(
        onDismissRequest = {
            onEvent(UserUiEvent.OnHideRegisterDialogClicked)
        },
        // Turns off Dialog width constraints to allow neobrutal offsets making sure they are not clipped
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ){
        val shadowOffset = 8.dp
        Box(
            modifier = modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = shadowOffset, end = shadowOffset)
        ){
            // NeoBrutal Thick Border
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = shadowOffset, y = shadowOffset)
                    .background(Color.Black, shape = RectangleShape)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, shape = RectangleShape)
                    .border(width = 6.dp, color = Color.White, shape = RectangleShape)
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
                            Text("Create Account", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                        IconButton(onClick = { onEvent(UserUiEvent.OnHideRegisterDialogClicked) }) {
                            Icon(painterResource(R.drawable.xmark_solid_full), contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)){
                        AppTextField(
                            value = state.name,
                            onValueChange = {
                                onEvent(UserUiEvent.OnNameChange(it))
                            },
                            label = "Name",
                            placeholder = "Your Name",
                            modifier = Modifier.fillMaxWidth().height(height = 80.dp)
                        )
                        AppTextField(
                            value = state.email,
                            onValueChange = {
                                onEvent(UserUiEvent.OnEmailChange(it))
                            },
                            label = "Email",
                            placeholder = "Your Email",
                            modifier = Modifier.fillMaxWidth().height(height = 80.dp),
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
                        textStyle = TextStyle(fontSize = 18.sp),
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.onPrimary
                    )
                }
        }
    }
}