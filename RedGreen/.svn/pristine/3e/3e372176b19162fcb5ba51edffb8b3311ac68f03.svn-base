package com.unfoldlabs.redgreen.global;

import android.content.Context;
import android.os.AsyncTask;

import com.unfoldlabs.redgreen.fragment.CleanMemoryFragment;
import com.unfoldlabs.redgreen.utilty.Utility;

public class RamUsage extends AsyncTask<Void, Void, Void>{
	private Context mContext;
	private double ram;
	private double internalMemory;

	public RamUsage (Context context){
		mContext = context;
	}
	@Override
	protected Void doInBackground(Void... params) {
		internalMemory = Utility.getInternalMemory();
		ram = Utility.getRAM(mContext);
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		AppData.getInstance().setRam(ram);
		if(AppData.getInstance().getInternalMemory() >= 0.0){
			AppData.getInstance().setInternalMemory(internalMemory);
		}
		if(null != CleanMemoryFragment.listener){
			CleanMemoryFragment.listener.getRam();
		}
	}

}
