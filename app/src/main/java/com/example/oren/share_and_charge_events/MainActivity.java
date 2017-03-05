package com.example.oren.share_and_charge_events;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.web3j.tx.TransactionManager;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "share&charege";
    private Handler mRunnableBlockNumberHandler;
    private static Eventhadling eventhadling1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        TransactionManager transactionManager = null;
        Log.e(TAG, "onStartCommand");
        BigInteger bigInteger = new BigInteger("0");
        eventhadling1 = new Eventhadling(null, null, transactionManager, bigInteger, bigInteger);
        eventhadling1.Connect(new Listener<BigInteger>() {
            @Override
            public void getResult(final BigInteger item) {

                newblock(item);

            }
        }, new Listener<Eventhadling.EventLogPoleSetUp>() {
            @Override
            public void getResult(final Eventhadling.EventLogPoleSetUp item) {

                eventpollsetup(item);


            }
        }, new Listener<Eventhadling.EventRented>() {
            @Override
            public void getResult(final Eventhadling.EventRented item) {
                eventrent(item);


            }
        }, new Listener<Eventhadling.EventReturn>() {
            @Override
            public void getResult(final Eventhadling.EventReturn item) {
                eventreturn(item);


            }
        }, new Listener<String>() {
            @Override
            public void getResult(String item) {
                eventversion(item);

            }
        });


        mRunnableBlockNumberHandler = new Handler();

        mRunnableBlockNumberHandler.post(mRunnableBlockNumber);

    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    Runnable mRunnableBlockNumber = new Runnable() {
        @Override
        public void run() {


            eventhadling1.getBlockNumberThread();
            mRunnableBlockNumberHandler.postDelayed(mRunnableBlockNumber, 10000);
        }
    };


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }


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

    public void newblock(final BigInteger item) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = (TextView) findViewById(R.id.blockcount);
                textView.setText("current block-> " + item.toString());

            }
        });

    }

    public void eventpollsetup(final Eventhadling.EventLogPoleSetUp item) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                TextView textView = (TextView) findViewById(R.id.event_name);
                textView.setText("LogPoleSetUp");
                textView = (TextView) findViewById(R.id.poll_id_str);
                textView.setText("poll_id:");
                textView = (TextView) findViewById(R.id.poll_id);
                textView.setText(item.pollid);
                textView = (TextView) findViewById(R.id.block_str);
                textView.setText("block #:" + item.blocknumber.toString());
            }
        });


    }


    public void eventversion(String version) {

        ShowNodeVersion(version);

    }

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
                textView.setText("block #:" + item.blocknumber.toString());


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
                textView.setText("block #:" + item.blocknumber.toString());

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
