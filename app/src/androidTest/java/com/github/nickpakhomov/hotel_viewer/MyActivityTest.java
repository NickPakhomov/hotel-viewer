package com.github.nickpakhomov.hotel_viewer;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.github.nickpakhomov.hotel_viewer.ui.activities.MainActivity;
import com.github.nickpakhomov.hotel_viewer.ui.activities.PreferenceActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Nikolay Pakhomov on 10/09/17.
 */

@RunWith(AndroidJUnit4.class)
public class MyActivityTest {
    
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);
    
    @Test
    public void testClickPreferencesMenuItem() {
        Intents.init();
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText("Preferences")).perform(click());
        intended(hasComponent(PreferenceActivity.class.getName()));
        Intents.release();
    }
}
