package com.g10.locomobile.ui.main.contents;

import com.g10.locomobile.BaseActivity;
import com.g10.locomobile.models.IDable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Content extends BaseActivity {

    /**
     * An array of sample items.
     */
    static final List<Object> CONTENT = new ArrayList<Object>();

    /**
     * A map of sample items, by ID.
     */
    static final Map<Integer, Object> ITEM_MAP = new HashMap<Integer, Object>();

    private static int COUNT = CONTENT.size();

    static {
        // Add some sample items.
    }

    private static void addItem(Object o) {
        CONTENT.add(o);
        ITEM_MAP.put(((IDable) o).getID(), o);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
}
