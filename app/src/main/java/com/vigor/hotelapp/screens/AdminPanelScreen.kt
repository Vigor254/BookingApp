package com.vigor.hotelapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vigor.hotelapp.model.Hotel
import com.vigor.hotelapp.viewmodel.HotelViewModel

@Composable
fun AdminPanelScreen(navController: NavHostController, viewModel: HotelViewModel = hiltViewModel()) {
    val hotels by viewModel.hotels
    var showAddHotelDialog by remember { mutableStateOf(false) }
    var newHotelName by remember { mutableStateOf(TextFieldValue("")) }
    var newHotelDescription by remember { mutableStateOf(TextFieldValue("")) }
    var newHotelPrice by remember { mutableStateOf(TextFieldValue("")) }
    var newHotelLocation by remember { mutableStateOf(TextFieldValue("")) }
    var newHotelImageResId by remember { mutableStateOf(0) } // Use Int for resource ID

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header with Back Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "Admin Panel",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { showAddHotelDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Hotel",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hotel List
        if (hotels.isEmpty()) {
            Text(text = "No hotels available", style = MaterialTheme.typography.bodyLarge)
        } else {
            hotels.forEach { hotel ->
                HotelItem(hotel = hotel, viewModel = viewModel)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    // Add Hotel Dialog
    if (showAddHotelDialog) {
        AlertDialog(
            onDismissRequest = { showAddHotelDialog = false },
            title = { Text("Add New Hotel") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newHotelName,
                        onValueChange = { newHotelName = it },
                        label = { Text("Name") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newHotelDescription,
                        onValueChange = { newHotelDescription = it },
                        label = { Text("Description") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newHotelPrice,
                        onValueChange = { newHotelPrice = it },
                        label = { Text("Price per Hour") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newHotelLocation,
                        onValueChange = { newHotelLocation = it },
                        label = { Text("Location") }
                    )
                    // Image selection (simplified to use a hardcoded resource ID for now)
                    Text("Image: Use drawable resource ID (e.g., R.drawable.hotel1)", style = MaterialTheme.typography.bodySmall)
                    // Note: In a real app, you might use a dropdown or picker for resource IDs
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val price = newHotelPrice.text.toDoubleOrNull() ?: 0.0
                        val newHotel = Hotel(
                            name = newHotelName.text,
                            description = newHotelDescription.text,
                            imageResId = newHotelImageResId, // Use imageResId instead of imageUrl
                            pricePerHour = price,
                            location = newHotelLocation.text
                        )
                        viewModel.addHotel(newHotel)
                        showAddHotelDialog = false
                        newHotelName = TextFieldValue("")
                        newHotelDescription = TextFieldValue("")
                        newHotelPrice = TextFieldValue("")
                        newHotelLocation = TextFieldValue("")
                        newHotelImageResId = 0
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(onClick = { showAddHotelDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun HotelItem(hotel: Hotel, viewModel: HotelViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Display hotel image using imageResId
            Image(
                painter = painterResource(id = hotel.imageResId), // Use imageResId instead of imageUrl
                contentDescription = hotel.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = hotel.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = hotel.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Price: $${hotel.pricePerHour}/hour",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Location: ${hotel.location}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { viewModel.bookHotel(hotel.id, 1) }, // Example booking action
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Book")
            }
        }
    }
}