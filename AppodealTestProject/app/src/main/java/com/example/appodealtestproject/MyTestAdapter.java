package com.example.appodealtestproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeAdView;
import com.appodeal.ads.NativeIconView;
import com.appodeal.ads.NativeMediaView;

import java.util.ArrayList;
import java.util.List;

public class MyTestAdapter extends RecyclerView.Adapter<MyTestAdapter.MyTestViewHolder> {

    private final LayoutInflater inflater;
    private List<NativeAd> nativeAdList;
    private Context context;

    public MyTestAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.nativeAdList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.include_native_ads, parent, false);
        return new MyTestViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyTestViewHolder holder, int position) {
        NativeAd nativeAd = nativeAdList.get(position);
        holder.fillNative(nativeAd);
    }

    @Override
    public int getItemCount() {
        return nativeAdList != null ? nativeAdList.size() : 0;
    }

    static class MyTestViewHolder extends RecyclerView.ViewHolder {
        private NativeAdView nativeAdView;
        private TextView tvTitle;
        private TextView tvDescription;
        private RatingBar ratingBar;
        private Button ctaButton;
        private NativeIconView nativeIconView;
        private TextView tvAgeRestrictions;
        private NativeMediaView nativeMediaView;
        private FrameLayout providerViewContainer;

        MyTestViewHolder(View itemView) {
            super(itemView);

            nativeAdView = itemView.findViewById(R.id.native_item);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            ratingBar = itemView.findViewById(R.id.rb_rating);
            ctaButton = itemView.findViewById(R.id.b_cta);
            nativeIconView = itemView.findViewById(R.id.icon);
            providerViewContainer = itemView.findViewById(R.id.provider_view);
            tvAgeRestrictions = itemView.findViewById(R.id.tv_age_restriction);
            nativeMediaView = itemView.findViewById(R.id.appodeal_media_view_content);
        }

        void fillNative(NativeAd nativeAd) {
            tvTitle.setText(nativeAd.getTitle());
            tvDescription.setText(nativeAd.getDescription());

            if (nativeAd.getRating() == 0) {
                ratingBar.setVisibility(View.INVISIBLE);
            } else {
                ratingBar.setVisibility(View.VISIBLE);
                ratingBar.setRating(nativeAd.getRating());
                ratingBar.setStepSize(0.1f);
            }

            ctaButton.setText(nativeAd.getCallToAction());

            View providerView = nativeAd.getProviderView(nativeAdView.getContext());
            if (providerView != null) {
                if (providerView.getParent() != null && providerView.getParent() instanceof ViewGroup) {
                    ((ViewGroup) providerView.getParent()).removeView(providerView);
                }
                providerViewContainer.removeAllViews();
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                providerViewContainer.addView(providerView, layoutParams);
            }

            if (nativeAd.getAgeRestrictions() != null) {
                tvAgeRestrictions.setText(nativeAd.getAgeRestrictions());
                tvAgeRestrictions.setVisibility(View.VISIBLE);
            } else {
                tvAgeRestrictions.setVisibility(View.GONE);
            }

            if (nativeAd.containsVideo()) {
                nativeAdView.setNativeMediaView(nativeMediaView);
            } else {
                nativeMediaView.setVisibility(View.GONE);
            }


            nativeAdView.setTitleView(tvTitle);
            nativeAdView.setDescriptionView(tvDescription);
            nativeAdView.setRatingView(ratingBar);
            nativeAdView.setCallToActionView(ctaButton);
            nativeAdView.setNativeIconView(nativeIconView);
            nativeAdView.setProviderView(providerView);

            nativeAdView.registerView(nativeAd);
            nativeAdView.setVisibility(View.VISIBLE);
        }
    }

    public void updateNativeAdList(List<NativeAd> nativeAds) {
        this.nativeAdList = nativeAds;
        notifyDataSetChanged();
    }
}

