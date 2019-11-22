/*
 * ----------------------------------------------------------------------------
 * "THE COFFEE-WARE LICENSE" (Revision 3):
 * <memaddict@gmail.com> wrote this file.  As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a coffee in return.      Alex Yermakov
 * ----------------------------------------------------------------------------
 */

package spi;

public interface Dictionary {

    String[] asStringArray();

    @Override
    String toString();

    final class Fake implements Dictionary {

        @Override
        public String[] asStringArray() {
            return new String[0];
        }

        @Override
        public String toString() {
            return "this is so fake";
        }
    }
}
