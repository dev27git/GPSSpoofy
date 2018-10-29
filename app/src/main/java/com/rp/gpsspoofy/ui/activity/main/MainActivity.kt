package com.rp.gpsspoofy.ui.activity.main

import android.annotation.SuppressLint
import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Bundle
import android.os.SystemClock
import android.preference.PreferenceManager
import android.provider.Settings
import android.widget.Toast
import com.rp.basefiles.BaseActivity
import com.rp.gpsspoofy.R
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class MainActivity : BaseActivity() {
    private lateinit var rxPermission: RxPermissions

    private lateinit var location: Location

    override val layoutRes: Int
        get() = R.layout.activity_main

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {

        Configuration.getInstance().load(
                applicationContext,
                PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        super.onCreate(savedInstanceState)

        loadMap()

        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE
        val mocLocationProvider = lm.getBestProvider(criteria, true)
        if (mocLocationProvider == null) {
            Toast.makeText(applicationContext, "No location provider found!", Toast.LENGTH_SHORT).show()
            return
        }
        lm.addTestProvider(
            mocLocationProvider, false, false,
            false, false, true, true, true, 0, 5
        )
        lm.setTestProviderEnabled(mocLocationProvider, true)

        val loc = Location(mocLocationProvider)
        val mockLocation = Location(LocationManager.GPS_PROVIDER) // a string
        mockLocation.latitude = -26.902038  // double
        mockLocation.longitude = -48.671337
        mockLocation.altitude = loc.altitude
        mockLocation.time = System.currentTimeMillis()
        mockLocation.accuracy = 3.0f
        mockLocation.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()

        lm.setTestProviderStatus(mocLocationProvider,
            LocationProvider.AVAILABLE,
            null,System.currentTimeMillis());

        lm.setTestProviderLocation(mocLocationProvider, mockLocation)
        Toast.makeText(applicationContext, "Working", Toast.LENGTH_SHORT).show()

    }

    override fun onRefresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun loadMap() {
        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's tile servers will get you banned based on this string

        //inflate and create the map

        val locationOverlayView =  MyLocationNewOverlay(GpsMyLocationProvider(this), mapView)
        locationOverlayView.enableMyLocation()

        mapView.apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setBuiltInZoomControls(false)
            setMultiTouchControls(true)
        }.controller.apply {
            setZoom(15.0)
            setCenter(GeoPoint(22.5726, 88.3639))
        }

        mapView.overlays.add(locationOverlayView)

        //your items
        val items = ArrayList<OverlayItem>()
        items.add(OverlayItem("Title", "Description", GeoPoint(22.5726, 87.0))) // Lat/Lon decimal degrees

//the overlay
        val mOverlay = ItemizedOverlayWithFocus<OverlayItem>(this, items,
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
        mapView.addMapListener(object : MapListener {
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

        })
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    fun isMockSettingsON() : Boolean =
        Settings.Secure.getString(contentResolver, "mock_location") != "0"
}
