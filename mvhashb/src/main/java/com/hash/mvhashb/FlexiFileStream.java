package com.hash.mvhashb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FlexiFileStream extends FileInputStream {

	public FlexiFileStream(File file) throws FileNotFoundException {
		super(file);
	}

	public FlexiFileStream(String name) throws FileNotFoundException {
		super(name);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			FlexiFileStream f = new FlexiFileStream(new File(
					"D:/DocDeduplication/samples/sample1.xml"));
			System.out.println(f.getChannel().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int readLocationByte(long location) {
		int unsignedByte = 0;
		try {
			getChannel().position(location);
			unsignedByte = read();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unsignedByte;

	}

	public int convertByteToInt(byte b) {
		int i = b & 0xff;
		return i;
	}

}
