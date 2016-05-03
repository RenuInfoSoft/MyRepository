package com.unfoldlabs.redgreen.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.provider.Browser;
import android.support.v4.text.BidiFormatter;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.adapter.AppsListAdapter;
import com.unfoldlabs.redgreen.adapter.ExpandableListAdapter;
import com.unfoldlabs.redgreen.global.AppData;
import com.unfoldlabs.redgreen.global.ApplicationData;
import com.unfoldlabs.redgreen.global.BrowserAsyncTask;
import com.unfoldlabs.redgreen.global.GlobalAsyncTask;
import com.unfoldlabs.redgreen.global.JunkAsyncTask;
import com.unfoldlabs.redgreen.interfaces.Constants;
import com.unfoldlabs.redgreen.interfaces.JunkListner;
import com.unfoldlabs.redgreen.interfaces.TotalListener;
import com.unfoldlabs.redgreen.model.AppsListItem;
import com.unfoldlabs.redgreen.model.ComparatorMethod;
import com.unfoldlabs.redgreen.model.ListItem;
import com.unfoldlabs.redgreen.runningapps.CommonLibrary;
import com.unfoldlabs.redgreen.runningapps.ProcessDetailInfo;
import com.unfoldlabs.redgreen.utilty.CleanCache;
import com.unfoldlabs.redgreen.utilty.Utility;
import com.unfoldlabs.redgreen.views.DonutProgress;
import com.unfoldlabs.redgreen.views.LinearColorBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CleanMemoryFragment extends Fragment implements TotalListener,
		JunkListner {
	private int sumFinal;
	private List<AppsListItem> mItems = new ArrayList<AppsListItem>();
	private ExpandableListAdapter listAdapter;
	private ExpandableListView expListView;
	private List<String> listDataHeader;
	private HashMap<String, List<ListItem>> listDataChild;
	private List<ListItem> listItem = new ArrayList<ListItem>();
	private List<ListItem> browseHistoryItem = new ArrayList<ListItem>();
	private List<ListItem> ramUsageList = new ArrayList<ListItem>();
	private ContentResolver contentResolver;
	private LinearColorBar mColorBar;
	private LinearColorBar mColorBarOne;
	private LinearColorBar mColorBarTwo;
	private LinearColorBar mColorBarThree;
	private TextView mSystemSizeText, deviceSizeText;
	private TextView mCacheSizeText;
	private String mCacheSizeNew;
	private TextView mFreeSizeText;
	private AppsListAdapter mAppsListAdapter;
	private TextView mEmptyView;
	private SharedPreferences mSharedPreferences;
	private boolean ramList = false ;
	private boolean junkList = false;
	private boolean backgroundListState = false;
	private DonutProgress donutProgressROM, donutProgressRAM;
	private Timer timerDonutROM, timerDonutRAM;
	private String mSortByKey;
	private ActivityManager mActivityManager = null;
	private ArrayList<ProcessDetailInfo> mDetailList;
	private View rootView;
	private Button clean_browse_buttton;
	private int dialogSettingsgallery = -1;
	private boolean isDirectionSelection = true;
	private EasyTracker easyTracker = null;
	private SparseBooleanArray cleanMemoryArray = new SparseBooleanArray();
	private SparseBooleanArray memoryArray = new SparseBooleanArray();
	private SparseBooleanArray historyArray = new SparseBooleanArray();
	private int dialogcleanMemoryArray;
	private int dialoghistoryArray;
	private boolean isDialogShowing = false;
	private ClipboardManager myClipboard;
	private CheckBox checkBoxCache, checkBoxClipData;
	private boolean historyState;
	private long junkSize;
	public static JunkListner listener;
	private SharedPreferences speedBoosterSharedPref;
	private Dialog booster_dialog;
	private ProgressDialog dialogNew;
	private boolean isCleanStorage;
	private Context context;
	private TextView cacheTextViewSize;
	private String newsizeStr, totalMemorySize;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		easyTracker = EasyTracker.getInstance(getActivity());
		setHasOptionsMenu(true);
		setRetainInstance(true);
		listener = this;
		context = getActivity();
		mSortByKey = getString(R.string.sort_by_key);
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mAppsListAdapter = new AppsListAdapter(getActivity());
	}

	/**
	 * view for clean memory activity
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.cleaner_memory_fragment, container, false);

		speedBoosterSharedPref = getActivity().getSharedPreferences("SPEED_BOOSTER_SHARED_PREF", Context.MODE_PRIVATE);

		mActivityManager = ((ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE));
		checkBoxCache = (CheckBox)rootView.findViewById(R.id.clean_cache_group_chk_box);
		cacheTextViewSize = (TextView)rootView.findViewById(R.id.cache_checked_size);
		checkBoxClipData = (CheckBox)rootView.findViewById(R.id.clipboard_chk_box);
		donutProgressRAM = (DonutProgress) rootView.findViewById(R.id.donut_progress_ram);
		donutProgressROM = (DonutProgress) rootView.findViewById(R.id.donut_progress_rom);
		mEmptyView = (TextView) rootView.findViewById(android.R.id.empty);
		ListView listView = (ListView) rootView.findViewById(android.R.id.list);
		View headerLayout = inflater.inflate(R.layout.apps_list_header, listView, false);
		headerLayout.findViewById(R.id.apps_list_header);
		listView.setEmptyView(mEmptyView);
		listView.addHeaderView(headerLayout, null, false);
		listView.setAdapter(mAppsListAdapter);

		mColorBar = (LinearColorBar) rootView.findViewById(R.id.color_bar);

		mColorBarOne = (LinearColorBar) rootView.findViewById(R.id.view_one);
		mColorBarTwo = (LinearColorBar) rootView.findViewById(R.id.view_two);
		mColorBarThree = (LinearColorBar) rootView.findViewById(R.id.view_three);
		try {
			if (null != getActivity())
				mColorBar.setColors(
						getResources().getColor(R.color.apps_list_system_memory),
						getResources().getColor(R.color.apps_list_cache_memory),
						getResources().getColor(R.color.apps_list_free_memory));
			mColorBarOne.setColorsOne(getResources().getColor(R.color.apps_list_system_memory));
			mColorBarTwo.setColorsTwo(getResources().getColor(R.color.apps_list_cache_memory));
			mColorBarThree.setColorsThree(getResources().getColor(R.color.apps_list_free_memory));

			mSystemSizeText = (TextView) rootView.findViewById(R.id.systemSize);
			mCacheSizeText = (TextView) rootView.findViewById(R.id.cacheSize);
			mFreeSizeText = (TextView) rootView.findViewById(R.id.freeSize);
			deviceSizeText = (TextView) rootView.findViewById(R.id.deviceSize);

			expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
			clean_browse_buttton = (Button) rootView
					.findViewById(R.id.cln_browse_history_button);
			clean_browse_buttton.setEnabled(false);
			clean_browse_buttton.setTextColor(getResources().getColor(R.color.btn_disable));
			clean_browse_buttton.setOnClickListener(btnClick);
			myClipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
			checkBoxClipData
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
													 boolean isChecked) {
							buttonDisable();
						}
					});
			checkBoxCache
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
													 boolean isChecked) {
							buttonDisable();
							cacheSize(newsizeStr, isChecked);
						}
					});

			getAllRunningProcess();
			checkClipDataJunkList();
		}catch (IllegalStateException e){
			e.printStackTrace();
		}

		donutProgressRAM.setProgress(0);
		donutProgressROM.setProgress(0);
		ramBackground();
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		updateStorageUsage();
		if (null != AppData.getInstance().getList()
				&& AppData.getInstance().getList().isEmpty()) {
			new GlobalAsyncTask(getActivity())
					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		if (null != AppData.getInstance().getBrowserHistory()
				&& AppData.getInstance().getBrowserHistory().isEmpty()) {
			new BrowserAsyncTask(getActivity())
					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		if (null != AppData.getInstance().getJunk()
				&& AppData.getInstance().getJunk().isEmpty()) {
			new JunkAsyncTask(getActivity())
					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
	}

	/**
	 * Checking for junk data and clipboard data and if data is empty make those
	 * check boxes uncheck.
	 */

	private void checkClipDataJunkList() {
		try {
			ClipData cp = myClipboard.getPrimaryClip();
			if (cp != null) {
				ClipData.Item item = cp.getItemAt(0);
				String text = item.getText().toString();

				if (text.matches("")) {
					checkBoxClipData.setChecked(false);
					checkBoxClipData.setEnabled(false);
				} else {
					checkBoxClipData.setChecked(true);
					checkBoxClipData.setEnabled(true);
				}
			} else {
				checkBoxClipData.setChecked(false);
				checkBoxClipData.setEnabled(false);
			}
		}catch (NullPointerException e){
			e.printStackTrace();
		}
	}

	/**
	 * Preparing lists in clean memory module
	 */

	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataHeader = new ArrayList<String>();
		listDataChild = new LinkedHashMap<String, List<ListItem>>();
		try {
			if (null != getActivity())
				if (isAdded()) {
					listDataHeader.add(getString(R.string.clean_memory));
					listDataHeader.add(getString(R.string.memory_boost));
					listDataHeader.add(getString(R.string.browse_history));

					listDataChild.put(listDataHeader.get(0), listItem);
					listDataChild.put(listDataHeader.get(1), ramUsageList);
					listDataChild.put(listDataHeader.get(2), browseHistoryItem);


					if (expListView != null) {
						listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild, sumFinal);
						listAdapter.setListener(this);
						expListView.setAdapter(listAdapter);

						dialogSettingsgallery = speedBoosterSharedPref.getInt(
								getString(
										R.string.speedBoosterSortingType),
								dialogSettingsgallery);
						isDirectionSelection = speedBoosterSharedPref.getBoolean(
								getString(R.string.speedBoosterSorting),
								isDirectionSelection);
						allSorting();
					}
				}
		}catch (IllegalStateException e){
			e.printStackTrace();
		}
		expListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
										int groupPosition, long id) {
				return false;
			}
		});
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			int previousGroup = -1;

			/**
			 * showing child list when click on grop item
			 */
			@Override
			public void onGroupExpand(int groupPosition) {
				if (groupPosition != previousGroup)
					expListView.collapseGroup(previousGroup);
				previousGroup = groupPosition;
			}

		});

		/**
		 * if the child list is opened then by clicking on grop item again, the
		 * child list will be closed
		 */
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
			}
		});

	}

	private void getAllRunningProcess() {
		junkSize = AppData.getInstance().getJunkData();
		getRunningProcess();
		if (null != ramUsageList && ramUsageList.isEmpty()) {
			AsendingList();
			if (isAdded())
				updateStorageUsage();
			if (null != AppData.getInstance().getList())
				ramUsageList = AppData.getInstance().getList();
		}
		if (null != AppData.getInstance().getBagroundlist()) {
			listItem = AppData.getInstance().getBagroundlist();
		}
		if (browseHistoryItem != null && browseHistoryItem.isEmpty() && null != AppData.getInstance().getBrowserHistory()) {
			browseHistoryItem = AppData.getInstance().getBrowserHistory();
		}
		sumFinal = (int) AppData.getInstance().getMemorysize();
		prepareListData();
	}

	/**
	 * getting running processes details
	 */
	public void getRunningProcess() {
		mDetailList = CommonLibrary.GetRunningProcess(getActivity(), mActivityManager, Integer.MIN_VALUE, Constants.SECURITY_LEVEL, false);
		AsendingList();
	}

	/**
	 * updating storage usage
	 */
	@SuppressWarnings({ "deprecation" })
	private void updateStorageUsage() {

		StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
		long blockSize;
		long totalSize;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
			blockSize = statFs.getBlockSizeLong();
			totalSize = statFs.getBlockCountLong() * blockSize;
		}else {
			blockSize = statFs.getBlockSize();
			totalSize = statFs.getBlockCount() * blockSize;
		}
		long medMemory = junkSize;
		long lowMemory = (long) statFs.getAvailableBlocks() * (blockSize);
		long highMemory = totalSize - medMemory - lowMemory;
		long usedMemory = highMemory + medMemory;
		BidiFormatter bidiFormatter = BidiFormatter.getInstance();

		totalMemorySize = bidiFormatter.unicodeWrap(Formatter.formatShortFileSize(getActivity(), totalSize));
		deviceSizeText.setText(totalMemorySize);
		String sizeStr = bidiFormatter.unicodeWrap(Formatter.formatShortFileSize(getActivity(), lowMemory));
		mFreeSizeText.setText(getString(R.string.apps_list_header_memory,sizeStr));
		sizeStr = bidiFormatter.unicodeWrap(Formatter.formatShortFileSize(getActivity(), usedMemory));

		sizeStr = bidiFormatter.unicodeWrap(Formatter.formatShortFileSize(getActivity(), medMemory));
		if(sizeStr.equals("0.00B")){
			mCacheSizeText.setText("Loading..");
			sizeStr = "Loading..";
		}else {
			mCacheSizeText.setText(getString(R.string.apps_list_header_memory, sizeStr));
		}
		newsizeStr = sizeStr;
		cacheSize(newsizeStr, true);

		mCacheSizeNew = getString(R.string.apps_list_header_memory, sizeStr);
		sizeStr = bidiFormatter.unicodeWrap(Formatter.formatShortFileSize(getActivity(), highMemory));
		mSystemSizeText.setText(getString(R.string.apps_list_header_memory, sizeStr));
		/** AWS setAppCacheUsage, setJunkFilesSize, setJunkFiles**/
		ApplicationData.getInstance().setAppCacheUsage("" + usedMemory);
		ApplicationData.getInstance().setJunkFilesSize("" + medMemory);
		ApplicationData.getInstance().setJunkFiles("" + mAppsListAdapter.getCount());
		mColorBarOne.setRatiosOne((float) highMemory / (float) totalSize);
		mColorBarTwo.setRatiosTwo((float) medMemory / (float) totalSize);
		mColorBarThree.setRatiosThree((float) lowMemory / (float) totalSize);
		mColorBar.setRatios((float) highMemory / (float) totalSize, (float) medMemory / (float) totalSize, (float) lowMemory / (float) totalSize);
	}

	private void cacheSize(String sizeStr, boolean isCheked){
		if(isCheked){
			String substring = sizeStr.substring(Math.max(sizeStr.length() - 2, 0));
			cacheTextViewSize.setText(removeLastChar(sizeStr) + " " + substring);
		}else{
			cacheTextViewSize.setText("0.0 MB");
		}

	}

	/**
	 * Speed Booster button click functionality(deleting app cache, cleaning
	 * browse history/bookmarks and killing background apps
	 */
	private OnClickListener btnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			clean_browse_buttton.setEnabled(false);
			if(isAdded() && null != getActivity())
				clean_browse_buttton.setTextColor(getResources().getColor(R.color.btn_disable));
			easyTracker.send(MapBuilder.createEvent(getString(R.string.speed_booster),
					getString(R.string.analytics_speedbooster_button_clicked), "track event",
					null).build());
			AppData.getInstance().setFromNoifiction(false);
			if(isAdded() && null!= getActivity())
				new CleanStorageTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			AppData.getInstance().setRam(0);
			AppData.getInstance().setInternalMemory(0);

		}
	};

	protected void newPrepareList() {
		Activity activity = getActivity();
		if(activity != null && isAdded()){
			if (expListView != null) {
				listAdapter = new ExpandableListAdapter(getActivity(),
						listDataHeader, listDataChild, sumFinal);
				listAdapter.setListener(this);
				expListView.setAdapter(listAdapter);
				dialogSettingsgallery = speedBoosterSharedPref.getInt(getString(R.string.speedBoosterSortingType),
						dialogSettingsgallery);
				isDirectionSelection = speedBoosterSharedPref.getBoolean(getString(R.string.speedBoosterSorting),
						isDirectionSelection);
				allSorting();
			}
		}

	}

	protected void cleanClipboardData() {
		ClipData data = ClipData.newPlainText("", "");
		myClipboard.setPrimaryClip(data);
	}

	private void setSortBy(AppsListAdapter.SortBy sortBy) {
		mSharedPreferences.edit().putString(mSortByKey, sortBy.toString())
				.apply();

	}

	/**
	 * method for all the lists in ascending when selecting sort by ascending
	 */
	private void AsendingList() {
		try {
			mItems = AppData.getInstance().getJunk();
			if (mItems != null)
				Collections.sort(mItems, ComparatorMethod.cacheNameAsscComparator);
			if (mItems != null)
				mAppsListAdapter.setItems(mItems);

			if (listItem != null)
				Collections.sort(listItem, ComparatorMethod.AsscComparatorCleanMemory);
			if (browseHistoryItem != null)

				Collections.sort(browseHistoryItem,
						ComparatorMethod.AsscComparatorBrowseHistory);

			if (ramUsageList != null)

				Collections.sort(ramUsageList,
						ComparatorMethod.AsscComparatorMemoryBoost);
		} catch (Exception e) {
		}

	}

	/**
	 * showing required options for the Speed Booster screen
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.menu_fahrenheit).setVisible(false);
		menu.findItem(R.id.menu_celsius).setVisible(false);
		menu.findItem(R.id.menu_dont_delete_apps).setVisible(false);
		menu.findItem(R.id.menu_auto_speed_setting).setVisible(true);
		menu.findItem(R.id.menu_auto_speed_memory).setVisible(true);
		menu.findItem(R.id.menu_auto_clean).setVisible(false);
		menu.findItem(R.id.action_sort_by_size_asc).setVisible(false);
		menu.findItem(R.id.action_sort_by_name_asc).setVisible(false);
		menu.findItem(R.id.action_sort_by_name_des).setVisible(false);
		menu.findItem(R.id.action_sort_by_size_des).setVisible(false);
		menu.findItem(R.id.action_sort_by_lastused_asc).setVisible(false);
		menu.findItem(R.id.action_sort_by_lastused_des).setVisible(false);
		menu.findItem(R.id.action_name_ascending).setVisible(false);
		menu.findItem(R.id.action_name_decending).setVisible(false);
		menu.findItem(R.id.action_remove_after_invite).setVisible(false);
	}

	/**
	 * speed booster option item click functionality
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

			case R.id.action_sort:
				speedBoosterSortingDialog();
		}
		return super.onOptionsItemSelected(item);
	}

	private void speedBoosterSortingDialog() {
		try{
			final Dialog speedBoosterSortingDlg = new Dialog(getActivity());
			speedBoosterSortingDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
			speedBoosterSortingDlg.setContentView(R.layout.cleanapp_sorting_dialog);
			SharedPreferences speedBoosterSharedPref = getActivity().getSharedPreferences("SPEED_BOOSTER_SHARED_PREF", Context.MODE_PRIVATE);
			final Editor speedBoosterEditor = speedBoosterSharedPref.edit();
			TextView button_ok = (TextView) speedBoosterSortingDlg.findViewById(R.id.button_done);
			RadioGroup appSettingGroup = (RadioGroup) speedBoosterSortingDlg.findViewById(R.id.app_setting_grp);
			RadioButton size_button = (RadioButton) speedBoosterSortingDlg.findViewById(R.id.size);
			size_button.setVisibility(View.VISIBLE);
			RadioGroup askBeforeAscendingGroup = (RadioGroup) speedBoosterSortingDlg.findViewById(R.id.ask_bfore_grp);
			easyTracker.send(MapBuilder.createEvent(getString(R.string.speed_booster),
					getString(R.string.analytics_speedbooster_sorting_dialog), "track event", null).build());
			WindowManager.LayoutParams wmlp = speedBoosterSortingDlg.getWindow()
					.getAttributes();
			wmlp.gravity = Gravity.TOP | Gravity.CENTER;
			int density = getResources().getDisplayMetrics().densityDpi;
			switch (density) {
				case DisplayMetrics.DENSITY_MEDIUM:
					wmlp.x = 560;
					wmlp.y = 50;
					break;
				case DisplayMetrics.DENSITY_LOW:
					wmlp.x = 560;
					wmlp.y = 30;
					break;
				case DisplayMetrics.DENSITY_HIGH:
					wmlp.x = 160;
					wmlp.y = 60;

					break;
				case DisplayMetrics.DENSITY_XHIGH:
					wmlp.x = 220;
					wmlp.y = 80;
					break;

				case DisplayMetrics.DENSITY_XXHIGH:
					wmlp.x = 450;
					wmlp.y = 120;
					break;
				case DisplayMetrics.DENSITY_XXXHIGH:
					wmlp.x = 160;
					wmlp.y = 220;
					break;
			}

			speedBoosterSortingDlg.show();

			dialogSettingsgallery = speedBoosterSharedPref.getInt(getString(R.string.speedBoosterSortingType),
					dialogSettingsgallery);
			isDirectionSelection = speedBoosterSharedPref.getBoolean(getString(R.string.speedBoosterSorting), isDirectionSelection);

			if (dialogSettingsgallery == -1) {
				appSettingGroup.check(R.id.name);
			} else if (dialogSettingsgallery == 1) {
				appSettingGroup.check(R.id.size);
			}

			isDirectionSelection = speedBoosterSharedPref.getBoolean(getString(R.string.speedBoosterSorting), isDirectionSelection);
			if (isDirectionSelection) {
				askBeforeAscendingGroup.check(R.id.ascending);
			} else {
				askBeforeAscendingGroup.check(R.id.decending);
			}
			appSettingGroup
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group, int checkedId) {

							switch (checkedId) {
								case R.id.name:
									dialogSettingsgallery = -1;
									easyTracker.send(MapBuilder.createEvent(getString(R.string.speed_booster),
											getString(R.string.analytics_speedbooster_sorting_by_name), "track event", null).build());
									break;
								case R.id.size:
									dialogSettingsgallery = 1;
									easyTracker.send(MapBuilder.createEvent(getString(R.string.speed_booster),
											getString(R.string.analytics_speedbooster_sorting_by_size), "track event", null).build());
									break;
								default:
									break;
							}
						}
					});

			askBeforeAscendingGroup
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group, int checkedId) {
							switch (checkedId) {
								case R.id.decending:
									isDirectionSelection = false;
									break;
								case R.id.ascending:
									isDirectionSelection = true;
									break;
								default:
									break;
							}

						}
					});
			if (!speedBoosterSortingDlg.isShowing())
				speedBoosterSortingDlg.show();
			button_ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						if (isAdded() && null != getActivity())
							easyTracker.send(MapBuilder.createEvent(
									getString(R.string.speed_booster),
									getString(
											R.string.sorting_dialog_done_button),
									"track event", null).build());
						allSorting();
						mAppsListAdapter.notifyDataSetChanged();
						mAppsListAdapter.notifyDataSetInvalidated();
						speedBoosterEditor.putBoolean(getString(R.string.speedBoosterSorting),
								isDirectionSelection);
						speedBoosterEditor.putBoolean(getString(R.string.speedBoosterSorting),
								isDirectionSelection);
						speedBoosterEditor.commit();
						speedBoosterEditor.putInt(getString(
										R.string.speedBoosterSortingType),
								dialogSettingsgallery);
						speedBoosterEditor.commit();

						listAdapter.notifyDataSetChanged();

						speedBoosterSortingDlg.dismiss();
					}catch (IllegalStateException e){
						e.printStackTrace();
					}
				}

			});
		}catch(NullPointerException e){
			e.printStackTrace();
		}

	}

	/**
	 * deleting bookmarks functionality
	 */
	@SuppressWarnings("unused")
	private void cleanBookMarks() {
		if (Browser.getAllBookmarks(contentResolver) != null) {
			contentResolver
					.delete(Browser.BOOKMARKS_URI, "bookmark == 1", null);

		}
	}

	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(getActivity()).activityStart(getActivity());
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(getActivity()).activityStop(getActivity());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if ( booster_dialog!=null && booster_dialog.isShowing() ){
			booster_dialog.dismiss();
		}
		if ( dialogNew!=null && dialogNew.isShowing() ){
			dialogNew.dismiss();
		}
		if (timerDonutRAM != null) {
			timerDonutRAM = null;

		}
		if (timerDonutROM != null) {
			timerDonutROM = null;

		}
		getActivity().finish();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	/**
	 * deleting history details
	 */
	private void clearHistory() {
		contentResolver = getActivity().getContentResolver();
		Browser.clearHistory(contentResolver);
		Browser.clearSearches(contentResolver);
		updateBrowseHistoryList();
		//}
	}

	/**
	 * updating browse history details
	 */
	private void updateBrowseHistoryList() {
		browseHistoryItem.clear();
	}


	@Override
	public void expandGroupEvent(int groupPosition, boolean isExpanded) {
		if (isExpanded)
			expListView.collapseGroup(groupPosition);
		else
			expListView.expandGroup(groupPosition);
	}

	@Override
	public void GroupItemSelection(SparseBooleanArray cleanMemory,
								   SparseBooleanArray memoryBoost, SparseBooleanArray history) {
		cleanMemoryArray = cleanMemory;
		memoryArray = memoryBoost;
		historyArray = history;
		dialogcleanMemoryArray = cleanMemory.size();
		dialoghistoryArray = history.size();
		buttonDisable();
		/** AWS setBrowserHistorySize, setBrowserHistoryDeletedSavedMemorySize, setEndingBackgroundProcessesSize, setEndingBackgroundProcessesSize
		 * , setStartupBackgroundProcessesSize,  setNoOfStartupProcessesItems **/
		ApplicationData.getInstance().setBrowserHistorySize("" + history.size());
		ApplicationData.getInstance().setNoOfbrowserHistoryItems("" + history.size());
		ApplicationData.getInstance().setBrowserHistoryDeletedSavedMemorySize("" + history.size());
		ApplicationData.getInstance().setNoOfDeletedbrowserHistoryItems("" + history.size());

		ApplicationData.getInstance().setEndingBackgroundProcessesSize("" + cleanMemory.size());
		ApplicationData.getInstance().setNoOfEndingBackgroundProcesses("" + cleanMemory.size());
		ApplicationData.getInstance().setEndingBackgroundProcessesSavedMemorySize("" + cleanMemory.size());
		ApplicationData.getInstance().setNoOfEndingBackgroundProcessesItems("" + cleanMemory.size());

		ApplicationData.getInstance().setStartupBackgroundProcessesSize("" + memoryBoost.size());
		ApplicationData.getInstance().setNoOfStartupProcessesItems("" + memoryBoost.size());
		ApplicationData.getInstance().setNoOfStartupBackgroundProcesses("" + memoryBoost.size());
		ApplicationData.getInstance().setEndingStartupProcessesSavedMemorySize("" + memoryBoost.size());

		if (cleanMemoryArray.size() == listItem.size()) {
			listAdapter.resetGrpCheckboxCleanMemory(true);
		} else {
			listAdapter.resetGrpCheckboxCleanMemory(false);
		}

		if (memoryArray.size() == ramUsageList.size()) {
			listAdapter.resetGrpCheckboxRamMemory(true);
		} else {
			listAdapter.resetGrpCheckboxRamMemory(false);
		}

		if (historyArray.size() == browseHistoryItem.size()) {
			listAdapter.resetGrpCheckboxBrowserHistory(true);
		} else {
			listAdapter.resetGrpCheckboxBrowserHistory(false);
		}

	}

	private void buttonDisable() {
		try {
			if (cleanMemoryArray.size() == 0 && memoryArray.size() == 0
					&& historyArray.size() == 0
					&& checkBoxCache.isChecked() == false
					&& checkBoxClipData.isChecked() == false) {
				clean_browse_buttton.setEnabled(false);
				if(isAdded())
					clean_browse_buttton.setTextColor(getResources().getColor(R.color.btn_disable));
			} else {
				clean_browse_buttton.setEnabled(true);
				clean_browse_buttton.setTextColor(Color.WHITE);
			}
		}catch(IllegalStateException e){
			e.printStackTrace();
		}
	}

	private void listClearbuttonDisable() {
		if (listItem.size() <= 0 || ramUsageList.size() <= 0
				|| mAppsListAdapter.getCount() <= 0
				|| browseHistoryItem.size() <= 0) {
			clean_browse_buttton.setEnabled(false);
			if(isAdded())
				clean_browse_buttton.setTextColor(getResources().getColor(R.color.btn_disable));
			checkBoxCache.setChecked(false);
			checkBoxCache.setEnabled(false);
			checkBoxClipData.setChecked(false);
			checkBoxClipData.setEnabled(false);
			listAdapter.resetGrpCheckboxBrowserHistory(false);
		} else {
			clean_browse_buttton.setEnabled(true);
			clean_browse_buttton.setTextColor(Color.WHITE);
			checkBoxCache.setChecked(true);
			checkBoxCache.setEnabled(true);
			checkBoxClipData.setChecked(true);
			checkBoxClipData.setEnabled(true);
			listAdapter.resetGrpCheckboxBrowserHistory(true);
		}
	}

	/**
	 * RAM animation functionality
	 *
	 * @param
	 */
	private void donutRAMProgress(final Double resultFinal) {
		timerDonutRAM = new Timer();
		timerDonutRAM.schedule(new TimerTask() {
			@Override
			public void run() {
				if (null != getActivity())
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Double result = 0.0;
							if (resultFinal >= 95) {
								result = 95.0;
							} else {
								result = resultFinal;
							}
							if (donutProgressRAM.getProgress() == result || donutProgressRAM.getProgress() <= result)
								donutProgressRAM.setProgress(donutProgressRAM .getProgress() + 1);
							if (donutProgressRAM.getProgress() == result) {
								donutProgressRAM.setProgress(donutProgressRAM .getProgress() + 1);
								if (donutProgressRAM.getProgress() == result) {
									donutProgressRAM.setProgress(donutProgressRAM .getProgress() + 1);
									try {
										if (timerDonutRAM != null) {
											timerDonutRAM.cancel();
											timerDonutRAM.purge();
											timerDonutRAM = null;
										}
									} catch (NullPointerException e) {
										e.printStackTrace();
									}
								}
								try {
									if (timerDonutRAM != null) {
										timerDonutRAM.cancel();
										timerDonutRAM.purge();
										timerDonutRAM = null;
									}
								} catch (NullPointerException e) {
									e.printStackTrace();
								}
								if (donutProgressRAM.getProgress() > result)
									donutRAMProgressBack(result);
							}

						}
					});
			}
		}, 1000, 5);
	}

	/**
	 * RAM BACK animation functionality
	 *
	 * @param result
	 */
	private void donutRAMProgressBack(final Double result) {
		timerDonutRAM = new Timer();
		timerDonutRAM.schedule(new TimerTask() {
			@Override
			public void run() {

				if (null != getActivity())
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (donutProgressRAM.getProgress() > 0) {
								donutProgressRAM.setProgress(donutProgressRAM.getProgress() - 1);
								if (donutProgressRAM.getProgress() == result - 1) {
									donutProgressRAM.setProgress(donutProgressRAM.getProgress() + 1);
									if (donutProgressRAM.getProgress() == result - 1) {
										donutProgressRAM.setProgress(donutProgressRAM.getProgress() - 1);
										try {
											if (timerDonutRAM != null) {
												timerDonutRAM.cancel();
												timerDonutRAM.purge();
												timerDonutRAM = null;
											}
										} catch (NullPointerException e) {
											e.printStackTrace();
										}
									}
									try {
										if (timerDonutRAM != null) {
											timerDonutRAM.cancel();
											timerDonutRAM.purge();
											timerDonutRAM = null;
										}
									} catch (NullPointerException e) {
										e.printStackTrace();
									}
								}

							}

						}
					});
			}
		}, 1000, 100);
	}

	/**
	 * ROM animation functionality
	 *
	 */
	private void donutROMProgress(final Double resultFinal) {
		timerDonutROM = new Timer();
		timerDonutROM.schedule(new TimerTask() {
			@Override
			public void run() {
				if (null != getActivity())
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Double result = 0.0;
							if (resultFinal >= 95) {
								result = 95.0;
							} else {
								result = resultFinal;
							}
							if (donutProgressROM.getProgress() == result || donutProgressROM.getProgress() <= result)
								donutProgressROM.setProgress(donutProgressROM
										.getProgress() + 1);
							if (donutProgressROM.getProgress() == result) {
								donutProgressROM.setProgress(donutProgressROM
										.getProgress() + 1);
								if (donutProgressROM.getProgress() == result) {
									donutProgressROM.setProgress(donutProgressROM
											.getProgress() + 1);
									try {
										if (timerDonutROM != null) {
											timerDonutROM.cancel();
											timerDonutROM.purge();
											timerDonutROM = null;
										}
									} catch (NullPointerException e) {
										e.printStackTrace();
									}
								}
								try {
									if (timerDonutROM != null) {
										timerDonutROM.cancel();
										timerDonutROM.purge();
										timerDonutROM = null;
									}
								} catch (NullPointerException e) {
									e.printStackTrace();
								}
								if (donutProgressROM.getProgress() > result)
									donutROMProgressBack(result);
							}

						}
					});
			}
		}, 1000, 5);
	}
	/**
	 * ROM BACK animation functionality
	 *
	 * @param result
	 */

	private void donutROMProgressBack(final Double result) {
		timerDonutROM = new Timer();
		timerDonutROM.schedule(new TimerTask() {
			@Override
			public void run() {

				if (null != getActivity())
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {

							if(donutProgressROM.getProgress() >0)
							{
								donutProgressROM.setProgress(donutProgressROM.getProgress() -1);
								if (donutProgressROM.getProgress() == result - 1) {
									donutProgressROM.setProgress(donutProgressROM.getProgress() + 1);
									if (donutProgressROM.getProgress() == result - 1) {
										donutProgressROM.setProgress(donutProgressROM.getProgress() - 1);
										try {
											if (timerDonutROM != null) {
												timerDonutROM.cancel();
												timerDonutROM.purge();
												timerDonutROM = null;
											}
										} catch (NullPointerException e) {
											e.printStackTrace();
										}
									}
									try {
										if (timerDonutROM != null) {
											timerDonutROM.cancel();
											timerDonutROM.purge();
											timerDonutROM = null;
										}
									} catch (NullPointerException e) {
										e.printStackTrace();
									}
								}

							}

						}
					});
			}
		}, 1000, 100);
	}
	/**
	 * group indicator up/down when clicked
	 */
	private void groupIndicatorReset() {
		if (listItem.size() <= 0) {
			expListView.collapseGroup(0);
		} else {
			expListView.expandGroup(0);
		}
		if (ramUsageList.size() <= 0) {
			expListView.collapseGroup(1);
		} else {
			expListView.expandGroup(1);
		}
		if (browseHistoryItem.size() <= 0) {
			expListView.collapseGroup(2);
		} else {
			expListView.expandGroup(2);
		}
	}



	/**
	 * Booster dialog box creation
	 */
	private int sumFinalListener;

	@SuppressLint("SetJavaScriptEnabled")
	private void speedBoosterDialog() {
		try{
			booster_dialog = new Dialog(getActivity());
			booster_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			booster_dialog.setContentView(R.layout.speed_booster_memory_dialogue);
			TextView button_ok = (TextView) booster_dialog.findViewById(R.id.ok);
			booster_dialog.setCanceledOnTouchOutside(false);
			booster_dialog.setCancelable(false);
			booster_dialog.show();
			TextView junkfileText = (TextView) booster_dialog.findViewById(R.id.junkfiles);
			TextView historyText = (TextView) booster_dialog.findViewById(R.id.history);
			TextView backgroundText = (TextView) booster_dialog.findViewById(R.id.background);
			TextView startupText = (TextView) booster_dialog.findViewById(R.id.startup);
			TextView mb1Text = (TextView) booster_dialog.findViewById(R.id.of_storage_mb);
			TextView mb2Text = (TextView) booster_dialog.findViewById(R.id.of_total_mb);

			historyText.setText("" + dialoghistoryArray);
			backgroundText.setText("" + dialogcleanMemoryArray);

			if(ramList == true){
				if (sumFinalListener <= 0) {
					startupText.setText("0.00 MB");
					mb2Text.setText("0.00 MB");
				} else {
					startupText.setText("" + sumFinalListener + " MB ");
					mb2Text.setText("" + sumFinalListener + " MB");
				}
			}else{
				mb2Text.setText("0.00 MB");
			}
			if(junkList == true){
				if(mCacheSizeNew.equals("0.00B")){
					junkfileText.setText("0.00 MB");
					mb1Text.setText("0.00 MB");
				}else {
					String substring = mCacheSizeNew.substring(Math.max(mCacheSizeNew.length() - 2, 0));
					junkfileText.setText(removeLastChar(mCacheSizeNew) + " " + substring);
					mb1Text.setText(removeLastChar(mCacheSizeNew) + " " + substring);
				}

			}else{
				junkfileText.setText("0.00 MB");
				mb1Text.setText("0.00 MB");
			}

			/** AWS setStorageTotalSaved, setMemoryTotalSaved**/
			ApplicationData.getInstance().setStorageTotalSaved("" + mb1Text.getText().toString());
			ApplicationData.getInstance().setMemoryTotalSaved("" + mb2Text.getText().toString());

			if (!booster_dialog.isShowing())
				booster_dialog.show();
			button_ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					easyTracker.send(MapBuilder.createEvent(
							getString(R.string.speed_booster_dialog),getString(R.string.speed_booster_dialog_button),
							"track event", null).build());
					booster_dialog.dismiss();
					getActivity().finish();
				}
			});
		}catch(NullPointerException e){
			e.printStackTrace();
		}

	}

	public String removeLastChar(String s) {
		if (s == null || s.length() == 0) {
			return s;
		}
		return s.substring(0, s.length()-2);

	}

	/**
	 * CleanStorage for cleaning the storage or memory
	 *
	 *
	 */
	private class CleanStorageTask extends AsyncTask<Void, Void, List<ListItem>> {
		boolean clipDataState ;
		boolean junkDataState ;

		@Override
		protected void onPreExecute() {
			clipDataState = checkBoxClipData.isChecked();
			junkDataState = checkBoxCache.isChecked();
			isCleanStorage = true;
			try{
				Activity activity = getActivity();
				if(activity != null && isAdded()){
					dialogNew = new ProgressDialog(getActivity());
					dialogNew.setMessage("Processing...");
					dialogNew.setCanceledOnTouchOutside(false);
					dialogNew.setCancelable(false);
					dialogNew.setIndeterminateDrawable(getResources().getDrawable(R.drawable.green_progress));

					dialogNew.show();
				}
			}catch(NullPointerException e){
				e.printStackTrace();
			}
		}

		@Override
		protected List<ListItem> doInBackground(Void... params) {

			try{
				if (clipDataState == true) {
					cleanClipboardData();
				}
				easyTracker.send(MapBuilder.createEvent(getString(R.string.speed_booster),
						getString(R.string.analytics_speedbooster_clipdata_checked) + clipDataState, "track event", null).build());
				if (junkDataState == true) {
					new CleanCache(getActivity().getApplicationContext());
					junkList = true;
				}
				easyTracker.send(MapBuilder.createEvent(getString(R.string.speed_booster),
						getString(R.string.analytics_speedbooster_junk_checked) + junkDataState, "track event", null).build());
				for (int X = memoryArray.size() - 1; X >= 0; X--) {
					ramList = true;
					int key = memoryArray.keyAt(X);
					boolean value = memoryArray.get(key);
					ListItem listItemName = (ListItem) listAdapter.getChild(1, key);
					String runningAppname = listItemName.getPackageName();
					mActivityManager.killBackgroundProcesses(runningAppname);
					if (value){
						//	ramUsageList.remove(key);
					}
				}
				easyTracker.send(MapBuilder.createEvent(getString(R.string.speed_booster),
						getString(R.string.analytics_speedbooster_ramboost_checked) + ramList, "track event", null).build());
				for (int X = cleanMemoryArray.size() - 1; X >= 0; X--) {
					backgroundListState = true;
					int key = cleanMemoryArray.keyAt(X);
					boolean value = cleanMemoryArray.get(key);
					ListItem listItemName = (ListItem) listAdapter.getChild(0, key);
					String runningAppname = listItemName.getApptitle();
					for (ProcessDetailInfo detailInfo : mDetailList) {
						if (!detailInfo.getLabel().equalsIgnoreCase(runningAppname)) {
							detailInfo.setSelected(true);
						}
					}
					CommonLibrary.KillProcess(getActivity(), mDetailList,
							mActivityManager, true);

					if (value) {
						//	listItem.remove(key);
					}

				}
				easyTracker.send(MapBuilder.createEvent(getString(R.string.speed_booster),
						getString(R.string.analytics_speedbooster_baground_apps_checked) + backgroundListState, "track event", null).build());
				//browseHistoryItem.clear();
				cleanMemoryArray.clear();
				memoryArray.clear();
				/** AWS setMemoryRAMFlag, setBrowserHistoryFlag, setClipBoardDataFlag, setResidualJunkFilesFlag **/
				ApplicationData.getInstance().setMemoryRAMFlag("" + ramList);
				ApplicationData.getInstance().setBrowserHistoryFlag("" + historyState);
				ApplicationData.getInstance().setClipBoardDataFlag("" + clipDataState);
				ApplicationData.getInstance().setResidualJunkFilesFlag("" + junkDataState);

				AppData.getInstance().setList(ramUsageList);
				AppData.getInstance().setBagroundlist(listItem);
				new GlobalAsyncTask(getActivity()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<ListItem> result) {
			super.onPostExecute(result);
			int m1 = (int) AppData.getInstance().getMemorysize();
			int m3 = (int) junkSize;
			Utility.cleanCacheDB(m3, m1);
			if (historyState == true) {
				if (!browseHistoryItem.isEmpty()) {
					clearHistory();
				}
			}
			listClearbuttonDisable();
			groupIndicatorReset();
			isDialogShowing = true;
			sumFinal = 0;
			if (mItems != null) {
				mItems.clear();
			}
			AppData.getInstance().setJunk(mItems);
			AppData.getInstance().setList(ramUsageList);
			AppData.getInstance().setBagroundlist(listItem);
			AppData.getInstance().setBrowserHistory(browseHistoryItem);
			newPrepareList();
			mAppsListAdapter.notifyDataSetChanged();
			mAppsListAdapter.notifyDataSetInvalidated();
			listAdapter.resetCheckedValues(false);
			listAdapter.notifyDataSetChanged();
			listAdapter.notifyDataSetInvalidated();
			expListView.invalidate();
			try{
				if (isAdded())
					if (isDialogShowing)
						speedBoosterDialog();

				try {
					if (dialogNew.isShowing()) {
						dialogNew.dismiss();
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}catch (IllegalStateException e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setHistoryState(boolean historyState) {
		this.historyState = historyState;
	}

	@Override
	public void getJunkItem() {
		setSortBy(AppsListAdapter.SortBy.APP_NAME);
		mItems = AppData.getInstance().getJunk();

		if (isCleanStorage) {
		}
		if (mItems != null)
			mAppsListAdapter.setItems(mItems);
		mAppsListAdapter.notifyDataSetChanged();
		try {
			if (isAdded()) {
				try {
					getAllRunningProcess();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}catch (IllegalStateException e){
			e.printStackTrace();
		}
		buttonDisable();
	}

	private void ramBackground() {
		double ram ;
		double internalMemory ;
		internalMemory = Utility.getInternalMemory();
		if(AppData.getInstance().getInternalMemory() >= 0.0){
			AppData.getInstance().setInternalMemory(internalMemory);
			donutROMProgress(AppData.getInstance().getInternalMemory());
		}

		ram = Utility.getRAM(getActivity().getApplicationContext());
		if(AppData.getInstance().getRam() >= 0.0){
			AppData.getInstance().setRam(ram);
			donutRAMProgress(AppData.getInstance().getRam());
		}
	}

	/*** Method to do sorting for expandable list and cache list**/
	public void allSorting(){
		try {
			if (isDirectionSelection == false) {
				if (dialogSettingsgallery == -1) {
					mItems = AppData.getInstance().getJunk();
					Collections.sort(mItems, ComparatorMethod.cacheNameDesceComparator);
					if (mItems != null)
						mAppsListAdapter.setItems(mItems);
					mAppsListAdapter.notifyDataSetChanged();

					Collections.sort(listItem,
							ComparatorMethod.DeseComparatorCleanMemory);
					Collections.sort(browseHistoryItem,
							ComparatorMethod.DescComparatorBrowseHistory);
					Collections.sort(ramUsageList,
							ComparatorMethod.DescComparatorMemoryBoost);
				} else if (dialogSettingsgallery == 1) {
					mItems = AppData.getInstance().getJunk();

					if (mItems != null) {
						mAppsListAdapter.setItems(mItems);
						Collections.sort(mItems, ComparatorMethod.cacheSizeDesceComparator);
					}
					mAppsListAdapter.notifyDataSetChanged();

					Collections.sort(ramUsageList, ComparatorMethod.DescComparatorMemory);
				}
			} else if (isDirectionSelection == true) {

				if (dialogSettingsgallery == -1) {
					AsendingList();
					mAppsListAdapter.notifyDataSetChanged();

				} else if (dialogSettingsgallery == 1) {
					mItems = AppData.getInstance().getJunk();
					Collections.sort(mItems, ComparatorMethod.cacheSizeAsseComparator);
					if (mItems != null)
						mAppsListAdapter.setItems(mItems);
					mAppsListAdapter.notifyDataSetChanged();
					Collections.sort(ramUsageList, ComparatorMethod.AsscComparatorMemory);
				}
			}
		}catch (NullPointerException e){
			e.printStackTrace();
		}
	}

	@Override
	public void getRam() {
		//ramBackground();
	}

	@Override
	public void getUpdateSelectedRam(int sumFinal) {
		this.sumFinalListener=sumFinal;
	}
}
