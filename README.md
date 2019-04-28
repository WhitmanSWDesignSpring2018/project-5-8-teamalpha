# **Project 6**
Edited to reflect changes for new project-6-release 

## **Design Overview:**
We used a command pattern and undoable interface to implement Undo and Redo. It consisted of two deques, because they are faster than stacks according to the internet. The Command Manager has six children commands: DeleteCommand, AddNoteCommand, MoveCommand, SelectCommand, GroupCommand and StretchCommand. 
We wanted to have a buttonController and ClickController with their owm FXML files to refactor TuneComposer but ran into techincal debt and reverted back to having everything in TuneComposer. We fixed several small issues related to the code reviews, but could not accomplish the refactoring issues due to technical debt. 

We made it so that menu items are disabled when appropriate. 

## **Elegancy:**
Our Command Manager is by far the most elegant class in this project. This class does one thing, manages all 6 commands, and it does it well(Single Responsibility Principle). On the other side of the same principle, our tunecomposer class has many responsibilites making it quite inelegant as well as being lengthy. As noted in Design Overview, we tried to refactor into multiple different classes but ran into technical debt. Despites its inelegancy, TuneComposer is the only class that deals with and knows about the set of playables, gestures and notes, making it slightly less bad. In reference to the other classes, our Group Command and Add Note Command are essentially the same thing but one deals with gestures and the other with notes making it inelegant for having code duplication. 

We coded to interfaces because we referred to Collections instead of specific classes. 

## **Velocity:**
We thought that each of the commands classes would be just 1 story point, the command manager 6 points, and refactoring around 3 points. In reality, the command classes did take around 1 story point per and the command manager took around 8. The Stretch, Move, and Select commands were much more difficult to implement than AddNote so we should have given them more story points. The refactoring however took well over 25 person-hours and we ended up just reverting all of our changes back due to the technical debt established. In project 5 we did no refactoring so most of the project 5 time was actually working on implementing the requirements, while project 6 we spent alot of time working on understanding how to have multiple FXML files and how to connect them all together. We believe that if we did not have to deal with the refactoring and its resulting technical debt and focused on the requirements we would have been able to accomplish this project more elegantly.

Since the original deadline, we have spent an additional 20 person hours trying to get more of the requirements to work. We met with Janet many times to figure out how to get stuff to work and what tasks we should prioritize. We hope to be able to refactor and fix more issues that have been pending for a while before we submit project 7 but we are going to prioritize the implementation requirements first because we did not for this project and that delayed our progress significantly. 

## **Team Retrospective:**
It was hard for all us to meet together, and most of our work time was spent in groups of two or three people who were available at a given time. This made it hard to get a decent planned design that we all agreed on. Also because we met in groups of two or three, it meant that those who were not there got left out of the loop which led to confusion about the project's direction. In the future we will work on projects everyday from day 1 till it is due.




