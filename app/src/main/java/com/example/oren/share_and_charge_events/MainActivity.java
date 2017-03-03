package com.example.oren.share_and_charge_events;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity implements ServiceCallbacks {

    private final String TAG = "share&charege";

    private Web3service myService;
    private boolean bound = false;

    private final String CONTRACT_ADDRESS = "0x5c66d6305ebec1980f94b852c03fd752fba9a1ae";


    private static Web3j web3;
    private Contract contract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        TransactionManager transactionManager = null;
//        BigInteger bigInteger = new BigInteger("0");



//        Eventhadling eventhadling1 = new Eventhadling(null, null, transactionManager, bigInteger, bigInteger);
//        eventhadling1.Connect(new Listener<BigInteger>() {
//            @Override
//            public void getResult(final BigInteger item) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        TextView textView = (TextView) findViewById(R.id.blockcount);
//                        textView.setText("current block-> "+item.toString());
//
//                    }
//                });
//
//            }
//        }, new Listener<Eventhadling.EventLogPoleSetUp>() {
//            @Override
//            public void getResult(final Eventhadling.EventLogPoleSetUp item) {
//
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        TextView textView = (TextView) findViewById(R.id.event_name);
//                        textView.setText("LogPoleSetUp");
//                        textView = (TextView) findViewById(R.id.poll_id);
//                        textView.setText(item.pollid);
//                        textView = (TextView) findViewById(R.id.block_str);
//                        textView.setText((CharSequence) item.blocknumber);
//                    }
//                });
//
//            }
//        }, new Listener<Eventhadling.EventRented>() {
//            @Override
//            public void getResult(final Eventhadling.EventRented item) {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        TextView textView = (TextView) findViewById(R.id.event_name);
//                        textView.setText("EventRented");
//
//                        textView = (TextView) findViewById(R.id.poll_id_str);
//                        textView.setText("poll_id:");
//                        textView = (TextView) findViewById(R.id.poll_id);
//                        textView.setText(item.pollid);
//
//                        textView = (TextView) findViewById(R.id.block_str);
//                        textView.setText("block #:"+ item.blocknumber.toString());
//
//
//                        textView = (TextView) findViewById(R.id.param1_str);
//                        textView.setText("Controller Address");
//                        textView = (TextView) findViewById(R.id.param1);
//                        textView.setText(item.ControllerAdrress.toString());
//
//
//                        textView = (TextView) findViewById(R.id.param2_str);
//                        textView.setText("hours to rent");
//                        textView = (TextView) findViewById(R.id.param2);
//                        textView.setText(item.hoursToRent.toString());
//
//
//                        textView = (TextView) findViewById(R.id.param3_str);
//                        textView.setText("watt power");
//                        textView = (TextView) findViewById(R.id.param3);
//                        textView.setText(item.wattPower.toString());
//
//
//                        textView = (TextView) findViewById(R.id.param4_str);
//                        textView.setVisibility(View.GONE);
//                        textView = (TextView) findViewById(R.id.param4);
//                        textView.setVisibility(View.GONE);
//
//
//                    }
//                });
//
//            }
//        }, new Listener<Eventhadling.EventReturn>() {
//            @Override
//            public void getResult(final Eventhadling.EventReturn item) {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        TextView textView = (TextView) findViewById(R.id.event_name);
//                        textView.setText("EventReturn");
//
//
//                        textView = (TextView) findViewById(R.id.poll_id_str);
//                        textView.setText("poll_id:");
//
//                        textView = (TextView) findViewById(R.id.poll_id);
//                        textView.setText(item.pollid);
//
//                        textView = (TextView) findViewById(R.id.block_str);
//                        textView.setText("block #:"+ item.blocknumber.toString());
//
//                        textView = (TextView) findViewById(R.id.param1_str);
//                        textView.setText("charge amount");
//                        textView = (TextView) findViewById(R.id.param1);
//                        textView.setText(item.ChargeAmount.toString());
//
//
//                        textView = (TextView) findViewById(R.id.param2_str);
//                        textView.setText("Elapse time");
//                        textView = (TextView) findViewById(R.id.param2);
//                        textView.setText(item.ElapseSecond.toString());
//
//
//                        textView = (TextView) findViewById(R.id.param3_str);
//                        textView.setText("watt");
//                        textView = (TextView) findViewById(R.id.param3);
//                        textView.setText(item.Watt.toString());
//
//
//                        textView = (TextView) findViewById(R.id.param4_str);
//                        textView.setVisibility(View.VISIBLE);
//                        textView.setText("Contract Type");
//                        textView = (TextView) findViewById(R.id.param4);
//                        textView.setVisibility(View.VISIBLE);
//                        textView.setText(item.ContractType.toString());
//
//                    }
//                });
//
//            }
//        }, new Listener<String>() {
//            @Override
//            public void getResult(String item) {
//                ShowNodeVersion(item);
//            }
//        });


        // CreateWeb3Connection();


    }


    @Override
    protected void onStart() {
        super.onStart();
        // bind to Service

            Intent intent = new Intent(this, Web3service.class);
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            Log.e(TAG,"service  run");
            startService(intent);



    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from service
        if (bound) {
            myService.setCallbacks(null); // unregister
            unbindService(serviceConnection);
            bound = false;
        }
    }

    /** Callbacks for service binding, passed to bindService() */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // cast the IBinder and get MyService instance
            Web3service.LocalBinder binder = (Web3service.LocalBinder) service;
            myService = binder.getService();
            bound = true;
            myService.setCallbacks(MainActivity.this); // register
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };



    public void ShowNodeVersion(final String version) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                TextView textView = (TextView) findViewById(R.id.node_version);
                textView.setText(version);
            }
        });

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


    @Override
    public void newblock(final BigInteger item) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = (TextView) findViewById(R.id.blockcount);
                textView.setText("current block-> "+item.toString());

            }
        });

    }

    @Override
    public void eventpollsetup(final Eventhadling.EventLogPoleSetUp item) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                TextView textView = (TextView) findViewById(R.id.event_name);
                textView.setText("LogPoleSetUp");
                textView = (TextView) findViewById(R.id.poll_id);
                textView.setText(item.pollid);
                textView = (TextView) findViewById(R.id.block_str);
                textView.setText((CharSequence) item.blocknumber);
            }
        });


    }

    @Override
    public void eventversion(String version) {

        ShowNodeVersion(version);

    }

    @Override
    public void eventrent(final Eventhadling.EventRented item) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                TextView textView = (TextView) findViewById(R.id.event_name);
                textView.setText("EventRented");

                textView = (TextView) findViewById(R.id.poll_id_str);
                textView.setText("poll_id:");
                textView = (TextView) findViewById(R.id.poll_id);
                textView.setText(item.pollid);

                textView = (TextView) findViewById(R.id.block_str);
                textView.setText("block #:"+ item.blocknumber.toString());


                textView = (TextView) findViewById(R.id.param1_str);
                textView.setText("Controller Address");
                textView = (TextView) findViewById(R.id.param1);
                textView.setText(item.ControllerAdrress.toString());


                textView = (TextView) findViewById(R.id.param2_str);
                textView.setText("hours to rent");
                textView = (TextView) findViewById(R.id.param2);
                textView.setText(item.hoursToRent.toString());


                textView = (TextView) findViewById(R.id.param3_str);
                textView.setText("watt power");
                textView = (TextView) findViewById(R.id.param3);
                textView.setText(item.wattPower.toString());


                textView = (TextView) findViewById(R.id.param4_str);
                textView.setVisibility(View.GONE);
                textView = (TextView) findViewById(R.id.param4);
                textView.setVisibility(View.GONE);


            }
        });

    }

    @Override
    public void eventreturn(final Eventhadling.EventReturn item) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                TextView textView = (TextView) findViewById(R.id.event_name);
                textView.setText("EventReturn");


                textView = (TextView) findViewById(R.id.poll_id_str);
                textView.setText("poll_id:");

                textView = (TextView) findViewById(R.id.poll_id);
                textView.setText(item.pollid);

                textView = (TextView) findViewById(R.id.block_str);
                textView.setText("block #:"+ item.blocknumber.toString());

                textView = (TextView) findViewById(R.id.param1_str);
                textView.setText("charge amount");
                textView = (TextView) findViewById(R.id.param1);
                textView.setText(item.ChargeAmount.toString());


                textView = (TextView) findViewById(R.id.param2_str);
                textView.setText("Elapse time");
                textView = (TextView) findViewById(R.id.param2);
                textView.setText(item.ElapseSecond.toString());


                textView = (TextView) findViewById(R.id.param3_str);
                textView.setText("watt");
                textView = (TextView) findViewById(R.id.param3);
                textView.setText(item.Watt.toString());


                textView = (TextView) findViewById(R.id.param4_str);
                textView.setVisibility(View.VISIBLE);
                textView.setText("Contract Type");
                textView = (TextView) findViewById(R.id.param4);
                textView.setVisibility(View.VISIBLE);
                textView.setText(item.ContractType.toString());

            }
        });


    }
}
/*

{
        "constant": false,
        "inputs": [
        {
        "name": "_poleID",
        "type": "bytes32"
        },
        {
        "name": "_wattPower",
        "type": "uint256"
        },
        {
        "name": "_secondsToRent",
        "type": "uint256"
        }
        ],
        "name": "start",
        "outputs": [],
        "payable": true,
        "type": "function"
        },
        {
        "constant": false,
        "inputs": [
        {
        "name": "_poleID",
        "type": "bytes32"
        },
        {
        "name": "measuredWatt",
        "type": "uint256"
        }
        ],
        "name": "stop",
        "outputs": [],
        "payable": false,
        "type": "function"
        },
        {
        "constant": false,
        "inputs": [
        {
        "name": "_id",
        "type": "bytes32"
        },
        {
        "name": "_deviceOwner",
        "type": "address"
        },
        {
        "name": "_meterProvider",
        "type": "address"
        },
        {
        "name": "_maxWattPower",
        "type": "uint256"
        },
        {
        "name": "_maxRentingTime",
        "type": "uint256"
        },
        {
        "name": "_priceProvider",
        "type": "uint256"
        }
        ],
        "name": "setUpChargingPole",
        "outputs": [],
        "payable": false,
        "type": "function"
        },
        {
        "inputs": [],
        "payable": false,
        "type": "constructor"
        },
        {
        "payable": false,
        "type": "fallback"
        },
        {
        "anonymous": false,
        "inputs": [
        {
        "indexed": true,
        "name": "poleID",
        "type": "bytes32"
        }
        ],
        "name": "LogPoleSetUp",
        "type": "event"
        },
        {
        "anonymous": false,
        "inputs": [
        {
        "indexed": true,
        "name": "poleID",
        "type": "bytes32"
        },
        {
        "indexed": false,
        "name": "controller",
        "type": "address"
        },
        {
        "indexed": false,
        "name": "wattPower",
        "type": "uint256"
        },
        {
        "indexed": false,
        "name": "hoursToRent",
        "type": "uint256"
        }
        ],
        "name": "LogRented",
        "type": "event"
        },
        {
        "anonymous": false,
        "inputs": [
        {
        "indexed": true,
        "name": "poleID",
        "type": "bytes32"
        },
        {
        "indexed": false,
        "name": "chargeAmount",
        "type": "uint256"
        },
        {
        "indexed": false,
        "name": "elapsedSeconds",
        "type": "uint256"
        },
        {
        "indexed": false,
        "name": "watt",
        "type": "uint256"
        },
        {
        "indexed": false,
        "name": "contractType",
        "type": "uint8"
        }
        ],
        "name": "LogReturned",
        "type": "event"
        }
        ]
        */
