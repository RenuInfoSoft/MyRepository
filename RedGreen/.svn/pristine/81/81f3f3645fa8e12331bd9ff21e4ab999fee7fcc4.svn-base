package com.unfoldlabs.redgreen.model;

import android.content.Context;
import android.content.pm.PackageManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ComparatorMethod {
	private static PackageManager packageManager;

	/**
	 * comparing clean memory list for ascending order
	 */
	public static Comparator<ListItem> AsscComparatorCleanMemory = new Comparator<ListItem>() {

		@Override
		public int compare(ListItem lhs, ListItem rhs) {
			return lhs.getApptitle().compareToIgnoreCase(rhs.getApptitle());
		}
	};

	/**
	 * comparing clean memory list for descending order
	 */
	public static Comparator<ListItem> DeseComparatorCleanMemory = new Comparator<ListItem>() {

		@Override
		public int compare(ListItem lhs, ListItem rhs) {
			return rhs.getApptitle().compareToIgnoreCase(lhs.getApptitle());
		}
	};
	/**
	 * comparing Browser history list for ascending order
	 */
	public static Comparator<ListItem> AsscComparatorBrowseHistory = new Comparator<ListItem>() {

		@Override
		public int compare(ListItem lhs, ListItem rhs) {
			return lhs.getHistoryTitle().compareToIgnoreCase(
					rhs.getHistoryTitle());
		}
	};
	/**
	 * comparing Browser history list for descending order
	 */
	public static Comparator<ListItem> DescComparatorBrowseHistory = new Comparator<ListItem>() {

		@Override
		public int compare(ListItem lhs, ListItem rhs) {
			return rhs.getHistoryTitle().compareToIgnoreCase(
					lhs.getHistoryTitle());
		}
	};
	/**
	 * comparing memory boost list for ascending order
	 */
	public static Comparator<ListItem> AsscComparatorMemoryBoost = new Comparator<ListItem>() {

		@Override
		public int compare(ListItem lhs, ListItem rhs) {
			return lhs.getInfo().compareToIgnoreCase(rhs.getInfo());
		}
	};
	/**
	 * comparing memory boost list for descending order
	 */
	public static Comparator<ListItem> DescComparatorMemoryBoost = new Comparator<ListItem>() {

		@Override
		public int compare(ListItem lhs, ListItem rhs) {
			return rhs.getInfo().compareToIgnoreCase(lhs.getInfo());
		}
	};
	/**
	 * comparing memory list for ascending order
	 */
	public static Comparator<ListItem> AsscComparatorMemory = new Comparator<ListItem>() {

		@Override
		public int compare(ListItem lhs, ListItem rhs) {
			return (int) (lhs.getId() - rhs.getId());
		}
	};
	/**
	 * comparing memory list for descending order
	 */
	public static Comparator<ListItem> DescComparatorMemory = new Comparator<ListItem>() {

		@Override
		public int compare(ListItem lhs, ListItem rhs) {
			return (int) (rhs.getId() - lhs.getId());
		}
	};

	/**
	 * comparing save battery list for ascending order
	 */
	public static Comparator<SaveBatteryModel> saveBatteryNewAsceComparator = new Comparator<SaveBatteryModel>() {
		@Override
		public int compare(SaveBatteryModel lhs,
						   SaveBatteryModel rhs) {

			return lhs.getApptitle().compareToIgnoreCase(rhs.getApptitle());
		}

	};

	/**
	 * comparing save battery list for descending order
	 */
	public static Comparator<SaveBatteryModel> saveBatteryNewDescComparator = new Comparator<SaveBatteryModel>() {
		@Override
		public int compare(SaveBatteryModel lhs,
						   SaveBatteryModel rhs) {

			return rhs.getApptitle().compareToIgnoreCase(lhs.getApptitle());
		}
	};

	/**
	 * comparing save battery list for ascending order
	 */
	public static Comparator<SaveBatteryModel> saveBatteryNewAsceCpuComparator = new Comparator<SaveBatteryModel>() {
		@Override
		public int compare(SaveBatteryModel lhs,
						   SaveBatteryModel rhs) {

			return lhs.getInfo().compareToIgnoreCase(rhs.getInfo());
		}

	};

	/**
	 * comparing save battery list for descending order
	 */
	public static Comparator<SaveBatteryModel> saveBatteryNewDescCpuComparator = new Comparator<SaveBatteryModel>() {
		@Override
		public int compare(SaveBatteryModel lhs,
						   SaveBatteryModel rhs) {

			return rhs.getInfo().compareToIgnoreCase(lhs.getInfo());
		}
	};

	/**
	 * comparing Cache applications names list for Ascending order
	 */
	public static Comparator<AppsListItem> cacheNameAsscComparator = new Comparator<AppsListItem>(){

		@Override
		public int compare(AppsListItem lhs, AppsListItem rhs) {

			return lhs.getApplicationName().compareToIgnoreCase(rhs.getApplicationName());
		}

	};

	/**
	 * comparing Cache applications names list for descending order
	 */
	public static Comparator<AppsListItem> cacheNameDesceComparator = new Comparator<AppsListItem>(){

		@Override
		public int compare(AppsListItem lhs, AppsListItem rhs) {

			return rhs.getApplicationName().compareToIgnoreCase(lhs.getApplicationName());
		}

	};
	/**
	 * comparing Cache applications size list for Ascending order
	 */
	public static Comparator<AppsListItem> cacheSizeDesceComparator = new Comparator<AppsListItem>(){

		@Override
		public int compare(AppsListItem lhs, AppsListItem rhs) {

			return (int) (rhs.getCacheSize() - lhs.getCacheSize());
		}

	};


	/**
	 * comparing Cache applications size list for Ascending order
	 */
	public static Comparator<AppsListItem> cacheSizeAsseComparator = new Comparator<AppsListItem>(){

		@Override
		public int compare(AppsListItem lhs, AppsListItem rhs) {

			return (int) (lhs.getCacheSize() - rhs.getCacheSize());
		}

	};
	/**
	 * Apps list ascending order comparing the list according to name.
	 */
	public static void packageAsscMethod(Context context, List<CleanAppDB> list){
		packageManager = context.getPackageManager();
		Collections.sort(list, packageAsscComparator);
	}
	public static Comparator<CleanAppDB> packageAsscComparator = new Comparator<CleanAppDB>() {
		@Override
		public int compare(CleanAppDB lhs, CleanAppDB rhs) {

			String appName1 = "";
			String appName2 = "";
			try {
				appName1 = (String) packageManager
						.getApplicationLabel(packageManager.getApplicationInfo(
								lhs.getPackageName(),
								PackageManager.GET_META_DATA));
				appName2 = (String) packageManager
						.getApplicationLabel(packageManager.getApplicationInfo(
								rhs.getPackageName(),
								PackageManager.GET_META_DATA));
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}
			return appName1.compareToIgnoreCase(appName2);
		}
	};

	/**
	 * Apps list descending order comparing the list according to name.
	 */
	public static void packageDescMethod(Context context, List<CleanAppDB> list){
		packageManager = context.getPackageManager();
		Collections.sort(list, packageDesComparator);
	}
	public static Comparator<CleanAppDB> packageDesComparator = new Comparator<CleanAppDB>() {

		@Override
		public int compare(CleanAppDB lhs, CleanAppDB rhs) {
			String appName1 = "";
			String appName2 = "";

			try {
				appName1 = (String) packageManager
						.getApplicationLabel(packageManager.getApplicationInfo(
								lhs.getPackageName(),
								PackageManager.GET_META_DATA));
				appName2 = (String) packageManager
						.getApplicationLabel(packageManager.getApplicationInfo(
								rhs.getPackageName(),
								PackageManager.GET_META_DATA));
			} catch (PackageManager.NameNotFoundException e) {

				e.printStackTrace();
			}
			return appName2.compareToIgnoreCase(appName1);
		}
	};

	/**
	 * Apps list ascending order comparing the list according to last used date.
	 */
	public static Comparator<CleanAppDB> dateDesComparator = new Comparator<CleanAppDB>() {

		@Override
		public int compare(CleanAppDB lhs, CleanAppDB rhs) {
			String date1 = lhs.getDateTime();
			String date2 = rhs.getDateTime();
			return date1.compareToIgnoreCase(date2);
		}
	};

	/**
	 * Apps list descending order comparing the list according to last used
	 * date.
	 */
	public static Comparator<CleanAppDB> dateAsscComparator = new Comparator<CleanAppDB>() {

		@Override
		public int compare(CleanAppDB lhs, CleanAppDB rhs) {
			String date1 = lhs.getDateTime();
			String date2 = rhs.getDateTime();
			return date2.compareToIgnoreCase(date1);
		}
	};
}
