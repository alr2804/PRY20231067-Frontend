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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.upc.pry20231067.ArActivity
import com.upc.pry20231067.R
import com.upc.pry20231067.common.Utils.showToast
import com.upc.pry20231067.databinding.FragmentMapsBinding



class MapsFragment : Fragment() , OnMapReadyCallback {

    private var _binding: FragmentMapsBinding? = null

    private val binding get() = _binding!!


    private var currentLocation: LatLng? = null


    //map variables
    private lateinit var mMap: GoogleMap
    private var myLocationMarker: Marker? = null

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {

            currentLocation = LatLng(location.latitude, location.longitude)


            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation!!, 15f))
            updateOrSetMyLocationMarker(location)
        }

        // Other required methods...
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

//        assign button to create arview activity
        binding.arButton.setOnClickListener {

            //validation of zone
            val currentLocation = getCurrentLocation()
            val variationZone = 1

            if(currentLocation != null){
                //get all locations
                

                var topLeft = LatLng(currentLocation.latitude - variationZone, currentLocation.longitude -variationZone)     // Replace with the top-left corner of your bounding box
                var bottomRight = LatLng(currentLocation.latitude + variationZone, currentLocation.latitude + variationZone) // Replace with the bottom-right corner of your bounding box

//                topLeft = LatLng(1.0, 1.0)
//                bottomRight = LatLng(1.0, 1.0)

                if (isWithinBoundingBox(currentLocation, topLeft, bottomRight)) {
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



}