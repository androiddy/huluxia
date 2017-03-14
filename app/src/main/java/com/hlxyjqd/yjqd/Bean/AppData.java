package com.hlxyjqd.yjqd.Bean;

import android.graphics.drawable.Drawable;

/**
 * @author Lody
 */

public interface AppData {

    boolean isLoading();

    boolean isFirstOpen();

    Drawable getIcon();

    String getName();

    boolean canReorder();

    boolean canLaunch();

    boolean canDelete();

    boolean canCreateShortcut();
}
