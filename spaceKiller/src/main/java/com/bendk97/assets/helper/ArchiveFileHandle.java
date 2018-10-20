/*
 * Developed by Benjamin Lefèvre
 * Last modified 20/10/18 12:32
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.assets.helper;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.*;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class ArchiveFileHandle extends FileHandle {
    private final FileHandle _fileHandle;
    private ZipEntry _archiveEntry;
    private final String _zipPath;


    public ArchiveFileHandle(FileHandle fileHandle_zip, FileHandle fileHandle_content) {
        super(fileHandle_content.file(), FileType.Classpath);

        //
        _fileHandle = fileHandle_zip;

        //
        String zipPath = file.getPath().replace('\\', '/');
        _zipPath = zipPath;

        try {
            _archiveEntry = getEntry(zipPath);

        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    public ArchiveFileHandle(FileHandle fileHandle_zip, File file_content) {
        super(file_content, FileType.Classpath);

        //
        _fileHandle = fileHandle_zip;

        //
        String zipPath = file.getPath().replace('\\', '/');
        _zipPath = zipPath;

        try {
            _archiveEntry = getEntry(zipPath);

        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    public ArchiveFileHandle(FileHandle fileHandle_zip, String fileName_content) {
        super(fileName_content.replace('\\', '/'), FileType.Classpath);

        //
        _fileHandle = fileHandle_zip;

        //
        String zipPath = fileName_content.replace('\\', '/');
        _zipPath = zipPath;

        try {
            _archiveEntry = getEntry(zipPath);

        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    private ZipEntry getEntry(String url) throws IOException {
        try {
            ZipInputStream zipInputStream = new ZipInputStream(_fileHandle.read());

            try {
                ZipEntry entry;
                while ((entry = zipInputStream.getNextEntry()) != null) {
                    if (!entry.isDirectory()) {
                        String name = entry.getName();
                        if(url.equals(name)) {
                            return entry;
                        }
                    }
                }
            }
            finally {
                zipInputStream.close();
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public FileHandle child(String name) {
        name = name.replace('\\', '/');
        if (file.getPath().length() == 0) return new ArchiveFileHandle(_fileHandle, new File(name));
        return new ArchiveFileHandle(_fileHandle, new File(file, name));
    }

    @Override
    public FileHandle sibling(String name) {
        name = name.replace('\\', '/');
        if (file.getPath().length() == 0) throw new GdxRuntimeException("Cannot get the sibling of the root.");
        return new ArchiveFileHandle(_fileHandle, new File(file.getParent(), name));
    }

    @Override
    public FileHandle parent() {
        File parent = file.getParentFile();
        if (parent == null) {
            if (type == FileType.Absolute)
                parent = new File("/");
            else
                parent = new File("");
        }
        return new ArchiveFileHandle(_fileHandle, parent);
    }

    private InputStream getInputStream(String url) throws IOException {
        try {
            ZipInputStream zipIsZ = new ZipInputStream(_fileHandle.read());

            try {
                ZipEntry entry;
                while ((entry = zipIsZ.getNextEntry()) != null) {
                    if (!entry.isDirectory()) {
                        String name = entry.getName();
                        if(url.equals(name)) {
                            return convertZipInputStreamToInputStream(zipIsZ);
                        }
                    }
                }
            }
            finally {
                zipIsZ.close();
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private InputStream convertZipInputStreamToInputStream(final ZipInputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copyLarge(in, out); //IOUtils.copyLarge(in, out);
        InputStream is = new ByteArrayInputStream(out.toByteArray());
        return is;
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException
    {
        byte[] buffer = new byte[4096];
        long count = 0L;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    @Override
    public InputStream read() {
        try {
            return getInputStream(_zipPath);

        } catch (IOException e) {
            throw new GdxRuntimeException("File not found: " + file + " (Archive2)");
        }
    }

    @Override
    public boolean exists() {
        return _archiveEntry != null;
    }

    @Override
    public long length () {
        return _archiveEntry.getSize();
    }

    @Override
    public long lastModified () {
        return _archiveEntry.getTime();
    }

    /////////////

    public void print() throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(_fileHandle.read());
        for (ZipEntry zipEntry; (zipEntry = zipInputStream.getNextEntry()) != null;) {
            Scanner scanner = new Scanner(zipInputStream);
            while(scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
        }
    }
}