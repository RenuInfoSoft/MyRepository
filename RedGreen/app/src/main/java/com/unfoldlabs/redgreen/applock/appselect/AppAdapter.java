package com.unfoldlabs.redgreen.applock.appselect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.global.ApplicationData;
import com.unfoldlabs.redgreen.interfaces.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppAdapter extends BaseAdapter {
	private final LayoutInflater mInflater;
	private final PackageManager mPm;
	private final Context mContext;
	private  Set<AppListElement> mInitialItems;
	private List<AppListElement> mItems;
	private Set<String> set  = null;
	private SharedPreferences sharedPreferencesAppLock;
	private Editor editor;

	public AppAdapter(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPm = context.getPackageManager();
		// Empty
		mInitialItems = new HashSet<>();
		mItems = new ArrayList<>();
		sharedPreferencesAppLock = context.getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
		editor = sharedPreferencesAppLock.edit();

		set = sharedPreferencesAppLock.getStringSet("key", null);
		new LoaderClass().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	/**
	 * Below class is to load the apps which are installed in device in
	 * background, because of this async task load on main thread will be
	 * reduced.
	 */
	private class LoaderClass extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			loadAppsIntoList();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			sort();
			if (mListener != null) {
				mLoadComplete = true;
				mListener.onLoadComplete();
			}
		}

	}

	private boolean mLoadComplete;

	public boolean isLoadComplete() {
		return mLoadComplete;
	}

	private OnEventListener mListener;

	/**
	 *
	 * @param listener
	 */
	public void setOnEventListener(OnEventListener listener) {
		mListener = listener;
	}

	public interface OnEventListener {
		void onLoadComplete();

		void onDirtyStateChanged(boolean dirty);
	}

	public boolean areAllAppsLocked() {
		for (AppListElement app : mItems)
			if (app.isApp() && !app.locked)
				return false;
		return true;
	}

	/**
	 * Creates a completely new list with the apps. Should only be called once.
	 * Does not sort
	 *
	 */
	void loadAppsIntoList() {

		// Get all tracked apps from preferences
		addImportantAndSystemApps(mInitialItems);

		// other apps
		final Intent i = new Intent(Intent.ACTION_MAIN);
		i.addCategory(Intent.CATEGORY_LAUNCHER);
		final List<ResolveInfo> ris = mPm.queryIntentActivities(i, 0);

		for (ResolveInfo ri : ris) {
			if (!mContext.getPackageName().equals(ri.activityInfo.packageName)) {
				final AppListElement ah = new AppListElement(ri.loadLabel(mPm)
						.toString(), ri.activityInfo,
						AppListElement.PRIORITY_NORMAL_APPS);
				mInitialItems.add(ah);
			}
		}

		for (AppListElement ah : mInitialItems) {
			try{
				if(set.size() != 0){
					ah.locked = set.contains(ah.packageName);
				}else{
					ah.locked = set.contains(ah.packageName);
				}
			}catch(NullPointerException e){

			}
		}

		mItems = new ArrayList<>(mInitialItems);
	}

	/**
	 * Getting the apps in list of three groups "important apps", "system apps"
	 * and "apps"
	 *
	 * @param apps
	 */
	private void addImportantAndSystemApps(Collection<AppListElement> apps) {
		final String installer = "com.android.packageinstaller";

		final List<String> important = Arrays.asList("com.android.vending", "com.android.settings");

		final List<String> system = Arrays
				.asList("com.android.dialer");

		final PackageManager pm = mContext.getPackageManager();
		List<ApplicationInfo> list = pm.getInstalledApplications(0);
		boolean haveSystem = false;
		boolean haveImportant = false;
		for (ApplicationInfo pi : list) {
			 if (installer.equals(pi.packageName)) {
				apps.add(new AppListElement(mContext
						.getString(R.string.applist_app_pkginstaller), pi,
						AppListElement.PRIORITY_IMPORTANT_APPS));
				haveImportant = true;
			}
			if (important.contains(pi.packageName)) {
				apps.add(new AppListElement(pi.loadLabel(pm).toString(), pi,
						AppListElement.PRIORITY_IMPORTANT_APPS));
				haveImportant = true;
			}
			if (system.contains(pi.packageName)) {
				apps.add(new AppListElement(pi.loadLabel(pm).toString(), pi,
						AppListElement.PRIORITY_SYSTEM_APPS));
				haveSystem = true;
			}

			apps.add(new AppListElement(mContext
					.getString(R.string.applist_tit_apps),
					AppListElement.PRIORITY_NORMAL_CATEGORY));
			if (haveImportant) {
				apps.add(new AppListElement(mContext
						.getString(R.string.applist_tit_important),
						AppListElement.PRIORITY_IMPORTANT_CATEGORY));
			}
			if (haveSystem) {
				apps.add(new AppListElement(mContext
						.getString(R.string.applist_tit_system),
						AppListElement.PRIORITY_SYSTEM_CATEGORY));
			}
		}
	}

	/**
	 * Sort the apps and notify the ListView that the items have changed. Should
	 * be called from the working thread
	 */
	public void sort() {
		Collections.sort(mItems);
		notifyDataSetChanged();
		notifyDirtyStateChanged(false);
	}

	/**
	 * count
	 */
	@Override
	public int getCount() {
		return mItems.size();
	}

	/**
	 * items
	 */
	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	public List<AppListElement> getAllItems() {
		return mItems;
	}

	/**
	 * Item id
	 */
	@Override
	public long getItemId(int position) {
		return 0;
	}

	/**
	 * view type count
	 */
	@Override
	public int getViewTypeCount() {
		// Number of different views we have
		return 2;
	}

	/**
	 * itemView
	 */
	@Override
	public int getItemViewType(int position) {
		return mItems.get(position).isApp() ? 0 : 1;
	}

	/**
	 * shows the parent and child separated apps list in activity
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (mItems.get(position).isApp()) {
			return createAppViewFromResource(position, convertView, parent);
		} else {
			return createSeparatorViewFromResource(position, convertView,
					parent);
		}
	}

	/**
	 * In app list separates the parent and child lists
	 *
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	private View createSeparatorViewFromResource(int position,
												 View convertView, ViewGroup parent) {
		AppListElement ah = mItems.get(position);

		View view = convertView;
		if (view == null)
			view = mInflater.inflate(R.layout.applist_item_category, parent,
					false);
		TextView tv = (TextView) view.findViewById(R.id.listName);
		tv.setText(ah.title);

		return view;
	}

	/**
	 * Shows separated apps list with respective images and switch button in
	 * activity
	 *
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	private View createAppViewFromResource(int position, View convertView,
										   ViewGroup parent) {

		AppListElement ah = mItems.get(position);
		View view = convertView;
		if (view == null)
			view = mInflater.inflate(R.layout.applist_item_app, parent, false);
		// changes with every click

		final Switch lock = (Switch) view.findViewById(R.id.applist_item_image);
		lock.setChecked(ah.locked ? true : false);
		final TextView name = (TextView) view.findViewById(R.id.listName);
		name.setText(ah.getLabel(mPm));

		final ImageView icon = (ImageView) view.findViewById(R.id.listIcon);
		final Drawable bg = ah.getIcon(mPm);
		if (bg == null)
			icon.setVisibility(View.GONE);
		else
			setBackgroundCompat(icon, bg);

		return view;
	}

	// Important: Undo action.
	private ArrayList<AppListElement> mUndoItems;

	public void prepareUndo() {
		mUndoItems = new ArrayList<>(mItems);
	}

	public void undo() {
		mItems = new ArrayList<>(mUndoItems);
		notifyDataSetChanged();
	}

	/**
	 * Setting lock to all apps
	 *
	 * @param lock
	 * @param item
	 */
	public void setAllLocked(boolean lock, AppListElement item) {
		ArrayList<String> apps = new ArrayList<>();
		for (AppListElement app : mItems) {

			if (app.isApp()) {
				app.locked = lock;
				apps.add(app.packageName);
			}
		}
		setLocked(lock, apps.toArray(new String[apps.size()]));
		sort();
		lockPreferenceSave2(apps.toArray(new String[apps.size()]), lock, item);
	}

	private void lockPreferenceSave2(String[] packageNames, boolean lock, AppListElement item) {
		/** AWS setNoOfAppsLocked , setAppsLocked **/
		for (String packageName : packageNames) {
			if (set == null)
				set = new HashSet<String>();
			//Set the values
			if(lock == true){
				editor.remove("key");
				editor.commit();

				set.add(packageName);
				editor.putStringSet("key", set);
				editor.commit();
			}else{
				set.remove(packageName);
				editor.remove("key");
				editor.commit();
				editor.putStringSet("key", set);
				editor.commit();
			}
			ApplicationData.getInstance().setAppsLocked("" + packageName);
		}
		ApplicationData.getInstance().setNoOfAppsLocked("" + set.size());

	}

	private boolean mDirtyState;

	private void notifyDirtyStateChanged(boolean dirty) {
		if (mDirtyState != dirty) {
			mDirtyState = dirty;
			if (mListener != null) {
				mListener.onDirtyStateChanged(dirty);
			}
		}
	}

	public void toggle(AppListElement item) {
		if (item.isApp()) {
			item.locked = !item.locked;
			setLocked(item.locked, item.packageName);
			lockPreferenceSave(item, item.locked);
		}
		List<AppListElement> list = new ArrayList<>(mItems);
		Collections.sort(list);
		boolean dirty = !list.equals(mItems);

		notifyDirtyStateChanged(dirty);
	}

	private void lockPreferenceSave(AppListElement item, boolean locked) {
		String packageName = item.packageName;
		//Retrieve the values

		if (set == null)
			set = new HashSet<String>();
		//Set the values
		if(locked == true){
			editor.remove("key");
			editor.commit();
			set.add(packageName);

			editor.putStringSet("key", set);
			editor.commit();
		}else{
			set.remove(packageName);
			editor.remove("key");
			editor.commit();
			editor.putStringSet("key", set);
			editor.commit();
		}
		/** AWS setNoOfAppsLocked , setAppsLocked **/
		ApplicationData.getInstance().setNoOfAppsLocked("" + set.size());
		ApplicationData.getInstance().setAppsLocked("" + packageName);
	}


	/**
	 * apply lock to app
	 *
	 * @param lock
	 * @param packageNames
	 */
	void setLocked(boolean lock, String... packageNames) {
		for (String packageName : packageNames) {
			if (lock) {
				editor.putStringSet("key", set);
				editor.commit();
			} else {
				editor.remove(packageName);
				editor.commit();
			}
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void setBackgroundCompat(View v, Drawable bg) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
			v.setBackgroundDrawable(bg);
		else
			v.setBackground(bg);
	}

}