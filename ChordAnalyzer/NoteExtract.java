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

public class NoteExtract {
    private static List<Integer> currentNotes = new ArrayList<>();

    public void analyzeMidiFile(String midiFilePath) {
        try {
            Sequence sequence = MidiSystem.getSequence(new File(midiFilePath));

            Track[] tracks = sequence.getTracks();
            for (Track track : tracks) {
                for (int i = 0; i < track.size(); i++) {
                    MidiEvent event = track.get(i);
                    MidiMessage message = event.getMessage();

                    if (message instanceof ShortMessage) {
                        ShortMessage sm = (ShortMessage) message;

                        if (sm.getCommand() == ShortMessage.NOTE_ON) {
                            int note = sm.getData1();
                            int velocity = sm.getData2();
                            System.out.println("Note: " + note + " Velocity: " + velocity);
                            currentNotes.add(note % 12);

                            String chord = identifyChord();
                            if (chord != null) {
                                System.out.println("Chord: " + chord);
                                currentNotes.clear();
                            }
                        } else if (sm.getCommand() == ShortMessage.NOTE_OFF) {
                            int note = sm.getData1();
                            currentNotes.remove(note % 12);
                        }
                    }
                }
            }
        } catch (InvalidMidiDataException | java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> getCurrentNotes() {
        return currentNotes;
    }

    public static String identifyChord() {
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