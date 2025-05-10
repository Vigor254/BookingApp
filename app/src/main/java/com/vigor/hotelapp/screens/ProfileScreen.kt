package com.vigor.hotelapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            .padding(16.dp)
    ) {
        // Profile icon and user details
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Large profile icon aligned to the start
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile Icon",
                modifier = Modifier
                    .size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            // User email
            user?.let {
                Text(
                    text = "Welcome, ${it.id}",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Bookings section
        Text(text = "Your Bookings:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (bookings.isEmpty()) {
            Text(text = "No bookings yet.")
        } else {
            bookings.forEach { booking ->
                val hotel = viewModel.hotels.value.find { it.id == booking.hotelId }
                hotel?.let {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Hotel: ${it.name}", fontSize = 18.sp)
                            Text(text = "Location: ${it.location}")
                            Text(text = "Price per Hour: $${it.pricePerHour}")
                            Text(text = "Hours: ${booking.hours}")
                            Text(text = "Total Cost: $${booking.totalCost}")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Logout button
        Button(
            onClick = {
                viewModel.logout()
                navController.navigate("home") {
                    popUpTo("profile") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Home button at the bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Home",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        navController.navigate("home") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        }
                    },
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}