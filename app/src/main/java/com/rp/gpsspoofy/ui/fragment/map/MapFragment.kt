package com.rp.gpsspoofy.ui.fragment.map


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Bundle
import android.os.SystemClock
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.dev.rahul.imago.base.BaseFragment
import com.patloew.rxlocation.RxLocation
import com.rp.gpsspoofy.R
import com.rp.gpsspoofy.util.LocationNotFoundException
import com.rp.util.toast.ERROR
import com.rp.util.toast.RToast
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_map.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class MapFragment : BaseFragment(), MapMVP.IView {

    val TAG = MapFragment::class.java.simpleName

    private lateinit var rxLocation: RxLocation

    override val layoutRes: Int
        get() = R.layout.fragment_map

    override fun onCreate(savedInstanceState: Bundle?) {
        Configuration.getInstance().load(
            requireContext().applicationContext,
            PreferenceManager.getDefaultSharedPreferences(requireContext().applicationContext)
        )
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showPermissionDialog()
    }

    override fun onRefresh() {}

    private fun showPermissionDialog() {
        RxPermissions(this)
            .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            .subscribe {
                if (it) loadMap() else showOnPermissionRejected()
            }
    }

    private fun showOnPermissionRejected() {
        AlertDialog.Builder(requireContext())
            .setTitle("Alert!")
            .setMessage("These permission required to use this app.")
            .setPositiveButton("Grant") { _, _ ->
                showPermissionDialog()
            }
            .setNegativeButton("Cancel") { _, _ -> }
    }

    @SuppressLint("MissingPermission")
    private fun loadMap() {

        mapView.apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setBuiltInZoomControls(false)
            setMultiTouchControls(true)
        }
        mapView.controller.setZoom(17.0)

        val locationOverlayView = MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), mapView)
        locationOverlayView.enableMyLocation()
        mapView.overlays.add(locationOverlayView)

        rxLocation = RxLocation(requireContext())
        rxLocation.location()
            .isLocationAvailable
            .flatMap {
                if (it) rxLocation.location().lastLocation().toSingle()
                else throw LocationNotFoundException("Location not found")
            }
            .subscribeBy(
                onSuccess = { location ->
                    Log.e(TAG, "latitude : ${location.latitude} longitude ${location.longitude}")
                    mapView.controller.apply {
                        setCenter(GeoPoint(location.latitude, location.longitude))
                    }
                },
                onError = {
                    RToast.text(it.message ?: "").type(ERROR).show()
                }
            )


        /*mapView.addMapListener(object : MapListener {
            override fun onZoom(event: ZoomEvent?): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onScroll(event: ScrollEvent?): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

        val mapEventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun longPressHelper(p: GeoPoint?): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })*/
    }

    override fun showMockLocation(latitude: Double, longitude: Double) {
        val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE
        val mocLocationProvider = lm.getBestProvider(criteria, true)
        if (mocLocationProvider == null) {
            RToast.text("No location provider found!")
                .type(ERROR).show()
            return
        }
        lm.addTestProvider(
            mocLocationProvider, false, false,
            false, false, true, true, true, 0, 5
        )
        lm.setTestProviderEnabled(mocLocationProvider, true)

        val loc = Location(mocLocationProvider)
        val mockLocation = Location(LocationManager.GPS_PROVIDER) // a string
        mockLocation.latitude = latitude  // double
        mockLocation.longitude = longitude
        mockLocation.altitude = loc.altitude
        mockLocation.time = System.currentTimeMillis()
        mockLocation.accuracy = 3.0f
        mockLocation.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()

        lm.setTestProviderStatus(
            mocLocationProvider,
            LocationProvider.AVAILABLE,
            null, System.currentTimeMillis()
        );

        lm.setTestProviderLocation(mocLocationProvider, mockLocation)
        RToast.text("Working").show()
    }

    override fun setNewLocationOnMap(latitude: Double, longitude: Double) {
        //your items
        val items = ArrayList<OverlayItem>()
        items.add(OverlayItem("Title", "Description", GeoPoint(latitude, longitude))) // Lat/Lon decimal degrees

        //the overlay
        val mOverlay = ItemizedOverlayWithFocus<OverlayItem>(requireContext(), items,
            object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
                override fun onItemSingleTapUp(index: Int, item: OverlayItem): Boolean {
                    //do something
                    return true
                }

                override fun onItemLongPress(index: Int, item: OverlayItem): Boolean {
                    return false
                }
            })
        mOverlay.setFocusItemsOnTap(true)
        mapView.overlays.add(mOverlay)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

}
