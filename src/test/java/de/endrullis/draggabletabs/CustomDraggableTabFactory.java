package de.endrullis.draggabletabs;

import com.sun.javafx.scene.traversal.Direction;
import javafx.scene.control.Label;

/**
 * @author Stefan Endrullis &lt;stefan@endrullis.de&gt;
 */
@SuppressWarnings("WeakerAccess")
public class CustomDraggableTabFactory extends DraggableTabFactory {

	@Override
	public DraggableTabStage createNewStage(DraggableTab tab) {
		return new DraggableTabStage(tab) {{
			getScene().getStylesheets().add(CustomDraggableTabFactory.class.getResource("styles.css").toString());
		}};
	}

	@Override
	public DraggableTabInsertPane createInsertPane(DraggableTabLayoutExtender draggableTabLayoutExtender, Direction direction) {
		return new DraggableTabInsertPane(draggableTabLayoutExtender, direction) {{
			getChildren().add(new Label("⬚"));
		}};
	}

}
