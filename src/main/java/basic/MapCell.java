/*
 * ----------------------------------------------------------------------------
 * "THE COFFEE-WARE LICENSE" (Revision 3):
 * <memaddict@gmail.com> wrote this file.  As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a coffee in return.      Alex Yermakov
 * ----------------------------------------------------------------------------
 */

package basic;

import spi.Cell;

public final class MapCell<K, V> implements Cell {

    //Strong anti pattern, because in OOP there is no global scope
    private static byte DEFAULT_WEALTH = 0;

    private final K key;
    private final V value;

    // byte Type is enough, however it is *possible* breach of hierarchy,
    // Since we know it won't be higher than 64
    // But we should not have this information
    private byte povertyLevel;


    public MapCell(MapCell<K, V> c) {
        this(c.key, c.value, c.povertyLevel);
    }

    public MapCell(MapCell<K, V> c, byte debt) {
        this(c.key, c.value, debt);
    }

    public MapCell(K key, V value) {
        this(key, value, DEFAULT_WEALTH);
    }

    public MapCell(K key, V value, byte debt) {
        this.key = key;
        this.value = value;
        this.povertyLevel = debt;
    }

    public int hashCode() {
        //According to documentation, on pure strings,
        //toString() just returns this.toString()
        return MurmurHash.hashUTF8String(key.toString());
    }

    //technically we only compare keys
    public boolean equals(MapCell c) {
        return this.key.equals(c.key);
    }

    public boolean poorerThan(MapCell c) {
        return c == null || this.povertyLevel > c.povertyLevel;
    }

    public V value() {
        return this.value;
    }

    public boolean happy() {
        return povertyLevel == 0;
    }

    public byte nextPovertyLevel() {
        return ++povertyLevel;
    }

    public byte previousPovertyLevel() {
        return --povertyLevel;
    }

    //Probably there is a more graceful way to handle value
    //However it was a last minute conversion to support easy checks for
    //When HashMap has to work as HashSet
    @Override
    public String toString() {
        String strigifiedValue = "";
        if (value != null) {
            strigifiedValue = value.toString();
        }
        return key.toString() + " " + strigifiedValue;
    }

}


