# 2IP90-CBL-project
## 2IP90 Programming CBL Project - Project Group 176
Project Group 176: Huy Hong (1935569) and Long Pham (2000954) from Eindhoven University of Technology (TU/e).

### Git Repository
The Git repository can be viewed via this link: https://github.com/huyhong04/2IP90-CBL-project

### Game Description
This game is called "Escapade", a Pac-mn inspired game, where the objective is to reach the end goal in the fastest time possible
while avoiding main game elements such as:
- Walls, to stop the player from moving in a certain direction.
- Obstacles, to kill the player upon touch (these don't move).
- Monsters, to kill the player upon touch (these do move).

There are 3 different stages ranging in difficulty, where the stage map differs by its size and the amount of game elements:
- In the 'Easy' stage, there are no monsters and very few walls and obstacles. The corresponding map is of dimension 15x15.
- As for the 'Medium' stage, there are now monsters, but the amount of walls and obstacles is still relatively few. The corresponding map is of dimension 25x25.
- Lastly, for the 'Hard' stage, there are much more walls, obstacles, and monsters. The corresponding map is of dimension 40x40.

### To Play the Game
The playing process is classified into steps for easier reference.
1. To run the game, open GameWindow and run it. This file 'GameWindow' contains the main GUI along with all the panels which represent different pages within the game. When opened, the player is then directed to the home screen, where the player will see a welcome message and three buttons of choice:
- The 'New Stage' button, in which the player will be redirected to a name input screen. The player can then input their name, where this name must contain only alphabetical characters and cannot have more than 20 characters.
- The 'Leaderboard' button, in which the player will be redirected to the leaderboard. The player can see the top 10 scores with the fastest finished stage times.
- The 'Quit Game' button, in which a popup will appear for confirmation. If 'Yes' is clicked, then the game is exited, and if 'No' is clicked, the player will remain on the home screen.

2. Once the 'New Stage' button is pressed, the player then fills in their player name with the aforementioned instructions above. Press the 'Submit Player Name' to be further directed to the stage selection screen, where there are 3 stages of choice, ranging in difficulty ('Easy', 'Medium', 'Hard'). There are also descriptions of each stage and a brief description of what game elements are to be expected in each stage map, along with the dimensions of the stage map:
- Walls: They are immobile and block the player from moving in a certain direction.
- Obstacles: They are immobile and can kill the player upon collision.
- Monsters: They are mobile and can kill the player upon collision, but they also destroy walls upon collision.

Note that once the player has inputted their name, they are not allowed to change their name (see Step 3 how to change the player name if needed). 

3. Once the player has chosen a stage, the corresponding stage map will appear, where within the stage map, each game element is denoted by a color on the stage map. Aside from the game elements, the player is spawned there as the player (denoted blue), and the player's task is to reach the goal (denoted green) in the fastest time possible without being killed. For each stage window, there are 3 default buttons:
- 'Quit Stage': The player is then redirected to the stage selection screen.
- 'Quit Game Session': The player is then redirected to the home screen, where they can then input a new name when reaching the name input screen again by following the same process as mentioned above.
- 'Quit Game': This button is identical to the 'Quit Game' button on the home screen, where it will ask for confirmation to quit the game. If 'Yes' is clicked, then the game is exited, and if 'No' is clicked, they will remain in the chosen stage map screen. 

Note that because the 'Easy' stage doesn't have any monsters, the chance of there being a valid path in the stage map might be lower. 
In this case, please click the 'Quit Stage' button and choose the 'Easy' stage again until a valid path appears. 
In the case of there being no initial valid path in the 'Medium' or 'Hard' stages, monsters are present and thus have the ability to destroy the walls. 
Therefore, there will be a certain point where the monsters have destroyed enough walls to create a valid path, which allows the player to move accordingly. 
However, a consequence of this is the loss of time, resulting in a slower finish stage time, which can affect the chance of being on the leaderboards.

In the stage map, no two game elements can occupy one cell, so in the case of a player colliding with an obstacle or monster, this would result in death. 
4. There will be a popup message indicating this to inform the player what has happened, and thus the player is then redirected back to the stage selection screen. Vice versa, if the player has reached the goal, a popup message will appear which indicates that the player has passed the stage and hence will be directed to the leaderboard screen, where the player can see if the finish stage time is quick enough to be ranked on the leaderboard.
Within the leaderboard, there is a 'Home Screen' button which allows the player to be redirected to the home screen. If the player chooses to play again, please follow Step 1.

Note that once the player exits the game, the scores saved in the leaderboards will be gone. The scores are only saved in the time when the game is run.

### Learning Goals
Our two learning goals are version control and test-driven development. 
Regarding version control, the commit history and branch structure of the Git repository can be seen in the assignment submission. As for test-driven development, there is an attached document named "tdd-process" regarding our implementation of test-driven development with corresponding references in this Git repository.

### Inspiration and References
This game was inspired by the platformers "Pacman" and "Tomb of the Mask".
