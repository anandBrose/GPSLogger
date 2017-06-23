package com.anand.brose.gpslogger.transport;

import java.io.File;

/**
 * Created by Anand on 23-06-2017.
 */

public interface ITransportView {
    File getFileDirectory();
    boolean checkStoragePermission();
    void showFileStoredMessage();
    void showNoStoragePermissionMessage();
    void openXmlFile(File file);
}
