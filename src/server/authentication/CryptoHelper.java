//************************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved
//
// Note: This class does not catch any exception -
// clients need to catch exception that is propagated from this
//************************************************************
package com.cisco.ettx.admin.authentication;

import java.io.*;
import java.security.*;
import javax.crypto.*;
import org.apache.log4j.Logger;

import com.cisco.ettx.admin.common.util.*;

public class CryptoHelper
{
    static final String componentName = "CryptoHelper";
	private static CryptoHelper instance = null;

    Key key;
    Cipher cipher;
    Logger logger;

	public static final String CRYPT_INIT_ERROR = "Crypto Package initialization error";

	public static CryptoHelper getInstance() {
		return  instance;
	}

    public CryptoHelper(String dir) throws Exception
    {

	logger = Logger.getLogger("CryptoHelper");
	if (instance != null)  {
		//We will not allow more than one helper
		throw new Exception(CRYPT_INIT_ERROR);
	}
	initialize(dir);
	instance = this;
    }

   private synchronized void initialize(String dir) throws Exception {
	boolean initializedKey = false;
        String key_filename;
	StringBuffer lpath = new StringBuffer(dir);
	lpath.append("/config/.key");
	String keyFile = lpath.toString();
	try {
	    ObjectInputStream in = new ObjectInputStream(
		new FileInputStream(keyFile));
	    key = (Key)in.readObject();
	    in.close();
	} catch (FileNotFoundException e) {
	    initializedKey = true;
	} 

	if (initializedKey == true) {
	    logger.debug("Generate key file" + keyFile);
	    KeyGenerator generator = KeyGenerator.getInstance("DES");
	    generator.init(new SecureRandom());
	    key = generator.generateKey();
	    ObjectOutputStream out = new ObjectOutputStream(
	    new FileOutputStream(keyFile));
	    out.writeObject(key);
	    out.close();
	}
    }

    public synchronized String encryption(String plainText)
	throws Exception
    {
	String encryptedString = null;

	cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
	cipher.init(Cipher.ENCRYPT_MODE, key);
	byte[] stringBytes = plainText.getBytes("UTF8");
	byte[] raw = cipher.doFinal(stringBytes);
	BASE64Encoder encoder = new BASE64Encoder();
	encryptedString = encoder.encode(raw);

	return encryptedString;
    }

    public synchronized String decryption(String encryptedText)
	throws Exception
    {

	String decryptedString = null;

	cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
	cipher.init(Cipher.DECRYPT_MODE, key);
	BASE64Decoder decoder = new BASE64Decoder();
	byte[] raw = decoder.decodeBuffer(encryptedText);
	byte[] stringBytes = cipher.doFinal(raw);
	decryptedString = new String(stringBytes, "UTF8");

	return decryptedString;

    }

}
