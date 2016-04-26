/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mife.sandbox.view;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 *
 * @author Dialog
 */
public class APIRequestWrapper extends HttpServletRequestWrapper {

    private HttpServletRequest original;
    private byte[] reqBytes;
    private boolean firstTime = true;

    public APIRequestWrapper(HttpServletRequest request) {
        super(request);
        original = request;
    }

    @Override
    public BufferedReader getReader() throws IOException {

        if (firstTime) {
            firstTime();
        }

        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(reqBytes));
        return new BufferedReader(isr);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        if (firstTime) {
            firstTime();
        }

        ServletInputStream sis = new ServletInputStream() {
            private int i;

            @Override
            public int read() throws IOException {
                byte b;
                if (reqBytes.length > i) {
                    b = reqBytes[i++];
                } else {
                    b = -1;
                }

                return b;
            }
        };

        return sis;
    }

    private void firstTime() throws IOException {
        firstTime = false;
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = original.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        reqBytes = buffer.toString().getBytes();
    }
}
