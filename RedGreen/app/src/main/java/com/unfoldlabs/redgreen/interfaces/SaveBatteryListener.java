package com.unfoldlabs.redgreen.interfaces;

import android.util.SparseBooleanArray;

public interface SaveBatteryListener {
	void expandGroupEvent(int groupPosition, boolean isExpanded);

	void GroupItemSelection(SparseBooleanArray systemAppsArray,
							SparseBooleanArray bagroundAppsArray, SparseBooleanArray memoryBoostArray);

	void getBackRoundApps();
}
