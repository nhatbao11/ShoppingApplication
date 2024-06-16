package com.project.ecommerceapplication.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.project.ecommerceapplication.model.remote.entity.Address
import com.project.ecommerceapplication.model.repository.AddressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManageAddressViewModel(val context: Application) : AndroidViewModel(context) {
    private val addressRepository = AddressRepository

    private var userId = ""
    fun setUser(userId: String) {
        this.userId = userId
    }

    var contactName by mutableStateOf("")
    var unit by mutableStateOf("")
    var building by mutableStateOf("")
    var zone by mutableStateOf("")
    var street by mutableStateOf("")
    var contactNumber by mutableStateOf("")
    var poBox by mutableStateOf("")


    var nameError by mutableStateOf(false)
    var unitError by mutableStateOf(false)
    var buildingError by mutableStateOf(false)
    var zoneError by mutableStateOf(false)
    var streetError by mutableStateOf(false)
    var numberError by mutableStateOf(false)
    var poBoxError by mutableStateOf(false)

    private var error = false

    fun validateAddingAddress(): Boolean {
        if (contactName.isEmpty()) nameError = true
        if (unit.isEmpty()) unitError = true
        if (building.isEmpty()) buildingError = true
        if (zone.isEmpty()) zoneError = true
        if (street.isEmpty()) streetError = true
        if (contactNumber.isEmpty()) numberError = true
        if (poBox.isEmpty()) poBoxError = true
        error =
            nameError || unitError || buildingError || zoneError || streetError || numberError || poBoxError
        return !error
    }

    fun resetFields() {
        contactName = ""
        unit = ""
        building = ""
        zone = ""
        street = ""
        contactNumber = ""
        poBox = ""
    }

    fun setCurrentAddress(addressId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val address = addressRepository.getAddressById(addressId)
            address?.let {
                contactName = address.name
                unit = address.unit.toString()
                building = address.building.toString()
                zone = address.zone.toString()
                street = address.street.toString()
                contactNumber = address.phone
                poBox = address.poBox.toString()
            }
        }
    }

    fun addAddress() {
        val address = Address(
            uid = userId,
            name = contactName,
            unit = unit.toInt(),
            building = building.toInt(),
            street = street.toInt(),
            zone = zone.toInt(),
            poBox = poBox.toInt(),
            phone = contactNumber
        )

        viewModelScope.launch(Dispatchers.IO) {
            addressRepository.insertAddress(address)
        }
    }

    fun updateAddress(addressId: String) {
        val address = Address(
            id = addressId,
            uid = userId,
            name = contactName,
            unit = unit.toInt(),
            building = building.toInt(),
            street = street.toInt(),
            zone = zone.toInt(),
            poBox = poBox.toInt(),
            phone = contactNumber
        )

        viewModelScope.launch(Dispatchers.IO) {
            addressRepository.updateAddress(address)
        }
    }
}