import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Found words count: 775
 */
public class A9WordsSimple {

    public static final int WORD_LENGTH = 9;

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        String urlAddress = "https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt"; // Change this to the URL of your text file
        Map<Integer, Set<String>> wordsBySize = new HashMap<>();
        Set<String> allWords = new HashSet<>();

        /**
         * Cache for all words, each of them is pointing to a 1-character less word,
         * that's made out of eating one of the characters of the first word.
         * Why Set<String>? Because by eating out one char can be produced more than
         * one word. fi
         *    BRUSHINGS -> BUSHINGS, BRUSHINGS -> BRUSHING
         *    CRAP -> CAP, CRAP -> RAP
         *    SHIT -> HIT, SHIT -> SIT
         *
         *    I've tried this shing the cache by using tighter optimisation, but that's increasing
         *    the calculation phase time.
         */
        Map<String, Set<String>> linksCache = new HashMap<>();

        URL url = new URL(urlAddress);
        URLConnection conn = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line;
        reader.readLine();
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            int l = line.length();
            if (l <= WORD_LENGTH) {
                allWords.add(line);
                wordsBySize.computeIfAbsent(l, x -> new HashSet<>()).add(line);
            }
        }
        reader.close();
        long startAfterLoading = System.currentTimeMillis();

        allWords.add("A");
        allWords.add("I");
        wordsBySize.computeIfAbsent(1, x -> new HashSet<>()).add("I");
        wordsBySize.get(1).add("A");

        for(String word : allWords) {
            for(String cutWord : A9WordsUtil.cutWords(word)) {
                if(allWords.contains(cutWord)) {
                    linksCache.computeIfAbsent(word, z -> new HashSet<>()).add(cutWord);
                }
            }
        }

        HashSet<String> wordsByReturn = new HashSet<>();
        long starLinks = System.currentTimeMillis();
        for (String key : wordsBySize.get(WORD_LENGTH)) {
            ArrayList<String> path = new ArrayList<>();
            if (A9WordsUtil.isWordStartling(key, WORD_LENGTH, linksCache, path)) {
                System.out.println(path);
                wordsByReturn.add(key);
            }
        }
        System.out.println("Word links buffer count: " + linksCache.size());
        System.out.println("Found words: " + wordsByReturn);

        System.out.println("Found words count: " + wordsByReturn.size());

        long end = System.currentTimeMillis();
        System.out.println("Time elapsed: " + (end - start));
        System.out.println("Time elapsed (startAfterLoading): " + (end - startAfterLoading));

        System.out.println("Time elapsed (links calc): " + (end - starLinks));

    }

}