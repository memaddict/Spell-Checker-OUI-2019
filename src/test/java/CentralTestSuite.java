/*
 * ----------------------------------------------------------------------------
 * "THE COFFEE-WARE LICENSE" (Revision 3):
 * <memaddict@gmail.com> wrote this file.  As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a coffee in return.      Alex Yermakov
 * ----------------------------------------------------------------------------
 */

import basic.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("WeakerAccess") // Block IntelliJ negging about public/private
public final class CentralTestSuite {

    static String word = "TEST_WORD";

    @Test
    public void primaryHashTest() {
        final int fakeHash = MurmurHash.fakeHashCode();
        assertEquals(fakeHash, 163);

        final int hash = MurmurHash.hashUTF8String(word);
        assertNotEquals(hash, "");
    }

    @Test
    public void primaryMapCellTest() {
        MapCell<String, Integer> firstCell = new MapCell<>(word, 1);
        MapCell<String, Integer> secondCell = new MapCell<>(word, 2);
        MapCell<String, Integer> clonedFirstCell = new MapCell<>(firstCell);
        MapCell<String, Integer> poorerClonedFirstCell = new MapCell<>(firstCell, (byte) 1);

        assertEquals(firstCell.hashCode(), MurmurHash.hashUTF8String(word));

        assertTrue(firstCell.equals(secondCell));
        assertFalse(firstCell.poorerThan(secondCell));

        assertTrue(firstCell.equals(clonedFirstCell));
        assertTrue(poorerClonedFirstCell.poorerThan(firstCell));
    }

    @Test
    public void primaryMajellaMapTest() {
        final int minimalSize = 8;
        final byte minimalLookUp = 4;
        MajellaMap<String, Boolean> firstMap = new MajellaMap<>();

        assertEquals(firstMap.size(), minimalSize + minimalLookUp);

        // Expansion tests
        int startingSize = 1;
        int sizeIncrement = 5;
        int maxTableSize = 150000;
        for (int currentSize = startingSize; currentSize < maxTableSize; currentSize *= sizeIncrement) {
            //Fill
            for (int i = 0; i < currentSize; ++i) {
                firstMap.put(Integer.toString(i));
            }
            assertEquals(currentSize, firstMap.taken());

            //Single item removal
            firstMap.erase("0");
            assertEquals(currentSize - 1, firstMap.taken());

            //Erase everything
            for (int j = 0; j < currentSize; ++j) {
                firstMap.erase(Integer.toString(j));
            }
            assertEquals(0, firstMap.taken());
        }
    }

    @Test
    public void primaryRBTreeCellTest() {
        RBTreeCell<String> firstCell = new RBTreeCell<>(word);
        RBTreeCell<String> secondCell = new RBTreeCell<>(word + "2");

        final int hash = MurmurHash.hashUTF8String(word);
        assertEquals(hash, firstCell.hashCode());
        assertFalse(firstCell.looselyEqual(secondCell));
    }

    @Test
    public void primaryOkasakiRBTreeTest() {
        OkasakiRBTree<String> firstTree = new OkasakiRBTree<>();

        // Expansion tests
        int startingSize = 1;
        int sizeIncrement = 5;
        int maxTableSize = 15000;
        for (int currentSize = startingSize; currentSize < maxTableSize; currentSize *= sizeIncrement) {
            for (int i = 0; i < currentSize; ++i) {
                firstTree.add(Integer.toString(i));
            }
            /* firstTree.printTree();  -> If you want */
            for (int i = 0; i < currentSize; ++i) {
                firstTree.remove(Integer.toString(i));
                assertEquals(currentSize - i - 1, firstTree.size());
            }
        }

        //Simple collision test
        for (int i = startingSize; i < sizeIncrement; ++i) {
            firstTree.add(word);
            assertEquals(1, firstTree.size());
        }
    }

    @Test
    public void testParsing() throws Exception {
        String testText = "src/main/resources/sample_text";
        String testDictionary = "src/main/resources/raw_dictionaries/3of6all.txt";

        BasicText firstBasicText = new BasicText(testText);
        BasicDictionary firstBasicDictionary = new BasicDictionary(testDictionary);

        firstBasicText.filterWith(firstBasicDictionary);
        /* firstBasicText.printAligned(); */

    }
}