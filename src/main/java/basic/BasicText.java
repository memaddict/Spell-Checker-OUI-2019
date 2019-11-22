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
import spi.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.util.Arrays.sort;

public final class BasicText implements Text {

    private final String filePath;
    private OkasakiRBTree<String> data;

    public BasicText(String filePath) throws Exception {
        super();
        this.filePath = filePath;
        data = new OkasakiRBTree<>();
        try {
            initializeTree();
        } catch (Exception ex) {
            throw new Exception(
                    "Can't initialize the RB Tree",
                    ex
            );
        }
    }

    public int size() {
        return data.size();
    }

    public void printDataComposition() {
        data.printTree();
    }

    public void filterWith(Dictionary dictionary) {
        for (String word : dictionary.asStringArray()) {
            data.remove(word);
        }
    }

    @Override
    public String toString() {
        return data.toString().replaceAll("^ +| +$|( )+", "$1");
    }

    public void printAligned() {
        int maxWordWidth = 5;
        //Multiple conversions all around, but at least works on deadline...
        String[] split = this.toString().split("\\s+");
        for (String i : split) {
            maxWordWidth = Math.max(maxWordWidth, i.length());
        }
        sort(split);
        int maxWordsAsPlasticRatio = maxWordWidth > 8 ? 5 : ((int) (split.length * 0.3247));
        for (int i = 0, splitLength = split.length; i < splitLength; i++) {
            split[i] = String.format("%" + maxWordWidth + "s", split[i] + " ");
            System.out.print(split[i]);
            if ((i + 1) % maxWordsAsPlasticRatio == 0) System.out.println();
        }
    }

    private void initializeTree() throws Exception {
        try {
            final Stream<String> stream = Files.lines(Paths.get(filePath));
            stream.forEach(line -> {
                for (String i : line.split("\\s+")) {
                    data.add(i.toLowerCase().replaceAll(" ", "").replaceAll("[^a-zA-Z ]", ""));
                }
            });
        } catch (IOException ex) {
            throw new Exception(
                    "Can't parse the file, check the path",
                    ex
            );
        }
    }
}
