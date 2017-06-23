package com.anand.brose.gpslogger.transport;

import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Anand on 23-06-2017.
 */

public class LocationSenderPresentImpl implements ILocationSenderPresenter {
    ITransportView transportView;
    LocationDetailsReader locationReader;
    LocationDetailsModel locationDetailModel;
    public LocationSenderPresentImpl(ITransportView transportView, LocationDetailsReader locationReader) {
        this.transportView = transportView;
        this.locationReader=locationReader;
        locationDetailModel = new LocationDetailsModel(locationReader);
    }

    @Override
    public void sendLocation(String username, Location location) {
        locationDetailModel.pushLocation(username, location);
        if(transportView.checkStoragePermission()){
            exportToXML();
        } else {
            transportView.showNoStoragePermissionMessage();
        };
    }

    @Override
    public void exportToXML(){
        Thread exportThread = new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap map = locationDetailModel.getLocations();
                try {
                    String xmlString = generateXMLFile(map);
                    File folder = new File(transportView.getFileDirectory(), "locationdetails");

                    boolean success = true;
                    if (!folder.exists()) {
                        success = folder.mkdirs();
                    }
                    final File file = new File(folder,"locationDetails.xml");
                    FileOutputStream fileOutput = new FileOutputStream(file);
                    OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutput);
                    outputStreamWriter.write(xmlString);
                    outputStreamWriter.flush();
                    fileOutput.getFD().sync();
                    outputStreamWriter.close();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            transportView.showFileStoredMessage();
                            transportView.openXmlFile(file);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        exportThread.start();
    }

    String generateXMLFile(HashMap<String, Location> map) throws IOException {
        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();

        xmlSerializer.setOutput(writer);
        // start DOCUMENT
        xmlSerializer.startDocument("UTF-8", true);
        // open tag: <record>
        xmlSerializer.startTag("", "GPSDetails");
        xmlSerializer.startTag("", "Msgheader");
        xmlSerializer.startTag("", "TransactionIds");
        xmlSerializer.text(new Date().getTime()+"");
        xmlSerializer.endTag("", "TransactionIds");
        xmlSerializer.startTag("", "Flowname");
        xmlSerializer.text("UserLocation");
        xmlSerializer.endTag("", "Flowname");
        xmlSerializer.endTag("", "Msgheader");
        xmlSerializer.startTag("", "LocationDetails");
        for (String key : map.keySet()) {
            xmlSerializer.startTag("", "UserLocation");
            xmlSerializer.startTag("", "UserName");
            xmlSerializer.text(key);
            xmlSerializer.endTag("", "UserName");
            xmlSerializer.startTag("", "Longitude");
            xmlSerializer.text(map.get(key).getLongitude()+"");
            xmlSerializer.endTag("", "Longitude");
            xmlSerializer.startTag("", "Latitude");
            xmlSerializer.text( map.get(key).getLatitude()+"");
            xmlSerializer.endTag("", "Latitude");
            xmlSerializer.endTag("", "UserLocation");
        }
        xmlSerializer.endTag("", "LocationDetails");
        xmlSerializer.endTag("", "GPSDetails");

        // end DOCUMENT
        xmlSerializer.endDocument();

        return writer.toString();
    }
}
