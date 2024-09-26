package ChordAnalyzer;
import java.io.File;
import java.util.*;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class ChordAI {
    private static Set<Integer> currentNotes = new HashSet<>();

    public static void main(String[] args) {
        String midiFilePath = "C:/Users/Mynew/Downloads/chords.mid";

        try {
            Sequence sequence = MidiSystem.getSequence(new File(midiFilePath));

            // Iterate over tracks
            Track[] tracks = sequence.getTracks();
            for (Track track : tracks) {
                // Iterate over events in the track
                for (int i = 0; i < track.size(); i++) {
                    MidiEvent event = track.get(i);
                    MidiMessage message = event.getMessage();

                    if (message instanceof ShortMessage) {
                        ShortMessage sm = (ShortMessage) message;

                        if (sm.getCommand() == ShortMessage.NOTE_ON) {
                            int note = sm.getData1();
                            int velocity = sm.getData2();
                            System.out.println("Note: " + note + " Velocity: " + velocity);

                            // Add note to current notes
                            currentNotes.add(note % 12);

                            // Identify the chord based on the current notes
                            String chord = identifyChord();
                            if (chord != null) {
                                System.out.println("Chord: " + chord);
                                // Reset current notes after identifying a chord
                                currentNotes.clear();
                            }
                        } else if (sm.getCommand() == ShortMessage.NOTE_OFF) {
                            int note = sm.getData1();
                            // Remove note from current notes
                            currentNotes.remove(note % 12);
                        }
                    }
                }
            }
        } catch (InvalidMidiDataException | java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private static String identifyChord() {
        for (Map.Entry<String, int[]> entry : Chord.getChordsList().entrySet()) {
            boolean isChord = true;
            for (int note : entry.getValue()) {
                if (!currentNotes.contains(note)) {
                    isChord = false;
                    break;
                }
            }
            if (isChord) {
                return entry.getKey();
            }
        }
        return null;
    }
}