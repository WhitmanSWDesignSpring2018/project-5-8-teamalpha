/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

/**
 * An interface to represent all the commands that will be undoable.
 * @author Abbey Felley Colin Aslett Kimberly Taylor Angie Mead
 */

public interface Undoable {
	void undo();
	void redo();
}
