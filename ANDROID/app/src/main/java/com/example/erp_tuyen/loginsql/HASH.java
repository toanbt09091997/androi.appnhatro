package com.example.erp_tuyen.loginsql;

/**
 * Created by Erp-Tuyen on 03/01/2018.
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Erp-Tuyen on 02/01/2018.
 */

public class HASH {

    public static final String md5(final String s) {
        final String md5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(md5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}