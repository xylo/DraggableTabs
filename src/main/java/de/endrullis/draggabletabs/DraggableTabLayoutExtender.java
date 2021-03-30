package de.endrullis.draggabletabs;

import javafx.beans.DefaultProperty;
import javafx.beans.NamedArg;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.skin.TextInputControlSkin.Direction;
import javafx.scene.layout.BorderPane;

/**
 * Wrapper for the DraggableTabPane and other components allowing the user to move DraggableTabs
 * to the top, left, right, or bottom of this DraggableTabLayoutExtender.
 *
 * @author Stefan Endrullis (stefan@endrullis.de)
 */
@SuppressWarnings("WeakerAccess")
@DefaultProperty("center")
public class DraggableTabLayoutExtender extends BorderPane {

	public static int                          EXTENDER_SIZE = 20;
	private final ChangeListener<DraggableTab> draggingTabListener;

	/** Tab that was dragged last. */
	private DraggableTab lastDraggingTab = null;

	/**
	 * Creates an extensible layout around the given component.
	 *
	 * @param component component that shall be wrapped
	 */
	public DraggableTabLayoutExtender(@NamedArg("center") Node component) {
		super(component);

		// offer drop areas in all directions when user is dragging a tab
		draggingTabListener = (observable, oldTab, newTab) -> {
			if (lastDraggingTab != newTab) {
				lastDraggingTab = newTab;

				if (getParent() == null) {
					return;
				}

				if (!(getParent().getParent() instanceof SplitPane && getCenter() instanceof SplitPane) && newTab != null) {
					DraggableTabFactory factory = DraggableTabFactory.getFactory();
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
		};
		DraggableTab.draggingTab.addListener(draggingTabListener);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		DraggableTab.draggingTab.removeListener(draggingTabListener);
	}
}
