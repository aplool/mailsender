package com.aplool.mail.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by leokao on 11/25/2016.
 */
public class IpAddress {
    private static final Logger mLogger = LoggerFactory.getLogger(IpAddress.class);
    private final int value;

    public IpAddress(int value) {
        this.value = value;
    }

    public IpAddress(String stringValue) {
        String[] parts = stringValue.split("\\.");
        if (parts.length != 4) {
            throw new IllegalArgumentException();
        }
        for (int i =0; i<4; i++) {
            if ((Integer.parseInt(parts[i]) < 0) || (Integer.parseInt(parts[i]) > 255)) {
                mLogger.error("{}",Integer.parseInt(parts[i]));
                throw new IllegalArgumentException();
            }
        }
        value =
                (Integer.parseInt(parts[0], 10) << (8 * 3)) & 0xFF000000 |
                        (Integer.parseInt(parts[1], 10) << (8 * 2)) & 0x00FF0000 |
                        (Integer.parseInt(parts[2], 10) << (8 * 1)) & 0x0000FF00 |
                        (Integer.parseInt(parts[3], 10) << (8 * 0)) & 0x000000FF;
    }

    public int getOctet(int i) {

        if (i < 0 || i >= 4) throw new IndexOutOfBoundsException();

        return (value >> (i * 8)) & 0x000000FF;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 3; i >= 0; --i) {
            sb.append(getOctet(i));
            if (i != 0) sb.append(".");
        }

        return sb.toString();

    }

    public boolean equals(Object obj) {
        if (obj instanceof IpAddress) {
            return value == ((IpAddress) obj).value;
        }
        if (obj instanceof String) {
            return value == (new IpAddress((String) obj)).getValue();
        }
        return false;
    }

    public int getValue() {
        return value;
    }

    public IpAddress nextIP() {
        return new IpAddress(value + 1);
    }

    public IpAddress nextServerIP() {
        IpAddress ipAddress = new IpAddress(value + 1);

        for (int i=0;i<4;i++) {
            if ((ipAddress.getOctet(i) == 255) ||
                    (ipAddress.getOctet(i) ==0) ||
                    (ipAddress.getOctet(3) == 224)||
                    (ipAddress.getOctet(3) == 127)) {
                ipAddress = ipAddress.nextServerIP();
            }
        }
        return ipAddress;
    }
}
