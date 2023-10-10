package com.upc.pry20231067.ui.maps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.upc.pry20231067.ArActivity
import com.upc.pry20231067.R
import com.upc.pry20231067.common.Utils.showToast
import com.upc.pry20231067.data.Place.Place
import com.upc.pry20231067.databinding.FragmentMapsBinding
import com.upc.pry20231067.models.PlaceResponse
import com.upc.pry20231067.services.ApiService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


data class NamedLocation(val latLng: LatLng, val name: String)

class MapsFragment : Fragment() , OnMapReadyCallback {

    private val placeList = mutableListOf<Place>()
    private var _binding: FragmentMapsBinding? = null

    private val binding get() = _binding!!


    private var currentLocation: LatLng? = null

    private var locationMarkers: MutableList<Marker>? = null


    // Sample locations from Peru
//    private val locationsList : MutableList<NamedLocation> = mutableListOf(
//        NamedLocation(LatLng(-12.0464, -77.0428), "Lima"),
//        NamedLocation(LatLng(-13.5319, -71.9675), "Cusco"),
//        NamedLocation(LatLng(-16.4090, -71.5375), "Arequipa")
//    )



    //map variables
    private lateinit var mMap: GoogleMap
    private var myLocationMarker: Marker? = null

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {

            currentLocation = LatLng(location.latitude, location.longitude)

             val test = LatLng(currentLocation!!.latitude+ 1, currentLocation!!.longitude + 1)

//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation!!, 15f))
//            updateOrSetMyLocationMarker(location)

            updateOrSetLocationMarkers(placeList);
        }


    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        getPlaces()

//        assign button to create arview activity
        binding.arButton.setOnClickListener {

            //validation of zone
            val currentLocation = getCurrentLocation()
            val variationZone = 1

            if(currentLocation != null){
                var valid = false

                placeList.add(Place("", "My Position", "Esta es mi posición", "", currentLocation.longitude, currentLocation.latitude))
//                locationsList.add(NamedLocation(LatLng(currentLocation.latitude, currentLocation.longitude), "My Position"))

                //get all locations
//                for (place in locationsList){
                for (place in placeList){
                    var topLeft = LatLng(place.latitude - variationZone, place.longitude -variationZone)     // Replace with the top-left corner of your bounding box
                    var bottomRight = LatLng(place.latitude + variationZone, place.latitude + variationZone) // Replace with the bottom-right corner of your bounding box
                    if (isWithinBoundingBox(currentLocation, topLeft, bottomRight)) valid = true;
                }

//                var topLeft = LatLng(currentLocation.latitude - variationZone, currentLocation.longitude -variationZone)     // Replace with the top-left corner of your bounding box
//                var bottomRight = LatLng(currentLocation.latitude + variationZone, currentLocation.latitude + variationZone) // Replace with the bottom-right corner of your bounding box

//                topLeft = LatLng(1.0, 1.0)
//                bottomRight = LatLng(1.0, 1.0)

                if (valid) {
                    val intent = Intent(requireContext(), ArActivity::class.java)
                    startActivity(intent)
                } else {
                    // Handle the scenario when the location is outside the bounding box, if needed.
                    showToast(requireContext(), "outside of ARZone")
                }

            }
            else {
                showToast(requireContext(), "Not able to get location")
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

//        updateOrSetLocationMarkers(locationsList);
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Adjust location button if needed
        val locationButton = (view?.findViewById<View>(Integer.parseInt("1"))?.parent as View?)?.findViewById<View>(Integer.parseInt("2"))
        val layoutParams = locationButton?.layoutParams as? RelativeLayout.LayoutParams
        layoutParams?.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        layoutParams?.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        layoutParams?.setMargins(0, 0, 30, 180)

        setMyLocationEnabled()
    }

    private fun updateOrSetMyLocationMarker(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        if (myLocationMarker == null) {
            myLocationMarker = mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("My Location")
            )
        } else {
            myLocationMarker?.position = latLng
        }
    }

    private fun updateOrSetLocationMarkers(locations: MutableList<Place> = placeList) {
        // Ensure we have an initialized list of markers
        if (locationMarkers == null) {
            locationMarkers = mutableListOf()
        }

        // Update existing markers or add new ones
        for (i in locations.indices) {
            val placeLocation = locations[i]
            if (i < locationMarkers!!.size) {
                // Update existing marker
                locationMarkers!![i].position = LatLng(placeLocation.latitude, placeLocation.longitude)
                locationMarkers!![i].title = placeLocation.name
            } else {
                // Add new marker
                val marker = mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(placeLocation.latitude, placeLocation.longitude))
                        .title(placeLocation.name)
                )
                if (marker != null) {
                    locationMarkers!!.add(marker)
                }
            }
        }

        // Remove excess markers if there are any
        val excess = locationMarkers!!.size - locations.size
        if (excess > 0) {
            for (i in 1..excess) {
                locationMarkers!![locationMarkers!!.size - i].remove()
            }
            locationMarkers = locationMarkers!!.subList(0, locationMarkers!!.size - excess)
        }
    }

    private fun setMyLocationEnabled() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

            mMap.isMyLocationEnabled = true

            var locationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager?
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10f, locationListener)
        } else {
            Toast.makeText(requireContext(), "Location permission not granted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isWithinBoundingBox(point: LatLng, topLeft: LatLng, bottomRight: LatLng): Boolean {
        return point.latitude in topLeft.latitude..bottomRight.latitude &&
                point.longitude in topLeft.longitude..bottomRight.longitude
    }

    private fun getCurrentLocation(): LatLng? {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Handle the case where you don't have permission
            return null
        }

        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        return if (location != null) LatLng(location.latitude, location.longitude) else null
    }

    private fun getRetrofit(): Retrofit {
        // Crear una instancia de OkHttpClient personalizada
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS) // Configura el tiempo de espera de lectura
            .connectTimeout(30, TimeUnit.SECONDS) // Configura el tiempo de espera de conexión
            .build()

        return Retrofit.Builder().baseUrl("https://api-ar-app.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    private fun getPlaces(){
        val call = getRetrofit().create(ApiService::class.java).getPlaces()
        call.enqueue(object: Callback<PlaceResponse> {
            override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>){
                if(response.isSuccessful){
                    val places = response.body()
                    val placeData = places?.data ?: emptyList()
                    binding.textViewMaps.text = placeData.toString()
                    placeList.clear()
                    placeList.addAll(placeData)
                }
            }
            override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }



}