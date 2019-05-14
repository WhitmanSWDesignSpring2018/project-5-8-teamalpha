# Project 8
## Design Overview:

We implemented the following new features: right click dialog box (with options to change instrument,change volume, delete, group, ungroup, play selected),  vertical beat lines, note line labels, and changing intensity of the color of notes when their volume changes. We chose to implement these features because Angie (the musically inclined person in our group) said these were the features she would want if she was actually using the program to compose a tune. 

From feedback for earlier projects, we resolved many bugs (however we added more during this project). The bugs we resolved include fixing enabling/disabling menu items, making it exit after exit and save, fixing new, dividers in menu items, and fixing the names of the old project folders. We also have worked 

## Elegance:
We implemented our right click dialog box within TuneComposer which is not the most elegant way to do it. However, it did go nicely with our other event handlers. Additonally, for the functionality of group, ungroup, delete, we were able to just call those functions which is elegant because the function code did not need to be repeated.  

To implement change volume, we changed the volume field in Note to no longer be static. It was simple for us to add volume to our XMLParser and toString method which means that our design can respond to change. 

Implementing the color intensity change with volume change ended up being more difficult than we thought. We tried to change the opacity of the note, but this changed the opacity of the whole note including the selection and note outline. We got around this by giving each note two rectangles, one of which is responsible for the border and one of which is responsible for the fill and handlers. This was surprisingly easy to implement because we gave notes the responsibility of changing their own opacity, which reduced coupling in TuneComposer and brings notes closer to fulfilling the expert pattern. We also put the two rectangles in a group, which made it easy to reflect these changes in the composition pane.

We added NoteNames as a new class. 

TuneComposer continues to be a god class. 

We have significant bugs in our program. Technical debt caught up with us and it became harder to add new features and fix stuff.  

## Velocity:
We were unsure how long this project would take us since it was rather open ended. In the end, we decided that we would try to implement all the features described in instructions described under the dialog box addition option as well as note names and beat lines. We estimated it would take 2 story points to revise most of the simpler bugs from project 7. We thought that implementing change volume and change instrument would each also take 2 story points each. We intially thought that changing the color of the note when the volume changes would be simple (1 story point) but it ended up being more complicated than we thought. Implementing delete, group and ungroup from the right click dialog box would take 1 story point all together. We thought that adding vertical beat lines and note line labels would also take 1 story point together. In total, that is 9 story points. 

We spent a total of 30 person hours on this project. 

Veloctiy: .3 story points/hour. This is a similar velocity to our last project. 

## Team Retrospective:
We worked realy hard on this project. Our final outcome is not perfect, but we feel like we learned a lot through the struggle. The technical debt that we incurred throughout this project was much more than we ever could have imagined. In the future, we will be more cautious about the accumulation of technical debt. 
