=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: mahadiks
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays
  I used a 2D array to represent the state of my board as the game progressed. The array was of 
  size court width/square size, court height/square size. The size decision was made this way 
  because the game objects should only appear on certain pixel levels to maintain a user aesthetic.
  The grid stored values to represent whether the location was occupied by the snake (1), an 
  enemy (2), an apple (3), or nothing (0). These values had to be updated each time an event 
  occurred. This helped keep track of where apples and enemies could be generated so there would be
  no overlap.

  2. Java Collections
  I used a LinkedList<Square> to represent my snake internally. The list stored squares 
  sequentially in the order in which they should be generated. The snake had a head that was 
  accessible through a get method. The linked list provided many advantages when programming. It
  allowed for iteration through the list which could be used for movement and drawing the squares 
  in the proper location on the board. 
  
  LinkedList<Square> was also used to store the newly generated enemies and apples. This 
  allowed for ease in using a for each loop to check for intersections with the snake 
  and drawing.
  

  3. File I/O
  I used File I/O to read and write high scores from file. Though my initial idea was to 
  include levels of difficulty, the feedback said this was too simple. The high score reader
  and writer are contained in the ScoreParser.java file, and use the HighScoreFileIterator to
  go through the file. After each game ends, the writer puts the game score in the file if it
  is greater that the smallest element in that file or if the file has less than 3 lines. Then
  the high score is displayed in game and top three scores on the pregame menu along with the date 
  the high score occurred.

  4. Testable Component
  I designed my game using the MVC method to make it JUnit Testable. I used this to test methods
  in Snake.java, HighScoreFileIterator.java, ScoreParser.java, and GameBoard.java. 

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  Game -> GUI portion of game; controls user interactions
  GameBoard -> maintained state of the current game; handled timer increments, reseting, drawing,
               and keypad interactions
  GameObj -> kept/updated from Mushroom of Doom
  Square -> kept/updated from Mushroom of Doom
  Snake -> extension of GameObj; handled snake movement, intersection methods, and growth
  RandomPositionGenerator -> randomized position for apple/enemy movement
  HighScoreFileIterator -> Iterator that goes through a specific file
  ScoreParser -> File I/O meant to read and write high scores


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  I found that moving the snake was difficult, as each square had to move with a seemingly 
  different velocity, but I noticed that the square merely adopted the preceding square's
  velocities.
  Snake intersections were also difficult, but I was apple to rely on dynamic dispatch with
  GameObj to handle this issue

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  I believe there is a good separation of functionality. The state doesn't seem to be 
  accessible outside the class. If I could refactor, I would most likely handle drawing and
  movement by going through the state array, as this would allow for different functionalities
  like saving the game.



========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
