/*
 * Copyright (C) 2019 by Alex Yermakov
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE
 * INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES
 * OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
 * WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION,
 * ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package basic;


/**
 * Implementation of MurmurHash3 algorithm | https://en.wikipedia.org/wiki/MurmurHash
 * <p>
 * Created by Austin Appleby | https://github.com/aappleby/smhasher
 * Based on port by Yonik Seeley | https://github.com/yonik/java_util
 * <p>
 * 0BSD license used, to preserve public domain licenses, from both sources above.
 */
public final class MurmurHash {


    //Unfinished design
    public static int fakeHashCode() {
        return 163;
    }

    /**
     * Returns the MurmurHash3_x86_32 hash of the UTF-8 bytes of the String without encoding.
     */
    public static int hashUTF8String(CharSequence data) {
        //seed is 163 in unsigned 32 bit representation, because it's anecdotally cool
        return murmurhash3_x86_32(data, data.length(), 0x000000A3);
    }

    /**
     * MurmurHash3 - x86 implementation to get 32bit hash value.
     * Only for UTF8 Strings.
     */
    private static int murmurhash3_x86_32(CharSequence data, int len, int seed) {

        final int c1 = 0xcc9e2d51;
        final int c2 = 0x1b873593;

        int h1 = seed;

        int pos = 0;
        int k1 = 0;
        int k2 = 0;
        int shift = 0;
        int bits = 0;
        int nBytes = 0;


        while (pos < len) {
            int code = data.charAt(pos++);
            if (code < 0x80) {
                k2 = code;
                bits = 8;

            } else if (code < 0x800) {
                k2 = (0xC0 | (code >> 6))
                        | ((0x80 | (code & 0x3F)) << 8);
                bits = 16;
            } else if (code < 0xD800 || code > 0xDFFF || pos >= len) {
                k2 = (0xE0 | (code >> 12))
                        | ((0x80 | ((code >> 6) & 0x3F)) << 8)
                        | ((0x80 | (code & 0x3F)) << 16);
                bits = 24;
            } else {
                int utf32 = (int) data.charAt(pos++);
                utf32 = ((code - 0xD7C0) << 10) + (utf32 & 0x3FF);
                k2 = (0xff & (0xF0 | (utf32 >> 18)))
                        | ((0x80 | ((utf32 >> 12) & 0x3F))) << 8
                        | ((0x80 | ((utf32 >> 6) & 0x3F))) << 16
                        | (0x80 | (utf32 & 0x3F)) << 24;
                bits = 32;
            }

            k1 |= k2 << shift;

            shift += bits;
            if (shift >= 32) {

                k1 *= c1;
                k1 = (k1 << 15) | (k1 >>> 17);
                k1 *= c2;

                h1 ^= k1;
                h1 = (h1 << 13) | (h1 >>> 19);
                h1 = h1 * 5 + 0xe6546b64;

                shift -= 32;

                if (shift != 0) {
                    k1 = k2 >>> (bits - shift);
                } else {
                    k1 = 0;
                }
                nBytes += 4;
            }

        }

        if (shift > 0) {
            nBytes += shift >> 3;
            k1 *= c1;
            k1 = (k1 << 15) | (k1 >>> 17);
            k1 *= c2;
            h1 ^= k1;
        }

        h1 ^= nBytes;

        h1 ^= h1 >>> 16;
        h1 *= 0x85ebca6b;
        h1 ^= h1 >>> 13;
        h1 *= 0xc2b2ae35;
        h1 ^= h1 >>> 16;

        return h1;
    }

}