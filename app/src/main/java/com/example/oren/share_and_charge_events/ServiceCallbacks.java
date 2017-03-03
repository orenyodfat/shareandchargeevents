package com.example.oren.share_and_charge_events;

import java.math.BigInteger;

/**
 * Created by oren on 03/03/2017.
 */

public interface ServiceCallbacks {
    void newblock(final BigInteger item);
    void eventpollsetup(final Eventhadling.EventLogPoleSetUp item);
    void eventversion(String version);
    void eventrent(final Eventhadling.EventRented item);
    void eventreturn(final Eventhadling.EventReturn item);
}
