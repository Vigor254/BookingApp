package com.vigor.hotelapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vigor.hotelapp.R
import com.vigor.hotelapp.data.HotelRepository
import com.vigor.hotelapp.model.Booking
import com.vigor.hotelapp.model.Hotel
import com.vigor.hotelapp.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HotelViewModel @Inject constructor(
    private val repository: HotelRepository
) : ViewModel() {

    // State for hotels
    val hotels = mutableStateOf<List<Hotel>>(emptyList())

    // State for current user
    val currentUser = mutableStateOf<User?>(null)

    // State for bookings
    val bookings = mutableStateOf<List<Booking>>(emptyList())

    init {
        // Load hotels when ViewModel is created
        viewModelScope.launch {
            Log.d("HotelViewModel", "Initializing ViewModel")
            val currentHotels = repository.getAllHotels()
            Log.d("HotelViewModel", "Retrieved hotels from DB: $currentHotels")
            if (currentHotels.isEmpty()) {
                Log.d("HotelViewModel", "Database is empty, inserting initial hotels")
                val initialHotels = listOf(
                    Hotel(
                        name = "Grand Hotel",
                        description = "A luxurious hotel with stunning views.",
                        imageResId = R.drawable.hotel1,
                        pricePerHour = 50.0,
                        location = "Downtown"
                    ),
                    Hotel(
                        name = "Ocean Breeze",
                        description = "A beachfront hotel with relaxing vibes.",
                        imageResId = R.drawable.hotel2,
                        pricePerHour = 40.0,
                        location = "Beachside"
                    ),
                    Hotel(
                        name = "Mountain Retreat",
                        description = "A cozy retreat in the mountains.",
                        imageResId = R.drawable.hotel3,
                        pricePerHour = 60.0,
                        location = "Mountains"
                    )
                )
                initialHotels.forEach { repository.insertHotel(it) }
                Log.d("HotelViewModel", "Inserted initial hotels: $initialHotels")
            }
            loadHotels()
        }
    }

    /** Loads hotels from the repository and applies a fallback for invalid imageResId */
    fun loadHotels() {
        viewModelScope.launch {
            Log.d("HotelViewModel", "Loading hotels")
            val loadedHotels = repository.getAllHotels().map { hotel ->
                if (hotel.imageResId == 0) {
                    Log.w("HotelViewModel", "Found invalid imageResId (0) for hotel ${hotel.name}, using fallback R.drawable.hotel1")
                    hotel.copy(imageResId = R.drawable.hotel1)
                } else {
                    hotel
                }
            }
            hotels.value = loadedHotels
            Log.d("HotelViewModel", "Hotels loaded: $loadedHotels")
        }
    }

    /** Adds a new hotel to the database with a fallback for invalid imageResId */
    fun addHotel(hotel: Hotel) {
        viewModelScope.launch {
            Log.d("HotelViewModel", "Adding hotel: $hotel")
            val hotelWithValidImage = if (hotel.imageResId == 0) {
                Log.w("HotelViewModel", "Invalid imageResId (0) for new hotel ${hotel.name}, using fallback R.drawable.hotel1")
                hotel.copy(imageResId = R.drawable.hotel1)
            } else {
                hotel
            }
            repository.insertHotel(hotelWithValidImage)
            loadHotels()
            Log.d("HotelViewModel", "Hotel added: $hotelWithValidImage")
        }
    }

    /** Books a hotel for the specified hours and refreshes the bookings list */
    fun bookHotel(hotelId: Int, hours: Int) {
        viewModelScope.launch {
            Log.d("HotelViewModel", "Booking hotel $hotelId for $hours hours")
            val hotel = repository.getHotelById(hotelId) ?: return@launch
            val totalCost = hotel.pricePerHour * hours
            val booking = Booking(
                userId = currentUser.value?.id ?: return@launch,
                hotelId = hotelId,
                hours = hours,
                totalCost = totalCost,
                bookingDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
            )
            repository.insertBooking(booking)
            loadBookings()
            Log.d("HotelViewModel", "Booking completed: $booking")
        }
    }

    /** Loads bookings for the current user */
    fun loadBookings() {
        viewModelScope.launch {
            val userId = currentUser.value?.id ?: run {
                Log.w("HotelViewModel", "No current user, skipping bookings load")
                return@launch
            }
            val loadedBookings = repository.getBookingsByUser(userId)
            bookings.value = loadedBookings
            Log.d("HotelViewModel", "Bookings loaded for user $userId: $loadedBookings")
        }
    }

    /** Logs in a user and loads their bookings */
    fun login(email: String, password: String): Boolean {
        val user = User(id = email, password = password, isAdmin = email == "admin@hotelapp.com")
        currentUser.value = user
        loadBookings()
        Log.d("HotelViewModel", "Logged in as: $email")
        return true
    }

    /** Signs up a new user */
    fun signup(email: String, password: String): Boolean {
        val user = User(id = email, password = password)
        currentUser.value = user
        Log.d("HotelViewModel", "Signed up as: $email")
        return true
    }

    /** Logs out the current user and clears bookings */
    fun logout() {
        currentUser.value = null
        bookings.value = emptyList()
        Log.d("HotelViewModel", "Logged out")
    }
}