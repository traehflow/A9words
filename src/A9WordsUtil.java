import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class A9WordsUtil {

    /**
     *
     * @param word - word to be checked.
     * @param wordSize - Size of the word to be checked
     * @param linksCache - chache of words, connected to other words
     * @param path - List filled with words after removing one letter of each until going to a 1-letter word.
     * @return - True if the word yields another word by removing one letter each time.
     */
    public static boolean isWordStartling(String word, int wordSize, Map<String, Set<String>> linksCache, ArrayList<String> path) {
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
    public static List<String> cutWords(String word) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < word.length(); ++i) {
            result.add(word.substring(0, i) + word.substring(i + 1));
        }
        return result;
    }
}
