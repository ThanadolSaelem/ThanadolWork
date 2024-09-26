const words = [
  "apple",
  "banana",
  "orange",
  "mango",
  "strawberry",
  "grape",
  "pineapple",
  "blueberry",
  "raspberry",
  "blackberry",
  "kiwi",
  "watermelon",
  "cantaloupe",
  "honeydew",
  "papaya",
  "guava",
  "lychee",
  "dragonfruit",
  "passionfruit",
  "grapefruit",
  "lime",
  "lemon",
  "cherry",
  "peach",
  "nectarine",
  "apricot",
  "plum",
  "pomegranate",
  "fig",
  "date",
  "persimmon",
  "tangerine",
  "clementine",
  "kumquat",
  "durian",
  "jackfruit",
  "starfruit",
  "avocado",
  "coconut",
  "cranberry",
  "currant",
  "gooseberry",
  "elderberry",
  "quince",
  "pear",
  "acai",
  "sapodilla",
  "soursop",
  "mulberry",
  "boysenberry",
  "loganberry",
  "jambolan",
  "salak",
  "rambutan",
  "longan",
  "pomelo",
];

let word = words[Math.floor(Math.random() * words.length)];
let guessedLetters = [];
let guessesLeft = 6;
let message = "";

function updateWord() {
    let displayWord = "";
    for (let i = 0; i < word.length; i++) {
        if (guessedLetters.includes(word[i])) {
            displayWord += word[i];
        } else {
            displayWord += "_";
        }
    }
    document.getElementById("word").innerHTML = displayWord;
}

function updateLetters() {
    let letters = "";
    for (let i = 0; i < guessedLetters.length; i++) {
        letters += guessedLetters[i] + " ";
    }
    document.getElementById("letters").innerHTML = letters;
}

function updateMessage() {
    if (guessesLeft === 0) {
        message = "You lost! The word was " + word;
    } else if (word.split("").every(letter => guessedLetters.includes(letter))) {
        message = "You won!";
    } else {
        message = "Guesses left: " + guessesLeft;
    }
    document.getElementById("message").innerHTML = message;
}

function guessLetter(letter) {
    if (!guessedLetters.includes(letter)) {
        guessedLetters.push(letter);
        if (!word.split("").includes(letter)) {
            guessesLeft--;
        }
    }
    updateWord();
    updateLetters();
    updateMessage();
    if ((guessesLeft === 0) || (word.split("").every(letter => guessedLetters.includes(letter))))
    {
        document.getElementById("playAgain").style.display = "block";
    }
}

function resetGame() {
    word = words[Math.floor(Math.random() * words.length)];
    guessedLetters = [];
    guessesLeft = 6;
    message = "";
    document.getElementById("word").innerHTML = "_ _ _ _ _";
    document.getElementById("letters").innerHTML = "";
    document.getElementById("message").innerHTML = "";
    document.getElementById("playAgain").style.display = "none";
}

document.onkeyup = function(event) {
    guessLetter(event.key);
}

resetGame();