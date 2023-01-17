# 3D Maze Game #

![2D Maze](docs/images/2D%20Maze%20Start.jpg "2D Maze")
*2D Maze*

![3D Maze](docs/images/3D%20Maze%20Start.jpg "3D Maze")
*3D Maze*

## Description ##
A first-person maze game made with Java Swing.

Maze games are a classic puzzle game in which a player moves in branched passages to find a particular target or location. These games are typically done on a 2D board, but a 3D approach allows for a unique new perspective to better immerse the user in the puzzle. From a top down view, users can very easily see if they are on the right path, but a first-person perspective limits their vision and ability to look beyond what is right in front of them.

Corridor Example 1| Corridor Example 2
:---------------------|------------:
![3D Maze Corridor](docs/images/3D%20Maze%20Corridors.jpg) | ![3D Maze Corridor 2](docs/images/3D%20Maze%20Corridors%202.jpg)

As seen in the images above, the player can see that there are two corridors that branch off their current path, but can not see if they are a dead end without turning into them.

The game also supports basic "power-ups" in the form of flashlights, hammers, and sprays.

Flashlight Off | Flashlight On
:---------------------|------------:
![3D Maze Flashlight Off](docs/images/3D%20Maze%20Flashlight%20Off.jpg) | ![3D Maze Flashlight On](docs/images/3D%20Maze%20Flashlight%20On.jpg)

The picture on the left hand side above shows the player's view without a flashlight, and the picture on the right shows the player's view with a flashlight. The player can see 3 additional tiles with the flashlight on, and in this case, sees that there is a corridor at the end of his path.

Before Hammer | After Hammer
:---------------------|------------:
![3D Maze Before Hammer](docs/images/3D%20Maze%20Wall.jpg) | ![3D Maze After Hammer](docs/images/3D%20Maze%20Broken%20Wall.jpg)

The pictures above show the before and after of a hammer use on a wall. A hammer destroys a single wall, and can help a user find their path to the finish line with ease.

![3D Maze Spray](docs/images/3D%20Maze%20Painted%20Wall.jpg)

The last powerup currently supported in the game is a spray. A user is allowed to spraypaint 10 blocks to mark paths they have previously visited. This allows a user to quickly identify that they have already gone down a certain path, and lets them know to continue elsewhere.

## Building, Compiling, and Running ##
Note: This project requires java.
```
$ make
$ cd out
$ java Application
```

The commands above will build, compile, and run the application, bringing out a second screen to play the game.
