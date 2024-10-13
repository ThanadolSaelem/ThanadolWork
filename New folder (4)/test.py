import re
from xml.etree.ElementTree import Element, SubElement, tostring

def lilypond_to_musicxml(lilypond_str):
    # Initialize MusicXML structure
    score = Element('score-partwise', version='3.1')
    part = SubElement(score, 'part', id='P1')

    # Split the input into measures
    measures = lilypond_str.strip().split('|')

    for measure_index, measure in enumerate(measures):
        measure = measure.strip()
        if not measure:
            continue

        # Create a MusicXML measure
        xml_measure = SubElement(part, 'measure', number=str(measure_index + 1))

        # Process notes within the measure
        notes = measure.split()
        for note in notes:
            note = note.strip()
            if not note or note.startswith('%'):
                continue

            # Detect and handle chords
            if note.startswith('<') and note.endswith('>'):
                chord_notes = note[1:-1].split()
                first_note = True
                for chord_note in chord_notes:
                    add_note_to_xml(xml_measure, chord_note, is_chord=not first_note)
                    first_note = False
            else:
                add_note_to_xml(xml_measure, note, is_chord=False)

    # Convert the XML tree to a string and return
    return tostring(score, encoding='unicode')

def add_note_to_xml(xml_measure, note, is_chord=False):
    # Match note patterns such as d,,,4 or e,,8, etc.
    note_match = re.match(r'([A-Ga-g])([#b]*),*(\d+)', note)
    if note_match:
        pitch_name = note_match.group(1).upper()  # Convert pitch name to upper case for MusicXML
        accidental = note_match.group(2)
        commas = note.count(',')  # Count commas to determine the octave shift
        octave = 4 - commas  # Base octave in LilyPond is 4; adjust for commas
        duration = note_match.group(3)  # Note duration (e.g., 4 for quarter, 8 for eighth)

        xml_note = SubElement(xml_measure, 'note')

        # Add pitch
        pitch = SubElement(xml_note, 'pitch')
        step = SubElement(pitch, 'step')
        step.text = pitch_name
        octave_elem = SubElement(pitch, 'octave')
        octave_elem.text = str(octave)

        # Add accidental if present
        if accidental == '#':
            accidental_elem = SubElement(xml_note, 'accidental')
            accidental_elem.text = 'sharp'
        elif accidental == 'b':
            accidental_elem = SubElement(xml_note, 'accidental')
            accidental_elem.text = 'flat'
        elif accidental == 'bb':
            accidental_elem = SubElement(xml_note, 'accidental')
            accidental_elem.text = 'double-flat'
        elif accidental == '##':
            accidental_elem = SubElement(xml_note, 'accidental')
            accidental_elem.text = 'double-sharp'

        # Add duration
        duration_elem = SubElement(xml_note, 'duration')
        duration_elem.text = str(duration)

        # Add note type (e.g., quarter, half)
        note_type = SubElement(xml_note, 'type')
        note_type_mapping = {
            '1': 'whole',
            '2': 'half',
            '4': 'quarter',
            '8': 'eighth',
            '16': 'sixteenth',
            '32': 'thirty-second'
        }
        note_type.text = note_type_mapping.get(duration, 'quarter')  # Default to quarter

        # Handle chords: add <chord/> tag if this note is part of a chord
        if is_chord:
            SubElement(xml_note, 'chord')

# Example usage
lilypond_input = '''
  \\time 4/4
  % Measure 1
  <d,,,4 e,,,4 e,,4 >
  |
'''

# Generate MusicXML output from LilyPond input
musicxml_output = lilypond_to_musicxml(lilypond_input)

# Print the MusicXML output
print(musicxml_output)

# Optionally, write the output to a file
with open('output.musicxml', 'w') as f:
    f.write(musicxml_output)

# Example usage
#lilypond_file = 'lilypond_file.ly'
#musicxml_tree = lilypond_to_musicxml(lilypond_file)

# Write the MusicXML to a file
#with open('output.musicxml', 'w') as f:
#    xml_tree = ElementTree(musicxml_tree)
#    xml_tree.write(f, encoding='unicode', xml_declaration=True)

#print("MusicXML file 'output.musicxml' generated.")