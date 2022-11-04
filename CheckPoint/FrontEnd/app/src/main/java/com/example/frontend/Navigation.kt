import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frontend.Screen
import com.example.frontend.activities.LocationScreen
import com.example.frontend.activities.LoginScreen
import com.example.frontend.activities.MainScreen
import com.example.frontend.activities.RegisterScreen

@Composable
fun Navigation()
{
    val navController = rememberNavController();
    NavHost(navController = navController, startDestination = Screen.MainScreen.route){
        composable(route = Screen.MainScreen.route){
            MainScreen(navController = navController)
        }
        composable(route = Screen.LoginScreen.route){
            LoginScreen(navController = navController)
        }
        composable(route = Screen.RegisterScreen.route){
            RegisterScreen(navController = navController)
        }
        composable(route = Screen.SearchLocationScreen.route){
            LocationScreen(navController = navController)
        }
    }
}

