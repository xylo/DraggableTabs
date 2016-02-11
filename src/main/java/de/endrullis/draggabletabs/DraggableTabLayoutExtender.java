package de.endrullis.draggabletabs;

import com.sun.javafx.scene.traversal.Direction;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

/**
 * Wrapper for the DraggableTabPane and other components allowing the user to move DraggableTabs
 * to the top, left, right, or bottom of this DraggableTabLayoutExtender.
 *
 * @author Stefan Endrullis (stefan@endrullis.de)
 */
public class DraggableTabLayoutExtender extends BorderPane {

	public static int EXTENDER_SIZE = 20;

	/** Tab that was dragged last. */
	private DraggableTab lastDraggingTab = null;

	/**
	 * Creates an extensible layout around the given component.
	 *
	 * @param component component that shall be wrapped
	 */
	public DraggableTabLayoutExtender(Node component) {
		super(component);

		// offer drop areas in all directions when user is dragging a tab
		DraggableTab.draggingTab.addListener((observable, oldTab, newTab) -> {
			if (lastDraggingTab != newTab) {
				lastDraggingTab = newTab;

				if (newTab != null) {
					DraggableTabFactory factory = DraggableTabFactory.getDefaultFactory();
					setTop(factory.createInsertPane(this, Direction.UP));
					setBottom(factory.createInsertPane(this, Direction.DOWN));
					setLeft(factory.createInsertPane(this, Direction.LEFT));
					setRight(factory.createInsertPane(this, Direction.RIGHT));
				} else {
					setTop(null);
					setBottom(null);
					setLeft(null);
					setRight(null);
				}
			}
		});
	}

}
