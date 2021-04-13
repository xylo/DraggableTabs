package de.endrullis.draggabletabs;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.awt.*;

/**
 * Utilities for the DraggableTab classes.
 *
 * @author Stefan Endrullis (stefan@endrullis.de)
 */
@SuppressWarnings("WeakerAccess")
public class DraggableTabUtils {

	/**
	 * Cleans up the given tab pane.
	 *
	 * @param tabPane tab pane
	 */
	public static void cleanup(TabPane tabPane) {
		Platform.runLater(() -> {
			if (tabPane.getTabs().isEmpty()) {
				// remove from parent
				if (tabPane.getParent() != null && tabPane.getParent() instanceof DraggableTabLayoutExtender) {
					DraggableTabLayoutExtender extender = (DraggableTabLayoutExtender) tabPane.getParent();
					extender.setCenter(null);
					cleanup(extender);
				}
			}
		});
	}

	/**
	 * Cleans up the given extender.
	 *
	 * @param extender extender
	 */
	public static void cleanup(DraggableTabLayoutExtender extender) {
		Platform.runLater(() -> {
			if (extender.getCenter() == null && extender.getParent() != null) {
				if (extender.getParent().getParent() == null) {
					cleanup(extender);
				} else {
					if (extender.getParent() != null && extender.getParent().getParent() != null && extender.getParent().getParent() instanceof SplitPane) {
						SplitPane splitPane = (SplitPane) extender.getParent().getParent();

						splitPane.getItems().remove(extender);
						rearrangeDividers(splitPane);

						cleanup(splitPane);
					}
				}
			}
		});
	}

	/**
	 * Cleans up the given split pane.
	 *
	 * @param splitPane split pane
	 */
	public static void cleanup(SplitPane splitPane) {
		Platform.runLater(() -> {
			Parent parent = splitPane.getParent();
			if (splitPane.getItems().isEmpty()) {
				if (parent != null && parent instanceof DraggableTabLayoutExtender) {
					DraggableTabLayoutExtender extender = (DraggableTabLayoutExtender) parent;
					extender.setCenter(null);
					cleanup(extender);
				}
			}
			else if (splitPane.getItems().size() == 1) {
				Node node = splitPane.getItems().get(0);
				if (parent != null && parent.getParent() != null && parent.getParent().getParent() != null) {
					if (parent.getParent().getParent() instanceof SplitPane) {
						SplitPane parentSplitPane = (SplitPane) parent.getParent().getParent();
						splitPane.getItems().remove(node);
						int i = parentSplitPane.getItems().indexOf(parent);
						parentSplitPane.getItems().add(i, node);
						cleanup(splitPane);
					}
				}
			}
		});
	}

	/**
	 * Rearranges the delimiters of given split pane.
	 *
	 * @param splitPane split pane
	 */
	public static void rearrangeDividers(SplitPane splitPane) {
		if (!splitPane.getItems().isEmpty()) {
			double[] dividerPositions = splitPane.getDividerPositions();
			for (int i = 0; i < dividerPositions.length; i++) {
				dividerPositions[i] = ((double) i + 1) / (dividerPositions.length + 1);
			}
			splitPane.setDividerPositions(dividerPositions);
		}
	}

	public static void centerStageUnderMouseCursor(Stage stage) {
		// place window centered under the cursor
		Point p = MouseInfo.getPointerInfo().getLocation();
		stage.setX(p.x / stage.getRenderScaleX() - stage.getScene().getWidth() / 2);
		stage.setY(p.y / stage.getRenderScaleY());
	}

}
