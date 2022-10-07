# Word-finder
An incredibly good task to do for learning more about storing and searching data using different data structures. 
Implementing datastructure lik Trie and HashSet that uses a hash table for storage. 

# How to run 
The project is inside the Word-finder repository, just clone Solver-Final as a project

1. Place the “Solver.java” file inside the directory of the task.
2. Compile the java file using “javac Solver.java”
3. Run the program using “java Solver <grid-file> <words-file> <min-count>”
   a.  E.g : java Solver grid10x10.txt words.txt 3
4. Output will be appended to the “results.txt” file in the same directory


# Task description:
- Write a short command-line program which takes some input, reads a couple of files, solves the problem and prints the solution to screen.
  There is several bouns tasks as well!

- **Problem**:
  We want to find all the words with a given length hidden in
  a 2D grid. A word can be created by connecting neighboring
  characters. You can not connect characters diagonally, only
  vertical and horizontal. The words don't need to be in a
  straight line, see the example below. We want these words
  listed to us by length, then by alphabetical order.

Example:
Given this 3x3 grid:

jop
shn
tvq

With a minimum length of 3 characters, you'll want to find
these words in the grid and list them in the following
order:
- john
- shop
- hop
- ohs

For more examples, see the other files.

Input format:
- Path to the file with the grid to be solved
- Path to the word file with all the words to search for.
- Optional, specify a minimum length for the word to find.

Example input:
java Solver grid3x3.txt words.txt 3

Grid file format:
- A grid of ascii lower case characters (a-z).
- Each character represent a field.
- A newline character ("\n") separates each row.

Word file format:
- All lower case ascii characters (a-z).
- A newline character ("\n") separates each word.

What was the main focus for me:
- Readability of the code (try to keep it simple)

**Bonus**:
-- What 16 letter word is hidden in the 1000x1000 grid?
- Google check style.
- Write solution to disk.
- No third-party library other than for tests.
- Alternative and more efficient solutions.
- Add support for wildcard and/or empty field in grid.
- Create a grid generator (maybe ensure long words exists).
