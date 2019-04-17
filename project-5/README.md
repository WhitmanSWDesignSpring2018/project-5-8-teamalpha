**Give a concise overview of your design for the new features. How did you divide the code into classes and methods? Make sure you address whether and how you refactored the code implementing earlier features.**

We created a new class Gesture. We decided to make a Playable interface that Gesture and Note implement. Each Gesture has a set of Playables that it contains. We also added the group and ungroup methods within the TuneComposer class. We built on the project 5 starter solution and did not refactor this code in a significant way. 
    
**Explain what, if anything, is particularly elegant about your solution and why it is elegant. For full credit, apply concepts, guidelines, and/or principles you learned in class.**

Having an interface is elegant because it deals with the similarities between notes and gestures. It made scheduling notes to be played and nesting gestures fairly simple to implement. Having a Gesture that is a set of playables allows a Gesture to contain notes and other Gestures. Each class inherits the abstract methods in the interface. 
  
**Explain what, if anything, in your solution is inelegant and why you didn't make it elegant (for example, maybe you didn't have time or the knowledge to fix it). For full credit, apply concepts, guidelines, and/or principles you learned in class.**

The Ungroup method seems inelegant because it contains three for loops, one of which is nested inside another. _So are you concerned about the cyclomatic complexity?_ It also requires the creation of two new arraylists. It's less elegant because we wrote it last so we couldn't think of another way to get it to work at all within the infrastructure we had made for the gestures and also we were running out of time at that point. Also, the way makeGroup ended up being implemented is inelegant and is the result of a patch to a documented issue. Writing a more elegant version would have required rewriting and redesigning a lot of code and there wasn't time by the time the issue was discovered.

**Include an estimate of your velocity. How many story points did you estimate you would complete during this assignment? How many did you actually complete, how many person-hours did the team spend, and what is the ratio of points/person-hour? I want you to monitor your velocity to help you plan better over the final iterations. There are no "good" or "bad" numbers for velocity.**
    
We worked about 23 person hours on this project. We thought there would be about 5 story points and that seems fairly accurate having completed it. The ratio of points/person hour is 1/4 to 1/5. 

_This is really helpful! I'll be very curious to see if that estimate changes for Project 6._

**How did you colloborate? What role[s] did each team member play? What went well that your team will keep doing during the next project assignment? What will you improve? How?**

We primarily did pair programmming and occasionally worked in groups of three. To a certain extent everyone played the roles of developer, solutions architecht, and quality assurance. Occasionally someone would perform some of the functions of the development lead. We liked using the GitHub issues feature to systematically handle one implementation challenge at a time. This also allowed us to update members of the group on what parts still needed to be implemented. Hopefully we will all be able to meet together at once more in the future.
