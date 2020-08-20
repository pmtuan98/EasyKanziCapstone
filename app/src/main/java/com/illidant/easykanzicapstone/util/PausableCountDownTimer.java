package com.illidant.easykanzicapstone.util;
/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.os.Handler;
import android.os.SystemClock;
import android.os.Message;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * Schedule a countdown until a time in the future, with
 * regular notifications on intervals along the way.
 * <p>
 * Example of showing a 30 second countdown in a text field:
 *
 * <pre class="prettyprint">
 * new CountdownTimer(30000, 1000) {
 *
 *     public void onTick(long millisUntilFinished) {
 *         mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
 *     }
 *
 *     public void onFinish() {
 *         mTextField.setText("done!");
 *     }
 *  }.start();
 * </pre>
 * <p>
 * The calls to {@link #onTick(long)} are synchronized to this object so that
 * one call to {@link #onTick(long)} won't ever occur before the previous
 * callback is complete.  This is only relevant when the implementation of
 * {@link #onTick(long)} takes an amount of time to execute that is significant
 * compared to the countdown interval.
 */
public abstract class PausableCountDownTimer {

    /**
     * Millis since epoch when alarm should stop.
     */
    private final long mMillisInFuture;

    /**
     * The interval in millis that the user receives callbacks
     */
    private final long mCountdownInterval;

    private long mStopTimeInFuture;

    private long mPauseTime;

    private boolean mCancelled = false;

    private boolean mPaused = false;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public PausableCountDownTimer(long millisInFuture, long countDownInterval) {
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
    }

    /**
     * Cancel the countdown.
     * <p>
     * Do not call it from inside CountDownTimer threads
     */
    public final void cancel() {
        mHandler.removeMessages(MSG);
        mCancelled = true;
    }

    /**
     * Start the countdown.
     */
    public synchronized final PausableCountDownTimer start() {
        if (mMillisInFuture <= 0) {
            onFinish();
            return this;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        mCancelled = false;
        mPaused = false;
        return this;
    }

    /**
     * Pause the countdown.
     */
    public long pause() {
        mPauseTime = mStopTimeInFuture - SystemClock.elapsedRealtime();
        mPaused = true;
        return mPauseTime;
    }

    /**
     * Resume the countdown.
     */
    public long resume() {
        mStopTimeInFuture = mPauseTime + SystemClock.elapsedRealtime();
        mPaused = false;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return mPauseTime;
    }

    /**
     * Callback fired on regular interval.
     *
     * @param millisUntilFinished The amount of time until finished.
     */
    public abstract void onTick(long millisUntilFinished);

    /**
     * Callback fired when the time is up.
     */
    public abstract void onFinish();

    private static final int MSG = 1;

    // handles counting down
    private Handler mHandler = new TimerHandler(this);

    private static class TimerHandler extends Handler {
        private final WeakReference<PausableCountDownTimer> mTimer;

        TimerHandler(PausableCountDownTimer timer) {
            mTimer = new WeakReference<>(timer);
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            PausableCountDownTimer timer = mTimer.get();
            synchronized (timer) {
                if (!timer.mPaused) {
                    final long millisLeft = timer.mStopTimeInFuture - SystemClock.elapsedRealtime();

                    if (millisLeft <= 0) {
                        timer.onFinish();
                    } else if (millisLeft < timer.mCountdownInterval) {
                        // no tick, just delay until done
                        sendMessageDelayed(obtainMessage(MSG), millisLeft);
                    } else {
                        long lastTickStart = SystemClock.elapsedRealtime();
                        timer.onTick(millisLeft);

                        // take into account user's onTick taking time to execute
                        long delay = lastTickStart + timer.mCountdownInterval - SystemClock.elapsedRealtime();

                        // special case: user's onTick took more than interval to
                        // complete, skip to next interval
                        while (delay < 0) delay += timer.mCountdownInterval;

                        if (!timer.mCancelled) {
                            sendMessageDelayed(obtainMessage(MSG), delay);
                        }
                    }
                }
            }
        }
    }
}