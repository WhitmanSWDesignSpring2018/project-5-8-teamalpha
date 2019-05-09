# GRADE: 76/76

## Functional and implementation requirements: 52/52 points 

### Menu: 14/14 points

| Pts Earned | Pts Possible | Requirement | Comments
|-----------:|-------------:|-------------|---------
| 1 | 1 | Add **Cut**, **Copy**, and **Paste** menu items to the **Edit** menu  with the standard keyboard shortcuts: Shortcut-X, Shortcut-C, Shortcut-V.
| 1 | 1 | Add to the **File** menu: **About...**, **New**, **Open...**, **Save**, and **Save as...**. Use standard shortcuts: Shortcut-N, Shortcut-O, Shortcut-S, and Shortcut-Shift-S.  **About** need not have a shortcut.
| 1 | 1 | There should be a separator between the **About...** menu item and the rest of the **File** menu items and there should be a separator between the **Exit** menu item (placed at the bottom of the menu) and the rest of the menu items.
| 1 | 1 | **Cut** is disabled when nothing is selected.
| 1 | 1 | **Copy** is disabled when nothing is selected.
| 1 | 1 | **Paste** is disabled when there is nothing on the clipboard or it does not contain appropriate data.
| 1 | 1 | **Save** is disabled when the document is unchanged since the previous save.
| 1 | 1 | **Undo** is disabled when the program starts and when all actions have been undone; it is enabled when the first action is taken.
| 1 | 1 | **Redo** is disabled unless there is an action to redo.
| 1 | 1 | **Group** is disabled when there are fewer than two items selected.
| 1 | 1 |  **Ungroup** is disabled when the selection is not (or does not contain) a gesture.
| 1 | 1 | **Delete** is disabled when nothing is selected.
| 1 | 1 |  **Play** is disabled when there are no notes.
| 1 | 1 |  **Stop** is disabled when the composition is not playing.

### Cut/Copy/Paste Implementation: 12/12 points

| Pts Earned | Pts Possible | Requirement | Comments
|-----------:|-------------:|-------------|---------
| 1 | 1 | When **Copy** is chosen, all currently selected note rectangles and gesture rectangles are copied.
| 1 | 1 | When **Cut** is chosen, all currently selected note rectangles and gesture rectangles are copied and deleted.
| 2 | 2 | When **Paste** is chosen, the cut or copied rectangles are added to the composition panel. 
| 1 | 1 | Pasted rectangles become the only selected rectangles. 
| 1 | 1 | You should be able to paste the contents of the clipboard multiple times.
| 2 | 2 |  Gestures can be cut/copied/pasted just like individual notes.
| 1 | 1 | The system clipboard should be used, rather than a clipboard internal to your program.
| 3 | 3 |  **Cut** and **Paste** should be undoable/redoable actions. ( **Copy** is not undoable.) 

### File Dialogs: 14/14 points

| Pts Earned | Pts Possible | Requirement | Comments
|-----------:|-------------:|-------------|---------
| 2 | 2 |  When the **About...** menu item is selected, a dialog box appears whose text includes the authors of the software.  The dialog should have only a **Close** or **OK** button.
| 2 | 2 |  When the **Save as...** menu item is chosen, a dialog appears in which the user is asked for the name of the file into which the sounds in the composition panel are to be saved. If the user enters a legal name and presses the **OK** button in the dialog, then the file should be saved.  If the user presses the **Cancel** button in the dialog, then the dialog closes and no saving occurs.
| 2 | 2 |  When the **Save** menu item is chosen for a composition that was not loaded from a file nor ever saved to a file, it behaves like the **Save as...** menu item. If the current composition was loaded from a file or previously saved to a file, then the composition is saved to that file. No dialog appears in that case.
| 3 | 3 | If the **New** menu item is chosen and the current composition has already been saved to a file, then the composition panel is cleared as if the application had just started up. If the current composition has been changed since it was last saved to a file, a dialog appears asking whether you want to save the composition before closing it. There should be 3 options: **Yes**, **No**, **Cancel**. If the user chooses **No** from the dialog, the current composition is disposed of and the composition panel is cleared.  If the user chooses **Cancel**, the dialog just disappears. If the user chooses **Yes**, the behavior is the same as if the **Save** menu item was chosen.
| 3 | 3 | If the **Open...** menu item is chosen, it behaves like the **New** menu item with regard to saving the current composition. Then, if the user hasn't canceled, an dialog appears in which the user can select a file to open. If the user chooses a valid file, it is loaded into the composition pane. If the user cancels, the dialog disappears. If the user chooses an invalid file, an error message is displayed and the open dialog stays visible.
| 2 | 2 |  If the **Exit** menu item is chosen and there have been changes made to the composition since it was last saved, then a dialog appears asking the user whether she wants to save the composition before exiting. If the user chooses **No** from the dialog, then the program exits.  If the user chooses **Cancel**, then the exiting is canceled and the application stays running. If the user chooses **Yes**, then the application behaves as if the **Save** menu item was chosen, after which the application exits.
| 1 | 0 | If the user clicks the window close button, the behavior should be the same as for the **Exit** menu item.

### Save/Open Implementation: 8/8 points

| Pts Earned | Pts Possible | Requirement | Comments
|-----------:|-------------:|-------------|---------
| 2 | 2 |  Saved files include complete information about all notes and gestures (ideally in plain text so it can be verified - if not this will be assessed based on the point below).
| 4 | 4 | Opening a saved file restores all notes and gestures from the saved composition.
| 2 | 2 |  Starting a new composition or opening a file should cause all undo/redo state to be cleared. You cannot undo the saving or loading of a file or a new composition.

### Above & beyond: 0/0 points

| Pts Earned | Pts Possible | Requirement | Comments
|-----------:|-------------:|-------------|---------
| 2 | 0 | For better usability, pasted elements are slightly offset from their original location (down and to the right).
| 2 | 0 | The clipboard content is restored to its previous value when undoing a **Cut** operation.

### No regressions - 4/4 points

| Pts Earned | Pts Possible | Requirement | Comments
|-----------:|-------------:|-------------|---------
| 4 | 4 | All prior requirements are met, unless they have been superseded by new requirements or are documented by the team as known bugs for this iteration.

## Release - 2/2 points

| Pts Earned | Pts Possible | Requirement | Comments
|-----------:|-------------:|-------------|---------
| 1 | 1 | The release is tagged as ```project-7-release```.
| 1 | 1 | Documentation is in the ```project-7``` directory.

## Reflection and elegance - 22/22 points

| Pts Earned | Pts Possible | Requirement | Comments
|-----------:|-------------:|-------------|---------
| 4 | 4 | UML diagram is accurate and complete.
| 4 | 4 | New classes/methods are reasonably self-explanatory.
| 2 | 2 | Design overview addresses changes from Project 6 in general.
| 1 | 1 | Design overview specifically addresses classes and methods responsible for **Cut/Copy/Paste**. 
| 1 | 1 | Design overview addresses strategy for disabling/enabling menu items.
| 1 | 1 | Design overview explains which classes are responsible for saving and loading files, and how they interact with other classes.
| 1 | 1 | Design overview addresses which classes are responsible for composition state management (Does the composition have a filename? Has it changed since it was created, opened, or last saved?), and how they interact with other classes.
| 6 | 6 | Assessment of what is elegant and what is not thoughtfully addresses object-oriented design principles.
| 1 | 1 | Velocity is presented. 
| 1 | 1 | Team retrospective is presented.
