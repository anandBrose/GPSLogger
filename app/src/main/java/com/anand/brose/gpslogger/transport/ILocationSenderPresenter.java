package com.anand.brose.gpslogger.transport;

import android.location.Location;

/**
 * Created by Anand on 23-06-2017.
 */

public interface ILocationSenderPresenter {
    void sendLocation(String username, Location location);
    void exportToXML();
}
