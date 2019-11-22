/*
 * ----------------------------------------------------------------------------
 * "THE COFFEE-WARE LICENSE" (Revision 3):
 * <memaddict@gmail.com> wrote this file.  As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a coffee in return.      Alex Yermakov
 * ----------------------------------------------------------------------------
 */

package basic;

/**
 * Functional Red Black Tree implementation
 */
public final class OkasakiRBTree<V> {

    //Color arithmetic's
    private static final int WHITE = -1;
    private static final int RED = 0;
    private static final int BLACK = 1;
    private static final int DOUBLE_BLACK = 2;

    private RBTreeCell root;

    public OkasakiRBTree() {
        root = null;
    }

    public int size() {
        return size(root);
    }

    private int size(RBTreeCell fromCell) {
        if (fromCell == null) {
            return 0;
        } else {
            return 1 + size(fromCell.left()) + size(fromCell.right());
        }
    }

    public void add(V value) {
        RBTreeCell<V> newborn = new RBTreeCell<>(value, RED);
        if (root == null) {
            root = newborn;
        } else {
            root.adoptOffspring(newborn);
        }
        fixAfterAdd(newborn);
    }

    @SuppressWarnings("unchecked")
    public void remove(V value) {
        RBTreeCell sneakyPete = new RBTreeCell<>(value);
        RBTreeCell toBeRemoved = root;
        boolean found = false;
        while (!found && toBeRemoved != null) {
            if (toBeRemoved.looselyEqual(sneakyPete)) {
                found = true;
            } else {
                if (toBeRemoved.smallerThan(sneakyPete)) {
                    toBeRemoved = toBeRemoved.left();
                } else {
                    toBeRemoved = toBeRemoved.right();
                }
            }
        }

        if (!found) {
            return;
        }

        //Only if we have a single child sub tree
        if (toBeRemoved.left() == null || toBeRemoved.right() == null) {
            //Unlink and buffer non empty subtree
            RBTreeCell subTree;
            if (toBeRemoved.left() == null) {
                subTree = toBeRemoved.right();
            } else {
                subTree = toBeRemoved.left();
            }

            //We don't need to fix anything, since if we have one child
            //We are guaranteed red node, and our father is black
            fixBeforeRemove(toBeRemoved);

            //If we became root
            if (toBeRemoved.parent() == null) {
                root = subTree;
                if (root != null) {
                    root.killParent();
                }
            }
            //Become what was our father before
            else {
                toBeRemoved.dieAndBeReplacedBy(subTree);
            }
            return;
        }

        //We find the maximum (rightmost) element in its left (less-than) sub-tree
        //We mutate current node to have it's value, and unlink it from tree
        RBTreeCell smallest = toBeRemoved.right();
        while (smallest.left() != null) {
            smallest = smallest.left();
        }

        //So we donate it's value to current node
        //And proceed with deleting
        V mutagen = (V) smallest.value();
        toBeRemoved.dangerousMutation(mutagen);
        fixBeforeRemove(smallest);
        smallest.dieAndBeReplacedBy(smallest.right());
    }

    private void fixBeforeRemove(RBTreeCell soonToBeRemoved) {
        if (soonToBeRemoved.color() == RED) {
            return;
        }

        //We have some singular subtree present
        if (soonToBeRemoved.left() != null || soonToBeRemoved.right() != null) {
            if (soonToBeRemoved.left() == null) {
                soonToBeRemoved.right().paint(BLACK);
                return;
            } else {
                soonToBeRemoved.left().paint(BLACK);
                return;
            }
        }
        //Else we are a leaf
        else {
            bubbleUp(soonToBeRemoved.parent());
        }
    }

    private void bubbleUp(RBTreeCell parent) {
        if (parent == null) {
            return;
        }

        parent.paint(parent.color() + 1);
        parent.left().paint(parent.left().color() - 1);
        parent.right().paint(parent.right().color() - 1);


        RBTreeCell child = parent.left();
        if (child.color() == WHITE) {
            fixWhite(child);
            return;
        } else if (child.color() == RED) {
            if (child.left() != null && child.left().color() == RED) {
                fixDoubleRed(child.left());
                return;
            }
            if (child.right() != null && child.right().color() == RED) {
                fixDoubleRed(child.right());
                return;
            }
        }

        child = parent.right();
        if (child.color() == WHITE) {
            fixWhite(child);
            return;
        } else if (child.color() == RED) {
            if (child.left() != null && child.left().color() == RED) {
                fixDoubleRed(child.left());
                return;
            }
            if (child.right() != null && child.right().color() == RED) {
                fixDoubleRed(child.right());
                return;
            }
        }

        if (parent.color() == DOUBLE_BLACK) {
            //Root became double black, just turn in black
            if (parent.parent() == null) {
                parent.paint(BLACK);
            } else {
                bubbleUp(parent.parent());
            }
        }
    }

    public String toString() {
        return toString(root);
    }

    private String toString(RBTreeCell fromCell) {
        return fromCell == null ? "" : String.format("%s %s %s", toString(fromCell.left()), fromCell.toString(), toString(fromCell.right()));
    }

    public void printTree() {
        printTree(root, "", true, true);
    }

    public void printTreeAsList() {
        printTree(root, "", false, true);
    }

    private void printTree(RBTreeCell cell, String prefix, boolean pretty, boolean isTail) {
        if (cell == null) return;
        String tail;
        String direction;
        String prettyString;
        String[] pN = pretty ? new String[]{"●【", "】", "○『", "』"} : new String[]{"", "", "", ""};
        String[] pP = pretty ? new String[]{"    ", "│   ", "└── ", "┌── "} : new String[]{"", "", "", ""};
        prettyString = cell.color() == 1 ? pN[0] + cell.toString() + pN[1] : pN[2] + cell.toString() + pN[3];
        direction = isTail ? pP[1] : pP[0];
        printTree(cell.left(), prefix + direction, pretty, false);
        tail = isTail ? pP[2] : pP[3];
        System.out.println(prefix + tail + prettyString);
        direction = isTail ? pP[0] : pP[1];
        printTree(cell.right(), prefix + direction, pretty, true);
    }


    private void fixAfterAdd(RBTreeCell newborn) {
        if (newborn.orphan()) {
            newborn.paint(BLACK);
        } else {
            newborn.paint(RED);
            if (newborn.parentColor() == RED) {
                fixDoubleRed(newborn);
            }
        }
    }

    private void fixDoubleRed(RBTreeCell child) {
        RBTreeCell parent = child.parent();
        RBTreeCell grandParent = parent.parent();
        //Grandparent is root case
        if (grandParent == null) {
            parent.paint(BLACK);
            return;
        }
        RBTreeCell x, y, z, a, b, c, d;
        if (parent == grandParent.left()) {
            z = grandParent;
            d = grandParent.right();
            if (child == parent.left()) {
                x = child;
                y = parent;
                a = child.left();
                b = child.right();
                c = parent.right();
            } else {
                x = parent;
                y = child;
                a = parent.left();
                b = child.left();
                c = child.right();
            }
        } else {
            x = grandParent;
            a = grandParent.left();
            if (child == parent.left()) {
                y = child;
                z = parent;
                b = child.left();
                c = child.right();
                d = parent.right();
            } else {
                y = parent;
                z = child;
                b = parent.left();
                c = child.left();
                d = child.right();
            }
        }

        if (grandParent == root) {
            root = y;
            y.killParent();
        } else {
            grandParent.dieAndBeReplacedBy(y);
        }

        x.adoptLeftChild(a);
        x.adoptRightChild(b);
        y.adoptLeftChild(x);

        y.adoptRightChild(z);
        z.adoptLeftChild(c);
        z.adoptRightChild(d);

        y.paint(grandParent.color() - 1);
        x.paint(BLACK);
        z.paint(BLACK);

        if (y == root) {
            root.paint(BLACK);
        } else if (y.color() == RED && y.parent().color() == RED) {
            fixDoubleRed(y);
        }
    }


    @SuppressWarnings("unchecked")
    private void fixWhite(RBTreeCell white) {
        RBTreeCell newLeft, newHead, newRight, newParent, tempSubLeft, tempSubRight, tempUnlinkedSibling, child;
        RBTreeCell originalParent = white.parent();
        if (originalParent.left() == white) {
            newLeft = white.left();
            newHead = white;
            newRight = white.right();
            newParent = originalParent;

            tempSubLeft = newRight.left();
            tempSubRight = newRight.right();

            tempUnlinkedSibling = newParent.right();

            newLeft.paint(RED);
            newHead.paint(BLACK);
            newParent.paint(BLACK);

            newHead.adoptRightChild(tempSubLeft);

            V mutagen = (V) newParent.value();
            newParent.dangerousMutation(newRight.value());
            newRight.dangerousMutation(mutagen);

            newRight.adoptLeftChild(tempSubRight);
            newRight.adoptRightChild(tempUnlinkedSibling);
            newParent.adoptRightChild(newRight);
            child = newLeft;
        } else {
            newParent = white.right();
            newRight = white;
            newHead = white.left();
            newLeft = originalParent;
            tempUnlinkedSibling = newHead.right();
            tempSubRight = newHead.left();
            tempSubLeft = newLeft.left();
            newParent.paint(RED);
            newRight.paint(BLACK);
            newLeft.paint(BLACK);
            newRight.adoptLeftChild(tempUnlinkedSibling);

            V mutagen = (V) newLeft.value();
            newLeft.dangerousMutation(newHead.value());
            newHead.dangerousMutation(mutagen);

            newHead.adoptRightChild(tempSubRight);
            newHead.adoptLeftChild(tempSubLeft);
            newLeft.adoptLeftChild(newHead);
            child = newParent;
        }

        if (child.left() != null && child.left().color() == RED) {
            fixDoubleRed(child.left());
            return;
        }
        if (child.right() != null && child.right().color() == RED) {
            fixDoubleRed(child.right());
        }
    }
}
