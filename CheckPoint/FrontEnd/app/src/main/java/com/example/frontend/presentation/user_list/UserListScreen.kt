package com.example.frontend.presentation.user_list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.frontend.domain.model.User
import com.example.frontend.presentation.destinations.ProfileScreenDestination
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun UserListScreen(
    userTypeList: String,
    userId : Long,
    navigator : DestinationsNavigator,
    viewModel: UserListViewModel = hiltViewModel()
)
{
    val state = viewModel.state.value
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    var searchText by remember{ mutableStateOf("") }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = {
            if(searchText == "") viewModel.getAllUsers();
            else viewModel.searchUsers(searchText);
        }
    ){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ){
            UserListTopBar(modifier = Modifier.padding(20.dp), viewModel.typeOfUsers);
            Spacer(modifier = Modifier.height(5.dp))

            UserListSearchBar(searchText, onChange = {
                searchText = it
                if(searchText == "") viewModel.getAllUsers();
                else viewModel.searchUsers(searchText);
            });
            Spacer(modifier = Modifier.height(5.dp))

            if(state.isLoading){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            else if(state.users != null){
                UserList(userList = state.users, navigator = navigator)
            }

        }

    }

}

@Composable
fun UserListTopBar(
    modifier: Modifier = Modifier,
    userListType : String
)
{
    Row(
        modifier = modifier
            .fillMaxWidth()

    ){

        Text(userListType)

    }
}

@Composable
fun UserListSearchBar(
    searchText : String,
    onChange: (String) -> Unit = {}
){
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


    TextField(
        value = searchText,
        trailingIcon = if(searchText.isNotBlank()) trailingIconView else null,
        onValueChange = {
            onChange(it)
        },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null)},
        modifier = Modifier
            .fillMaxWidth(),
        placeholder = {
            Text("Search users")
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.LightGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun UserList(
    userList : List<User>?,
    navigator: DestinationsNavigator
){
    if(userList == null || userList.isEmpty()){
        Text(
            text = "No users found!",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
    else{
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = 10.dp,
                vertical = 10.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ){
            items(userList){
                user -> OneUser(user, navigator = navigator)
            }
        }

    }
}

@Composable
fun OneUser(
    user : User,
    navigator: DestinationsNavigator
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                Log.d(
                    "ID",
                    user.id.toString()
                )
                navigator.navigate(
                    ProfileScreenDestination(user.id)
                )
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ){

        Text(
            text = user.username,
            textAlign = TextAlign.Center
        )

        Text(
            text = user.email,
            textAlign = TextAlign.Center
        )

    }
}