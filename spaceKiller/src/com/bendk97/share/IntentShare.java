/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.share;

public interface IntentShare {

    void shareScore(String filePath);

    void verifyStoragePermissions();
}
