/*
 * ----------------------------------------------------------------------------
 * "THE COFFEE-WARE LICENSE" (Revision 3):
 * <memaddict@gmail.com> wrote this file.  As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a coffee in return.      Alex Yermakov
 * ----------------------------------------------------------------------------
 */

package basic;

import spi.Dictionary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public final class BasicDictionary implements Dictionary {

    private final String filePath;
    private MajellaMap<String, Boolean> data;

    //Commented out attempts at lazy loading
    public BasicDictionary(String filePath) throws Exception {
        super();
        this.filePath = filePath;
        data = new MajellaMap<>();
        try {
            toMap();
        } catch (Exception ex) {
            throw new Exception(
                    "Can't initialize the HashMap",
                    ex
            );
        }
        /*
        this(
                () ->{
                    return new MajellaMap(filePath);
                }
        );
         */
    }

    public String[] asStringArray() {
        return data.asStringArray();
    }


    @Override
    public String toString() {
        //needs some sort of decorator for loading
        // return String.format("%s", this.filePath); //Cool way to decorate
        return data.toString();
    }

    private void toMap() throws Exception {
        try {
            final Stream<String> stream = Files.lines(Paths.get(filePath));
            stream.forEach(word -> data.put(word.toLowerCase()));
        } catch (IOException ex) {
            throw new Exception(
                    "Can't parse the file, check the path",
                    ex
            );
        }
    }
}
