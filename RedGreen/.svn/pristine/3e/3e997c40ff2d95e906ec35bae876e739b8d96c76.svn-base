package com.unfoldlabs.redgreen.applock.lock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.unfoldlabs.redgreen.R;
import com.unfoldlabs.redgreen.applock.util.Util;


@SuppressLint("WrongViewCast")
public class PasswordView extends ViewGroup implements OnClickListener,
		OnLongClickListener {

	/** The current numbers that the user has input */
	private String mPassword = "";

	private Button[] mButtons;

	/** The default is left for back, right for OK */

	private ImageButton mBackButton;
	private Button mOkButton;
	private Button mNextConfirm;

	private int mRows = 3;
	private int mCols = 3;

	/**
	 * Workaround for button incorrect centering
	 */
	private int mPaddingLeft = 0;
	private int mPaddingTop = 0;
	private int mPaddingRight = 0;
	private int mPaddingBottom = 0;

	private int mHorizontalSpacing;
	private int mVerticalSpacing;
	private int mChildWidth;
	private int mChildHeight;

	private int mMaxHeight;
	private int mMaxWidth;
	private int mHeight;
	private int mWidth;

	private final int mOkImageResource = R.drawable.ic_action_accept;
	private final int mBackImageResource = R.drawable.ic_action_cancel;
	private final int mTickButtonOnOkButton = R.drawable.ic_action_accept;

	/** How many times may the view be taller than wide? */
	private float mMaxVScale = 1.2f;
	/** How many times may the view be wider than tall? */
	private float mMaxHScale = 1.2f;

	private boolean mStarted;

	private boolean mEnableHapticFeedback = false;

	private OnNumberListener mListener;

	public void setListener(OnNumberListener listener) {
		this.mListener = listener;
	}

	public PasswordView(Context context) {
		super(context);
	}

	public PasswordView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.PasswordView);

		boolean mSquareChildren;
		try {
			mHorizontalSpacing = a.getDimensionPixelSize(
					R.styleable.PasswordView_horizontalSpacing, 0);
			mVerticalSpacing = a.getDimensionPixelSize(
					R.styleable.PasswordView_verticalSpacing, 0);
			mRows = a.getInteger(R.styleable.PasswordView_rows, mRows);
			mCols = a.getInteger(R.styleable.PasswordView_cols, mCols);

			/**
			 * Avoid Arithmetic Exceptions
			 */
			if (mRows <= 0)
				mRows = 1;
			if (mCols <= 0)
				mCols = 1;

			mMaxWidth = a.getDimensionPixelSize(
					R.styleable.PasswordView_maxWidth, 0);
			mMaxHeight = a.getDimensionPixelSize(
					R.styleable.PasswordView_maxHeight, 0);
			mMaxHScale = a.getFloat(R.styleable.PasswordView_maxHScale, 1F);
			mMaxVScale = a.getFloat(R.styleable.PasswordView_maxVScale, 1F);

			mSquareChildren = a.getBoolean(
					R.styleable.PasswordView_squareChildren, false);

		} finally {
			a.recycle();
		}

		/**
		 * This prevents a bug with children not being measured correctly
		 */
		mPaddingLeft = getPaddingLeft();
		mPaddingRight = getPaddingRight();
		mPaddingTop = getPaddingTop();
		mPaddingBottom = getPaddingBottom();
		setPadding(0, 0, 0, 0);

		if (mSquareChildren && mHorizontalSpacing == mVerticalSpacing) {
			mMaxHScale = (float) mCols / mRows;
			mMaxVScale = (float) mRows / mCols;
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	public void setButtonBackgrounds(int backgroundResId) {
		for (Button b : mButtons) {
			b.setBackgroundResource(backgroundResId);
		}
		mBackButton.setBackgroundResource(backgroundResId);
		mOkButton.setBackgroundResource(backgroundResId);
	}

	public void setButtonBackgrounds(Drawable backgroundDrawable) {
		for (Button b : mButtons) {
			Util.setBackgroundDrawable(b, backgroundDrawable);
		}
		Util.setBackgroundDrawable(mBackButton, backgroundDrawable);
		Util.setBackgroundDrawable(mOkButton, backgroundDrawable);
	}

	public interface OnNumberListener {

		void onStart();

		void onNumberButton(String newNumber);

		void onOkButton();

		void onNextConfirm();

		void onOkButtonLong();

		void onBackButton();

		void onBackButtonLong();
	}

	@Override
	public void onClick(View v) {
		if (!mStarted) {
			mListener.onStart();
			mStarted = true;
		}

		if (v.getId() == mOkButton.getId()) {
			onOkButtonImpl();
		} else if (v.getId() == mBackButton.getId()) {
			onBackButtonImpl();
		} else if(v.getId() == mNextConfirm.getId()){
			onNextConfirmImpl();
		}

		else {
			onNumberButtonImpl(v);
		}
		if (mEnableHapticFeedback) {
			performHapticFeedback(
					HapticFeedbackConstants.VIRTUAL_KEY,
					HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
							| HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
		}
	}

	/**
	 *
	 * @return The distance in inches that the finger has swiped over the
	 *         pattern<br>
	 *         This is calculated as the distance between the pattern circles,
	 *         not the real distance of the finger
	 */
	public float getFingerDistance() {
		float xppi = getResources().getDisplayMetrics().xdpi;
		float yppi = getResources().getDisplayMetrics().ydpi;
		float ppi = (xppi + yppi) / 2;
		float inchesPerDot = (mChildWidth + mChildHeight) / 2 / ppi;
		return inchesPerDot * mPassword.length();
	}

	@Override
	public boolean onLongClick(View v) {
		if (!mStarted) {
			mListener.onStart();
			mStarted = true;
		}

		if (v.getId() == mOkButton.getId()) {
			onOkButtonLongImpl();
		} else if (v.getId() == mBackButton.getId()) {
			onBackButtonLongImpl();
		}
		if (mEnableHapticFeedback) {
			performHapticFeedback(
					HapticFeedbackConstants.LONG_PRESS,
					HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
							| HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
		}
		return true;

	}

	private void onBackButtonLongImpl() {
		clearPassword();
		if (mListener != null) {
			mListener.onBackButtonLong();
		}
	}

	private void onOkButtonLongImpl() {
		if (mListener != null) {
			mListener.onOkButtonLong();
		}
	}

	private void onOkButtonImpl() {
		if (mListener != null) {
			mListener.onOkButton();
		}
	}

	private void onBackButtonImpl() {
		if (mPassword.length() != 0) {
			StringBuilder sb = new StringBuilder(mPassword);
			sb.deleteCharAt(sb.length() - 1);
			setPassword(sb.toString());
		}
		if (mListener != null) {
			mListener.onBackButton();
		}
	}

	private void onNextConfirmImpl() {
		if (mListener != null) {
			mListener.onNextConfirm();
		}
	}

	/**
	 * What happens when a number button is pressed<br>
	 *
	 * @param v
	 *            The view that has been clicked
	 */
	private void onNumberButtonImpl(View v) {
		Button b = (Button) v;
		final String newPassword = new StringBuilder().append(mPassword)
				.append(b.getText()).toString();
		setPassword(newPassword);
		// post instead of executing, so that the
		// last dot in the password gets displayed
		post(new Runnable() {
			@Override
			public void run() {
				if (mListener != null) {
					mListener.onNumberButton(newPassword);
				}
			}
		});
	}

	public String getCurrentNumbers() {
		return mPassword;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		mWidth = resolveSize(MeasureSpec.getSize(widthMeasureSpec),
				widthMeasureSpec);
		mHeight = resolveSize(MeasureSpec.getSize(heightMeasureSpec),
				heightMeasureSpec);

		correctViewSize(mWidth, mHeight, mMaxWidth, mMaxHeight, mMaxHScale,
				mMaxVScale);

		// Reset width and height because some loose pixels at the end:
		int childMSW = MeasureSpec.makeMeasureSpec(mChildWidth,
				MeasureSpec.EXACTLY);
		int childMSH = MeasureSpec.makeMeasureSpec(mChildHeight,
				MeasureSpec.EXACTLY);

		final int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			measureChild(child, childMSW, childMSH);
		}

		setMeasuredDimension(mWidth, mHeight);
	}

	private void correctViewSize(int width, int height, int maxWidth,
								 int maxHeight, float maxHScale, float maxVScale) {
		if (maxWidth != 0)
			width = Math.min(width, maxWidth);
		if (maxHeight != 0)
			height = Math.min(height, maxHeight);
		float hScale = (float) width / height;
		float vScale = (float) height / width;
		// Vertical stretch
		if (hScale <= maxHScale) {
			int desiredHeight = (int) ((float) width * maxVScale);
			height = Math.min(height, desiredHeight);
		}
		// Horizontal stretch
		else if (vScale <= maxVScale) {
			int desiredWidth = (int) ((float) height * maxHScale);
			width = Math.min(width, desiredWidth);
		}

		int horizontalSpacing = mHorizontalSpacing * (mCols - 1);
		int verticalSpacing = mVerticalSpacing * (mRows - 1);

		mChildWidth = (width - mPaddingLeft - mPaddingRight - horizontalSpacing)
				/ mCols;
		mChildHeight = (height - mPaddingTop - mPaddingBottom - verticalSpacing)
				/ mRows;

		// Set the correct values
		mWidth = mPaddingLeft + mPaddingRight + (mChildWidth * mCols)
				+ (mHorizontalSpacing * (mCols - 1));
		mHeight = mPaddingTop + mPaddingBottom + (mChildHeight * mRows)
				+ (mVerticalSpacing * (mRows - 1));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int count = getChildCount();
		int childL, childT;
		childL = childT = 0;
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			childL = mPaddingLeft
					+ ((mHorizontalSpacing + mChildWidth) * (i % mCols));
			childT = mPaddingTop
					+ ((mVerticalSpacing + mChildHeight) * (i / mCols));

			child.layout(childL, childT, childL + mChildWidth, childT
					+ mChildHeight);
		}
	}

	/**
	 * Sets the horizontal spacing in pixels
	 *
	 * @param horizontalSpacing
	 */
	public void setHorizontalSpacing(int horizontalSpacing) {
		this.mHorizontalSpacing = horizontalSpacing;
	}

	/**
	 * Sets the vertical spacing in pixels
	 *
	 * @param verticalSpacing
	 */
	public void setVerticalSpacing(int verticalSpacing) {
		this.mVerticalSpacing = verticalSpacing;
	}

	/**
	 * Update the internal password this {@link PasswordView} is working with.
	 * If password is null then it will be cleared.
	 *
	 * @param password
	 */
	public void setPassword(String password) {
		this.mPassword = (password != null) ? password : "";
	}

	public void clearPassword() {
		setPassword(null);
	}

	/**
	 * Get the current password entered by the user. Never null.
	 *
	 * @return
	 */
	public String getPassword() {
		return mPassword;
	}

	public int getUnpaddedWidth() {
		return getWidth() - mPaddingLeft - mPaddingRight;
	}

	public void setOkButtonVisibility(int visibility) {
		if (mOkButton != null) {
			mOkButton.setVisibility(visibility);
		}
	}

	public void setBackButtonVisibility(int visibility) {
		if (mBackButton != null) {
			mBackButton.setVisibility(visibility);
		}
	}

	public void setCancelButtonOnOkButton() {
		mOkButton.setText("Cancel");
	}

	public void setNextConfirmHide() {
		mNextConfirm.setVisibility(View.GONE);
	}

	public void setNextConfirmButtonToNext() {
		mNextConfirm.setText("Next");
	}

	public void setNextConfirmButtonToConfirm() {
		mNextConfirm.setText("Confirm");
	}

	public void setTickButtonOnOkButton() {
		mOkButton.setBackgroundResource(R.drawable.ic_action_accept);
	}

	/**
	 * @param swap
	 *            True if the buttons should be swapped
	 */
	public void setSwitchButtons(boolean swap) {
		int okVisibility = mOkButton.getVisibility();
		int backVisibility = mBackButton.getVisibility();
		if (swap) {
			mBackButton = (ImageButton) findViewById(R.id.numlock_bRight);
		} else {
			mBackButton = (ImageButton) findViewById(R.id.numlock_bLeft);
		}
		mOkButton.setVisibility(okVisibility);
		mBackButton.setVisibility(backVisibility);

	}

	/**
	 * @return Whether the view has tactile feedback enabled.
	 */
	public boolean isTactileFeedbackEnabled() {
		return mEnableHapticFeedback;
	}

	/**
	 * Set whether the view will use tactile feedback. If true, there will be
	 * tactile feedback as the user enters the pattern.
	 *
	 * @param tactileFeedbackEnabled
	 *            Whether tactile feedback is enabled
	 */
	public void setTactileFeedbackEnabled(boolean tactileFeedbackEnabled) {
		mEnableHapticFeedback = tactileFeedbackEnabled;
	}

}
