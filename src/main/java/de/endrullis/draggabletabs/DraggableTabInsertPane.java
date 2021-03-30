package de.endrullis.draggabletabs;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.skin.TextInputControlSkin.Direction;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

import static de.endrullis.draggabletabs.DraggableTab.isDraggingTab;
import static de.endrullis.draggabletabs.DraggableTabUtils.rearrangeDividers;

/**
 * Pane used as placeholder for dragging tabs onto.
 *
 * @author Stefan Endrullis (endrullis@iat.uni-leipzig.de)
 */
@SuppressWarnings("WeakerAccess")
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
				final DraggableTab tab        = DraggableTab.draggingTab.get();
				TabPane            oldTabPane = tab.getTabPane();
				oldTabPane.getTabs().remove(tab);

				DraggableTabLayoutExtender sourceDraggableTabLayoutExtender = DraggableTabFactory.getFactory().wrapTab(tab);

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

			Orientation insertOrientation = direction == Direction.UP || direction == Direction.DOWN ? Orientation.VERTICAL : Orientation.HORIZONTAL;

			if (splitPane.getOrientation() != insertOrientation) {

				Node center = draggableTabLayoutExtender.getCenter();

				if (center instanceof SplitPane) {
					SplitPane pane = (SplitPane) center;

					if (pane.getOrientation() == insertOrientation) {
						splitPane = pane;

						if (direction == Direction.UP || direction == Direction.LEFT) {
							index = 0;
						} else {
							index = pane.getItems().size();
						}
					}
				}
			}

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

		insertInto(parent, index, DraggableTabFactory.getFactory().createLayoutExtender(newSplitPane));
	}

	protected void insertInto(SplitPane parent, int index, Node component) {
		parent.getItems().add(index, component);
		rearrangeDividers(parent);
	}
}
