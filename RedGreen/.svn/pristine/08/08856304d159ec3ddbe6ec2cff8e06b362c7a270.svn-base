package com.unfoldlabs.redgreen.global;



import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.Browser;

import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.model.ListItem;

import java.util.ArrayList;
import java.util.List;

public class BrowserAsyncTask extends AsyncTask<Void, Void, List<ListItem>>{
	private Context ctx;
	private List<ListItem> browseHistoryItem = new ArrayList<ListItem>();
	private ContentResolver contentResolver;

	public BrowserAsyncTask(Context ctx) {
		this.ctx = ctx;
	}


	@Override
	protected List<ListItem> doInBackground(Void... params) {
		getBrowsingHistory(ctx);
		return browseHistoryItem;
	}

	@Override
	protected void onPostExecute(List<ListItem> result) {

		super.onPostExecute(result);
		AppData.getInstance().setBrowserHistory(result);

	}

	private void getBrowsingHistory(Context ctx) {

		Bitmap icon = null;
		contentResolver = ctx.getContentResolver();
		String sel = Browser.BookmarkColumns.BOOKMARK + " = 0";
		String[] projection = { Browser.BookmarkColumns.TITLE,
				Browser.BookmarkColumns.URL, Browser.BookmarkColumns.FAVICON };
		Cursor rows = contentResolver.query(Browser.BOOKMARKS_URI, projection, sel,
				null, null);
		if (null != rows && rows.getCount() > 0) {
			while (rows.moveToNext()) {

				String title = rows.getString(rows.getColumnIndex(projection[0]));
				String url = rows.getString(rows.getColumnIndex(projection[1]));

				byte[] bicon = rows.getBlob(rows.getColumnIndex(projection[2]));
				if (bicon != null) {
					icon = BitmapFactory
							.decodeByteArray(bicon, 0, bicon.length);
				} else {
					icon = BitmapFactory.decodeResource(ctx.getResources(),
							R.drawable.no_iamge);
				}

				ListItem item = new ListItem();
				item.setHistoryTitle(title);
				item.setHistoryUrl(url);
				item.setHistoryBitmap(icon);
				browseHistoryItem.add(item);

			}
			rows.close();
		}
	}
}
