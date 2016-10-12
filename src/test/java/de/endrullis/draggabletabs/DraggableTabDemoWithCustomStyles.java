package de.endrullis.draggabletabs;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Random;

/**
 * Demo for DraggableTab.
 *
 * @author Stefan Endrullis (endrullis@iat.uni-leipzig.de)
 */
public class DraggableTabDemoWithCustomStyles extends Application {

	private static final int TABS_PER_PANE = 5;

	public static void main(String[] args) {
		DraggableTabFactory.setFactory(new CustomDraggableTabFactory());

		launch(args);
	}

	@Override
	public void start(Stage stage) {
		stage.setTitle("DraggableTabDemo");

		DraggableTabFactory tabFactory = DraggableTabFactory.getFactory();

		TabPane tabPane1 = tabFactory.createTabPane();
		TabPane tabPane2 = tabFactory.createTabPane();

		SplitPane splitPane = new SplitPane();
		splitPane.setOrientation(Orientation.VERTICAL);

		splitPane.getItems().addAll(
				tabFactory.createLayoutExtender(tabPane1),
				tabFactory.createLayoutExtender(tabPane2)
		);

		final Random rand = new Random();

		for (TabPane tabPane : Arrays.asList(tabPane1, tabPane2)) {
			for (int i = 1; i <= TABS_PER_PANE; i++) {
				final Tab tab = new DraggableTab("Tab " + i);

				tab.setContent(new StackPane() {{
					int red = rand.nextInt(256);
					int green = rand.nextInt(256);
					int blue = rand.nextInt(256);

					setStyle(String.format("-fx-background-color: rgb(%d, %d, %d);", red, green, blue));

					setPrefWidth(400);
					setPrefHeight(300);
				}});

				tabPane.getTabs().add(tab);
			}
		}

		Scene scene = new Scene(splitPane, 600, 600);
		scene.getStylesheets().add(CustomDraggableTabFactory.class.getResource("styles.css").toString());
		stage.setScene(scene);
		stage.show();
	}

}
