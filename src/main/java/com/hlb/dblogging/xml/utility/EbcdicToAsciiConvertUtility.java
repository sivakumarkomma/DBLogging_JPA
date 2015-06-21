package com.hlb.dblogging.xml.utility;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import com.hlb.dblogging.log.utility.ApplLogger;

public class EbcdicToAsciiConvertUtility {

	private static final int INITIAL_BUFFER_SIZE = 2048;
	private static final int LF = '\n';
	private static final int NEL = 0x15;
	private static final int WS = ' ';
	static final Charset ebcdicCharset = Charset.forName("Cp1047");
	static final Charset outputCharset = Charset.defaultCharset();
	private static final char[] NON_PRINTABLE_EBCDIC_CHARS = new char[] { 0x00,
			0x01, 0x02, 0x03, 0x9C, 0x09, 0x86, 0x7F, 0x97, 0x8D, 0x8E, 0x0B,
			0x0C, 0x0D, 0x0E, 0x0F, 0x10, 0x11, 0x12, 0x13, 0x9D, 0x85, 0x08,
			0x87, 0x18, 0x19, 0x92, 0x8F, 0x1C, 0x1D, 0x1E, 0x1F, 0x80, 0x81,
			0x82, 0x83, 0x84, 0x0A, 0x17, 0x1B, 0x88, 0x89, 0x8A, 0x8B, 0x8C,
			0x05, 0x06, 0x07, 0x90, 0x91, 0x16, 0x93, 0x94, 0x95, 0x96, 0x04,
			0x98, 0x99, 0x9A, 0x9B, 0x14, 0x15, 0x9E, 0x1A, 0x20, 0xA0 };

	private int fixedLength = -1;
	
	public void setFixedLength(int numberOfColumn) {
		this.fixedLength = numberOfColumn;
	}
	
	// Testing String type.
	private static String EBCDIC_STRING="�\\���@���K���K��K���@�����������@��������������\\��������@@@@@\\���@@@@@@������������@@@@@@@@@@@@�������������@@@@@@@@@@@@@@@@@@@@�������������@@@@@@@@@@@@@@@@@@@@@@@@@@@@@����������@@������������������@@@@������@@@@�����@@@@@�@@@@@@@@@@@@@@@@@@@@@@@������@@@@";
	
	private int[] loadContent(Reader reader) throws IOException {
		int[] buffer = new int[INITIAL_BUFFER_SIZE];
		int bufferIndex = 0;
		int bufferSize = buffer.length;
		int character;
		while ((character = reader.read()) != -1) {
			if (bufferIndex == bufferSize) {
				buffer = resizeArray(buffer, bufferSize + INITIAL_BUFFER_SIZE);
				bufferSize = buffer.length;
			}
			buffer[bufferIndex++] = character;
		}
		return resizeArray(buffer, bufferIndex);
	}

	public String convert(String ebcdicInputString) {
		Reader reader = null;
		StringBuffer convertedOutputString = new StringBuffer();
		try {
			reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(ebcdicInputString.getBytes()),ebcdicCharset));
			int[] ebcdicInput = loadContent(reader);
			return	convert(ebcdicInput, convertedOutputString);
		} catch (Exception e) {
			ApplLogger.getLogger().error("Error caught while converting to ASCII", e);
		} 
		return convertedOutputString.toString();
	}

	
	final int[] resizeArray(int[] orignalArray, int newSize) {
		int[] resizedArray = new int[newSize];
		for (int i = 0; i < newSize && i < orignalArray.length; i++) {
			resizedArray[i] = orignalArray[i];
		}
		return resizedArray;
	}

		int convertedChar;
		private String convert(int[] ebcdicInput, StringBuffer convertedOutputWriter) throws IOException {
		for (int index = 0; index < ebcdicInput.length; index++) {
			int character = ebcdicInput[index];
			if (fixedLength != -1 && index > 0 && index % fixedLength == 0) {
				convertedOutputWriter.append((char) LF);
			}
			if (fixedLength == -1 && character == NEL) {
				convertedChar = LF;
			} else {
				convertedChar = replaceNonPrintableCharacterByWhitespace(character);
			}
			convertedOutputWriter.append((char) convertedChar);
		}
		ApplLogger.getLogger().info("Output ASCII data is : "+convertedOutputWriter);
		return convertedOutputWriter.toString();
	}

	private int replaceNonPrintableCharacterByWhitespace(int character) {
		for (char nonPrintableChar : NON_PRINTABLE_EBCDIC_CHARS) {
			if (nonPrintableChar == (char) character) {
				return WS;
			}
		}
		return character;
	}

	

	  public static void main(String[] args) {
		  try{
		  // File source = new File("C:\\Users\\Lakshminarayana\\Downloads\\EBCDIC\\EBCDIC.txt");
		  // File destination = new File("C:\\Users\\Lakshminarayana\\Downloads\\ASCII\\ASCII.txt");
		  new EbcdicToAsciiConvertUtility().convert(EBCDIC_STRING);
		  ApplLogger.getLogger().info("Successfully Converted...!!!");
		  }catch(Exception e){
			  e.printStackTrace();
		  }
	  }
	
}