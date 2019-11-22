/*
 * ----------------------------------------------------------------------------
 * "THE COFFEE-WARE LICENSE" (Revision 3):
 * <memaddict@gmail.com> wrote this file.  As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a coffee in return.      Alex Yermakov
 * ----------------------------------------------------------------------------
 */

package basic;

import java.util.AbstractMap;
import java.util.Set;

/*
 * Open addressed HashMap with RobinHood linear probing and Fibonacci mapping
 */
public final class MajellaMap<K, V> extends AbstractMap<K, V> {

    private static final int MINIMAL_SIZE = 8;
    private static final byte MINIMAL_LOOK_UP = 4;
    private static final float LOAD_FACTOR = 0.53f;
    private MapCell[] table;
    private int size;
    private int filled = 0;

    private byte lookUp;

    public MajellaMap() {
        this.lookUp = MINIMAL_LOOK_UP;
        this.size = MINIMAL_SIZE + lookUp;
        this.table = new MapCell[size];
    }

    @Override
    public int size() {
        return this.size;
    }

    public int taken() {
        return this.filled;
    }

    public String[] asStringArray() {
        String[] filledSpace = new String[filled];
        int i = 0;
        for (MapCell c : table) {
            if (c != null) {
                filledSpace[i] = c.toString().trim();
                ++i;
            }
        }
        return filledSpace;
    }

    public V put(K key) {
        return putIfAbsent(key, null);
    }

    public V put(K key, V value) {
        return putIfAbsent(key, value);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        int index = findIndexIfPresent(key);
        if (index == size - 1) {
            justPut(new MapCell<>(key, value));
            expandIfFull();
        }
        return value;
    }

    public boolean storing(K key) {
        return find(key) != null;
    }

    //Technically we suppress warning about generics
    //One of the alternative ways to do it would be
    //To avoid this, we can change internal data structure
    //From Array to List for example
    @SuppressWarnings("unchecked")
    public V find(K key) {

        int index = findIndexIfPresent(key);
        if (index != size - 1) {
            return (V) table[index].value();
        }

        //This is the point I realized I'm spending too much time on this Map
        //And won't be able to finish it in time, if I continue
        //This HAS TO BE REWORKED in the future
        return null;
    }

    private int findIndexIfPresent(K key) {
        MapCell<K, V> current = new MapCell<>(key, null);

        int index = indexOfHash(current.hashCode());

        for (byte i = 0; i < lookUp; ++i) {
            if (table[index + i] != null) {
                if (current.equals(table[index + i])) {
                    return index + i;
                }
            }
        }
        return size - 1;
    }

    @SuppressWarnings("unchecked")
    private void justPut(MapCell c) {
        byte i = 0;
        MapCell<K, V> current = new MapCell<>(c, i);

        int index = indexOfHash(current.hashCode());

        while (i < lookUp) {
            if (current.poorerThan(table[index + i])) {
                table[size - 1] = table[index + i];
                table[index + i] = current;
                if (table[size - 1] == null) {
                    break;
                }
            }
            if (table[size - 1] != null) {
                current = new MapCell<>(table[size - 1], table[size - 1].nextPovertyLevel());
            } else {
                current = new MapCell<>(current, current.nextPovertyLevel());
            }
            ++i;
        }
        ++filled;
    }

    private void expandIfFull() {
        if (full()) {
            MapCell[] oldTable = table;
            this.size = Calculator.closestPowerOfTwoNumber(size + lookUp);
            this.lookUp = (byte) Calculator.log2of(this.size);
            this.filled = 0;
            table = new MapCell[this.size + this.lookUp];
            for (MapCell mapCell : oldTable) {
                if (mapCell != null) {
                    justPut(mapCell);
                }
            }
        }
    }

    private boolean full() {
        return (float) filled / size > LOAD_FACTOR || table[size - 1] != null;
    }

    @SuppressWarnings("unchecked")
    public void erase(K key) {
        int index = findIndexIfPresent(key);
        if (index != size - 1) {
            table[index] = null;
            --filled;
            ++index;
            for (int i = index; i < table.length; i++) {
                if (table[i] != null) {
                    if (!table[i].happy()) {
                        table[i - 1] = new MapCell<>(table[i], table[i].previousPovertyLevel());
                        table[i] = null;
                    }
                    //Stop on item with best fit (happiest = wealthiest = least poor)
                    else {
                        break;
                    }
                    //Stop on empty cell
                } else {
                    break;
                }
            }
        }
    }

    @Override
    public boolean remove(Object key, Object value) {
        return false;
    }

    private int indexOfHash(int hash) {
        // This is a signed version on 11400714819323198485ull
        // Multiplication by it causes overflow and looping around mod(2^64)
        // And relatively even distribution of keys
        final long signedGoldenFactor = -7046029254386353131L;

        // >>> is unsigned shift right
        // (int) typecast is scary and a bad programming style for sure
        return (int) (signedGoldenFactor * hash >>> Calculator.offsetFor(this.size - this.lookUp));
    }

    public String toString() {
        StringBuilder flatRepresentation = new StringBuilder();
        for (MapCell c : table) {
            if (c != null) {
                flatRepresentation.append(c.toString());
            }
        }
        return flatRepresentation.toString();
    }

    public void print() {
        for (int i1 = 0; i1 < table.length; i1++) {
            MapCell c = table[i1];
            if (table[i1] != null) {
                System.out.println("INDEX " + i1 + ": " + c.toString() + " (Poverty: " + (c.nextPovertyLevel() - 1) + ")");
            }
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }


    //STRONG Anti Pattern, OOP wise, both in terms of static,
    //Having funky name, and general attitude
    //It is a half way between eradicating utility classes
    //And trying to conform to OOP practices
    private static final class Calculator {

        //We are thinking in terms of power of two
        private static int offsetFor(int bound) {
            return 64 - log2of(closestPowerOfTwoNumber(bound));
        }

        //To what power we should put the 2, to get this number
        private static int log2of(int bits) {
            return bits == 0 ? 0 : 31 - Integer.numberOfLeadingZeros(bits);
        }

        //Closest higher number in terms of 2 in some power
        private static int closestPowerOfTwoNumber(int i) {

            --i;
            i |= i >> 16;
            i |= i >> 8;
            i |= i >> 4;
            i |= i >> 2;
            i |= i >> 1;
            ++i;

            //Fix for number 1
            if (i < 2) {
                i = 2;
            }

            return i;
        }
    }
}
