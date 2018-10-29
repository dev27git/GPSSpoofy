package dev.rahul.baseutils.v1.util.intentUtillity

import android.app.Activity
import android.content.Intent
import android.os.Bundle

fun Activity.start(openActivity: Class<*>, bundle: Bundle? = null) {
    val intent = Intent(this, openActivity)
    intent.putExtras(bundle ?: Bundle.EMPTY)
    this.startActivity(intent)
}

fun Activity.startWithExtras(openActivity: Class<*>): Pair<Activity, Intent> {
    val intent = Intent(this, openActivity)
    return Pair(this, intent)
}

fun Activity.startForResult(openActivity: Class<*>, requestCode: Int, bundle: Bundle? = null) {
    val intent = Intent(this, openActivity)
    intent.putExtras(bundle ?: Bundle.EMPTY)
    this.startActivityForResult(intent, requestCode)
}

fun Pair<Activity, Intent>.putExtra(key: String, value: Any): Pair<Activity, Intent> {
    this.putExtra(key, value)

    return this
}

fun Pair<Activity, Intent>.now() {
    this.first.startActivity(this.second)
}


