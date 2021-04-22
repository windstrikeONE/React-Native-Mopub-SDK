package com.reactlibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.common.logging.MoPubLog;

import java.util.concurrent.Callable;

/**
 * Created by usamaazam on 29/03/2019.
 */

public class AdLibSDK {

    static void initializeAdSDK(final RNMoPubBanner banner, final String adUnitId, final Activity context) {

        Handler mainHandler = new Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {

                SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder(adUnitId)
                        .withLogLevel(MoPubLog.LogLevel.DEBUG)
                        .withLegitimateInterestAllowed(false)
                        .build();

                MoPub.initializeSdk(context, sdkConfiguration, initSdkListener());
            }

            private SdkInitializationListener initSdkListener() {
                return new SdkInitializationListener() {
                    @Override
                    public void onInitializationFinished() {
                        if (banner != null) {
                            banner.setAdUnitId(adUnitId);
                            banner.loadAd();
                        }
                    }
                };
            }
        };
        mainHandler.post(myRunnable);
    }

    static void initializeAdSDK(final RNMoPubBanner b,final Callable<Void> sdkInitializedCallback, final String adUnitId, final Activity context) {

        Handler mainHandler = new Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {

                SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder(adUnitId)
                        .withLogLevel(MoPubLog.LogLevel.DEBUG)
                        .withLegitimateInterestAllowed(false)
                        .build();

                MoPub.initializeSdk(context, sdkConfiguration, initSdkListener());

            }

            private SdkInitializationListener initSdkListener() {
                return new SdkInitializationListener() {
                    @Override
                    public void onInitializationFinished() {
                        if (sdkInitializedCallback != null) {
//                            banner.setAdUnitId(adUnitId);
//                            banner.loadAd();
                            Log.d("mopub", "sdk initialized, call the callback");
                            try {
                                sdkInitializedCallback.call();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
            }
        };
        mainHandler.post(myRunnable);
    }
}
