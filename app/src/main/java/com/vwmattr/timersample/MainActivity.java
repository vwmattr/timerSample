package com.vwmattr.timersample;

import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.TextView;

import java.text.DecimalFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

    private static long ROUND_TIME = 10000l;
    private static long TIMER_TICK_INTERVAL = 10l;
    private static double SECONDS = 1000.0;

    boolean started = false;
    boolean gameOver = false;
    int tapCount = 0;

    @InjectView(R.id.roundTimer) TextView roundTimerText;
    @InjectView(R.id.lastTapTimer) TextView lastTapTimerText;
    @InjectView(R.id.tapCount) TextView tapCountText;

    DecimalFormat timerFormat = new DecimalFormat("0.00");

    CountUpTimer lastTapTimer = new CountUpTimer(TIMER_TICK_INTERVAL) {
        @Override
        public void onTick(long elapsedTime) {
            lastTapTimerText.setText(timerFormat.format(elapsedTime / SECONDS));
        }
    };

    CountDownTimer roundTimer = new CountDownTimer(ROUND_TIME, TIMER_TICK_INTERVAL) {

        public void onTick(long millisUntilFinished) {
            roundTimerText.setText(timerFormat.format(millisUntilFinished / SECONDS));
        }

        public void onFinish() {
            gameOver = true;
            lastTapTimer.stop();
            lastTapTimerText.setText("0.00");
            roundTimerText.setText("Game Over");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.start)
    public void start() {
        
        if (!gameOver) {
            if (!started) {
                started = true;
                roundTimer.start();
            }

            updateTapCount(true);
            lastTapTimer.stop();
            lastTapTimer.reset();
            lastTapTimer.start();
        }
    }

    private void updateTapCount(boolean inc) {
        if (inc) {
            tapCount++;
        }
        tapCountText.setText(String.valueOf(tapCount));
    }
}
