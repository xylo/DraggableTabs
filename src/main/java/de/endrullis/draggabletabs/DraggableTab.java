package de.endrullis.draggabletabs;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.util.HashMap;
import java.util.Map;

/**
 * Draggable tab.
 *
 * @author Stefan Endrullis (stefan@endrullis.de)
 */
public class DraggableTab extends Tab {

	/** Data format used as the drag content of dragged tabs. */
	protected static final DataFormat dataFormat = new DataFormat("DraggableTab");

	/** The tab that is currently dragged. */
	public static final ObjectProperty<DraggableTab> draggingTab = new SimpleObjectProperty<>();

	/** Factory for creating new stages with draggable tabs. */
	protected DraggableTabStageFactory stageFactory = DraggableTabStageFactory.getDefaultFactory();

	/** Tab label component. */
	protected final Label label;

	/**
	 * Creates a draggable tab with the given title.
	 *
	 * @param title title of the tab
	 */
	public DraggableTab(String title) {
		super();

		label = new Label(title) {{
			setOnDragDetected(event -> {
				WritableImage snapshot = snapshot(new SnapshotParameters(), null);

				Map<DataFormat, Object> dragContent = new HashMap<>();
				dragContent.put(dataFormat, "");

				Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
				dragboard.setDragView(snapshot);
				dragboard.setContent(dragContent);

				draggingTab.set(DraggableTab.this);

				event.consume();
			});

			setOnDragOver(event -> {
				if (isDraggingTab(event.getDragboard())) {
					event.acceptTransferModes(TransferMode.MOVE);
					event.consume();
				}
			});

			setOnDragDropped(event -> {
				if (isDraggingTab(event.getDragboard())) {
					final Tab draggingTab = DraggableTab.draggingTab.get();

					if (draggingTab != DraggableTab.this) {
						TabPane oldTabPane = draggingTab.getTabPane();
						oldTabPane.getTabs().remove(draggingTab);

						int thisTabIndex = getTabPane().getTabs().indexOf(DraggableTab.this);
						getTabPane().getTabs().add(thisTabIndex, draggingTab);
						getTabPane().getSelectionModel().select(draggingTab);

						DraggableTabUtils.cleanup(oldTabPane);
					}

					event.setDropCompleted(true);
					DraggableTab.draggingTab.set(null);
					event.consume();
				}
			});

			setOnDragDone(new EventHandler<DragEvent>() {
				public void handle(DragEvent event) {
					if (!event.isAccepted()) {
						TabPane oldTabPane = getTabPane();
						oldTabPane.getTabs().remove(DraggableTab.this);

						stageFactory.createNewStage(DraggableTab.this);

						DraggableTabUtils.cleanup(oldTabPane);
					}
					draggingTab.set(null);
					event.consume();
				}
			});
		}};

		setGraphic(label);
	}

	/**
	 * Returns true if the given dragboard contains the tab that is currently dragged.
	 *
	 * @param dragboard dragboard
	 * @return true if the given dragboard contains the tab that is currently dragged
	 */
	public static boolean isDraggingTab(Dragboard dragboard) {
		return dragboard.hasContent(dataFormat) && DraggableTab.draggingTab.get() != null;
	}

	/**
	 * Returns the title of this tab.
	 * This method replaces the {@link #getText()} method, but since some genius JavaFX developers thought
	 * it would be FUN to make {@link #getText()} FINAL we are not allowed to overwrite it and may create a
	 * new method for the same purpose! Thanks!
	 *
	 * @return title of the tab
	 */
	public String getTitle() {
		return label.getText();
	}

	/**
	 * Sets the title of this tab.
	 * This method replaces the {@link #setText(String)} method, but since some genius JavaFX developers thought
	 * it would be FUN to make {@link #setText(String)} FINAL we are not allowed to overwrite it and may create a
	 * new method for the same purpose! Thanks!
	 *
	 * @param title title of the tab
	 */
	public void setTitle(String title) {
		label.setText(title);
	}

}
