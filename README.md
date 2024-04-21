# A9words

Algorithm for finding out all 9-letter words that still remains a word after we remove a letter from it each time. 

For instance:

SUPINATES, SUPINATE, SPINATE, SPINAE, SPINE, SPIN, PIN, IN, I

The algorithm consist of three phases:
1. Load the data
2. Optimize the loaded data and build words cache.
3. Find out the words.

A9Words.java is the optimized algorithm. A9WordsSimple.java is doing it in a simplified way - loading the data and then producing the word-cache by comparing each word with another from the set. the result is more memory consuming and the speed is almost the same and even slower with about 100ms. Word links buffer consists of 90390 entries while in A9Words.java is of 4340 words.

Word links cache is defined in the following way:

Map<String, Set<String>> linksCache = new HashMap<>();


It's a Cache for all words, each of them is pointing to a 1-character less word, that's made out of eating one of the characters of the first word. 

Why Set<String>? Because by eating out one char can be produced more than one word. fi:

         -    BRUSHINGS -> BUSHINGS, BRUSHINGS -> BRUSHING

         -    CRAP -> CAP, CRAP -> RAP

        