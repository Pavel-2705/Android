package com.example.appodealtestproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appodeal.ads.NativeAd;

import java.util.ArrayList;
import java.util.List;

public class MyTestAdapter extends RecyclerView.Adapter<MyTestAdapter.MyTestViewHolder> {

    private final LayoutInflater inflater;
    private List<NativeAd> moviesList;
    private Context context;

    public class MyTestViewHolder extends RecyclerView.ViewHolder {


        public MyTestViewHolder(View view) {
            super(view);
        }
    }

    public MyTestAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.moviesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.include_native_ads, parent, false);
        return new MyTestViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyTestViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return moviesList != null ? moviesList.size() : 0;
    }

    public void updateAdapter(List<NativeAd> newNativeAds){
        notifyDataSetChanged();
    }
}

