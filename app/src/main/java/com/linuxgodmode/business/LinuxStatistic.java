package com.linuxgodmode.business;

import android.content.Context;

import com.google.common.base.Preconditions;

/**
 * Created by sandro on 15.04.17 for project androidapp.
 */

public class LinuxStatistic {
    private final int icon, name;
    private final String unit, emptyValue;
    private String value;

    public LinuxStatistic(int icon, int name, String emptyValue, String unit) {
        Preconditions.checkNotNull(emptyValue, "The emptyValue cannot be null");
        Preconditions.checkNotNull(unit, "The unit cannot be null");
        this.unit = unit;
        this.icon = icon;
        this.name = name;
        this.value = emptyValue;
        this.emptyValue = emptyValue;
    }

    public int getIcon() {
        return icon;
    }

    public String getValue() {
        return value;
    }

    public String getEmptyValue() {
        return emptyValue;
    }

    public String getName(Context context) {
        Preconditions.checkNotNull(context, "The context cannot be null");
        return context.getString(this.name);
    }

    public String getUnit() {
        return unit;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
