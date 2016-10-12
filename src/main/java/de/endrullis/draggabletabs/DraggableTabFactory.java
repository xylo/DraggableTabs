package de.endrullis.draggabletabs;

import com.sun.javafx.scene.traversal.Direction;
import javafx.scene.Node;

/**
 * Factory for stages created for draggable tabs.
 *
 * @author Stefan Endrullis (stefan@endrullis.de)
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class DraggableTabFactory {

	private static DraggableTabFactory factory = new DraggableTabFactory();

	/**
	 * Returns the factory for stages created for draggable tabs.
	 *
	 * @return factory for stages created for draggable tabs
	 */
	public static DraggableTabFactory getFactory() {
		return factory;
	}

	/**
	 * Sets the factory for stages created for draggable tabs.
	 *
	 * @param factory factory for stages created for draggable tabs
	 */
	public static void setFactory(DraggableTabFactory factory) {
		DraggableTabFactory.factory = factory;
	}

	/**
	 * Creates a new stage for the given draggable tab.
	 *
	 * @param tab draggable tab
	 * @return new stage
	 */
	public DraggableTabStage createNewStage(DraggableTab tab) {
		return new DraggableTabStage(tab);
	}

	/**
	 * Creates a new insert pane for a draggable tab layout extender.
	 *
	 * @param draggableTabLayoutExtender  draggable tab layout extender
	 * @param direction                   direction
	 * @return insert pane
	 */
	public DraggableTabInsertPane createInsertPane(DraggableTabLayoutExtender draggableTabLayoutExtender, Direction direction) {
		return new DraggableTabInsertPane(draggableTabLayoutExtender, direction);
	}

	/**
	 * Creates a new tab pane for draggable tabs.
	 *
	 * @param tabs tabs to add
	 * @return tab pane
	 */
	public DraggableTabPane createTabPane(DraggableTab... tabs) {
		return new DraggableTabPane(tabs);
	}

	/**
	 * Creates a new layout extender for draggable tab panes and other components.
	 *
	 * @param content component to wrap by the layout extender.
	 * @return layout extender
	 */
	public DraggableTabLayoutExtender createLayoutExtender(Node content) {
		return new DraggableTabLayoutExtender(content);
	}

	/**
	 * Wraps the tab in a tab pane with layout extender.
	 * @param tabs tabs
	 * @return wrapped tab
	 */
	public DraggableTabLayoutExtender wrapTab(DraggableTab... tabs) {
		return createLayoutExtender(createTabPane(tabs));
	}

}
