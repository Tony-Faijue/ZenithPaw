package com.example.zenithpaw.roomdatabase

import android.app.Application
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import com.example.zenithpaw.AppLifecycleManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ZenithPawApplication: Application(), DefaultLifecycleObserver, SingletonImageLoader.Factory {
    private val tag = "ZenithPawApplication Lifecycle"
    @Inject
    lateinit var appLifecycleManager: AppLifecycleManager

    // Lifecycle observer methods

    /**
     * Called when the application is created
     */
    override fun onCreate(){
        super<Application>.onCreate()
        // Register the lifecycle observer
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        Log.d(tag, "Application created")
    }

    /**
     * Called when the application is started
     */
    override fun onStart(owner: LifecycleOwner){
        super.onStart(owner)
        // Handle offline/off app state gap/ catch up calculation
        appLifecycleManager.handleAppResumed()
        Log.d(tag, "Application started")
    }

    /**
     * Called when the application is stopped
     */
    override fun onStop(owner: LifecycleOwner){
        super.onStop(owner)
        // When the app is closed/unfocused, save the timestamp
        appLifecycleManager.handleAppPaused()
        Log.d(tag, "Application stopped")
    }

    /**
     * Override the newImageLoader method from SingletonImageLoader.Factory
     * uses the AnimatedImageDecoder for GIF images if SDK_INT >= 28 else uses the GifDecoder
     */
    override fun newImageLoader(context: android.content.Context): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                if (SDK_INT >= 28){
                add(AnimatedImageDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }
}