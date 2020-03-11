package com.example.appodealtestproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String LOG_TAG = "LOG_TAG";
    private final static String MY_APPODEAL_APP_KEY =
            "677709b8cabec24ea240c567e7a67408932e780932eb1c66";
    private final static int AD_TYPES = Appodeal.BANNER |
            Appodeal.INTERSTITIAL |
            Appodeal.REWARDED_VIDEO |
            Appodeal.NATIVE;

    private static int BANNER_COUNTER = 0;
    private final static int BANNER_MAX_COUNTER = 5;

    private final static long INTERSTATIAL_SLEEP_TIME_IN_MILLISECONDS = 60 * 1000;

    private static int REWARDED_VIDEO_COUNTER = 0;
    private final static int REWARDED_VIDEO_MAX_COUNTER = 3;

    private final static int NATIVE_AD_MAX_COUNTER = 3;
    private List<NativeAd> nativeAdList = new ArrayList();

    private Button buttonBanner,
            buttonInterstitials,
            buttonRewardVideo,
            buttonNative;

    private TemplateView nativeTemplateView;
    private RecyclerView nativeAdsRecyclerView;
    private AppodealWrapperAdapter appodealWrapperAdapter;
    private AdLoader adLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ToDo: initialize MobileAds
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //ToDo: initialize Appodeal
        Appodeal.initialize(this,
                MY_APPODEAL_APP_KEY,
                AD_TYPES,
                true);

        //ToDo: Set Banner
        setBanner();

        //ToDo: Set InterstatialAd
        setInterstitialAd();

        //ToDo: Set RewardVideoAd
        setRewardedVideoAd();

        //ToDo: Set NativeAd
        setNativeAd();
    }

    private void setBanner() {
        Appodeal.setBannerCallbacks(new BannerCallbacks() {
            @Override
            public void onBannerLoaded(int i, boolean b) {
                BANNER_COUNTER++;
                if (BANNER_COUNTER > BANNER_MAX_COUNTER) {
                    Appodeal.hide(MainActivity.this, Appodeal.BANNER_TOP);
                }
            }

            @Override
            public void onBannerFailedToLoad() {

            }

            @Override
            public void onBannerShown() {

            }

            @Override
            public void onBannerShowFailed() {

            }

            @Override
            public void onBannerClicked() {

            }

            @Override
            public void onBannerExpired() {

            }
        });

        buttonBanner = findViewById(R.id.buttonBanner);
        buttonBanner.setOnClickListener(this);
    }

    private void setInterstitialAd() {
        buttonInterstitials = findViewById(R.id.buttonInterstitials);
        buttonInterstitials.setOnClickListener(this);
    }

    private void setRewardedVideoAd() {
        Appodeal.setRewardedVideoCallbacks(new RewardedVideoCallbacks() {
            @Override
            public void onRewardedVideoLoaded(boolean b) {
                if (REWARDED_VIDEO_COUNTER <= REWARDED_VIDEO_MAX_COUNTER) {
                    buttonRewardVideo.setClickable(true);
                    REWARDED_VIDEO_COUNTER++;
                }
            }

            @Override
            public void onRewardedVideoFailedToLoad() {

            }

            @Override
            public void onRewardedVideoShown() {

            }

            @Override
            public void onRewardedVideoShowFailed() {

            }

            @Override
            public void onRewardedVideoFinished(double v, String s) {

            }

            @Override
            public void onRewardedVideoClosed(boolean b) {

            }

            @Override
            public void onRewardedVideoExpired() {

            }

            @Override
            public void onRewardedVideoClicked() {

            }
        });

        buttonRewardVideo = findViewById(R.id.buttonRewardVideo);
        buttonRewardVideo.setOnClickListener(this);
    }

    private void setNativeAd() {
        initializeView();

        Appodeal.cache(this, Appodeal.NATIVE, NATIVE_AD_MAX_COUNTER);

        adLoader = new AdLoader.Builder(this,
                "ca-app-pub-3940256099942544/2247696110")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // some code that displays the ad.
                        NativeTemplateStyle style = new
                                NativeTemplateStyle.Builder()
                                .withMainBackgroundColor(new ColorDrawable()).build();

                        nativeTemplateView = findViewById(R.id.nativeTemplateView);
                        nativeTemplateView.setStyles(style);
                        nativeTemplateView.setNativeAd(unifiedNativeAd);


                        if (adLoader.isLoading()) {
                            // The AdLoader is still loading ads.
                            // Expect more adLoaded or onAdFailedToLoad callbacks.
                        } else {
                            // The AdLoader has finished loading ads.
                        }
                    }
                })
                .build();

        adLoader.loadAds(new AdRequest.Builder().build(), 3);

        buttonNative = findViewById(R.id.buttonNative);
        buttonNative.setOnClickListener(this);
    }

    private void initializeView() {
        nativeAdsRecyclerView = findViewById(R.id.nativeAdsRecyclerView);

        //ToDo: Need do some tests
        RecyclerView.Adapter myTestAdapter = new MyTestAdapter(this);
        appodealWrapperAdapter = new AppodealWrapperAdapter(myTestAdapter, 0);
        nativeAdsRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        nativeAdsRecyclerView.setAdapter(appodealWrapperAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBanner:
                BANNER_COUNTER = 0;
                if (Appodeal.isLoaded(Appodeal.BANNER_TOP)) {
                    Appodeal.show(this, Appodeal.BANNER_TOP);
                }
                break;
            case R.id.buttonInterstitials:
                if (Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
                    Appodeal.show(this, Appodeal.INTERSTITIAL);

                    SleepTask sleepTask = new SleepTask();
                    sleepTask.execute(INTERSTATIAL_SLEEP_TIME_IN_MILLISECONDS);
                } else {
                    Log.d(LOG_TAG, "The interstitial wasn't loaded yet.");
                }
                break;
            case R.id.buttonRewardVideo:
                if (Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)) {
                    buttonRewardVideo.setClickable(false);
                    Appodeal.show(this, Appodeal.REWARDED_VIDEO);
                }
                break;
            case R.id.buttonNative:
                Appodeal.hide(this, Appodeal.BANNER_TOP);
                if (Appodeal.isLoaded(Appodeal.NATIVE)) {
                    //ToDo: Need do some tests
                    nativeAdList = Appodeal.getNativeAds(NATIVE_AD_MAX_COUNTER);
                    Appodeal.cache(this, Appodeal.NATIVE, NATIVE_AD_MAX_COUNTER);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Appodeal.onResume(this, Appodeal.BANNER_TOP);
    }

    class SleepTask extends AsyncTask<Long, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            buttonInterstitials.setClickable(false);
        }

        @Override
        protected Void doInBackground(Long... sleepTimeInMillisecond) {
            try {
                TimeUnit.MILLISECONDS.sleep(sleepTimeInMillisecond[0]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            buttonInterstitials.setClickable(true);
        }
    }
}
