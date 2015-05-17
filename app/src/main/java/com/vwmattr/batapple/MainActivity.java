package com.vwmattr.batapple;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static java.lang.Math.abs;


public class MainActivity extends ActionBarActivity {

    private static long ROUND_TIME = 10000l;
    private static long TIMER_TICK_INTERVAL = 10l;
    private static double SECONDS = 1000.0;
    private static int POINTS_PER_ROUND = 100;

    boolean started = false;
    boolean gameOver = false;
    int tapCount = 0;
    int xBounds;
    int yBounds;
    List<Point> roundPositions;

    @InjectView(R.id.roundTimer)
    TextView roundTimerText;
    @InjectView(R.id.lastTapTimer)
    TextView lastTapTimerText;
    @InjectView(R.id.tapCount)
    TextView tapCountText;
    @InjectView(R.id.start)
    Button tapButton;
    @InjectView(R.id.mainView)
    View mainView;

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

    @Override
    protected void onResume() {
        super.onResume();
        createRandomPoints();
    }

    private void createRandomPoints() {
        //TODO: Inject a points provider for flexibility
        ViewTreeObserver vto = mainView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mainView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                xBounds = mainView.getMeasuredWidth() - tapButton.getMeasuredWidth();
                yBounds = mainView.getMeasuredHeight() - tapButton.getMeasuredHeight();

                roundPositions = new ArrayList<Point>(POINTS_PER_ROUND);
                Random random = new Random(System.currentTimeMillis());  //Seed with Now

                for (int i = 0; i < POINTS_PER_ROUND; i++) {
                    roundPositions.add(
                            new Point(
                                    abs(random.nextInt() % xBounds),
                                    abs(random.nextInt() % yBounds)
                            ));
                }

            }
        });
    }

    @OnClick(R.id.start)
    public void start() {

        if (!gameOver) {
            if (!started) {
                started = true;
                roundTimer.start();
            }

            moveTapButton();
            updateTapCount(true);
            resetTapTimer();
        }
    }

    private void moveTapButton() {
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) tapButton.getLayoutParams();
        final Point curr = roundPositions.get(tapCount);
        tapButton.setX(curr.x);
        tapButton.setY(curr.y);

    }

    private void resetTapTimer() {
        lastTapTimer.stop();
        lastTapTimer.reset();
        lastTapTimer.start();
    }

    private void updateTapCount(boolean inc) {
        if (inc) {
            tapCount++;
        }
        tapCountText.setText(String.valueOf(tapCount));
    }
}
