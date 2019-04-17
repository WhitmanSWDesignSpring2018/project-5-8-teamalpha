/**
 *
 * @author aslettcj
 */
package tunecomposer;

import java.util.Collection;
import java.util.Set;

public class DeleteCommand implements Undoable {
	private static Collection<Playable> removedPlayables;
	
	public DeleteCommand(Collection<Playable> toRemove) {
            removedPlayables = toRemove;
	}

	public void undo() {
            for (Playable p : removedPlayables) {
		p.getRectangle().setVisible(true);
            }
            TuneComposer.allPlayables.addAll(removedPlayables);
	}
	
	public void redo() {
            for (Playable p : removedPlayables) {
		p.getRectangle().setVisible(false);
            }
            TuneComposer.allPlayables.removeAll(removedPlayables);
	}
}
