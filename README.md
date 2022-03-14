# Hangman Game - Multimedia NTUA

This is a version of the Hangman game with some adjustments, for the course of multimedia technology in NTUA/ECE for the academic year 2021-2022.
The project is written in Java using the OOP principles. The game is  created in a way that supports multiple dictionaries with the assumption
that they are named "hangman_DICTIONARΥ-ID.txt" and they are stored in a pre-determined directory named "\medialab". 

![medialab/images/README_images/img.png](img.png)

To create a dictionary the user has to click on the menu bar "Application", choose the menu item "Create" and provide a dictionary-ID and a valid
open-library-ID (OL-ID). If something  goes wrong, error messages will be displayed (Ubalanced, Ubdersized, not valid OLID) and after closing that
tab, if no valid input is given, the application will terminate. If everything is ok,a dictionary will be created, stored in the previously stated 
folder and loaded so the user can start a game. 

![medialab/images/README_images/img_1.png](img_1.png)  

If the "medialab" folder already contains dictionaries (in this Github repository are provided 6 valid dictionarys with IDs from '01'-'06' and 
some not valid for testing purposes), the user can load them, without having to create them. In order to load a dictionary choose the menu item 
"Load" and provide a dictionary-ID. If something  goes wrong, error messages (Ubalanced, Ubdersized, InvalidRange, InvalidCount) will be displayed 
and after closing that tab, if no valid input is given, the application will terminate. If everything is ok, a dictionary will be loaded so the 
user can start a game.

![medialab/images/README_images/img_2.png](img_2.png)

In order to play a game, the user has to choose the menu item "Start" from the "Application" menu and a game will be initialized choosing a random
word from the loaded or created dictionary. If a game is already active and the user presses Start, a new game will begin and the previous progress
will be lost.

![medialab/images/README_images/img_3.png](img_3.png)

When the game starts, the player is shown in the middle-right part of the screen a probability list for every letter of the word, so that she/he/they
can decide on the move they make (letter and position of the letter in the word). The list is updated throughout the game based on the correct
choices of the player. 

![medialab/images/README_images/img_4.png](img_4.png)  ![medialab/images/README_images/img_5.png](img_5.png)

The player can make at most 5 wrong choices. If a 6th wrong choice is made, the game ends and the computer wins. If the 
player finds the word, the game ends and the player wins. Also if the player chooses the menu item "Solution" from the "Details" menu, the game 
ends and the computer wins.

![medialab/images/README_images/img_6.png](img_6.png)  ![medialab/images/README_images/img_7.png](img_7.png)  ![medialab/images/README_images/img_8.png](img_8.png)

The input is provided by typing the letter and the position in the textfields on the bottom-left part of the game screen and pressing the "GO" 
button. Throughout the game the player can see messages on the bottom-right part of the screen, the choices already made on the bottom-left and on
the upper part the: number of availabe words in the dictionary, ID of the selected dictionary, points in current game, percentage of successful
choices and the number of mistakes. 

![medialab/images/README_images/img_9.png](img_9.png)

The player can see the last 5 games results by clicking on the menu item "Rounds" from menu "Details". She/he/they can also see some dictionary
details by clicking on the menu item "Dictionary" from menu "Details".

![medialab/images/README_images/img_10.png](img_10.png)  ![medialab/images/README_images/img_11.png](img_11.png)  ![medialab/images/README_images/img_12.png](img_12.png)

Player can exit the game at any time from the menu "Application" by clicking on the "Exit" menu item.