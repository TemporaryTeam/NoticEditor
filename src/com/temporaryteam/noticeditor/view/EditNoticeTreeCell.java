package com.temporaryteam.noticeditor.view;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import com.temporaryteam.noticeditor.model.NoticeItem;

public class EditNoticeTreeCell extends TreeCell<String> {

	private TextField noticeName;
	private NoticeController controller;

	public void handleContextMenu(ActionEvent e) {
		MenuItem source = (MenuItem) e.getSource();
		NoticeTreeItem selected = controller.getCurrentTreeItem();

		ArrayList<NoticeItem> childItems;
		ObservableList<NoticeTreeItem> childTreeItems;
		if (selected != null) {
			if (!selected.getNotice().isBranch() || source == controller.getDeleteItem()) {
				childTreeItems = selected.getParent().getChildren();
				childItems = ((NoticeTreeItem) (selected.getParent())).getNotice().childrens();
			} else {
				childTreeItems = selected.getChildren();
				childItems = selected.getNotice().childrens();
			}
		} else {
			childTreeItems = ((NoticeTreeItem) (getTreeView().getRoot())).getChildren();
			childItems = ((NoticeTreeItem) (getTreeView().getRoot())).getNotice().childrens();
		}
		if (source == controller.getAddBranchItem()) {
			ArrayList<NoticeItem> list = new ArrayList<>();
			NoticeItem toAdd = new NoticeItem("New branch", list);
			NoticeTreeItem treeItem = new NoticeTreeItem(toAdd);
			childTreeItems.add(treeItem);
			childItems.add(toAdd);
		} else if (source == controller.getAddNoticeItem()) {
			NoticeItem toAdd = new NoticeItem("New notice", "");
			NoticeTreeItem newNotice = new NoticeTreeItem(toAdd);
			childTreeItems.add(newNotice);
			childItems.add(toAdd);
		} else if (source == controller.getDeleteItem()) {
			NoticeItem toDel = selected.getNotice();
			childTreeItems.remove(selected);
			childItems.remove(toDel);
		}
	}

	@Override
	public void startEdit() {
		super.startEdit();
		if (noticeName == null) {
			createTextField();
		}
		setText(null);
		setGraphic(noticeName);
		noticeName.selectAll();
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		setText((String) getItem());
		setGraphic(getTreeItem().getGraphic());
	}

	@Override
	public void commitEdit(String str) {
		super.commitEdit(str);
		getNoticeTreeItem().getNotice().setName(str);
	}

	@Override
	public void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			if (isEditing()) {
				if (noticeName != null) {
					noticeName.setText(getString());
				}
				setText(null);
				setGraphic(noticeName);
			} else {
				setText(getString());
				setGraphic(getTreeItem().getGraphic());
			}
		}
	}

	private NoticeTreeItem getNoticeTreeItem() {
		return (NoticeTreeItem<String>) getTreeItem();
	}

	private void createTextField() {
		noticeName = new TextField(getString());
		noticeName.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				if (t.getCode() == KeyCode.ENTER) {
					commitEdit(noticeName.getText());
				} else if (t.getCode() == KeyCode.ESCAPE) {
					cancelEdit();
				}
			}
		});
	}

	private String getString() {
		return ((getItem() == null) ? "" : getItem());
	}

	public NoticeController getController() {
		return controller;
	}

	public void setController(NoticeController controller) {
		this.controller = controller;
	}

}
