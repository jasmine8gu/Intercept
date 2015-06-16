package com.jasmine.intercept;

import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    public static String hostAddress="http://pyontia.com/intercept";
    //public static String hostAddress="http://192.168.2.14/intercept";


    public static final int INTERCEPT_SPECIAL = 999;
    public static final int NO_SPECIAL = 998;

    public static MainActivity mThis = null;

    private MyHandler handler = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView ivButton = (ImageView) findViewById(R.id.ivButton);
        ivButton.setImageResource(R.drawable.red);
        TextView tvMessage = (TextView) findViewById(R.id.tvMessage);
        tvMessage.setText(getString(R.string.no_special));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mThis = this;

        //Intent j = new Intent(this, MainService.class);
        //startService(j);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mThis = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Handler getHandler() {
        return handler;
    }

    class MyHandler extends Handler {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INTERCEPT_SPECIAL: {
                    try {
                        ImageView ivButton = (ImageView) findViewById(R.id.ivButton);
                        ivButton.setImageResource(R.drawable.green);
                        TextView tvMessage = (TextView) findViewById(R.id.tvMessage);
                        tvMessage.setText(getString(R.string.intercept_special));

                        final Timer t = new Timer();
                        t.schedule(new MyTimerTask(), 3000);
                    }
                    catch (Exception e) {
                        Log.i("jasmine", e.getMessage());
                    }
                    break;
                }

                case NO_SPECIAL: {
                    try {
                        ImageView ivButton = (ImageView) findViewById(R.id.ivButton);
                        ivButton.setImageResource(R.drawable.red);
                        TextView tvMessage = (TextView) findViewById(R.id.tvMessage);
                        tvMessage.setText(getApplicationContext().getResources().getString(R.string.no_special));
                    }
                    catch (Exception e) {
                        Log.i("jasmine", e.getMessage());
                    }
                    break;
                }
            }
        }
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage(MainActivity.NO_SPECIAL));
        }
    }

}
