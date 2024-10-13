import 'dart:io';

class Note {
  String noteName;
  int octave;
  int duration;
  String type;

  Note(this.noteName, this.octave, this.duration, this.type);
}

class Measure {
  List<dynamic> notes = [];

  void addNote(Note note) {
    notes.add(note);
  }

  void addChord(List<Note> chord) {
    notes.add(chord);
  }
}

class MusicXMLConverter {
  static String convertToMusicXML(String input) {
    List<Measure> measures = [];
    List<String> lines = input.split('\n');

    for (String line in lines) {
      if (line.startsWith('\\meter')) {
        // Time signature, skip parsing octave and duration
        Measure measure = Measure();
        measures.add(measure); // Add the measure to the list
        continue;
      } else if (line.startsWith('[') && line.endsWith(']')) {
        Measure measure = Measure();
        List<String> notesStr = line.substring(1, line.length - 1).split(' ');
        for (String noteStr in notesStr) {
          if (noteStr.startsWith('\\meter')) {
            // Time signature, skip parsing octave and duration
            List<String> parts = noteStr.split('<');
            String timeSignature = parts[1].substring(0, parts[1].length - 1);
            // You can add code here to handle the time signature
            continue;
          } else if (noteStr.startsWith('(') && noteStr.endsWith(')')) {
              List<String> chordNotesStr = noteStr.substring(1, noteStr.length - 1).split(' ');
              List<Note> chord = [];
              for (String chordNoteStr in chordNotesStr) {
                List<String> parts = chordNoteStr.split('/');
                String pitch = parts[0];
                String noteName = pitch.substring(0, 1); // Get the note name (C, D, E, etc.)
                int octave = int.parse(pitch.substring(1)); // Get the octave
                int duration;
                if (parts[1].length > 1) {
                  duration = int.parse(parts[1].substring(0, 1));
                } else {
                  duration = int.parse(parts[1]);
                }
                String type;
                if (duration == 4) {
                  duration = 1;
                  type = 'quarter';
                } else if (duration == 2) {
                  type = 'half';
                } else if (duration == 1) {
                  duration = 4;
                  type = 'whole';
                } else {
                  type = 'quarter';
                }
                Note note = Note(noteName, octave, duration, type);
                chord.add(note);
              }
              measure.addChord(chord);
            } else {
                List<String> parts = noteStr.split('/');
                String pitch = parts[0];
                String noteName = pitch.substring(0, 1); // Get the note name (C, D, E, etc.)
                int octave = int.parse(pitch.substring(1)); // Get the octave
                int duration;
                if (parts[1].length > 1) {
                  duration = int.parse(parts[1].substring(0, 1));
                } else {
                  duration = int.parse(parts[1]);
                }
                String type;
                if (duration == 4) {
                  duration = 1;
                  type = 'quarter';
                } else if (duration == 2) {
                  type = 'half';
                } else if (duration == 1) {
                  duration = 4;
                  type = 'whole';
                } else {
                  type = 'quarter';
                }
                Note note = Note(noteName, octave, duration, type);
            measure.addNote(note);
          }
        }
        measures.add(measure); // Add the measure to the list
      }
    }

    String musicXML = '''
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<!DOCTYPE score-partwise PUBLIC "-//Recordare//DTD MusicXML 3.1 Partwise//EN" "http://www.musicxml.org/dtds/partwise.dtd">
<score-partwise version="3.1">
  <part-list>
    <score-part id="P1">
      <part-name>Part 1</part-name>
    </score-part>
  </part-list>
  <part id="P1">
''';

    for (Measure measure in measures) {
      musicXML += '    <measure number="${measures.indexOf(measure) + 1}">\n';
      for (dynamic note in measure.notes) {
        if (note is Note) {
          musicXML += '      <note>\n';
          musicXML += '        <pitch>\n';
          musicXML += '          <step>${note.noteName}</step>\n';
          musicXML += '          <octave>${note.octave}</octave>\n';
          musicXML += '        </pitch>\n';
          musicXML += '        <duration>${note.duration}</duration>\n';
          musicXML += '        <type>${note.type}</type>\n';
          musicXML += '      </note>\n';
        } else if (note is List<Note>) {
          // Chord
          musicXML += '      <chord>\n';
          for (int i = 0; i < note.length; i++) {
            musicXML += '        <note>\n';
            if (i > 0) {
              musicXML += '          <chord/>\n';
            }
            musicXML += '          <pitch>\n';
            musicXML += '            <step>${note[i].noteName}</step>\n';
            musicXML += '            <octave>${note[i].octave}</octave>\n';
            musicXML += '          </pitch>\n';
            musicXML += '          <duration>${note[i].duration}</duration>\n';
            musicXML += '          <type>${note[i].type}</type>\n';
            musicXML += '        </note>\n';
          }
          musicXML += '      </chord>\n';
        }
      }
      musicXML += '    </measure>\n';
    }

    musicXML += '''
  </part>
</score-partwise>
''';

    return musicXML;
  }
}

void main() {
  String input = '''
[\\meter<"4/4"> (C4/1 D4/1 E4/1) F4/4 G4/2 A4/4 B4/4 C5/4]
[\\meter<"4/4"> C5/4 D5/2 E5/4 F5/2 G5/4 A5/2 B5/4 C5/4] 
[\\meter<"4/4"> (C4/2 D4/4 E4/2) F4/4 G4/2 A4/4 B4/4 C5/4] 
''';

  String musicXML = MusicXMLConverter.convertToMusicXML(input);

  File file = File('output.musicxml');
  file.writeAsStringSync(musicXML);
}