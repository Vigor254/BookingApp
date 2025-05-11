package com.vigor.hotelapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vigor.hotelapp.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vigor.hotelapp.viewmodel.HotelViewModel

@Composable
fun AdminScreen(navController: NavHostController, viewModel: HotelViewModel = hiltViewModel()) {
    var hotelName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var pricePerHour by remember { mutableStateOf("") }
    var imageResId by remember { mutableStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Admin Panel - Add Hotel", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = hotelName,
            onValueChange = { hotelName = it },
            label = { Text("Hotel Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = pricePerHour,
            onValueChange = { pricePerHour = it },
            label = { Text("Price per Hour") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Note: For now, imageResId is hardcoded or set to a fallback. Enhance this with a resource picker if needed.
        Button(
            onClick = {
                val price = pricePerHour.toDoubleOrNull() ?: 0.0
                if (hotelName.isNotBlank() && description.isNotBlank() && location.isNotBlank() && price > 0) {
                    viewModel.addHotel(
                        com.vigor.hotelapp.model.Hotel(
                            name = hotelName,
                            description = description,
                            location = location,
                            pricePerHour = price,
                            imageResId = R.drawable.hotel1 // Use a default or improve with dynamic selection
                        )
                    )
                    navController.navigate("home") {
                        popUpTo("admin") { inclusive = true }
                    }
                } else {
                    errorMessage = "Please fill all fields with valid data"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Hotel")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Hotel")
        }

        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Home Button at Bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}