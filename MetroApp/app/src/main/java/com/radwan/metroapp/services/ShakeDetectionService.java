package com.radwan.metroapp.services;

import android.content.Context;

import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;

public class ShakeDetectionService {
    private Context context;
    private ShakeListener shakeListener;

    public ShakeDetectionService(Context context) {
        this.context = context;
        Sensey.getInstance().init(context);
    }

    public void startShakeDetection() {
        Sensey.getInstance().startShakeDetection(new ShakeDetector.ShakeListener() {
            @Override
            public void onShakeDetected() {
                if (shakeListener != null) {
                    shakeListener.onShakeDetected();
                }
            }

            @Override
            public void onShakeStopped() {
                // Not used in this implementation
            }
        });
    }

//    public void stopShakeDetection() {
//        Sensey.getInstance().stopShakeDetection();
//    }

    public void setShakeListener(ShakeListener listener) {
        this.shakeListener = listener;
    }

    public interface ShakeListener {
        void onShakeDetected();
    }
}