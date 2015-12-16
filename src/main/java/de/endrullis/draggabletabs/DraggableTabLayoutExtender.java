package de.endrullis.draggabletabs;

import com.sun.javafx.scene.traversal.Direction;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import static de.endrullis.draggabletabs.DraggableTab.isDraggingTab;
import static de.endrullis.draggabletabs.DraggableTabUtils.rearrangeDividers;

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
					setTop(new InsertPane(Direction.UP));
					setBottom(new InsertPane(Direction.DOWN));
					setLeft(new InsertPane(Direction.LEFT));
					setRight(new InsertPane(Direction.RIGHT));
				} else {
					setTop(null);
					setBottom(null);
					setLeft(null);
					setRight(null);
				}
			}
		});
	}

	protected class InsertPane extends Pane {
		private final Direction direction;

		public InsertPane(Direction direction) {
			this.direction = direction;
			switch (this.direction) {
				case UP:
				case DOWN:
					setMinHeight(EXTENDER_SIZE);
					setMaxHeight(EXTENDER_SIZE);
					break;
				case LEFT:
				case RIGHT:
					setMinWidth(EXTENDER_SIZE);
					setMaxWidth(EXTENDER_SIZE);
					break;
			}

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

					DraggableTabLayoutExtender draggableTabLayoutExtender = new DraggableTabLayoutExtender(new DraggableTabPane(tab));

					addComponent(DraggableTabLayoutExtender.this.getParent(), draggableTabLayoutExtender);

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

				int index = splitPane.getItems().indexOf(DraggableTabLayoutExtender.this);

				if (splitPane.getOrientation() == Orientation.VERTICAL) {
					switch (direction) {
						case UP:
							insertInto(splitPane, index, component);
							break;
						case DOWN:
							insertInto(splitPane, index + 1, component);
							break;
						case LEFT:
							splitInto(splitPane, index, component, DraggableTabLayoutExtender.this);
							break;
						case RIGHT:
							splitInto(splitPane, index, DraggableTabLayoutExtender.this, component);
							break;
					}
				}

				if (splitPane.getOrientation() == Orientation.HORIZONTAL) {
					switch (direction) {
						case UP:
							splitInto(splitPane, index, component, DraggableTabLayoutExtender.this);
							break;
						case DOWN:
							splitInto(splitPane, index, DraggableTabLayoutExtender.this, component);
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
			parent.getItems().remove(DraggableTabLayoutExtender.this);

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

}
