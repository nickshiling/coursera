public class Outcast {
    private final WordNet wordNet;

    public Outcast(WordNet w) {
        if (w == null) {
            throw new java.lang.IllegalArgumentException();
        }

        wordNet = w;
    }

    public String outcast(String[] nouns) {
        if (nouns == null) {
            throw new java.lang.IllegalArgumentException();
        }

        int maxDistance = 0;
        String maxWord = null;

        for (int i = 0; i < nouns.length; i++) {
            int distance = 0;
            for (int j = 0; j < nouns.length; j++) {
                distance += wordNet.distance(nouns[i], nouns[j]);
            }

            if (distance > maxDistance) {
                maxDistance = distance;
                maxWord = nouns[i];
            }
        }

        return maxWord;
    }
}
