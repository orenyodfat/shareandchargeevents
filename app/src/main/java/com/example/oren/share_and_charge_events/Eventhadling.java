package com.example.oren.share_and_charge_events;

import android.os.AsyncTask;
import android.util.Log;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by oren on 02/03/2017.
 */

public class Eventhadling extends Contract {

    private final String TAG = "eventhadling";
    private static Web3j web3;
    private final String CONTRACT_ADDRESS = "0x5c66d6305ebec1980f94b852c03fd752fba9a1ae";

    private static Listener<EventLogPoleSetUp> PoleSetUplistener;
    private static Listener<EventRented> Rentedlistener;
    private static Listener<EventReturn> Returnlistener;
    private static Listener<String> Versionlistener;
    private static Listener<BigInteger> Blockslistener;
    private static final boolean WITH_LOG_HISTORY = false;

    public Eventhadling(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }


    public void Connect(Listener<BigInteger> blockListner, Listener<EventLogPoleSetUp> poleSetUplistener, Listener<EventRented> rentedlistener, Listener<EventReturn> returnlistener, Listener<String> versionlistener)
    {
        this.PoleSetUplistener = poleSetUplistener;
        this.Rentedlistener = rentedlistener;
        this.Returnlistener = returnlistener;
        this.Versionlistener = versionlistener;
        this.Blockslistener = blockListner;
        new Web3Connect().execute();
    }
    private void CreateWeb3Connection()
    {
        String infura = "https://mainnet.infura.io/orenshare&chargetoken";
        String mynode = "http://ec2-52-17-240-245.eu-west-1.compute.amazonaws.com:8545";
        web3 = Web3jFactory.build(new HttpService(mynode));
        Web3ClientVersion web3ClientVersion = null;
        try {
            web3ClientVersion = web3.web3ClientVersion().send();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String clientVersion = web3ClientVersion.getWeb3ClientVersion();
        Log.e(TAG,"clientVersion ->" + clientVersion );
        Versionlistener.getResult(clientVersion);

        //MainActivity.ShowNodeVersion(clientVersion);


        Subscription subscription = web3.blockObservable(false).subscribe(new Action1<EthBlock>() {

            @Override
            public void call(EthBlock ethBlock) {

                Log.e(TAG,"ethBlock ->" + ethBlock.getBlock().getNumber() );
                Blockslistener.getResult( ethBlock.getBlock().getNumber());


            }
        });


      //  eventlistener_LogPoleSetUp(CONTRACT_ADDRESS,"LogPoleSetUp");
        eventlistener_LogRented(CONTRACT_ADDRESS,"LogRented");
        eventlistener_LogReturned(CONTRACT_ADDRESS,"LogReturned");

        Log.e(TAG,"now ");



    }
    public class Web3Connect extends AsyncTask<String, Void, String> {


        // constructor
        public Web3Connect() {

        }
        @Override
        protected String doInBackground(String... params) {


            CreateWeb3Connection();
            return null;


        }
        protected void onPostExecute(String page)
        {
            //onPostExecute
        }
    }
    void eventlistener_LogPoleSetUp(String contractAddress, final String eventname) {


        Log.e(TAG, "eventlistener");
        //   Event event = new Event(eventname, Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}), Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {},new TypeReference<Uint256>() {}));

        final Event event = new Event(eventname, Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
        }), Collections.<TypeReference<?>>emptyList());
        String encodedEventSignature = EventEncoder.encode(event);

        org.web3j.protocol.core.methods.request.EthFilter filter = new org.web3j.protocol.core.methods.request.EthFilter(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                contractAddress
        ).addSingleTopic(encodedEventSignature);

        web3.ethLogObservable(filter).subscribe(new Action1<org.web3j.protocol.core.methods.response.Log>() {
            @Override
            public void call(org.web3j.protocol.core.methods.response.Log log) {
                EventValues eventValues = extractEventParameters(event, log);

                List<String> topics = log.getTopics();
                EventLogPoleSetUp eventLogPoleSetUp = new EventLogPoleSetUp();
                eventLogPoleSetUp.pollid =topics.get(1);
                eventLogPoleSetUp.blocknumber = log.getBlockNumber();
                PoleSetUplistener.getResult(eventLogPoleSetUp);
                log.getBlockNumber();

                int i = 0;

                for (String str : topics) {
                    Log.e(TAG, "topic" + i + ":" + str);
                    i++;

                }

            }
        });
    }


    void eventlistener_LogReturned(String contractAddress, final String eventname)  {


        Log.e(TAG,"eventlistener_LogReturned");
        //   Event event = new Event(eventname, Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}), Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {},new TypeReference<Uint256>() {}));

        final Event event = new Event(eventname, Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}), Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {},new TypeReference<Uint256>(){},new TypeReference<Uint8>(){}));
        String encodedEventSignature = EventEncoder.encode(event);

        org.web3j.protocol.core.methods.request.EthFilter filter = new org.web3j.protocol.core.methods.request.EthFilter(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                contractAddress
        ).addSingleTopic(encodedEventSignature);

        if (WITH_LOG_HISTORY) {
            EthLog ethLog = null;
            try {
                ethLog = web3.ethGetLogs(filter).send();
                List<EthLog.LogResult> logResults = ethLog.getLogs();
                for (EthLog.LogResult logResult : logResults) {
                    EventValues eventValues = extractEventParameters(event, (org.web3j.protocol.core.methods.response.Log) logResult.get());
                    EventReturn eventReturn;
                    eventReturn = new EventReturn();
                    eventReturn.pollid = ((org.web3j.protocol.core.methods.response.Log) logResult.get()).getTopics().get(1);
                    eventReturn.ChargeAmount = ((Uint256) eventValues.getNonIndexedValues().get(0)).getValue();
                    eventReturn.ElapseSecond = ((Uint256) eventValues.getNonIndexedValues().get(1)).getValue();
                    eventReturn.Watt = ((Uint256) eventValues.getNonIndexedValues().get(2)).getValue();
                    eventReturn.ContractType = ((Uint8) eventValues.getNonIndexedValues().get(3)).getValue();
                    eventReturn.blocknumber = ((org.web3j.protocol.core.methods.response.Log) logResult.get()).getBlockNumber();
                    Log.e(TAG, "log return->ChargeAmount:" + eventReturn.ChargeAmount + ":" + eventReturn.ElapseSecond + ":" + eventReturn.Watt + ":" + eventReturn.ContractType);
                    Log.e(TAG, eventReturn.pollid + ":");
                    Returnlistener.getResult(eventReturn);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        web3.ethLogObservable(filter).subscribe(new Action1<org.web3j.protocol.core.methods.response.Log>() {
            @Override
            public void call(org.web3j.protocol.core.methods.response.Log log) {
                EventValues eventValues = extractEventParameters(event, log);

                EventReturn eventReturn;
                eventReturn = new EventReturn();
                eventReturn.pollid = log.getTopics().get(1);
                eventReturn.ChargeAmount = ((Uint256) eventValues.getNonIndexedValues().get(0)).getValue();
                eventReturn.ElapseSecond = ((Uint256) eventValues.getNonIndexedValues().get(1)).getValue();
                eventReturn.Watt = ((Uint256) eventValues.getNonIndexedValues().get(2)).getValue();
                eventReturn.ContractType = ((Uint8) eventValues.getNonIndexedValues().get(3)).getValue();
                eventReturn.blocknumber =  log.getBlockNumber();
                Log.e(TAG,"log return->ChargeAmount:"+eventReturn.ChargeAmount+":"+eventReturn.ElapseSecond+":"+eventReturn.Watt+":"+eventReturn.ContractType);
                Log.e(TAG,eventReturn.pollid+":");

                Returnlistener.getResult(eventReturn);

            }
        });
        Log.e(TAG,"eventlistener_LogReturned end");
    }

    void eventlistener_LogRented(String contractAddress, final String eventname) {


        Log.e(TAG,"eventlistener_LogRented");
        //   Event event = new Event(eventname, Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}), Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {},new TypeReference<Uint256>() {}));

        final Event event = new Event(eventname, Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}), Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {},new TypeReference<Uint256>() {},new TypeReference<Uint256>() {}));
        String encodedEventSignature = EventEncoder.encode(event);

        org.web3j.protocol.core.methods.request.EthFilter filter = new org.web3j.protocol.core.methods.request.EthFilter(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                contractAddress
        ).addSingleTopic(encodedEventSignature);

        if (WITH_LOG_HISTORY) {
            EthLog ethLog = null;
            try {
                ethLog = web3.ethGetLogs(filter).send();
                List<EthLog.LogResult> logResults = ethLog.getLogs();
                for (EthLog.LogResult logResult : logResults) {
                    EventValues eventValues = extractEventParameters(event, (org.web3j.protocol.core.methods.response.Log) logResult.get());
                    EventRented eventRented;
                    eventRented = new EventRented();
                    eventRented.pollid = ((org.web3j.protocol.core.methods.response.Log) logResult.get()).getTopics().get(1);
                    eventRented.ControllerAdrress = ((Address) eventValues.getNonIndexedValues().get(0)).toString();
                    eventRented.wattPower = ((Uint256) eventValues.getNonIndexedValues().get(1)).getValue();
                    eventRented.hoursToRent = ((Uint256) eventValues.getNonIndexedValues().get(2)).getValue();
                    eventRented.blocknumber = ((org.web3j.protocol.core.methods.response.Log) logResult.get()).getBlockNumber();
                    Log.e(TAG, "log return->ControllerAdrress:" + eventRented.ControllerAdrress + ":" + eventRented.wattPower + ":" + eventRented.hoursToRent + ":");
                    Log.e(TAG, eventRented.pollid + ":");
                    Rentedlistener.getResult(eventRented);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        web3.ethLogObservable(filter).subscribe(new Action1<org.web3j.protocol.core.methods.response.Log>() {
            @Override
            public void call(org.web3j.protocol.core.methods.response.Log log) {
                EventValues eventValues = extractEventParameters(event, log);

               // EventValues eventValues = extractEventParameters(event, (org.web3j.protocol.core.methods.response.Log) logResult.get());
                EventRented eventRented;
                eventRented = new EventRented();
                eventRented.pollid =  log.getTopics().get(1);
                eventRented.ControllerAdrress = ((Address) eventValues.getNonIndexedValues().get(0)).toString();
                eventRented.wattPower = ((Uint256) eventValues.getNonIndexedValues().get(1)).getValue();
                eventRented.hoursToRent = ((Uint256) eventValues.getNonIndexedValues().get(2)).getValue();
                eventRented.blocknumber =  log.getBlockNumber();
                Log.e(TAG,"log return->ControllerAdrress:"+eventRented.ControllerAdrress+":"+eventRented.wattPower+":"+eventRented.hoursToRent+":");
                Log.e(TAG,eventRented.pollid+":");
                Rentedlistener.getResult(eventRented);

            }
        });
    }


    public static class EventLogPoleSetUp {
        public String pollid;
        public BigInteger blocknumber;

    }

    public static class EventReturn {
        public String pollid;
        public BigInteger ChargeAmount ;
        public BigInteger ElapseSecond ;
        public BigInteger Watt ;
        public BigInteger ContractType;
        public BigInteger blocknumber;


    }

    public static class EventRented {
        public String pollid;
        public String ControllerAdrress ;
        public BigInteger hoursToRent ;
        public BigInteger wattPower ;
        public BigInteger blocknumber;

    }

}
