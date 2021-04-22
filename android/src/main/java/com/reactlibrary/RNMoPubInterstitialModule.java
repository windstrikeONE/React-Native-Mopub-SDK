package com.reactlibrary;

import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import java.util.concurrent.Callable;

import javax.annotation.Nullable;

/**
 * Created by usamaazam on 29/03/2019.
 */

public class RNMoPubInterstitialModule extends ReactContextBaseJavaModule implements MoPubInterstitial.InterstitialAdListener, LifecycleEventListener {

    public static final String EVENT_LOADED = "onLoaded";
    public static final String EVENT_FAILED = "onFailed";
    public static final String EVENT_CLICKED = "onClicked";
    public static final String EVENT_SHOWN = "onShown";
    public static final String EVENT_DISMISSED = "onDismissed";

    private MoPubInterstitial mInterstitial;
    ReactApplicationContext mReactContext;
    public String g_adUnitId;

    public RNMoPubInterstitialModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNMoPubInterstitial";
    }

    @ReactMethod
    public void initializeInterstitialAd(final String adUnitId) {
        final RNMoPubInterstitialModule that = this;
        AdLibSDK.initializeAdSDK(null, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                return actuallyInitializeInterstitial(adUnitId);
            }
        }, adUnitId, getCurrentActivity());
    }

    @ReactMethod
    public void initializeInterstitialAdAndLoad(final String adUnitId) {
        final RNMoPubInterstitialModule that = this;
        AdLibSDK.initializeAdSDK(null, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                return actuallyInitializeInterstitial(adUnitId);
            }
        }, adUnitId, getCurrentActivity());
    }


    public Void actuallyInitializeInterstitial(String adUnitId) {
        Log.d("mopub", "actually try to initialize interstitial with ad id: " + adUnitId);
        mInterstitial = new MoPubInterstitial(getCurrentActivity(), adUnitId);
        mInterstitial.setInterstitialAdListener(this);
        mInterstitial.load();
        return null;
    }

//    public Callable<Void> actuallyInitializeInterstitial(){
//        return Void call(){
//
//        }
//        Log.d("mopub","actually try to initialize interstitial with ad id: "+adUnitId);
//
//    }

    @ReactMethod
    public void setKeywords(String keywords) {
        if (mInterstitial != null)
            mInterstitial.setKeywords(keywords);
    }

    @ReactMethod
    public void isReady(Promise promise) {
        if (mInterstitial == null) {
            promise.resolve(false);
        } else {
            promise.resolve(mInterstitial.isReady());
        }
    }

    @ReactMethod
    public void loadAd() {
        Log.d("mopub", "NOW WE TRY TO LOAD THE INTERSTITIAL");
        if (mInterstitial != null) {
            mInterstitial.load();
        }
    }

    @ReactMethod
    public void show() {
        if (mInterstitial != null) {
            mInterstitial.show();
        }
    }

    @ReactMethod
    public void forceRefresh() {
        if (mInterstitial != null) {
            mInterstitial.forceRefresh();
        }
    }

    private void sendEvent(String eventName, @Nullable WritableMap params) {
        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    @Override
    public void onInterstitialLoaded(MoPubInterstitial interstitial) {
        Log.d("mopub", "success interstitial loaded!");
        sendEvent(EVENT_LOADED, null);
    }

    @Override
    public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
        WritableMap event = Arguments.createMap();
        event.putString("message", errorCode.toString());
        sendEvent(EVENT_FAILED, event);
    }

    @Override
    public void onInterstitialShown(MoPubInterstitial interstitial) {
        sendEvent(EVENT_SHOWN, null);
    }

    @Override
    public void onInterstitialClicked(MoPubInterstitial interstitial) {
        sendEvent(EVENT_CLICKED, null);
    }

    @Override
    public void onInterstitialDismissed(MoPubInterstitial interstitial) {
        sendEvent(EVENT_DISMISSED, null);
    }

    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {
        if (mInterstitial != null)
            mInterstitial.destroy();
    }
}
