package ChordAnalyzer;
import java.util.HashMap;
import java.util.Map;

public class Chord {
    private static final Map<String, int[]> chordsList = new HashMap<>();

    static {
        chordsList.put("C Major", new int[]{0, 4, 7});
        chordsList.put("G Major", new int[]{7, 11, 2});
        chordsList.put("Am (A minor)", new int[]{9, 0, 4});
        chordsList.put("Em (E minor)", new int[]{4, 7, 11});
        chordsList.put("Dm (D minor)", new int[]{2, 5, 9});
        chordsList.put("F Major", new int[]{5, 9, 0});
        chordsList.put("Bm (B minor)", new int[]{11, 2, 5});
        chordsList.put("Cm (C minor)", new int[]{0, 3, 7});
        chordsList.put("D Major", new int[]{2, 5, 9});
        chordsList.put("E Major", new int[]{4, 7, 11});
        chordsList.put("Fm (F minor)", new int[]{5, 8, 0});
        chordsList.put("F#m (F# minor)", new int[]{6, 9, 1});
        chordsList.put("Gm (G minor)", new int[]{7, 10, 2});
        chordsList.put("Ab (A flat) Major", new int[]{8, 11, 3});
        chordsList.put("Bb (B flat) Major", new int[]{10, 1, 4});     
        chordsList.put("C° (C diminished)", new int[]{0, 3, 6});
        chordsList.put("D° (D diminished)", new int[]{2, 5, 8});
        chordsList.put("E° (E diminished)", new int[]{4, 7, 10});
        chordsList.put("F° (F diminished)", new int[]{5, 8, 11});
        chordsList.put("G° (G diminished)", new int[]{7, 10, 1});
        chordsList.put("A° (A diminished)", new int[]{9, 0, 3});
        chordsList.put("B° (B diminished)", new int[]{11, 2, 5});
    }

    public static Map<String, int[]> getChordsList() {
        return chordsList;
    }
}