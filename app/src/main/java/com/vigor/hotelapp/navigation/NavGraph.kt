import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vigor.hotelapp.screens.BookingScreen
import com.vigor.hotelapp.screens.HomeScreen
import com.vigor.hotelapp.screens.LoginScreen
import com.vigor.hotelapp.screens.ProfileScreen
import com.vigor.hotelapp.screens.SignupScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable(
            "booking?hotelId={hotelId}",
            arguments = listOf(navArgument("hotelId") { defaultValue = 0 })
        ) { backStackEntry ->
            BookingScreen(navController)
        }
    }
}