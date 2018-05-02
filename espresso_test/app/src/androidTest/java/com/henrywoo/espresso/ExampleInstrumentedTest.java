package com.henrywoo.espresso;

import android.content.Context;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.SeekBar;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

//    @Rule
//    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        SystemClock.sleep(3000);
        Context appContext = InstrumentationRegistry.getTargetContext();
        String packageName = appContext.getPackageName();
        int id = appContext.getResources().getIdentifier("seekBar", "id", packageName);
        setProgress(withId(id));
        SystemClock.sleep(10000);
    }

    public void setProgress(Matcher<View> matcher) {
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }
            @Override
            public String getDescription() {
                return "seekbar";
            }
            @Override
            public void perform(UiController uiController, View view) {
                SeekBar seekBar = (SeekBar)view;
                seekBar.setProgress(3);
            }
        });
    }
}
