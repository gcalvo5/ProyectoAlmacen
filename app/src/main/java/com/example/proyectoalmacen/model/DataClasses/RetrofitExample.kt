package com.example.proyectoalmacen.model.DataClasses

import com.google.gson.annotations.SerializedName

// El serialized name debe ser identico al nombre que nos llega en el json
data class RetrofitExample(
    @SerializedName("status") var status: String,
)
