import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Found words count: 775
 */
public class A9Words {

    public static final int WORD_LENGTH = 9;

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        String urlAddress = "https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt"; // Change this to the URL of your text file
        List<String> textList = new ArrayList<>();
        Map<Integer, Set<String>> wordsBySize = new HashMap<>();

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
            textList.add(line);
            int l = line.length();
            if (l <= WORD_LENGTH) {
                wordsBySize.computeIfAbsent(l, x -> new HashSet<>()).add(line);
            }
        }
        reader.close();

        wordsBySize.computeIfAbsent(1, x -> new HashSet<>()).add("I");
        wordsBySize.get(1).add("A");

        long startAfterLoading = System.currentTimeMillis();
        //Compare line to line and remove redundant words
        for (int i = WORD_LENGTH; i > 2; --i) {
            final int upper = i - 1;
            Set<String> upperCache = new HashSet<>();
            Set<String> cache = new HashSet<>();
            for( String x : wordsBySize.get(i)) {
                for ( String y  : cutWords(x)) {
                    if (wordsBySize.get(i - 1).contains(y)) {
                        cache.add(x);
                        upperCache.add(y);
                    }
                }
            }
            wordsBySize.put(i, cache);
            wordsBySize.put(upper, upperCache);
        }


        //Without this: Word links buffer count: 31532
        //With this: Word links buffer count: 4340

        for (int i = 2; i <= WORD_LENGTH; ++i) {
            final int upper = i - 1;
            HashSet<String> cache = new HashSet<>();
            for( String x : wordsBySize.get(i)) {
                for( String y : cutWords(x)) {
                    if (wordsBySize.get(upper).contains(y)) {
                        cache.add(x);
                        linksCache.computeIfAbsent(x, z -> new HashSet<>()).add(y);
                    }
                }
            }
            wordsBySize.put(i, cache);
        }

        HashSet<String> wordsByReturn = new HashSet<>();
        long starLinks = System.currentTimeMillis();
        for (String key : wordsBySize.get(WORD_LENGTH)) {
            ArrayList<String> path = new ArrayList<>();
            if (isWordStartling(key, WORD_LENGTH, linksCache, path)) {
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

    private static boolean isWordStartling(String word, int wordSize, Map<String, Set<String>> linksCache, ArrayList<String> path) {
        if (wordSize == 1) {
            path.add(word);
            return true;
        }
        if (linksCache.get(word) != null) {
            for (String s : linksCache.get(word)) {
                if (isWordStartling(s, wordSize - 1, linksCache, path)) {
                    path.add(word);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * Returns all possible words produced by a single character.
     */
    private static List<String> cutWords(String word) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < word.length(); ++i) {
            result.add(word.substring(0, i) + word.substring(i + 1));
        }
        return result;
    }
}