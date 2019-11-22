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

public final class RBTreeCell<V> implements Cell {

    private static final int DAD = 0;

    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private final RBTreeCell[] parent = new RBTreeCell[1];
    private final RBTreeCell[] children = new RBTreeCell[2];
    //Color is clearly a state, we don't want to be able to manipulate it
    //In any way, after creation, I even prepared secondary constructors
    //But for now this acts as mutable variable
    private final int[] color = new int[1];
    //Basically we don't need key, out key is a hash value
    //Although I admit conceptually it's weaker, and more space hungry
    private int hash;
    private V value;

    public RBTreeCell(RBTreeCell<V> c, V value) {
        this.value = value;
        this.hash = hashCode();
        this.color[0] = c.color[0];
        this.children[LEFT] = c.children[LEFT];
        this.children[RIGHT] = c.children[RIGHT];
        this.parent[DAD] = c.parent[DAD];
    }

    public RBTreeCell(RBTreeCell<V> c, int color) {
        this.value = c.value;
        this.hash = c.hash;
        this.color[0] = color;
        this.children[LEFT] = c.children[LEFT];
        this.children[RIGHT] = c.children[RIGHT];
        this.parent[DAD] = c.parent[DAD];
    }

    public RBTreeCell(V value, int color) {
        this.value = value;
        this.hash = hashCode();
        this.color[0] = color;
    }

    public RBTreeCell(V value) {
        this.value = value;
        this.hash = hashCode();
        this.parent[DAD] = null;
    }

    public V value() {
        return value;
    }

    public void dangerousMutation(V value) {
        this.value = value;
        this.hash = hashCode();
    }

    public int color() {
        return color[0];
    }

    public int parentColor() {
        return parent[DAD].color[0];
    }

    public RBTreeCell parent() {
        return parent[DAD];
    }

    //Needs a stub, not a null
    public void killParent() {
        parent[DAD] = null;
    }

    public RBTreeCell left() {
        return children[LEFT];
    }

    public RBTreeCell right() {
        return children[RIGHT];
    }

    public final boolean orphan() {
        return parent[DAD] == null;
    }

    //We want it to build new object, not mutate this one...
    public final void paint(int color) {
        this.color[0] = color;
    }

    public void adoptLeftChild(RBTreeCell child) {
        children[LEFT] = child;
        if (child != null) {
            child.parent[DAD] = this;
        }
    }

    public void adoptRightChild(RBTreeCell child) {
        children[RIGHT] = child;
        if (child != null) {
            child.parent[DAD] = this;
        }
    }

    public void adoptOffspring(RBTreeCell newborn) {
        //We disallow duplication
        if (this.looselyEqual(newborn)) {
            return;
        }
        if (this.smallerThan(newborn)) {
            if (children[LEFT] == null) {
                children[LEFT] = newborn;
                children[LEFT].parent[DAD] = this;
            } else {
                children[LEFT].adoptOffspring(newborn);
            }
        } else if (!this.smallerThan(newborn)) {
            if (children[RIGHT] == null) {
                children[RIGHT] = newborn;
                children[RIGHT].parent[DAD] = this;
            } else {
                children[RIGHT].adoptOffspring(newborn);
            }
        }
    }

    public void dieAndBeReplacedBy(RBTreeCell replacement) {
        if (this.parent[DAD] == null) {
            return;
        }
        if (this == (parent[DAD].children[LEFT])) {
            parent[DAD].adoptLeftChild(replacement);
        } else {
            parent[DAD].adoptRightChild(replacement);
        }
    }

    public boolean smallerThan(RBTreeCell comparison) {
        return this.hash < comparison.hash;
    }

    public boolean looselyEqual(RBTreeCell comparison) {
        return this.equals(comparison);
    }


    public int hashCode() {
        //According to documentation, on pure strings,
        //toString() just returns this.toString()

        return MurmurHash.hashUTF8String(value.toString());
    }

    //technically we only compare keys
    public boolean equals(RBTreeCell c) {
        return this.value.equals(c.value);
    }

    //Probably there is a more graceful way to handle value
    //However it was a last minute conversion to support easy checks for
    //When Map has to work as HashSet
    @Override
    public String toString() {
        String strigifiedValue = "";
        if (value != null) {
            strigifiedValue = value.toString();
        }
        return strigifiedValue;
    }

}


