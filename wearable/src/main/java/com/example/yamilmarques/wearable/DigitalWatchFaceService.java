package com.example.yamilmarques.wearable;
/**
 * Created by yamil.marques on 3/3/15.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.WindowInsets;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class DigitalWatchFaceService extends CanvasWatchFaceService {
    private static final String TAG = "DigitalWatchFaceService";

    public static Double temperature;
    public static String city;

    public DecimalFormat decimalFormat = new DecimalFormat("#");

    private static  Typeface BOLD_TYPEFACE;
    private static  Typeface NORMAL_TYPEFACE;
    private static Typeface SECOND_TYPEFACE;

    private float degressOfSeconds = 0;
    private float extraHeight = 0;
    private Bitmap backgroundBit;

    private int secondAnimationNumber1 = 0,secondAnimationNumber2 = 180;

    private Paint mBackgroundPaint;
    private Paint mHourPaint;
    private Paint mMinutePaint;
    private Paint mSecondPaint;
    private Paint mColonPaint;
    private Paint dayTextPaint;
    private Paint mTempPaint;
    private Paint mDatePaint;
    private Paint mCityPaint;

    private int colorTextGeneral;

    private float mColonWidth;


    private static final long NORMAL_UPDATE_RATE_MS = 1; //500

    private static final long MUTE_UPDATE_RATE_MS = TimeUnit.MINUTES.toMillis(1);


    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private class Engine extends CanvasWatchFaceService.Engine implements DataApi.DataListener,
            GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
        static final String COLON_STRING = ":";

        /** Alpha value for drawing time when in mute mode. */
        static final int MUTE_ALPHA = 100;

        /** Alpha value for drawing time when not in mute mode. */
        static final int NORMAL_ALPHA = 255;

        static final int MSG_UPDATE_TIME = 0;

        /** How often {@link #mUpdateTimeHandler} ticks in milliseconds. */
        long mInteractiveUpdateRateMs = NORMAL_UPDATE_RATE_MS;

        /** Handler to update the time periodically in interactive mode. */
        final Handler mUpdateTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case MSG_UPDATE_TIME:
                        if (Log.isLoggable(TAG, Log.VERBOSE)) {
                            Log.v(TAG, "updating time");
                        }
                        invalidate();
                        if (shouldTimerBeRunning()) {
                            long timeMs = System.currentTimeMillis();
                            long delayMs =
                                    mInteractiveUpdateRateMs - (timeMs % mInteractiveUpdateRateMs);
                            mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
                        }
                        break;
                }
            }
        };

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(DigitalWatchFaceService.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mTime.clear(intent.getStringExtra("time-zone"));
                mTime.setToNow();
            }
        };

        boolean mRegisteredTimeZoneReceiver = false;

        boolean mMute;

        Time mTime;

        boolean mShouldDrawColons;

        float mXCenter;
        float mYCenter;

        int mInteractiveBackgroundColor = DigitalWatchFaceUtil.COLOR_VALUE_DEFAULT_AND_AMBIENT_BACKGROUND;
        int mInteractiveHourDigitsColor = DigitalWatchFaceUtil.COLOR_VALUE_DEFAULT_AND_AMBIENT_HOUR_DIGITS;
        int mInteractiveMinuteDigitsColor = DigitalWatchFaceUtil.COLOR_VALUE_DEFAULT_AND_AMBIENT_MINUTE_DIGITS;
        int mInteractiveSecondDigitsColor = DigitalWatchFaceUtil.COLOR_VALUE_DEFAULT_AND_AMBIENT_SECOND_DIGITS;

        boolean mLowBitAmbient;

        @Override
        public void onCreate(SurfaceHolder holder) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onCreate");
            }
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(DigitalWatchFaceService.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_OPACITY_MODE_TRANSLUCENT)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .build());

            DigitalWatchFaceService.temperature = Double.valueOf(0);
            DigitalWatchFaceService.city = "Unknown";

            NORMAL_TYPEFACE = Typeface.createFromAsset(getAssets(), "typography/Roboto-Thin.ttf");
            SECOND_TYPEFACE = Typeface.createFromAsset(getAssets(), "typography/Roboto-Bold.ttf");
            BOLD_TYPEFACE = NORMAL_TYPEFACE;

            Resources resources = DigitalWatchFaceService.this.getResources();
            //mYCenter = resources.getDimension(R.dimen.digital_y_offset);

            mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(mInteractiveBackgroundColor);
            mBackgroundPaint.setAntiAlias(true);
            mHourPaint = createTextPaintTime(mInteractiveHourDigitsColor,NORMAL_TYPEFACE);
            mHourPaint.setFakeBoldText(true);
            mMinutePaint = createTextPaintTime(mInteractiveMinuteDigitsColor,NORMAL_TYPEFACE);
            mSecondPaint = createTextPaintTime(mInteractiveSecondDigitsColor,SECOND_TYPEFACE);
            mTempPaint = createTextPaintTime(mInteractiveMinuteDigitsColor,NORMAL_TYPEFACE);
            mTempPaint.setFakeBoldText(true);
            mDatePaint = createTextPaintTime(mInteractiveMinuteDigitsColor,NORMAL_TYPEFACE);
            mDatePaint.setFakeBoldText(true);
            mCityPaint = createTextPaintTime(mInteractiveMinuteDigitsColor,NORMAL_TYPEFACE);
            mCityPaint.setFakeBoldText(true);

            //mColonPaint = createTextPaintTime(resources.getColor(R.color.digital_colons),NORMAL_TYPEFACE);

            backgroundBit = BitmapFactory.decodeResource(getResources(), R.drawable.basketball_bg_resized);
            colorTextGeneral = getResources().getColor(R.color.white);

            mTime = new Time();
        }

        @Override
        public void onDestroy() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            super.onDestroy();
        }

        private Paint createTextPaintSecond(int defaultInteractiveColor) {
            return createTextPaint(defaultInteractiveColor, NORMAL_TYPEFACE);
        }

        private Paint createTextPaintTime(int defaultInteractiveColor, Typeface typeface){
            return createTextPaint(defaultInteractiveColor, typeface);
        }

        private Paint createTextPaint(int defaultInteractiveColor, Typeface typeface) {
            Paint paint = new Paint();
            paint.setColor(defaultInteractiveColor);
            paint.setTypeface(typeface);
            paint.setAntiAlias(true);
            return paint;
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onVisibilityChanged: " + visible);
            }
            super.onVisibilityChanged(visible);

            if (visible) {
                mGoogleApiClient.connect();

                registerReceiver();

                // Update time zone in case it changed while we weren't visible.
                mTime.clear(TimeZone.getDefault().getID());
                mTime.setToNow();
            } else {
                unregisterReceiver();

                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    Wearable.DataApi.removeListener(mGoogleApiClient, this);
                    mGoogleApiClient.disconnect();
                }
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            DigitalWatchFaceService.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            DigitalWatchFaceService.this.unregisterReceiver(mTimeZoneReceiver);
        }


        @Override
        public void onApplyWindowInsets(WindowInsets insets) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onApplyWindowInsets: " + (insets.isRound() ? "round" : "square"));
            }
            super.onApplyWindowInsets(insets);

            // Load resources that have alternate values for round watches.
            Resources resources = DigitalWatchFaceService.this.getResources();
            boolean isRound = insets.isRound();
            mXCenter = resources.getDimension( isRound? R.dimen.digital_x_offset_round : R.dimen.digital_x_offset );

            float textSizeHour = resources.getDimension( isRound ? R.dimen.digital_text_size_round_hour : R.dimen.digital_text_size_hour );
            float textSizeMinute = resources.getDimension(isRound ? R.dimen.digital_text_size_round_minute : R.dimen.digital_text_size_minute);
            float textSizeSecond = 18;
            float textSizeTemperature = 30;
            float textSizeDate = 24;

            mHourPaint.setTextSize(textSizeHour);
            mMinutePaint.setTextSize(textSizeMinute);
            mSecondPaint.setTextSize(textSizeSecond);
            mTempPaint.setTextSize(textSizeTemperature);
            mDatePaint.setTextSize(textSizeDate);
            mCityPaint.setTextSize(20);

            //mColonWidth = mColonPaint.measureText(COLON_STRING);
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);

            boolean burnInProtection = properties.getBoolean(PROPERTY_BURN_IN_PROTECTION, false);
            //mHourPaint.setTypeface(burnInProtection ? NORMAL_TYPEFACE : BOLD_TYPEFACE);

            mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);

            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onPropertiesChanged: burn-in protection = " + burnInProtection
                        + ", low-bit ambient = " + mLowBitAmbient);
            }
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onTimeTick: ambient = " + isInAmbientMode());
            }
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onAmbientModeChanged: " + inAmbientMode);
            }
            adjustPaintColorToCurrentMode(mBackgroundPaint, mInteractiveBackgroundColor,
                    DigitalWatchFaceUtil.COLOR_VALUE_DEFAULT_AND_AMBIENT_BACKGROUND);
            adjustPaintColorToCurrentMode(mHourPaint, mInteractiveHourDigitsColor,
                    DigitalWatchFaceUtil.COLOR_VALUE_DEFAULT_AND_AMBIENT_HOUR_DIGITS);
            adjustPaintColorToCurrentMode(mMinutePaint, mInteractiveMinuteDigitsColor,
                    DigitalWatchFaceUtil.COLOR_VALUE_DEFAULT_AND_AMBIENT_MINUTE_DIGITS);
            adjustPaintColorToCurrentMode(mSecondPaint, mInteractiveSecondDigitsColor,
                    DigitalWatchFaceUtil.COLOR_VALUE_DEFAULT_AND_AMBIENT_SECOND_DIGITS);

            if (mLowBitAmbient) {
                boolean antiAlias = !inAmbientMode;
                mHourPaint.setAntiAlias(antiAlias);
                mMinutePaint.setAntiAlias(antiAlias);
                mSecondPaint.setAntiAlias(antiAlias);
                mColonPaint.setAntiAlias(antiAlias);
                mDatePaint.setAntiAlias(antiAlias);
                mTempPaint.setAntiAlias(antiAlias);
                mCityPaint.setAntiAlias(antiAlias);
            }
            invalidate();
            updateTimer();
        }

        private void adjustPaintColorToCurrentMode(Paint paint, int interactiveColor,
                int ambientColor) {
            paint.setColor(isInAmbientMode() ? ambientColor : interactiveColor);
        }

        @Override
        public void onInterruptionFilterChanged(int interruptionFilter) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onInterruptionFilterChanged: " + interruptionFilter);
            }
            super.onInterruptionFilterChanged(interruptionFilter);

            boolean inMuteMode = interruptionFilter == WatchFaceService.INTERRUPTION_FILTER_NONE;
            // We only need to update once a minute in mute mode.
            setInteractiveUpdateRateMs(inMuteMode ? MUTE_UPDATE_RATE_MS : NORMAL_UPDATE_RATE_MS);

            if (mMute != inMuteMode) {
                mMute = inMuteMode;
                int alpha = inMuteMode ? MUTE_ALPHA : NORMAL_ALPHA;
                mHourPaint.setAlpha(alpha);
                mMinutePaint.setAlpha(alpha);
                mColonPaint.setAlpha(alpha);
                mTempPaint.setAlpha(alpha);
                mDatePaint.setAlpha(alpha);
                mCityPaint.setAlpha(alpha);
                //mAmPmPaint.setAlpha(alpha);
                invalidate();
            }
        }

        public void setInteractiveUpdateRateMs(long updateRateMs) {
            if (updateRateMs == mInteractiveUpdateRateMs) {
                return;
            }
            mInteractiveUpdateRateMs = updateRateMs;

            // Stop and restart the timer so the new update rate takes effect immediately.
            if (shouldTimerBeRunning()) {
                updateTimer();
            }
        }

        private void updatePaintIfInteractive(Paint paint, int interactiveColor) {
            if (!isInAmbientMode() && paint != null) {
                paint.setColor(interactiveColor);
                paint.setAntiAlias(true);
            }
        }

        private void setInteractiveBackgroundColor(int color) {
            /*mInteractiveBackgroundColor = color;
            //Change drawables
            int constantColor = 0;
            if( color == Color.parseColor(getBaseContext().getString(R.string.color_black))) {
                constantColor = Constants.BACKGROUND_BLACK;
            }else{
                if(color == Color.parseColor(getBaseContext().getString(R.string.color_white))){
                    constantColor = Constants.BACKGROUND_WHITE;
                }
            }
            changeDrawables(constantColor);
            updatePaintIfInteractive(mBackgroundPaint, color);*/
        }

        private String formatTwoDigitNumber(int hour) {
            return String.format("%02d", hour);
        }


        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            mTime.setToNow();

            //shouldChangeColorMode();

            mShouldDrawColons = (System.currentTimeMillis() % 1000) < 500;

            canvas.drawBitmap(backgroundBit, -60,0,mBackgroundPaint);

            extraHeight = extraHeight(bounds.height());

            mYCenter = (bounds.height() + extraHeight) / 2;
            mXCenter = bounds.width() / 2;

            String hourString = String.valueOf(mTime.hour);
            String minuteString = formatTwoDigitNumber(mTime.minute);
            String secondString = String.valueOf(mTime.second);

            float timeTotalWidth = mHourPaint.measureText(hourString) + mColonWidth + mMinutePaint.measureText(minuteString);

            //Declares
            float mYTime = mYCenter + 20;

            drawTime(canvas,mXCenter,mYTime,hourString,minuteString,secondString);
            drawTemperature(mXCenter,mYTime,canvas);
            drawDate(mXCenter,mYTime,canvas);
            drawLocation(mXCenter,mYTime,canvas);

            if(isInAmbientMode())
                inAmbientMode();
            else
               // drawSeconds(canvas, mTime);


            drawMoto360Line(canvas,true);
        }

        private float extraHeight(float height){
            if( height == 290){ //Moto360 watch
                return 30;
            }else
                return 0;
        }

        private void inAmbientMode(){
            // In ambient and mute modes, draw AM/PM. Otherwise, draw a second blinking
            // colon followed by the seconds.
            /*
            if (isInAmbientMode() || mMute) {
                x += mColonWidth;
                //canvas.drawText(getAmPmString(mTime.hour), x, mYCenter, mAmPmPaint); PAINT AM AND PM
            } else {
                if (mShouldDrawColons) {
                    canvas.drawText(COLON_STRING, x, mYCenter, mColonPaint);
                }
                x += mColonWidth;
                canvas.drawText(formatTwoDigitNumber(mTime.second), x, mYCenter,
                        mSecondPaint);
            }*/
        }

        private void drawTime(Canvas canvas,float mXCenter,float mYTime,String hourString, String minuteString,String secondsString){

            float x1 = 0;
            x1 = (mHourPaint.measureText(hourString) + mColonWidth + mMinutePaint.measureText(minuteString)) / 2;

            //Starting point
            float x = mXCenter - x1;
            //Draw the hours
            canvas.drawText(hourString, x, mYTime, mHourPaint);
            x += mHourPaint.measureText(hourString);
            // Draw the minutes.
            canvas.drawText(minuteString, x, mYTime, mMinutePaint);
            x += mMinutePaint.measureText(minuteString);

            secondsAnimation(canvas,mYTime,secondsString);

        }

        private void secondsAnimation(Canvas canvas,float mYTime,String seconds){
            //DrawSeconds
            //mSecondPaint.setTextSize(18);
            float xsecondStart = (mSecondPaint.measureText(seconds))/2;
            mSecondPaint.setColor(getResources().getColor(R.color.pure_white));
            canvas.drawText(seconds,mXCenter-xsecondStart,mYTime+31,mSecondPaint);
            Paint rAnimationSecondP = new Paint();
            rAnimationSecondP.setAntiAlias(true);
            rAnimationSecondP.setColor(getResources().getColor(R.color.white));
            rAnimationSecondP.setStyle(Paint.Style.STROKE);
            rAnimationSecondP.setStrokeWidth(2);
            rAnimationSecondP.setShadowLayer(1, 0, 0, getResources().getColor(R.color.white));
            //Line 1
            if( secondAnimationNumber1 == 361 ){
                secondAnimationNumber1 = 0;
            }else {
                secondAnimationNumber1 = secondAnimationNumber1+4;
            }
            canvas.drawArc(mXCenter-15 , mYTime+10 , mXCenter+15 , mYTime+40 ,  secondAnimationNumber1, 50, false, rAnimationSecondP);
            //Line 2
            if( secondAnimationNumber2 == 361 ){
                secondAnimationNumber2 = 0;
            }else {
                secondAnimationNumber2 = secondAnimationNumber2+4;
            }
            canvas.drawArc(mXCenter-15 , mYTime+10 , mXCenter+15 , mYTime+40 ,  secondAnimationNumber2, 50, false, rAnimationSecondP);
        }

        private void drawTemperature(float mXCenter,float mYTimeText,Canvas canvas){
            canvas.drawText(decimalFormat.format(temperature)+"º",mXCenter+70,mYTimeText-70,mTempPaint);
        }

        private void drawDate(float mXCenter,float mYTimeText,Canvas canvas){
            String dateToShow = mTime.monthDay+"/"+mTime.month;
            canvas.drawText(dateToShow,mXCenter-70-(mDatePaint.measureText(dateToShow)),mYTimeText-70,mDatePaint);
        }

        private void drawLocation(float mXCenter,float mYTimeText,Canvas canvas){
            canvas.drawText(city,mXCenter-((mCityPaint.measureText(city))/2),mYTimeText+90,mCityPaint);
        }


        private void drawWidgetsMode1(Canvas canvas,String temperature){

            //Drawing Percentaje rec
            Paint percPaint = new Paint();
            percPaint.setAntiAlias(true);
            percPaint.setColor(colorTextGeneral);
            percPaint.setStyle(Paint.Style.STROKE);
            percPaint.setStrokeWidth(2);
            percPaint.setTypeface(BOLD_TYPEFACE);
            percPaint.setShadowLayer(1, 0, 0, colorTextGeneral);
            canvas.drawRoundRect(new RectF(mXCenter - 10, mYCenter + 105, mXCenter +70, (mYCenter*2) - 35 ), 10, 10, percPaint);
            //block transparency
            percPaint.setColor(mBackgroundPaint.getColor());
            percPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(mXCenter + 70, (mYCenter * 2) - 60, 25, percPaint);
            //Drawing percentaje text
            Paint textPercPaint = new Paint();
            textPercPaint.setAntiAlias(true);
            textPercPaint.setTypeface(BOLD_TYPEFACE);
            textPercPaint.setTextSize(14);
            int xplusnumber = 14;
            /*if(isActionUp) {
                textPercPaint.setColor(getResources().getColor(R.color.green));
            }
            else {
                textPercPaint.setColor(getResources().getColor(R.color.red));
            }

            canvas.drawText(percentajeActionChange +"%", mXCenter, (mYCenter*2) - 40,textPercPaint);*/

            //Draw Date
            Paint datePaint = new Paint();
            datePaint.setAntiAlias(true);
            datePaint.setColor(getResources().getColor(R.color.gray_1));
            datePaint.setTextSize(12);
            datePaint.setTypeface(BOLD_TYPEFACE);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK,mTime.weekDay+1);
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            String dayOfTheWeek = sdf.format(cal.getTime());
            String dateToShow = dayOfTheWeek+", "+mTime.monthDay;
            float dateTextWidth = datePaint.measureText(dateToShow);
            canvas.drawText(dateToShow , (mXCenter - (dateTextWidth/2)) , mYCenter + 90 , datePaint);

        }

        private void drawWidgetsMode2(Canvas canvas,String globActions,String degressTemperature,String shortLocation,boolean isActionUp, Bitmap arrowBit){

            /*//Drawing widget 2
            canvas.drawRoundRect(new RectF(mXCenter + 23, mYCenter + 75, mXCenter * 2 - 70, (mYCenter * 2) - 30), 30, 30, wPaint);
            //Draw text widget 2
            Paint textPaintW2 = new Paint();
            textPaintW2.setAntiAlias(true);
            textPaintW2.setColor(colorTextGeneral);
            textPaintW2.setTextSize(21);
            textPaintW2.setTypeface(BOLD_TYPEFACE);
            //test
            if(globActions == null)
                globActions = "14.07";

            float x = textPaintW2.measureText(globActions);
            String[] aux = globActions.split("\\.");

            canvas.drawText( aux[0].toString() , ( ((mXCenter*2 - 70) -(((mXCenter*2 - 70) - (mXCenter + 23))/2)) - (x/2) ) + 5, ((mYCenter * 2) - 58), textPaintW2);
            textPaintW2.setTextSize(12);
            float startDecimal = textPaintW2.measureText(aux[0].toString());
            if( aux.length > 1 ) {
                canvas.drawText("  ." + aux[1].toString(), ( ((mXCenter*2 - 70) -(((mXCenter*2 - 70) - (mXCenter + 23))/2)) - (x/2) + startDecimal + 9 ), ((mYCenter * 2) - 58), textPaintW2);
            }
            Paint arrowPaint = new Paint();
            arrowPaint.setAntiAlias(true);
            canvas.drawBitmap( arrowBit, ((mXCenter*2 - 70) -(((mXCenter*2 - 70) - (mXCenter + 23))/2)) - (arrowBit.getWidth()/2) , ((mYCenter * 2) - 52) ,arrowPaint);
            */
        }

        private void drawSeconds(Canvas canvas, Time mTime){


            int seconds = mTime.second;
            if( seconds == 0){
                degressOfSeconds = 360;
            }else {
                if (degressOfSeconds > 360) {
                    degressOfSeconds = 6;
                } else {
                    degressOfSeconds = 6 * seconds;
                }
            }

            //SweepGradient gradient1 = new SweepGradient(200, 520,Color.WHITE, getResources().getColor(R.color.globant_green));

            Paint p = new Paint();
            p.setColor(getResources().getColor(R.color.globant_green));
            p.setAntiAlias(true);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(3);
            //p.setAlpha(130);
            //p.setShader(gradient1);
            p.setShadowLayer(2, 0, 0, Color.WHITE);
            canvas.drawArc(0 + 5 ,0 + 5, (mXCenter *2) - 5 , (mYCenter *2) - 5, 90 , degressOfSeconds, false, p);
        }

        private void drawMoto360Line(Canvas canvas, boolean drawOrNot){
            if(drawOrNot) {
                Paint redMoto360Line = new Paint();
                redMoto360Line.setColor(Color.RED);
                redMoto360Line.setStrokeWidth(1);
                canvas.drawLine(0, (mYCenter * 2) - 30, mXCenter * 2, (mYCenter * 2) - 30, redMoto360Line);
            }
        }



        private void updateTimer() {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "updateTimer");
            }
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }


        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        private void updateConfigDataItemAndUiOnStartup() {
            DigitalWatchFaceUtil.fetchConfigDataMap(mGoogleApiClient,
                    new DigitalWatchFaceUtil.FetchConfigDataMapCallback() {
                        @Override
                        public void onConfigDataMapFetched(DataMap startupConfig) {
                            // If the DataItem hasn't been created yet or some keys are missing,
                            // use the default values.
                            setDefaultValuesForMissingConfigKeys(startupConfig);
                            DigitalWatchFaceUtil.putConfigDataItem(mGoogleApiClient, startupConfig);

                            updateUiForConfigDataMap(startupConfig);
                        }
                    }
            );
        }

        private void setDefaultValuesForMissingConfigKeys(DataMap config) {
            addIntKeyIfMissing(config, DigitalWatchFaceUtil.KEY_BACKGROUND_COLOR,
                    DigitalWatchFaceUtil.COLOR_VALUE_DEFAULT_AND_AMBIENT_BACKGROUND);
            addIntKeyIfMissing(config, DigitalWatchFaceUtil.KEY_HOURS_COLOR,
                    DigitalWatchFaceUtil.COLOR_VALUE_DEFAULT_AND_AMBIENT_HOUR_DIGITS);
            addIntKeyIfMissing(config, DigitalWatchFaceUtil.KEY_MINUTES_COLOR,
                    DigitalWatchFaceUtil.COLOR_VALUE_DEFAULT_AND_AMBIENT_MINUTE_DIGITS);
            addIntKeyIfMissing(config, DigitalWatchFaceUtil.KEY_SECONDS_COLOR,
                    DigitalWatchFaceUtil.COLOR_VALUE_DEFAULT_AND_AMBIENT_SECOND_DIGITS);
        }

        private void addIntKeyIfMissing(DataMap config, String key, int color) {
            if (!config.containsKey(key)) {
                config.putInt(key, color);
            }
        }

        @Override // DataApi.DataListener
        public void onDataChanged(DataEventBuffer dataEvents) {
            try {
                for (DataEvent dataEvent : dataEvents) {
                    if (dataEvent.getType() != DataEvent.TYPE_CHANGED) {
                        continue;
                    }

                    DataItem dataItem = dataEvent.getDataItem();
                    if (!dataItem.getUri().getPath().equals(
                            DigitalWatchFaceUtil.PATH_WITH_FEATURE)) {
                        continue;
                    }

                    DataMapItem dataMapItem = DataMapItem.fromDataItem(dataItem);
                    DataMap config = dataMapItem.getDataMap();
                    if (Log.isLoggable(TAG, Log.DEBUG)) {
                        Log.d(TAG, "Config DataItem updated:" + config);
                    }
                    updateUiForConfigDataMap(config);
                }
            } finally {
                dataEvents.close();
            }
        }

        private void updateUiForConfigDataMap(final DataMap config) {
            boolean uiUpdated = false;
            for (String configKey : config.keySet()) {
                if (!config.containsKey(configKey)) {
                    continue;
                }
                int color = config.getInt(configKey);
                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    Log.d(TAG, "Found watch face config key: " + configKey + " -> "
                            + Integer.toHexString(color));
                }
                if (updateUiForKey(configKey, color)) {
                    uiUpdated = true;
                }
            }
            if (uiUpdated) {
                invalidate();
            }
        }


        private boolean updateUiForKey(String configKey, int color) {
            if (configKey.equals(DigitalWatchFaceUtil.KEY_BACKGROUND_COLOR)) {
                setInteractiveBackgroundColor(color);
            } /*else if (configKey.equals(DigitalWatchFaceUtil.KEY_HOURS_COLOR)) {
                setInteractiveHourDigitsColor(color);
            } else if (configKey.equals(DigitalWatchFaceUtil.KEY_MINUTES_COLOR)) {
                setInteractiveMinuteDigitsColor(color);
            } else if (configKey.equals(DigitalWatchFaceUtil.KEY_SECONDS_COLOR)) {
                setInteractiveSecondDigitsColor(color);
            } else {
                Log.w(TAG, "Ignoring unknown config key: " + configKey);
                return false;
            }*/
            return true;
        }

        @Override  // GoogleApiClient.ConnectionCallbacks
        public void onConnected(Bundle connectionHint) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onConnected: " + connectionHint);
            }
            Wearable.DataApi.addListener(mGoogleApiClient, Engine.this);
            updateConfigDataItemAndUiOnStartup();
        }

        @Override  // GoogleApiClient.ConnectionCallbacks
        public void onConnectionSuspended(int cause) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onConnectionSuspended: " + cause);
            }
        }

        @Override  // GoogleApiClient.OnConnectionFailedListener
        public void onConnectionFailed(ConnectionResult result) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onConnectionFailed: " + result);
            }
        }
    }
}
