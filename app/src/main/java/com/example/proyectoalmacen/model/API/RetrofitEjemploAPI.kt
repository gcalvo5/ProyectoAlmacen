package com.example.proyectoalmacen.model.API

import com.example.proyectoalmacen.model.DataClasses.RetrofitExample
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface RetrofitEjemploAPI {
    @GET
    fun getRetrofitExample(@Url url: String): Response<RetrofitExample>
}