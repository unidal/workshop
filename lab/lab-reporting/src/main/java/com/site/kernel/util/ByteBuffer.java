package com.site.kernel.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public final class ByteBuffer implements Serializable {
   private static final long serialVersionUID = -5361015430649207791L;
   private byte[] value;
   private int count;
   private String charset;

   private static final String DEFAULT_CHARSET = "GBK";

   public ByteBuffer() {
      this(64);
   }

   public ByteBuffer(int length) {
      this(length,DEFAULT_CHARSET);
   }

   public ByteBuffer(int length,String charset) {
      if (length<=0)
         throw new IllegalArgumentException("length must be larger than 0");

      this.value   = new byte[length];
      this.charset = (charset==null?DEFAULT_CHARSET:charset);
   }

   public synchronized ByteBuffer append(byte b) {
      int newcount = count + 1;

      if (newcount > value.length)
         expandCapacity(newcount);

      value[count++] = b;
      return this;
   }

   public synchronized ByteBuffer append(byte[] ba) {
      int len = ba.length;
      int newcount = count + len;

      if (newcount > value.length)
         expandCapacity(newcount);

      System.arraycopy(ba, 0, value, count, len);
      count = newcount;
      return this;
   }

   public synchronized ByteBuffer append(char c) {
      byte hi = (byte)((c & 0xFF00) >> 8);
      byte lo = (byte)(c & 0xFF);
      int newcount = count + 2;

      if (newcount > value.length)
         expandCapacity(newcount);

      if (hi!=0)
         value[count++]=hi;

      value[count++]=lo;

      return this;
   }

   public synchronized ByteBuffer append(ByteBuffer bb) {
      if (bb == null)
         return this.append((String)null);

      int len = bb.length();
      int newcount = count + len;

      if (newcount > value.length)
         expandCapacity(newcount);

      byte[] ba = bb.getBytes();

      bb.append(ba);
      count = newcount;
      return this;
   }

   public synchronized ByteBuffer append(Object obj) {
      return this.append((String)obj);
   }

   public synchronized ByteBuffer append(String str) {
      if (str==null)
         str = String.valueOf(str);

      try {
          this.append(str.getBytes(this.charset));
      }
      catch (UnsupportedEncodingException uee) {
         this.append(str.getBytes());
      }

      return this;
   }

   public synchronized byte byteAt(int index) {
      if ( (index < 0) || (index >= count))
         throw new IllegalArgumentException("Index("+index+") exceeds max length("+count+")");

      return value[index];
   }

   public synchronized int capacity() {
      return value.length;
   }

   public synchronized ByteBuffer delete(int start, int end) {
      if (start < 0)
         throw new IllegalArgumentException("Start(" + start + ") must be larger than 0");

      if (end > count)
         end = count;

      if (start > end)
         throw new IllegalArgumentException("Start(" + start + ") exceeds max length(" + count + ")");

      int len = end - start;

      if (len > 0) {
         System.arraycopy(value, start + len, value, start, count - end);
         count -= len;
      }

      return this;
   }

   public synchronized ByteBuffer deleteCharAt(int index) {
      if ( (index < 0) || (index >= count))
         throw new IllegalArgumentException("Index("+index+") exceeds max length("+count+")");

      System.arraycopy(value, index + 1, value, index, count - index - 1);
      count--;
      return this;
   }

   public void ensureCapacity(int minimumCapacity) {
      if (minimumCapacity > value.length)
         expandCapacity(minimumCapacity);
   }

   private void expandCapacity(int minimumCapacity) {
      int newCapacity = (value.length + 1) * 2;

      if (newCapacity < 0)
         newCapacity = Integer.MAX_VALUE;
      else if (minimumCapacity > newCapacity)
         newCapacity = minimumCapacity;

      byte[] newValue = new byte[newCapacity];

      System.arraycopy(value, 0, newValue, 0, count);
      value = newValue;
   }

   public synchronized byte[] getBytes() {
      byte[] ba = new byte[this.count];

      System.arraycopy(this.value,0,ba,0,count);
      return ba;
   }

   public synchronized ByteBuffer insert(int offset, byte b) {
      int newcount = count + 1;

      if (newcount > value.length)
         expandCapacity(newcount);

      System.arraycopy(value, offset, value, offset + 1, count - offset);
      value[offset] = b;
      count = newcount;
      return this;
   }

   public synchronized ByteBuffer insert(int offset, byte[] ba) {
      if ( (offset < 0) || (offset > count))
         throw new IllegalArgumentException("Index("+offset+") exceeds max length("+count+")");

      int len = ba.length;
      int newcount = count + len;

      if (newcount > value.length)
         expandCapacity(newcount);

      System.arraycopy(value, offset, value, offset + len, count - offset);
      System.arraycopy(ba, 0, value, offset, len);
      count = newcount;
      return this;
   }

   public synchronized int length() {
      return count;
   }

   private synchronized void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
      s.defaultReadObject();
      value = (byte[]) value.clone();
   }

   public synchronized ByteBuffer reverse() {
      int n = count - 1;

      for (int j = (n - 1) >> 1; j >= 0; --j) {
         byte temp = value[j];
         value[j] = value[n - j];
         value[n - j] = temp;
      }

      return this;
   }

   public synchronized void setByteAt(int index, byte b) {
      if ( (index < 0) || (index >= count))
         throw new IllegalArgumentException("Index("+index+") exceeds max length("+count+")");

      value[index] = b;
   }

   public synchronized void setLength(int newLength) {
      if (newLength < 0)
         throw new IllegalArgumentException("New length("+newLength+") must be larger than 0");

      if (newLength > value.length)
         expandCapacity(newLength);

      if (count < newLength) {
         for (; count < newLength; count++) {
            value[count] = '\0';
         }
      }
      else
         count = newLength;
   }

   public synchronized String substring(int start) {
      return this.substring(start,this.count);
   }

   public synchronized String substring(int start, int end) {
      if (start < 0)
         throw new IllegalArgumentException("Start("+start+") must be larger than 0");

      if (end > count)
         throw new IllegalArgumentException("Index("+end+") exceeds max length("+count+")");

      if (start > end)
         throw new IllegalArgumentException("Start("+start+") must be smaller than end("+end+")");

      try {
         return new String(this.value, start, end - start, this.charset);
      }
      catch (UnsupportedEncodingException uee) {
         return new String(this.value, start, end - start);
      }
   }

   public String toString() {
      return this.substring(0,count);
   }
}