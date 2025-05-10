package com.vigor.hotelapp.data

import com.vigor.hotelapp.data.local.HotelDao
import com.vigor.hotelapp.data.local.HotelEntity

import com.vigor.hotelapp.data.local.toHotelEntity
import com.vigor.hotelapp.model.Booking
import com.vigor.hotelapp.model.Hotel

class HotelRepository(private val hotelDao: HotelDao) {

    suspend fun insertHotel(hotel: Hotel) {
        hotelDao.insertHotel(hotel.toHotelEntity()) // Uses toHotelEntity()
    }

    suspend fun getAllHotels(): List<Hotel> {
        return hotelDao.getAllHotels().map { it.toHotel() } // Uses toHotel()
    }

    suspend fun getHotelById(id: Int): Hotel? {
        return hotelDao.getHotelById(id)?.toHotel() // Uses toHotel()
    }

    suspend fun insertBooking(booking: Booking) {
        hotelDao.insertBooking(booking)
    }

    suspend fun getBookingsByUser(userId: String): List<Booking> {
        return hotelDao.getBookingsByUser(userId)
    }
}