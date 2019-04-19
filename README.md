# **Project 6**

## **Design Overview:**
We used a command pattern and undoable interface to implement Undo and Redo. It consisted of two deques, because they are faster than stacks according to the internet. The Command Manager has three children commands: DeleteCommand, AddNoteCommand, and EditCommand. We wanted to have a buttonController and ClickController with their owm FXML files to refactor TuneComposer but ran into techincal debt and reverted back to having everything in TuneComposer. We fixed several small issues related to the code reviews, but could not accomplish the refactoring issues due to technical debt. We chose not to disable the play and stop menu item actions because, as per John Cage, a song without notes is still a song.

## **Elegancy:**
Our Command Manager is by far the most elegant class in this project. This class does one thing, manages all 3 commands, and it does it well(Single Responsibility Principle). On the other side of the same principle, our tunecomposer class has many responsibilites making it quite inelegant as well as being lengthy. As noted in Design Overview, we tried to refactor into multiple different classes but ran into technical debt. Despites its inelegancy, TuneComposer is the only class that deals with and knows about the set of playables, gestures and notes, making it slightly less bad. In reference to the other classes, our Group Command and Add Note Command are essentially the same thing but one deals with gestures and the other with notes making it inelegant for having code duplication.

## **Velocity:**
We thought that each of the commands classes would be just 1 story point, the command manager 6 points, and refactoring around 3 points. In reality, the command classes did take around 1 story point per and the command manager took around 8. The refactoring however took well over 15 person-hours and we ended up just reverting all of our changes back due to the technical debt established. In project 5 we did no refactoring so most of the project 5 time was actually working on implementing the requirements, while project 6 we spent alot of time working on understanding how to have multiple FXML files and how to connect them all together. We believe that if we did not have to deal with the refactoring and its resulting technical debt and focused on the requirements we would have been able to accomplish this project more elegantly.

## **Team Retrospective:**
It was hard for all us to meet together, and most of our work time was spent in groups of two or three people who were available at a given time. This made it hard to get a decent planned design that we all agreed on. Also because we met in groups of two or three, it meant that those who were not there got left out of the loop which led to confusion about the project's direction. In the future we will work on projects everyday from day 1 till it is due.




