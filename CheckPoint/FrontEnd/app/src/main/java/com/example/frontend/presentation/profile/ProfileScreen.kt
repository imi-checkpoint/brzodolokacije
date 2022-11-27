package com.example.frontend.presentation.profile

import android.provider.ContactsContract.Profile
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontend.R
import com.example.frontend.common.navigation.Screen
import com.example.frontend.domain.model.ProfileData
import com.example.frontend.presentation.map.MapWindow
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
            name =
            if(viewModel.savedUserId == viewModel.loginUserId)
                viewModel.username
            else
                "USERNAME"
            ,
            modifier = Modifier.padding(20.dp),
            navController = navController,
            viewModel = viewModel
        )
        Spacer(modifier = Modifier.height(4.dp))

        ProfileSection(navController, state, viewModel.savedUserId);
        Spacer(modifier = Modifier.height(25.dp))

        //ako nije moj profil
        if(viewModel.savedUserId != viewModel.loginUserId){
            ButtonSection(viewModel, modifier = Modifier.fillMaxWidth());
            Spacer(modifier = Modifier.height(25.dp))
        }

        //MAPA
        Log.d("MAP FOR USERID", viewModel.savedUserId.toString());
        if(viewModel.savedUserId != 0L){
            MapSection(viewModel.savedUserId)
        }
    }
}

@Composable
fun TopBar(
    name : String,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProfileViewModel
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

        if(viewModel.savedUserId == viewModel.loginUserId)
            IconButton(onClick = {
                navController.navigate(Screen.ProfileSettingsScreen.route)
            }) {
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
    navController: NavController,
    state : ProfileDataState,
    userId : Long,
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

            StatSection(navController, state, userId, modifier.weight(7f))

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
    navController : NavController,
    state : ProfileDataState,
    userId: Long,
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
            ProfileStat(numberText = state.profileData.postCount.toString(), text = "Posts",
            onClick = {})
            ProfileStat(numberText = state.profileData.followersCount.toString(), text = "Followers",
            onClick = {
                navController.navigate(Screen.UserListScreen.route + "/followers/${userId}");
            })
            ProfileStat(numberText = state.profileData.followingCount.toString(), text = "Following",
            onClick = {
                navController.navigate(Screen.UserListScreen.route + "/following/${userId}");
            })
        }
    }
}

@Composable
fun ProfileStat(
    numberText : String,
    text : String,
    modifier: Modifier = Modifier,
    onClick : () -> Unit = {}
)
{
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable{
                onClick()
            }
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
    viewModel : ProfileViewModel,
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
            viewModel = viewModel,
            modifier = Modifier
                .defaultMinSize(minWidth = minWidth)
                .height(height)
        )

        ActionButton(
            text = "Message",
            viewModel = viewModel,
            modifier = Modifier
                .defaultMinSize(minWidth = minWidth)
                .height(height)
        )
    }
}

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    viewModel : ProfileViewModel,
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
            .clickable{
                if(text == "Follow")
                {
                    viewModel.followUnfollowUser();
                }
                else if(text == "Message"){
                    //message this user

                }
            }
    ){
        if(text != null){
            Text(
                text =
                if(text == "Follow")
                {
                    if(viewModel.state.value.profileData?.amFollowing == true)
                        "Unfollow"
                    else
                        "Follow"
                }
                else text,
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

@Composable
fun MapSection(
    userId : Long
)
{
    Column(
        modifier = Modifier
            .padding(
                horizontal = 20.dp,
                vertical = 20.dp
            )
            .border(
                width = Dp.Hairline,
                color = Color.Transparent,
                shape = RoundedCornerShape(5.dp)
            )
    ) {
        MapWindow(userId)
    }
}