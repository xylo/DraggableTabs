package de.endrullis.draggabletabs;

import com.sun.javafx.scene.traversal.Direction;

/**
 * Factory for stages created for draggable tabs.
 *
 * @author Stefan Endrullis (stefan@endrullis.de)
 */
@SuppressWarnings("unused")
public class DraggableTabFactory {

	private static DraggableTabFactory defaultFactory = new DraggableTabFactory();

	/**
	 * Returns the factory for stages created for draggable tabs.
	 *
	 * @return factory for stages created for draggable tabs
	 */
	public static DraggableTabFactory getDefaultFactory() {
		return defaultFactory;
	}

	/**
	 * Sets the factory for stages created for draggable tabs.
	 *
	 * @param defaultFactory factory for stages created for draggable tabs
	 */
	public static void setDefaultFactory(DraggableTabFactory defaultFactory) {
		DraggableTabFactory.defaultFactory = defaultFactory;
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

}
