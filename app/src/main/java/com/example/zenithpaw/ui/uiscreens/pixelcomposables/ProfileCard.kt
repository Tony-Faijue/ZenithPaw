package com.example.zenithpaw.ui.uiscreens.pixelcomposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zenithpaw.R
import com.example.zenithpaw.ui.theme.ZenithPawTheme

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    avatar: ImageBitmap,
    avatarSize: Dp = 80.dp,
    avatarDescription: String,
    avatarPadding : Dp = 4.dp,
    avatarContentScale: ContentScale = ContentScale.Crop,
    subtitle: String,
    subtitleStyle: TextStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    ),
    title: String,
    titleStyle: TextStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    stats: @Composable (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null,
    subtitleColor: Color = MaterialTheme.colorScheme.onSecondary,
    titleColor: Color = MaterialTheme.colorScheme.onSecondary,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    shadowColor: Color = Color.Black,
    shadowOffset: Dp = 8.dp,
    borderWidth: Dp = 2.2.dp,
    cardShape: Shape = RectangleShape,
    paddingContentSize: Dp = 16.dp,
){
    Box(
        modifier = modifier
            .padding(bottom = shadowOffset, end = shadowOffset)
    ){
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffset, y = shadowOffset)
                .background(shadowColor, cardShape)
        )
        Box(
            modifier = Modifier
                .offset(x =0.dp, y = 0.dp)
                .background(backgroundColor, cardShape)
                .border(width = borderWidth, color = shadowColor, shape = cardShape)
        ) {
            Column(
                modifier = Modifier
                    .padding(paddingContentSize),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Box(
                    modifier = Modifier
                        .size(avatarSize)
                        .clip(CircleShape)
                        .border(width = borderWidth, color = shadowColor, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ){
                    Image(
                        bitmap = avatar,
                        contentDescription = avatarDescription,
                        contentScale = avatarContentScale,
                        modifier = Modifier.fillMaxSize()
                            .padding(avatarPadding)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = title,
                    color = titleColor,
                    fontSize = titleStyle.fontSize,
                    fontWeight = titleStyle.fontWeight,
                    fontFamily = titleStyle.fontFamily
                )

                Text(
                    text = subtitle,
                    color = subtitleColor,
                    fontSize = subtitleStyle.fontSize,
                    fontWeight = subtitleStyle.fontWeight,
                    fontFamily = subtitleStyle.fontFamily,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                if (stats != null) {
                    HorizontalDivider(
                        modifier = Modifier.padding(bottom = 16.dp),
                        thickness = 2.dp,
                        color = shadowColor
                    )
                    stats()
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (actions != null){
                    if (stats == null){
                        HorizontalDivider(
                            modifier = Modifier.padding(bottom = 16.dp),
                            thickness = 2.dp,
                            color = shadowColor
                        )
                    }
                    actions()
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "ProfileCard")
@Composable
fun ProfileCardPreview() {
    ZenithPawTheme(dynamicColor = false) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
            val myAvatarImage: ImageBitmap = ImageBitmap.imageResource(R.drawable.cat_run_jump)
            ProfileCard(
                title = "John Doe",
                subtitle = "JohnDoe@example.com",
                avatar = myAvatarImage,
                avatarDescription = "User Avatar",
                stats = {
                    // Example stats with simple gold example
                    Column(
                        modifier = Modifier.width(150.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Row(){
                            Text("Gold:")
                            Text("150")
                        }
                    }
                },
                actions = {
                    // Example actions with button example
                    Column(
                        modifier = Modifier.width(150.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        AppButton(
                            modifier = Modifier
                                .height(50.dp)
                                .width(180.dp),
                            text = "Edit Profile",
                            onClick = {},
                            textStyle = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            )
        }
    }
}

