package com.example.oren.share_and_charge_events;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.web3j.tx.TransactionManager;

import java.math.BigInteger;

public class Web3service extends Service {

    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    // Registered callbacks
    private ServiceCallbacks serviceCallbacks;
    private final String TAG = "Web3service";
    public Web3service() {

        Log.e(TAG,"CONSTRUCT");
    }




    // Class used for the client Binder.
    public class LocalBinder extends Binder {
        Web3service getService() {
            // Return this instance of MyService so clients can call public methods
            return Web3service.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG,"onBind");
        return binder;

    }

    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        TransactionManager transactionManager = null;
        Log.e(TAG,"onStartCommand");
        BigInteger bigInteger = new BigInteger("0");
        Eventhadling eventhadling1 = new Eventhadling(null, null, transactionManager, bigInteger, bigInteger);
        eventhadling1.Connect(new Listener<BigInteger>() {
            @Override
            public void getResult(final BigInteger item) {
                if (serviceCallbacks != null) {
                    serviceCallbacks.newblock(item);
                }

            }
        }, new Listener<Eventhadling.EventLogPoleSetUp>() {
            @Override
            public void getResult(final Eventhadling.EventLogPoleSetUp item) {

                if (serviceCallbacks != null) {
                    serviceCallbacks.eventpollsetup(item);
                }


            }
        }, new Listener<Eventhadling.EventRented>() {
            @Override
            public void getResult(final Eventhadling.EventRented item) {
                if (serviceCallbacks != null) {
                    serviceCallbacks.eventrent(item);
                }


            }
        }, new Listener<Eventhadling.EventReturn>() {
            @Override
            public void getResult(final Eventhadling.EventReturn item) {
                if (serviceCallbacks != null) {
                    serviceCallbacks.eventreturn(item);
                }


            }
        }, new Listener<String>() {
            @Override
            public void getResult(String item) {
                if (serviceCallbacks != null) {
                    serviceCallbacks.eventversion(item);
                }

            }
        });

        return Service.START_STICKY;
    }

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }
}
