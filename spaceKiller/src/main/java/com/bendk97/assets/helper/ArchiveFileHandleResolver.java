/*
 * Developed by Benjamin Lefèvre
 * Last modified 20/10/18 12:32
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.assets.helper;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;


public class ArchiveFileHandleResolver implements FileHandleResolver {
    private final FileHandle _fileHandle;


    public ArchiveFileHandleResolver(FileHandle fileHandle) {
        _fileHandle = fileHandle;
    }

    @Override
    public FileHandle resolve(String fileName) {
        return new ArchiveFileHandle(_fileHandle, fileName);
    }
}
