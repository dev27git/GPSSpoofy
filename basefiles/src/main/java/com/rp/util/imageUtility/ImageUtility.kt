package dev.rahul.baseutils.v1.util.imageUtility

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import de.hdodenhof.circleimageview.CircleImageView

fun ImageView.loadUrl(
        url: String,
        requestManager: RequestManager = Glide.with(this.context),
        requestOptions: RequestOptions = RequestOptions().centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC),
        onStart: (() -> Unit) = {},
        onComplete: (() -> Unit) = {},
        onError: ((e: Exception) -> Unit) = {}
) {
    load(
            imageView = this,
            url = url,
            requestManager = requestManager,
            requestOptions = requestOptions,
            onStart = onStart, onComplete = onComplete, onError = onError
    )
}

fun CircleImageView.loadUrl(
        url: String,
        requestManager: RequestManager = Glide.with(this.context),
        requestOptions: RequestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC),
        onStart: (() -> Unit) = {},
        onComplete: (() -> Unit) = {},
        onError: ((e: Exception) -> Unit) = {}
) {
    load(
            imageView = this,
            url = url,
            requestManager = requestManager,
            requestOptions = requestOptions,
            onStart = onStart, onComplete = onComplete, onError = onError
    )
}

private fun load(
        imageView: ImageView,
        url: String,
        requestManager: RequestManager,
        requestOptions: RequestOptions,
        onStart: (() -> Unit),
        onComplete: (() -> Unit),
        onError: ((e: Exception) -> Unit)
) {
    onStart()

    requestManager
            .load(url)
            .apply(requestOptions)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    onError(e as Exception)
                    onComplete()
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    onComplete()
                    return false
                }
            })
            .into(imageView)
}