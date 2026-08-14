package com.shadowvault.ui.screen;

import javafx.scene.Node;

public interface Screen {
    Node getRoot();
    void onShow();
    void onHide();
    String getTitle();
}