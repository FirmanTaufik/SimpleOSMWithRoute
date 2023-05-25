package com.xample.routeosm.retrofit

import com.xample.routeosm.RouteModel
import retrofit2.Call
import retrofit2.http.*


interface GetService {
    @GET("{routeType}/route/v1/driving/{startEndLatLng}?alternatives=false&overview=full&steps=true")
    fun getRoute(
        @Path("routeType") routeType: String,
        @Path("startEndLatLng") lngOrigin: String,
    ) : Call<RouteModel>

}