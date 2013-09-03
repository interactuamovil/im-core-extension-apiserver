/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver;

import java.io.Serializable;
import java.util.Arrays;


public class CompactMap<E> implements Serializable {
  static final long serialVersionUID = 1L;

  private static final int MAX_HASH_TABLE_SIZE = 1 << 24;
  private static final int MAX_HASH_TABLE_SIZE_WITH_FILL_FACTOR = 1 << 20;

  private static final long[] byteTable;
  private static final long HSTART = 0xBB40E64DA205B064L;
  private static final long HMULT = 7664345821815920749L;

  static {
    byteTable = new long[256];
    long h = 0x544B2FBACAAF1684L;
    for (int i = 0; i < 256; i++) {
      for (int j = 0; j < 31; j++) {
        h = (h >>> 7) ^ h;
        h = (h << 11) ^ h;
        h = (h >>> 10) ^ h;
      }
      byteTable[i] = h;
    }
  }

  private int maxValues;
  private int[] table;
  private int[] nextPtrs;
  private long[] hashValues;
  private E[] elements;
  private int nextHashValuePos;
  private int hashMask;
  private int size;

  @SuppressWarnings("unchecked")
  public CompactMap(int maxElements) {
    int sz = 128;
    int desiredTableSize = maxElements;
    if (desiredTableSize < MAX_HASH_TABLE_SIZE_WITH_FILL_FACTOR) {
      desiredTableSize = desiredTableSize * 4 / 3;
    }
    desiredTableSize = Math.min(desiredTableSize, MAX_HASH_TABLE_SIZE);
    while (sz < desiredTableSize) {
      sz <<= 1;
    }
    this.maxValues = maxElements;
    this.table = new int[sz];
    this.nextPtrs = new int[maxValues];
    this.hashValues = new long[maxValues];
    this.elements = (E[]) new Object[sz];
    Arrays.fill(table, -1);
    this.hashMask = sz-1;
  }

  public int size() {
    return size;
  }

  public E put(CharSequence key, E val) {
    return put(hash(key), val);
  }

  public E put(long hash, E val) {
    int hc = (int) hash & hashMask;
    int[] table = this.table;
    int k = table[hc];
    if (k != -1) {
      int lastk;
      do {
        if (hashValues[k] == hash) {
          E old = elements[k];
          elements[k] = val;
          return old;
        }
        lastk = k;
        k = nextPtrs[k];
      } while (k != -1);
      k = nextHashValuePos++;
      nextPtrs[lastk] = k;
    } else {
      k = nextHashValuePos++;
      table[hc] = k;
    }
    if (k >= maxValues) {
      throw new IllegalStateException("Hash table full (size " + size + ", k " + k);
    }
    hashValues[k] = hash;
    nextPtrs[k] = -1;
    elements[k] = val;
    size++;
    return null;
  }

  public E get(long hash) {
    int hc = (int) hash & hashMask;
    int[] table = this.table;
    int k = table[hc];
    if (k != -1) {
      do {
        if (hashValues[k] == hash) {
          return elements[k];
        }
        k = nextPtrs[k];
      } while (k != -1);
    }
    return null;
  }

  public E get(CharSequence hash) {
    return get(hash(hash));
  }

  public static long hash(CharSequence cs) {
    if (cs == null) return 1L;
    long h = HSTART;
    final long hmult = HMULT;
    final long[] ht = byteTable;
    for (int i = cs.length()-1; i >= 0; i--) {
      char ch = cs.charAt(i);
      h = (h * hmult) ^ ht[ch & 0xff];
      h = (h * hmult) ^ ht[(ch >>> 8) & 0xff];
    }
    return h;
  }

}
