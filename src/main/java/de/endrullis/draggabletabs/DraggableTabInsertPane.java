package de.endrullis.draggabletabs;

import com.sun.javafx.scene.traversal.Direction;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import static de.endrullis.draggabletabs.DraggableTab.isDraggingTab;
import static de.endrullis.draggabletabs.DraggableTabUtils.rearrangeDividers;

/**
 * @author Stefan Endrullis (endrullis@iat.uni-leipzig.de)
 */
public class DraggableTabInsertPane extends StackPane {
	protected final DraggableTabLayoutExtender draggableTabLayoutExtender;
	protected final Direction                  direction;

	public DraggableTabInsertPane(DraggableTabLayoutExtender draggableTabLayoutExtender, Direction direction) {
		this.draggableTabLayoutExtender = draggableTabLayoutExtender;
		this.direction = direction;
		switch (this.direction) {
			case UP:
			case DOWN:
				setMinHeight(DraggableTabLayoutExtender.EXTENDER_SIZE);
				setMaxHeight(DraggableTabLayoutExtender.EXTENDER_SIZE);
				break;
			case LEFT:
			case RIGHT:
				setMinWidth(DraggableTabLayoutExtender.EXTENDER_SIZE);
				setMaxWidth(DraggableTabLayoutExtender.EXTENDER_SIZE);
				break;
		}

		getStyleClass().add("draggableTabInsertPane");

		registerListeners();
	}

	protected void registerListeners() {
		setOnDragOver(event -> {
			if (isDraggingTab(event.getDragboard())) {
				event.acceptTransferModes(TransferMode.MOVE);
				event.consume();
			}
		});

		setOnDragDropped(event -> {
			if (isDraggingTab(event.getDragboard())) {
				final Tab tab = DraggableTab.draggingTab.get();
				TabPane oldTabPane = tab.getTabPane();
				oldTabPane.getTabs().remove(tab);

				DraggableTabLayoutExtender sourceDraggableTabLayoutExtender = new DraggableTabLayoutExtender(new DraggableTabPane(tab));

				addComponent(draggableTabLayoutExtender.getParent(), sourceDraggableTabLayoutExtender);

				DraggableTabUtils.cleanup(oldTabPane);

				DraggableTab.draggingTab.set(null);
				event.setDropCompleted(true);
				event.consume();
			}
		});
	}

	protected void addComponent(Parent parent, Node component) {
		if (parent.getParent() instanceof SplitPane) {
			SplitPane splitPane = (SplitPane) parent.getParent();

			int index = splitPane.getItems().indexOf(draggableTabLayoutExtender);

			if (splitPane.getOrientation() == Orientation.VERTICAL) {
				switch (direction) {
					case UP:
						insertInto(splitPane, index, component);
						break;
					case DOWN:
						insertInto(splitPane, index + 1, component);
						break;
					case LEFT:
						splitInto(splitPane, index, component, draggableTabLayoutExtender);
						break;
					case RIGHT:
						splitInto(splitPane, index, draggableTabLayoutExtender, component);
						break;
				}
			}

			if (splitPane.getOrientation() == Orientation.HORIZONTAL) {
				switch (direction) {
					case UP:
						splitInto(splitPane, index, component, draggableTabLayoutExtender);
						break;
					case DOWN:
						splitInto(splitPane, index, draggableTabLayoutExtender, component);
						break;
					case LEFT:
						insertInto(splitPane, index, component);
						break;
					case RIGHT:
						insertInto(splitPane, index + 1, component);
						break;
				}
			}
		}
	}

	protected void splitInto(SplitPane parent, int index, Node firstComponent, Node secondComponent) {
		parent.getItems().remove(draggableTabLayoutExtender);

		SplitPane newSplitPane = new SplitPane();
		// change orientation for new SplitPane
		newSplitPane.setOrientation(parent.getOrientation() == Orientation.HORIZONTAL ? Orientation.VERTICAL : Orientation.HORIZONTAL);

		newSplitPane.getItems().addAll(firstComponent, secondComponent);

		insertInto(parent, index, new DraggableTabLayoutExtender(newSplitPane));
	}

	protected void insertInto(SplitPane parent, int index, Node component) {
		parent.getItems().add(index, component);
		rearrangeDividers(parent);
	}
}
