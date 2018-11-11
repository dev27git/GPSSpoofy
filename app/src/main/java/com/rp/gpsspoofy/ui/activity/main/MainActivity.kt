package com.rp.gpsspoofy.ui.activity.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import com.rp.basefiles.BaseActivity
import com.rp.gpsspoofy.R
import com.rp.gpsspoofy.ui.fragment.map.MapFragment
import com.rp.util.fragment.FragmentBuilder


class MainActivity : BaseActivity() {

    override val layoutRes: Int
        get() = R.layout.activity_main

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FragmentBuilder.builder()
            .replace(R.id.flContainer, MapFragment())
            .commit()

    }

    override fun onRefresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun isMockSettingsON(): Boolean =
        Settings.Secure.getString(contentResolver, "mock_location") != "0"
}
