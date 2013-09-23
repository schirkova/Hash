package com.example.test;



import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.junit.Test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Tests {
	
	private final static int BYTE_READ_SIZE = 8192;
	
	

	
	public static String getBitmapHash(Bitmap bitmap) throws NoSuchAlgorithmException, IOException {
		MessageDigest digester = MessageDigest.getInstance("MD5");
		
		for(int h=0; h<bitmap.getHeight(); h++) {
			int[] row = new int[bitmap.getWidth()];
			bitmap.getPixels(row, 0, row.length, 0, h, row.length, 1);
			
			if (h < 30) {
			 Log.i("YO! : "+h, Arrays.toString(row));
			}
			
			byte[] rowBytes = new byte[row.length];
			for(int b=0; b<row.length; b++) {
				rowBytes[b] = (byte) row[b];
			}
			
			digester.update(rowBytes);
			rowBytes = null;
			row = null;
			
		}
		
		byte[] messageDigest = digester.digest();
		return new String(bytesToHex(messageDigest));
	}
	
	public static String getBitmapHash(java.io.File file) throws NoSuchAlgorithmException, IOException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		String hash = "";
		ByteBuffer buf;
		
		buf = ByteBuffer.allocate(bitmap.getRowBytes() * bitmap.getHeight());
		
		bitmap.copyPixelsToBuffer(buf);
		hash = hash(buf.array(), "MD5");
		buf.clear();
		buf = null;
		return hash;
	}
	

	
	public static String hash (byte[] bytes_, String hashFunction) throws IOException, NoSuchAlgorithmException
	{
		MessageDigest digester;
		InputStream is = new ByteArrayInputStream(bytes_);
		digester = MessageDigest.getInstance(hashFunction); //MD5 or SHA-1
	
		  byte[] bytes = new byte[BYTE_READ_SIZE];
		  int byteCount;
		  while ((byteCount = is.read(bytes)) > 0) {
		    digester.update(bytes, 0, byteCount);
		  }
		  
		  byte[] messageDigest = digester.digest();
		  
		// Create Hex String WTF?!
		  	/*
	        StringBuffer hexString = new StringBuffer();
	        for (int i=0; i<messageDigest.length; i++)
	            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
	        */
	        
		  return new String(bytesToHex(messageDigest));
	
	}
	
	final protected static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    int v;
	    for ( int j = 0; j < bytes.length; j++ ) {
	        v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
}
