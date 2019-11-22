/*
 * ----------------------------------------------------------------------------
 * "THE COFFEE-WARE LICENSE" (Revision 3):
 * <memaddict@gmail.com> wrote this file.  As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a coffee in return.      Alex Yermakov
 * ----------------------------------------------------------------------------
 */

package spi;

public interface Cell {
    int hashCode();

    final class Fake implements Cell {
        public int hashCode() {
            return 163;
        }
    }
}
