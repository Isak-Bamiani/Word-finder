import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
// i have much better explanations in the code review
class TrieNode {

  TrieNode[] next = new TrieNode[26];
  boolean isWord;
  String word;
}

class WordDictBuilder {

  /**
   * Builds a Trie structure - Optimal Data Structure for word searching and lookups
   *
   * @param words
   * @return
   */
  public static TrieNode buildTrie(List<String> words) {
    System.out.println("- Builing words dictionary(Total words : " + words.size() + ")");
    // dummy node/empty node
    TrieNode root = new TrieNode();
    words.stream().forEach(word -> {
      //current node
      TrieNode node = root;
      // toCharArray returnere en liste av char fra hvert ord
      for (char c : word.toCharArray()) {
        // creating some sort of index
        int i = c - 'a';
        if (node.next[i] == null) {
          node.next[i] = new TrieNode();
        }
        node = node.next[i];
      }
      node.isWord = true;
      node.word = word;
    });
    return root;
  }
}

class Move {

  int dr;
  int dc;

  public Move(int dr, int dc) {
    this.dr = dr;
    this.dc = dc;
  }
}

class GridBuilder {

  /**
   * N x M grid builder with the given line of words
   *
   * @param words
   * @return
   */
  public static List<List<Character>> buildGrid(List<String> words) {
    System.out.println("- Builing grid (Total rows : " + words.size() + ")");
    List<List<Character>> grid = new ArrayList<>();
    words.forEach(word -> {
      grid.add(word.chars().mapToObj(
                      // tar imot c (character type) og converter den
              c -> Character.valueOf((char) c)).collect(Collectors.toList()));
    });
    return grid;
  }

}

class WordFinder {

  final List<List<Character>> grid;
  final List<String> words;
  final int rowCount;
  final int colCount;
  Set<String> foundWords;
  int minimumLength;

  public WordFinder(List<List<Character>> grid, List<String> words) {
    this.grid = grid;
    this.words = words;
    rowCount = grid.size();
    colCount = grid.get(0).size();
  }

  /**
   * Find the words from the Grid
   * Can have optional object with an empty value.
   * @return
   */
  public List<String> findWords() {
    return findWords(Optional.empty());
  }

  public List<String> findWords(Optional<Integer> minWordCount) {
    foundWords = new HashSet<String>();
    minimumLength = minWordCount.orElse(0);
    // building a dictionary like structure of words as root node.
    TrieNode root = WordDictBuilder.buildTrie(words);

    System.out.println("---------------------------------------");
    for (int r = 0; r < rowCount; r++) {
      for (int c = 0; c < colCount; c++) {
        move(root, r, c, new HashSet<String>());
      }
    }
    List<String> output = foundWords.stream().collect(Collectors.toList());
    output.sort((o1, o2) -> {
      if (o1.length() != o2.length()) {
        return o2.length() - o1.length();
      }
      return o1.compareTo(o2);
    });
    return output;
  }

  private void move(TrieNode node, int r, int c, Set<String> visitedPath) {
    if (node == null) {
      return;
    }

    char charAt = grid.get(r).get(c);
    List<TrieNode> validNodes = new ArrayList<>();
    if (charAt != '*') {
      TrieNode charNode = node.next[charAt - 'a'];
      validNodes.add(charNode);
    } else {
      validNodes.addAll(Arrays.asList(node.next));
    }

    validNodes.forEach(currNode -> {
      if (currNode == null) {
        return;
      }
      Set<String> newVisitedPath = new HashSet<>(visitedPath);
      newVisitedPath.add(r + "," + c);

      if (currNode.isWord && currNode.word.length() >= minimumLength) {
        foundWords.add(currNode.word);
      }

      List<Move> movements = new ArrayList<>(Arrays.asList(new Move(1, 0), new Move(0, 1), new Move(-1, 0), new Move(0, -1)));
      movements.forEach(move -> {
        int newR = r + move.dr;
        int newC = c + move.dc;
        if ((newR >= 0 && newR < rowCount) && (newC >= 0 && newC < colCount)) {
          move(currNode, newR, newC, newVisitedPath);
        }
      });
    });

  }
}

public class Solver {

  private static WordFinder wordFinder;

  public static void main(String args[]) {
    System.out.println("******* Started Solver **********");
    // testRun();
    // testRunWithWildCard();
    runByArgs(args);
    System.out.println("******* Finished Solver **********");
  }

  private static void runByArgs(String[] args) {
    String gridPath = null;
    String wordsPath = null;
    String minCount = null;
    if (args.length > 0) {
      gridPath = args[0];
    }
    if (args.length > 1) {
      wordsPath = args[1];
    }
    if (args.length > 2) {
      minCount = args[2];
    }

    try {
      List<String> gridLines = readFile(gridPath);
      List<String> wordsLines = readFile(wordsPath);
      Optional<Integer> minimumLength = Optional.empty();
      if (minCount != null) {
        minimumLength = Optional.of(Integer.parseInt(minCount));
      }

      List<List<Character>> grid = GridBuilder.buildGrid(gridLines);
      wordFinder = new WordFinder(grid, wordsLines);
      List<String> output = wordFinder.findWords(minimumLength);

      System.out.println("- Found Words : " + output.size());
      verifyResultsAgainstWords(wordsLines, output);
      writeFile(output);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  private static List<String> readFile(String fileName) throws FileNotFoundException, IOException {
    List<String> fileLines = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      String line;
      while ((line = br.readLine()) != null) {
        fileLines.add((line));
      }
    }
    return fileLines;
  }

  public static void writeFile(List<String> foundWords) {
    Path file = Paths.get("results.txt");
    try {
      Files.write(file, foundWords, StandardCharsets.UTF_8);
      System.out.println("- Output appended to results.txt");
    } catch (IOException e) {
      System.out.println("Error : " + e);
    }
  }

  private static void verifyResultsAgainstWords(List<String> givenWords, List<String> foundWords) {
    Set<String> givenWordsSet = new HashSet<>(givenWords);
    long totalInvaid = foundWords.stream().filter(word -> !givenWordsSet.contains(word)).count();
    System.out.println("- Verification (Total invalid words found) : " + totalInvaid);
  }

  private static void testRun() {
    List<String> wordsLines = new ArrayList<>(Arrays.asList("john", "shop", "hop", "ohs", "a", "ksut"));
    List<String> gridWords = new ArrayList<>(Arrays.asList("jop", "shn", "tvq"));

    List<List<Character>> grid = GridBuilder.buildGrid(gridWords);
    wordFinder = new WordFinder(grid, wordsLines);
    List<String> output = wordFinder.findWords();

    System.out.println("- Found Words : " + output.size());
    verifyResultsAgainstWords(wordsLines, output);
    writeFile(output);
  }

  private static void testRunWithWildCard() {
    List<String> wordsLines = new ArrayList<>(Arrays.asList("john", "shop", "hop", "ohs", "a", "ksut"));
    List<String> gridWords = new ArrayList<>(Arrays.asList("j*p", "shn", "tvq"));

    List<List<Character>> grid = GridBuilder.buildGrid(gridWords);
    wordFinder = new WordFinder(grid, wordsLines);
    List<String> output = wordFinder.findWords();

    System.out.println("- Found Words : " + output.size());
    verifyResultsAgainstWords(wordsLines, output);
    writeFile(output);
  }
}
