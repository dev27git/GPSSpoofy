package com.rp.gpsspoofy.ui.activity.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import com.google.android.gms.location.LocationRequest
import com.patloew.rxlocation.RxLocation
import com.rp.basefiles.BaseActivity
import com.rp.gpsspoofy.R
import com.rp.gpsspoofy.ui.dialog.CheckLocationDialog
import com.rp.gpsspoofy.ui.fragment.map.MapFragment
import com.rp.util.fragment.FragmentBuilder
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit


class MainActivity : BaseActivity() {

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    private lateinit var locationRequest: LocationRequest
    private lateinit var rxLocation: RxLocation
    private val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    private val checkLocationDialog: CheckLocationDialog by lazy {
        CheckLocationDialog()
    }

    override val layoutRes: Int
        get() = R.layout.activity_main

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rxLocation = RxLocation(this)

        /*rxLocation.location()
            .updates(locationRequest)
            .zipWith(rxLocation.settings().check(locationRequest).toObservable())
            .flatMap {
                Observable.just(it.first)
            }
            .subscribe(
                Consumer {
                    Log.e(TAG, "Success Consumer called")
                    it?.apply {
                        Log.e(TAG, "latitude $latitude")
                        Log.e(TAG, "longitude $longitude")
                    }
                },
                Consumer {
                    it.printStackTrace()
                }
            )*/

        rxLocation.location()
            .updates(LocationRequest.create(), 100, TimeUnit.MILLISECONDS)
            .subscribeBy {
                it?.let {
                    FragmentBuilder.builder()
                        .replace(R.id.flContainer, MapFragment())
                        .commit()
                }
            }

        /*rxLocation.location()
            .isLocationAvailable
            .filter {
                if (!it) CheckLocationDialog().show(supportFragmentManager, CheckLocationDialog.TAG)
                it
            }
            .flatMap {
                rxLocation.location().lastLocation()
            }
            .subscribeBy(
                onSuccess = {

                }
            )*/

    }

    override fun onRefresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun isMockSettingsON(): Boolean =
        Settings.Secure.getString(contentResolver, "mock_location") != "0"

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
