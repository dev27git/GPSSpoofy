package com.rp.gpsspoofy.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import butterknife.ButterKnife
import butterknife.OnClick
import com.rp.gpsspoofy.R



class CheckLocationDialog: AppCompatDialogFragment() {

    companion object {
        val TAG : String? = CheckLocationDialog::class.simpleName
        const val OPEN_SETTING_REQUEST_CODE : Int = 7686
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_check_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)
    }

    @OnClick(R.id.btnCancel)
    fun onClickCancel() = dismiss()

    @OnClick(R.id.btnOpenSetting)
    fun onClickOpenSetting() {
        val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(callGPSSettingIntent, OPEN_SETTING_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(TAG, "request code $requestCode");
        Log.e(TAG, "result code $resultCode");

        data?.extras?.let {
            Log.e(TAG, "Bundle data " + it.toString())
        }
    }
}