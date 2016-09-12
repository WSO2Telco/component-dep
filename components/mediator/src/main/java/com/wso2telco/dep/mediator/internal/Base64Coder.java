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
package com.wso2telco.dep.mediator.internal;

 
// TODO: Auto-generated Javadoc
/**
 * The Class Base64Coder.
 */
public class Base64Coder {

/** The Constant systemLineSeparator. */
// The line separator string of the operating system.
private static final String systemLineSeparator = System.getProperty("line.separator");

/** The Constant map1. */
// Mapping table from 6-bit nibbles to Base64 characters.
private static final char[] map1 = new char[64];
   static {
      int i=0;
      for (char c='A'; c<='Z'; c++) map1[i++] = c;
      for (char c='a'; c<='z'; c++) map1[i++] = c;
      for (char c='0'; c<='9'; c++) map1[i++] = c;
      map1[i++] = '+'; map1[i++] = '/'; }

/** The Constant map2. */
// Mapping table from Base64 characters to 6-bit nibbles.
private static final byte[] map2 = new byte[128];
   static {
      for (int i=0; i<map2.length; i++) map2[i] = -1;
      for (int i=0; i<64; i++) map2[map1[i]] = (byte)i; }

 
/**
 * Encode string.
 *
 * @param s the s
 * @return the string
 */
public static String encodeString (String s) {
   return new String(encode(s.getBytes())); }

 
/**
 * Encode lines.
 *
 * @param in the in
 * @return the string
 */
public static String encodeLines (byte[] in) {
   return encodeLines(in, 0, in.length, 76, systemLineSeparator); }

 
/**
 * Encode lines.
 *
 * @param in the in
 * @param iOff the i off
 * @param iLen the i len
 * @param lineLen the line len
 * @param lineSeparator the line separator
 * @return the string
 */
public static String encodeLines (byte[] in, int iOff, int iLen, int lineLen, String lineSeparator) {
   int blockLen = (lineLen*3) / 4;
   if (blockLen <= 0) throw new IllegalArgumentException();
   int lines = (iLen+blockLen-1) / blockLen;
   int bufLen = ((iLen+2)/3)*4 + lines*lineSeparator.length();
   StringBuilder buf = new StringBuilder(bufLen);
   int ip = 0;
   while (ip < iLen) {
      int l = Math.min(iLen-ip, blockLen);
      buf.append (encode(in, iOff+ip, l));
      buf.append (lineSeparator);
      ip += l; }
   return buf.toString(); }

 
/**
 * Encode.
 *
 * @param in the in
 * @return the char[]
 */
public static char[] encode (byte[] in) {
   return encode(in, 0, in.length); }

 
/**
 * Encode.
 *
 * @param in the in
 * @param iLen the i len
 * @return the char[]
 */
public static char[] encode (byte[] in, int iLen) {
   return encode(in, 0, iLen); }

 
/**
 * Encode.
 *
 * @param in the in
 * @param iOff the i off
 * @param iLen the i len
 * @return the char[]
 */
public static char[] encode (byte[] in, int iOff, int iLen) {
   int oDataLen = (iLen*4+2)/3;       // output length without padding
   int oLen = ((iLen+2)/3)*4;         // output length including padding
   char[] out = new char[oLen];
   int ip = iOff;
   int iEnd = iOff + iLen;
   int op = 0;
   while (ip < iEnd) {
      int i0 = in[ip++] & 0xff;
      int i1 = ip < iEnd ? in[ip++] & 0xff : 0;
      int i2 = ip < iEnd ? in[ip++] & 0xff : 0;
      int o0 = i0 >>> 2;
      int o1 = ((i0 &   3) << 4) | (i1 >>> 4);
      int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
      int o3 = i2 & 0x3F;
      out[op++] = map1[o0];
      out[op++] = map1[o1];
      out[op] = op < oDataLen ? map1[o2] : '='; op++;
      out[op] = op < oDataLen ? map1[o3] : '='; op++; }
   return out; }

 
/**
 * Decode string.
 *
 * @param s the s
 * @return the string
 */
public static String decodeString (String s) {
   return new String(decode(s)); }

 
/**
 * Decode lines.
 *
 * @param s the s
 * @return the byte[]
 */
public static byte[] decodeLines (String s) {
   char[] buf = new char[s.length()];
   int p = 0;
   for (int ip = 0; ip < s.length(); ip++) {
      char c = s.charAt(ip);
      if (c != ' ' && c != '\r' && c != '\n' && c != '\t')
         buf[p++] = c; }
   return decode(buf, 0, p); }

 
/**
 * Decode.
 *
 * @param s the s
 * @return the byte[]
 */
public static byte[] decode (String s) {
   return decode(s.toCharArray()); }

 
/**
 * Decode.
 *
 * @param in the in
 * @return the byte[]
 */
public static byte[] decode (char[] in) {
   return decode(in, 0, in.length); }

 
/**
 * Decode.
 *
 * @param in the in
 * @param iOff the i off
 * @param iLen the i len
 * @return the byte[]
 */
public static byte[] decode (char[] in, int iOff, int iLen) {
   if (iLen%4 != 0) throw new IllegalArgumentException ("Length of Base64 encoded input string is not a multiple of 4.");
   while (iLen > 0 && in[iOff+iLen-1] == '=') iLen--;
   int oLen = (iLen*3) / 4;
   byte[] out = new byte[oLen];
   int ip = iOff;
   int iEnd = iOff + iLen;
   int op = 0;
   while (ip < iEnd) {
      int i0 = in[ip++];
      int i1 = in[ip++];
      int i2 = ip < iEnd ? in[ip++] : 'A';
      int i3 = ip < iEnd ? in[ip++] : 'A';
      if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)
         throw new IllegalArgumentException ("Illegal character in Base64 encoded data.");
      int b0 = map2[i0];
      int b1 = map2[i1];
      int b2 = map2[i2];
      int b3 = map2[i3];
      if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
         throw new IllegalArgumentException ("Illegal character in Base64 encoded data.");
      int o0 = ( b0       <<2) | (b1>>>4);
      int o1 = ((b1 & 0xf)<<4) | (b2>>>2);
      int o2 = ((b2 &   3)<<6) |  b3;
      out[op++] = (byte)o0;
      if (op<oLen) out[op++] = (byte)o1;
      if (op<oLen) out[op++] = (byte)o2; }
   return out; }

/**
 * Instantiates a new base64 coder.
 */
// Dummy constructor.
private Base64Coder() {}

} // end class Base64Coder
