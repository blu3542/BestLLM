package com.example.bestllm;

import android.os.Build;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import org.junit.Before;

/**
 * Base class for Espresso tests with common setup
 * Disables animations for reliable Espresso testing
 */
public class EspressoTestBase {
    
    @Before
    public void disableAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            device.executeShellCommand("settings put global animator_duration_scale 0");
            device.executeShellCommand("settings put global window_animation_scale 0");
            device.executeShellCommand("settings put global transition_animation_scale 0");
        }
    }
}

