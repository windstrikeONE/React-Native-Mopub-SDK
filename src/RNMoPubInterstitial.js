import {NativeModules, NativeEventEmitter} from 'react-native';
const { RNMoPubInterstitial } = NativeModules;


const emitter = new NativeEventEmitter(RNMoPubInterstitial);

module.exports = {
    initializeInterstitialAd: (adUnitId: string) => RNMoPubInterstitial.initializeInterstitialAd(adUnitId),
    initializeInterstitialAdAndLoad: (adUnitId: string) => RNMoPubInterstitial.initializeInterstitialAdAndLoad(adUnitId),
    loadAd: () => RNMoPubInterstitial.loadAd(),
    setKeywords: (keywords: string) => RNMoPubInterstitial.setKeywords(keywords),
    isReady: (): Promise => RNMoPubInterstitial.isReady(),
    show: () => RNMoPubInterstitial.show(),
    addEventListener: (eventType: string, listener: Function)  => emitter.addListener(eventType, listener),
    removeAllListeners: (eventType: string) => emitter.removeAllListeners(eventType)
};