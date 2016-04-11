/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 * 
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.aggregatorblacklist;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Class FileUtil.
 */
public class FileUtil {

    /** The props. */
    private static Properties props = new Properties();

    /**
     * Creates the directory.
     *
     * @param directoryName the directory name
     * @param directoryPath the directory path
     * @return true, if successful
     */
    public static boolean createDirectory(String directoryName, String directoryPath) {
        if ((new File(directoryPath + "\\" + directoryName)).exists()) {
            return true;
        } else {
            // File or directory does not exist
            return new File(directoryPath + "\\" + directoryName).mkdirs();
        }
    }

    /**
     * Delete file.
     *
     * @param filename the filename
     */
    public static void deleteFile(String filename) {

        try {
            File f1 = new File(filename);
            boolean success = f1.delete();
            if (!success) {
                //System.out.println("Deletion failed.");
                //System.exit(0);
            } else {
                //System.out.println("File deleted.");
            }
        } catch (Exception e) {
        }
    }

    /**
     * Gets the correct file name.
     *
     * @param fileName the file name
     * @return the correct file name
     */
    public static String getCorrectFileName(String fileName) {

        // REPLACING ILLEGAL CHARACTERS, Replacing characters with an underscore '_'
        fileName = fileName.replaceAll(" ", "_");


        return fileName;
    }

    /**
     * File write.
     *
     * @param filePath the file path
     * @param data the data
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void fileWrite(String filePath, String data) throws IOException {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(filePath));
            out.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }

    }

    static {
        try {
            props.load(FileUtil.class.getResourceAsStream("/application.properties"));
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
            System.err.println(
                    "Check your Property file, it should be in application home dir, Error:"
                    + e.getCause() + "Cant load APPLICATION.properties");

            //System.exit(-1);
        } catch (IOException e) {
            System.err.println(
                    "Check your Property file, it should be in application home dir, Error:"
                    + e.getCause() + "Cant load APPLICATION.properties");
            //System.exit(-1);
        }
    }


    /**
     * Gets the application property.
     *
     * @param key the key
     * @return the application property
     */
    public static String getApplicationProperty(String key) {
        return props.getProperty(key);
    }

    /**
     * Read fully into var.
     *
     * @param fullpath the fullpath
     * @return the string
     */
    public static String ReadFullyIntoVar(String fullpath) {

        String result = "";

        try {
            FileInputStream file = new FileInputStream(fullpath);
            DataInputStream in = new DataInputStream(file);
            byte[] b = new byte[in.available()];
            in.readFully(b);
            in.close();
            result = new String(b, 0, b.length, "Cp850");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Copy.
     *
     * @param src the src
     * @param dst the dst
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void copy(String src, String dst) throws IOException {

        String fileName = src.substring(src.lastIndexOf("/") + 1);

        File fsrc = new File(src);
        File fdst = new File(dst + "/" + fileName);

        InputStream in = new FileInputStream(fsrc);
        OutputStream out = new FileOutputStream(fdst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
