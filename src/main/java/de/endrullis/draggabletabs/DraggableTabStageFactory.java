package de.endrullis.draggabletabs;

/**
 * Factory for stages created for draggable tabs.
 *
 * @author Stefan Endrullis (stefan@endrullis.de)
 */
@SuppressWarnings("unused")
public class DraggableTabStageFactory {

	private static DraggableTabStageFactory defaultFactory = new DraggableTabStageFactory();

	/**
	 * Returns the factory for stages created for draggable tabs.
	 *
	 * @return factory for stages created for draggable tabs
	 */
	public static DraggableTabStageFactory getDefaultFactory() {
		return defaultFactory;
	}

	/**
	 * Sets the factory for stages created for draggable tabs.
	 *
	 * @param defaultFactory factory for stages created for draggable tabs
	 */
	public static void setDefaultFactory(DraggableTabStageFactory defaultFactory) {
		DraggableTabStageFactory.defaultFactory = defaultFactory;
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

}
