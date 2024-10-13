import re

def convert_to_lilypond(input_str):
    # Regular expressions to match notes, accidentals, and duration
    note_regexp = re.compile(r'([A-Ga-g])([#b]*)(\d)(/\d+)?')  # Capture accidentals
    chord_regexp = re.compile(r'\((.*?)\)')

    # Output buffer
    lilypond_buffer = []

    # Split input by measure groups (sections within brackets)
    sections = re.split(r'\]', input_str)
    measure_count = 1

    # Dictionary to map accidentals to LilyPond notation
    accidental_map = {
        '#': 'is',
        '##': 'isis',
        'b': 'es',
        'bb': 'eses'
    }

    for section in sections:
        if section.strip() == '':
            continue

        # Extract meter information
        meter_regexp = re.compile(r'\\\\meter<"(.*?)">')
        meter_match = meter_regexp.search(section)
        if meter_match:
            meter = meter_match.group(1)
            lilypond_buffer.append(f'\\time {meter}')

        # Remove meter and brackets, and split into notes/chords
        note_section = re.sub(meter_regexp, '', section).replace('[', '').replace(']', '')
        elements = note_section.split()

        # Start measure
        lilypond_buffer.append(f'% Measure {measure_count}')

        for element in elements:
            element = element.strip()
            if element == '':
                continue

            # Check for chords
            chord_match = chord_regexp.match(element)
            if chord_match:
                chord_content = chord_match.group(1)
                chord_notes = [note.strip() for note in chord_content.split(',')]
                chord_buffer = '<'  # Start chord
                for note in chord_notes:
                    note_match = note_regexp.match(note)
                    if note_match:
                        note_name = note_match.group(1)
                        accidental = note_match.group(2)  # Capture accidental
                        octave = int(note_match.group(3))
                        duration = note_match.group(4) or '/4'

                        # Convert duration to LilyPond format (without the '/')
                        duration = duration.replace('/', '')

                        # Apply accidentals to the note
                        accidental_suffix = ''.join([accidental_map.get(a, '') for a in accidental])

                        # Adjust octave for LilyPond format relative to middle C
                        octave_mod = ''
                        if octave == 4:
                            octave_mod = ""
                        elif octave > 4:
                            octave_mod = "'" * (octave - 4)
                        elif octave < 4:
                            octave_mod = "," * (4 - octave)

                        chord_buffer += f'{note_name.lower()}{accidental_suffix}{octave_mod}{duration} '

                chord_buffer += '>'  # End chord
                lilypond_buffer.append(chord_buffer)

            else:
                # Process single note
                note_match = note_regexp.match(element)
                if note_match:
                    note_name = note_match.group(1)
                    accidental = note_match.group(2)  # Capture accidental
                    octave = int(note_match.group(3))
                    duration = note_match.group(4) or '/4'

                    # Convert duration to LilyPond format (without the '/')
                    duration = duration.replace('/', '')

                    # Apply accidentals to the note
                    accidental_suffix = ''.join([accidental_map.get(a, '') for a in accidental])

                    # Adjust octave for LilyPond format relative to middle C
                    octave_mod = ''
                    if octave == 4:
                        octave_mod = ""
                    elif octave > 4:
                        octave_mod = "'" * (octave - 4)
                    elif octave < 4:
                        octave_mod = "," * (4 - octave)

                    lilypond_buffer.append(f'  {note_name.lower()}{accidental_suffix}{octave_mod}{duration} ')

        # End measure with barline
        lilypond_buffer.append('  |')
        measure_count += 1

    return '\n'.join(lilypond_buffer)


input_str = ''
# Read the input from a text file
with open('input.txt', 'r') as file:
    data = file.read()

# Split the data into individual note sequences
notes = data.split(']\n[')

# Process and print each note sequence
for note in notes:
    note = note.strip()  # Remove leading/trailing whitespace
    if note:  # Check if the note is not empty
        if note[0] != '[':  # Add a '[' at the beginning if it's not there
            note = '[ ' + note
        if note[-1] != ']':  # Add a ']' at the end if it's not there
            note += ' ]'
        input_str += note + '\n' 

lilypond_output = convert_to_lilypond(input_str)
print(lilypond_output)

# Create a file from the LilyPond output
with open('lilypond_file.ly', 'w') as f:
    f.write(lilypond_output)


