package de.endrullis.draggabletabs;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.TransferMode;

import static de.endrullis.draggabletabs.DraggableTab.isDraggingTab;

/**
 * TabPane for DraggableTabs.
 *
 * @author Stefan Endrullis (stefan@endrullis.de)
 */
@SuppressWarnings("WeakerAccess")
public class DraggableTabPane extends TabPane {

	public DraggableTabPane() {
		registerListeners();
	}

	public DraggableTabPane(Tab... tabs) {
		super(tabs);

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

				if (!isParentOf(this, tab.getContent())) {
					TabPane oldTabPane = tab.getTabPane();
					oldTabPane.getTabs().remove(tab);

					getTabs().add(tab);
					getSelectionModel().select(tab);

					DraggableTabUtils.cleanup(oldTabPane);

					DraggableTab.draggingTab.set(null);
				}
				event.setDropCompleted(true);
				event.consume();
			}
		});
	}

	/**
	 * Returns true if {@code child} node is a direct or indirect parent of {@code parent}.
	 *
	 * @param child  child node
	 * @param parent parent node
	 * @return true if {@code child} node is a direct or indirect parent of {@code parent}
	 */
	public boolean isParentOf(Node child, Node parent) {
		Node node = child;

		while (node.getParent() != null) {
			node = node.getParent();

			if (parent == node) {
				return true;
			}
		}

		return false;
	}

}
