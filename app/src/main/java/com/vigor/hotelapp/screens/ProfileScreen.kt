package com.vigor.hotelapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vigor.hotelapp.viewmodel.HotelViewModel

@Composable
fun ProfileScreen(navController: NavHostController, viewModel: HotelViewModel = hiltViewModel()) {
    val bookings by viewModel.bookings
    val user = viewModel.currentUser.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome, ${user?.id}", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Your Bookings:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        bookings.forEach { booking ->
            val hotel = viewModel.hotels.value.find { it.id == booking.hotelId }
            hotel?.let {
                Column {
                    Text(text = "Hotel: ${it.name}")
                    Text(text = "Location: ${it.location}")
                    Text(text = "Price per Hour: $${it.pricePerHour}")
                    Text(text = "Hours: ${booking.hours}")
                    Text(text = "Total Cost: $${booking.totalCost}")
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
        if (bookings.isEmpty()) {
            Text(text = "No bookings yet.")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.logout(); navController.navigate("home") { popUpTo("profile") { inclusive = true } } },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}