/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017. Shendy Aditya Syamsudin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.gamatechno.ggfw.core;

import com.gamatechno.ggfw.utils.Base64;

import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Project     : GGFW
 * Company     : PT. Gamatechno Indonesia
 * File        : Xcode.java
 * User        : Shendy Aditya S. a.k.a xcod
 * Date        : 03 October 2017
 * Time        : 10:40 AM
 */
public class Xcode {

    public static String pack(String msg, String eKey, String aKey){
        try{
            // Init vector
            SecureRandom random = new SecureRandom();
            byte[] ivBytes		= random.generateSeed(16);

            // Key bytes
            byte[] eKeyBytes	= eKey.getBytes("UTF-8");
            byte[] aKeyBytes	= aKey.getBytes("UTF-8");

            // Prepare chiper
            SecretKeySpec key	= new SecretKeySpec(eKeyBytes, "AES");
            IvParameterSpec iv 	= new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

            // Encrypt
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            byte[] encrypted  = cipher.doFinal(msg.getBytes());

            // Build document
            byte[] docBytes = new byte[ivBytes.length + encrypted.length];
            System.arraycopy(ivBytes	, 0, docBytes, 0				, ivBytes.length);
            System.arraycopy(encrypted	, 0, docBytes, ivBytes.length	, encrypted.length);

            // Build signature
            Mac hmac 	 = Mac.getInstance("HmacSHA256");
            SecretKeySpec macKey = new SecretKeySpec(aKeyBytes, "HmacSHA256");
            hmac.init(macKey);

            byte[] sigBytes = hmac.doFinal(docBytes);

            // Build certified document
            byte[] finalBytes = new byte[sigBytes.length + docBytes.length];
            System.arraycopy(sigBytes, 0, finalBytes, 0				 , sigBytes.length);
            System.arraycopy(docBytes, 0, finalBytes, sigBytes.length, docBytes.length);

            String ciphertext = Base64.getEncoder().encodeToString(finalBytes);
            return ciphertext;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }


    public static String unpack(String msg, String eKey, String aKey){
        try{
            byte[] msgBytes = Base64.getDecoder().decode(msg);
            byte[] sigBytes = Arrays.copyOfRange(msgBytes, 0, 32);
            byte[] ivBytes 	= Arrays.copyOfRange(msgBytes, 32, 48);
            byte[] txtBytes	= Arrays.copyOfRange(msgBytes, 48, msgBytes.length);
            byte[] docBytes	= Arrays.copyOfRange(msgBytes, 32, msgBytes.length);

            // Key bytes
            byte[] eKeyBytes = eKey.getBytes("UTF-8");
            byte[] aKeyBytes = aKey.getBytes("UTF-8");

            // Calculate signature
            Mac 		  hmac 	 = Mac.getInstance("HmacSHA256");
            SecretKeySpec macKey = new SecretKeySpec(aKeyBytes, "HmacSHA256");
            hmac.init(macKey);

            byte[] calculatedSig = hmac.doFinal(docBytes);

            // Authenticate
            if(Arrays.equals(sigBytes, calculatedSig)){
                // Prepare chiper
                SecretKeySpec	key	= new SecretKeySpec(eKeyBytes, "AES");
                IvParameterSpec iv 	= new IvParameterSpec(ivBytes);

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

                // Decrypt
                cipher.init(Cipher.DECRYPT_MODE, key, iv);

                byte[] decrypted  = cipher.doFinal(txtBytes);

                String plainText = new String(decrypted, "UTF-8");
                return plainText;
            }
            else{
                System.out.println("Auth failed.");
                return null;
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
