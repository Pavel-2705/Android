package com.example.appodealtestproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        RewardedVideoAdListener {

    private final static String LOG_TAG = "LOG_TAG";
    private final static String MY_APPODEAL_APP_KEY =
            "677709b8cabec24ea240c567e7a67408932e780932eb1c66";
    private final static int AD_TYPES = Appodeal.BANNER |
            Appodeal.INTERSTITIAL |
            Appodeal.REWARDED_VIDEO |
            Appodeal.NATIVE;

    private Button buttonBanner,
            buttonInterstitials,
            buttonRewardVideo,
            buttonNative;

    private AdView adView;
    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;

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
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        buttonBanner = findViewById(R.id.buttonBanner);
        buttonBanner.setOnClickListener(this);
    }

    private void setInterstitialAd() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialAd.loadAd(new AdRequest.Builder().build());

        buttonInterstitials = findViewById(R.id.buttonInterstitials);
        buttonInterstitials.setOnClickListener(this);
    }

    private void setRewardedVideoAd() {
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();

        buttonRewardVideo = findViewById(R.id.buttonRewardVideo);
        buttonRewardVideo.setOnClickListener(this);
    }

    private void loadRewardedVideoAd() {
        rewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    private void setNativeAd() {
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
                Appodeal.show(this, Appodeal.BANNER_TOP);

                break;
            case R.id.buttonInterstitials:
                if (interstitialAd.isLoaded()
//                Appodeal.isLoaded(Appodeal.INTERSTITIAL)
                ) {
                    interstitialAd.show();
                    //ToDo: Need test here
/*                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            buttonInterstitials.setVisibility(View.INVISIBLE);
                            try {
                                Thread.sleep(60 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            buttonInterstitials.setVisibility(View.VISIBLE);
                        }
                    });
                    thread.start();*/
//                    Appodeal.show(MainActivity.this, Appodeal.INTERSTITIAL);
                } else {
                    Log.d(LOG_TAG, "The interstitial wasn't loaded yet.");
                }
                break;
            case R.id.buttonRewardVideo:
                if (rewardedVideoAd.isLoaded()
//                Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)
                ) {
                    rewardedVideoAd.show();
//                    Appodeal.show(this, Appodeal.REWARDED_VIDEO);
                }
                break;
            case R.id.buttonNative:
                break;
        }
    }

    @Override
    public void onResume() {
        rewardedVideoAd.resume(this);
        super.onResume();
        Appodeal.onResume(this, Appodeal.BANNER_TOP);
    }

    @Override
    public void onPause() {
        rewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        rewardedVideoAd.destroy(this);
        super.onDestroy();
    }


    @Override
    public void onRewarded(RewardItem reward) {
        Toast.makeText(this, "onRewarded! currency: " + reward.getType() + "  amount: " +
                reward.getAmount(), Toast.LENGTH_SHORT).show();
        // Reward the user.
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "onRewardedVideoAdLeftApplication",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
        loadRewardedVideoAd();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        buttonRewardVideo.setVisibility(View.VISIBLE);
        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
        Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
    }
}
