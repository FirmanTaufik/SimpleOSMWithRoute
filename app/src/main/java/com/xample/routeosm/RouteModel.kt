package com.xample.routeosm

import com.google.gson.annotations.SerializedName

data class RouteModel(
    @SerializedName("code"      ) var code      : String?              = null,
    @SerializedName("routes"    ) var routes    : ArrayList<Routes>    = arrayListOf(),
    @SerializedName("waypoints" ) var waypoints : ArrayList<Waypoints> = arrayListOf()
){

    data class Maneuver (

        @SerializedName("bearing_after"  ) var bearingAfter  : Int?              = null,
        @SerializedName("bearing_before" ) var bearingBefore : Int?              = null,
        @SerializedName("location"       ) var location      : ArrayList<Double> = arrayListOf(),
        @SerializedName("modifier"       ) var modifier      : String?           = null,
        @SerializedName("type"           ) var type          : String?           = null

    )

    data class Intersections (

        @SerializedName("out"      ) var out      : Int?               = null,
        @SerializedName("entry"    ) var entry    : ArrayList<Boolean> = arrayListOf(),
        @SerializedName("bearings" ) var bearings : ArrayList<Int>     = arrayListOf(),
        @SerializedName("location" ) var location : ArrayList<Double>  = arrayListOf()

    )
    data class Steps (

        @SerializedName("geometry"      ) var geometry      : String?                  = null,
        @SerializedName("maneuver"      ) var maneuver      : Maneuver?                = Maneuver(),
        @SerializedName("mode"          ) var mode          : String?                  = null,
        @SerializedName("driving_side"  ) var drivingSide   : String?                  = null,
        @SerializedName("name"          ) var name          : String?                  = null,
        @SerializedName("intersections" ) var intersections : ArrayList<Intersections> = arrayListOf(),
        @SerializedName("weight"        ) var weight        : Double?                  = null,
        @SerializedName("duration"      ) var duration      : Double?                  = null,
        @SerializedName("distance"      ) var distance      : Double?                  = null

    )
    data class Legs (

        @SerializedName("steps"    ) var steps    : ArrayList<Steps> = arrayListOf(),
        @SerializedName("summary"  ) var summary  : String?          = null,
        @SerializedName("weight"   ) var weight   : Double?          = null,
        @SerializedName("duration" ) var duration : Double?          = null,
        @SerializedName("distance" ) var distance : Double?          = null

    )
    data class Routes (

        @SerializedName("geometry"    ) var geometry   : String?         = null,
        @SerializedName("legs"        ) var legs       : ArrayList<Legs> = arrayListOf(),
        @SerializedName("weight_name" ) var weightName : String?         = null,
        @SerializedName("weight"      ) var weight     : Double?         = null,
        @SerializedName("duration"    ) var duration   : Double?         = null,
        @SerializedName("distance"    ) var distance   : Double?         = null

    )
    data class Waypoints (

        @SerializedName("hint"     ) var hint     : String?           = null,
        @SerializedName("distance" ) var distance : Double?           = null,
        @SerializedName("name"     ) var name     : String?           = null,
        @SerializedName("location" ) var location : ArrayList<Double> = arrayListOf()

    )
}
