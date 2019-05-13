# **Project 7**

## **Design Overview:**
We made five new classes FileSaver, XMLParser, ClipboardWrapper, PasteCommand, and CutCommand. FileSaver is responsible for saving files in the directory the user chooses in the save dialog. XML parser takes an XML file with notes and gestures represented by strings in it and turns them into playables. ClipboardWrapper changes notes and gestures from XML to Playables (and the other way around) and pushes and pops these to the clipboard. PasteCommand and CutCommand take a Collection of playables and add or remove them from the composition. They can be undone and redone. 

From feedback for earlier projects, we finally implemented platform independent keyboard shortcuts and resolved some generics. 

## **Elegance:**
We were able to use the CommandManager that we made in the last project to quickly and easily add the new functionality of making cut and paste redo/undoable. CommandManager is extensible which is a characteristic of elegant software. Command pattern. 

The XMLParser uses recursion and follows the single-responsibility principle of only doing one thing. 

Not Elegant: 
FileSaver should probably have the code for open in addition to the code for save because now FileChooser is used in two different classes. 
We have an instrument in XML parser that is only used to make a different instrument because we couldn't figure out how to go from an XML string to an instrument. 
When we convert something into XML we call the toString method because we could not figure out in the amount of time we had the serializable interface. 

We have high coupling between TuneComposer and all our other classes.  

## **Velocity:**
We thought that Copy,Cut,and Paste Commands would each take 1 story point. We estimated that XML Parser,ClipboardWrapper, and FileSaver would each be 2 story points. Addtionally, we thought that implementing Save,Open,and New would each take 1 story point.Other miscellaneous implementation tasks (such as disabling menu items and adding new menu items) would take 1 story point total. In total, that is 13 story points.  
We spent a total of 40 person hours on this project. These 40 hours were in the span of one week since the first week that we had to do this project we were still working on implementing the previous project. Since we completed this project in a week, we did not have the time to make it as elegant as we would have liked or refactor. 
The ratio of points to person hours is 13/40 = .3 story points per hour.

## **Team Retrospective:**
We spent so much time on this project because we really struggled with the previous one and really wanted to make sure we were able to complete this project. We ended up meeting in groups of 2 or 3 everyday of the week. Spending time every single day on the project worked well as we were able to get much more of this project completed in time compared to the last one. 


We weren't sure if you should be able to save blank compositions or not but decided that you should since you are able to save blank documents in Word. 


