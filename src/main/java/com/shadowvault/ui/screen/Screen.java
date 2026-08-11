package com.shadowvault.ui.screen;

import javafx.scene.Node;

/**
 * Interface for all screens in ShadowVault.
 * Each screen should implement this interface to be managed by NavigationManager.
 */
public interface Screen {
    
    /**
     * Get the root UI node for this screen.
     */
    Node getRoot();
    
    /**
     * Called when the screen is shown.
     */
    void onShow();
    
    /**
     * Called when the screen is hidden.
     */
    void onHide();
    
    /**
     * Get a human-readable name for this screen.
     */
    String getTitle();
}
