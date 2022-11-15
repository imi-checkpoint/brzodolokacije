package com.example.frontend.presentation.profile

import android.provider.ContactsContract.Profile
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontend.R
import com.example.frontend.presentation.profile.components.ProfileDataState

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
)
{
    val state = viewModel.state.value

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopBar(
            name = "Ime korisnika",
            modifier = Modifier.padding(20.dp),
            navController = navController
        )
        Spacer(modifier = Modifier.height(4.dp))

        ProfileSection(state);
        Spacer(modifier = Modifier.height(25.dp))

        ButtonSection(modifier = Modifier.fillMaxWidth());
        Spacer(modifier = Modifier.height(25.dp))
    }
}

@Composable
fun TopBar(
    name : String,
    modifier: Modifier = Modifier,
    navController: NavController
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxWidth()
    ){
        IconButton(onClick = {
            navController.popBackStack()
        }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = modifier.size(24.dp),
            )
        }

        Text(
            text = name,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        
        IconButton(onClick = { /*user profile edit*/}) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun ProfileSection(
    state : ProfileDataState,
    modifier: Modifier = Modifier,
)
{
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {

            RoundImage(
                image = painterResource(id = R.drawable.ic_logo),
                modifier = Modifier
                    .size(100.dp)
                    .weight(3f) //da zauzima 3 sirine ovog reda
            )
            
            Spacer(modifier = Modifier.width(16.dp))

            StatSection(state, modifier.weight(7f))

        }
    }
}

@Composable
fun RoundImage(
    image : Painter,
    modifier: Modifier = Modifier
)
{
    Image(
        painter = image,
        contentDescription = null,
        modifier = modifier
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = CircleShape
            )
            .padding(3.dp)
            .clip(CircleShape)
    )
}

@Composable
fun StatSection(
    state : ProfileDataState,
    modifier: Modifier = Modifier
){
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
    ){
        if(state.error.isNotBlank()){
            Text("Error!");
        }

        if(state.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterVertically))
        }
        else if(state.profileData != null){
            ProfileStat(numberText = state.profileData.postCount.toString(), text = "Posts")
            ProfileStat(numberText = state.profileData.followersCount.toString(), text = "Followers")
            ProfileStat(numberText = state.profileData.followingCount.toString(), text = "Following")
        }
    }
}

@Composable
fun ProfileStat(
    numberText : String,
    text : String,
    modifier: Modifier = Modifier
)
{
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    )    {
        Text(
            text = numberText,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = text)
    }
}

@Composable
fun ButtonSection(
    modifier: Modifier = Modifier
){
    val minWidth = 120.dp
    val height = 35.dp

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxWidth()
//            .background(color = Color.Magenta)

    ){
        ActionButton(
            text = "Follow",
            icon = Icons.Default.Add,
            modifier = Modifier
                .defaultMinSize(minWidth = minWidth)
                .height(height)
        )

        ActionButton(
            text = "Message",
            modifier = Modifier
                .defaultMinSize(minWidth = minWidth)
                .height(height)
        )
    }
}

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    text : String? = null,
    icon : ImageVector? = null
){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(6.dp)
    ){
        if(text != null){
            Text(
                text = text,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }

        if(icon != null){
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
}