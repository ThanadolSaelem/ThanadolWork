package ChordAnalyzer;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
public class ChordUI {
    private JFrame frame;
    private JTextField filePathField;
    private JTextArea chordsTextArea;
    private JButton openFileButton;
    private JButton analyzeButton;

    public ChordUI() {
        createUI();
    }

    private void createUI() {
        frame = new JFrame("Chord Analyzer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
    
        // Create panel for file path and open file button
        JPanel filePathPanel = new JPanel();
        filePathPanel.setLayout(new FlowLayout());
        filePathField = new JTextField(30);
        openFileButton = new JButton("Open File");
        openFileButton.addActionListener(new OpenFileActionListener());
        filePathPanel.add(filePathField);
        filePathPanel.add(openFileButton);
    
        // Create panel for analyze button and chords text area
        JPanel analyzePanel = new JPanel();
        analyzePanel.setLayout(new BorderLayout());
        analyzeButton = new JButton("Analyze");
        analyzeButton.addActionListener(new AnalyzeActionListener());
        analyzeButton.setEnabled(false);
        chordsTextArea = new JTextArea(10, 30);
        chordsTextArea.setEditable(false);
        analyzePanel.add(analyzeButton, BorderLayout.NORTH);
        analyzePanel.add(new JScrollPane(chordsTextArea), BorderLayout.CENTER);
    
        // Create panel for clear button
        JPanel clearPanel = new JPanel();
        clearPanel.setLayout(new FlowLayout());
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chordsTextArea.setText("");
            }
        });
        clearPanel.add(clearButton);
    
        // Add panels to frame
        frame.add(filePathPanel, BorderLayout.NORTH);
        frame.add(analyzePanel, BorderLayout.CENTER);
        frame.add(clearPanel, BorderLayout.SOUTH);
    
        frame.pack();
        frame.setVisible(true);
    }

    private class OpenFileActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("MIDI files", "mid");
            fileChooser.setFileFilter(filter);
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePathField.setText(selectedFile.getAbsolutePath());
                analyzeButton.setEnabled(true);
            }
        }
    }

    private class AnalyzeActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String midiFilePath = filePathField.getText();
            if (midiFilePath != null && !midiFilePath.isEmpty()) {
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
    
                                    // Add note to current notes
                                    ChordAI.getCurrentNotes().add(note % 12);
    
                                    // Identify the chord based on the current notes
                                    String chord = ChordAI.identifyChord();
                                    if (chord != null) {
                                        chordsTextArea.append("Chord: " + chord + "\n");
                                    }
                                } else if (sm.getCommand() == ShortMessage.NOTE_OFF) {
                                    int note = sm.getData1();
    
                                    // Remove note from current notes
                                    ChordAI.getCurrentNotes().remove((Integer) (note % 12));
                                }
                            }
                        }
                    }
                } catch (InvalidMidiDataException | java.io.IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error analyzing MIDI file: " + ex.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChordUI();
            }
        });
    }
}