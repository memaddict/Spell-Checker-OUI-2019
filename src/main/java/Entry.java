import basic.BasicDictionary;
import basic.BasicText;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public final class Entry {

    /*
     * I've Spent 7 HOURS! Trying to load the resources from Jar!
     * I give up... that's why I just commented out all the default states...
     * And ask to provide for 2 files, [text] [dictionary]
     * I will try to figure it out after exams.
     */
    private static final String demoText = "src/main/resources/sample_text";
    private static final String demoDictionary = "src/main/resources/raw_dictionaries/50words.txt";

    private static final String defaultDictionary = "src/main/resources/raw_dictionaries/3of6all.txt";
    private static final String defaultDictionaryName = "3of6all by SCOWL (~82k words)";

    //In future, better to just use regex to generate
    private static final String decorator = "\n-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";

    public static void main(String[] args) throws Exception {

        BasicText mainText = null; //new BasicText(demoText);
        BasicDictionary mainDictionary = null; //new BasicDictionary(demoDictionary);
        try {
            switch (args.length) {
                default:
                    System.out.println("\nWrong number of arguments.");
                    break;
                /*
                case 0:
                    ouiDemo(mainText);
                    break;
                case 1:
                    mainText = new BasicText(args[0]);
                    mainDictionary = new BasicDictionary(defaultDictionary);
                    System.out.println("\nNo external dictionary detected, lets use: "
                            + defaultDictionaryName
                            + decorator
                    );
                    break;
                /*
                 */
                case 2:
                    mainText = new BasicText(args[0]);
                    mainDictionary = new BasicDictionary(args[1]);
                    System.out.println("\nOk, lets analyze your text with dictionary you provided: "
                            + decorator.substring(0, 58)
                    );
                    break;
            }
            mainText.filterWith(mainDictionary);
            mainText.printAligned();
            System.out.println("\n\n-> " + mainText.size() + " words might have spelling errors.");
        } catch (Exception ex) {
            System.err.println(
                    "Sorry, something gone wrong :"
                            + ex.getLocalizedMessage()
            );
        }
    }

    private static void ouiDemo(BasicText ouiText) throws IOException {
        System.out.println("No arguments detected, we will run DEMONSTRATION for your pleasure:" + decorator);
        final Stream<String> stream = Files.lines(Paths.get(demoText));
        System.out.println("-> First we will load a small text to our text storage:\n");
        stream.forEach(System.out::println);
        System.out.println("\n" + "-> Currently under the hood we store it like this:");
        System.out.println("--------------------------------------------------");
        ouiText.printDataComposition();
        System.out.println("\n" + "-> Then let's load a simple 50 words file as our dictionary:\n");
        System.out.println(ouiText.toString());
        System.out.println("\n" + "-> Now let's filter the text using our new dictionary:\n");
        System.out.println("BEFORE:");
        ouiText.printAligned();
        System.out.println("\n\n" + "AFTER:");
    }
}
