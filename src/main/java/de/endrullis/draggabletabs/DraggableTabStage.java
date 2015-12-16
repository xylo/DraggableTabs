package de.endrullis.draggabletabs;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

import java.awt.*;

/**
 * Stage containing an extensible SplitPane with DraggableTabPane.
 *
 * @author Stefan Endrullis (stefan@endrullis.de)
 */
public class DraggableTabStage extends Stage {

	/**
	 * Creates a new stage with a split pane, a tab pane and the given tab.
	 *
	 * @param tab tab to be added to the new stage
	 */
	public DraggableTabStage(DraggableTab tab) {
		setTitle(tab.getTitle());

		SplitPane splitPane = new SplitPane() {{
			getItems().add(new DraggableTabLayoutExtender(new DraggableTabPane(tab)));

			// close window automatically when last tab gets removed
			getItems().addListener((InvalidationListener) observable -> {
				Platform.runLater(() -> {
					if (getItems().isEmpty()) {
						close();
					}
				});
			});
		}};

		Scene scene = new Scene(splitPane);

		setScene(scene);

		show();

		// place window centered under the cursor
		Point p = MouseInfo.getPointerInfo().getLocation();
		setX(p.x - scene.getWidth() / 2);
		setY(p.y);
	}

}
