package org.ilapin.arvelocity.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class MessageDialog extends DialogFragment {

	private static final String TITLE_KEY = "TITLE_KEY";
	private static final String MESSAGE_KEY = "MESSAGE_KEY";
	private static final String POSITIVE_BUTTON_TITLE_KEY = "POSITIVE_BUTTON_TITLE_KEY";
	private static final String NEGATIVE_BUTTON_TITLE_KEY = "NEGATIVE_BUTTON_TITLE_KEY";

	private Listener mListener;

	private String mTitle;
	private String mMessage;
	private String mPositiveButtonTitle;
	private String mNegativeButtonTitle;

	public static MessageDialog newInstance(final String title, final String message,
											final String positiveButtonTitle, final String negativeButtonTitle) {
		final Bundle arguments = new Bundle();
		final MessageDialog fragment = new MessageDialog();

		arguments.putString(TITLE_KEY, title);
		arguments.putString(MESSAGE_KEY, message);
		arguments.putString(POSITIVE_BUTTON_TITLE_KEY, positiveButtonTitle);
		arguments.putString(NEGATIVE_BUTTON_TITLE_KEY, negativeButtonTitle);

		fragment.setArguments(arguments);

		return fragment;
	}

	@Override
	public void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mListener = (Listener) getActivity();

		final Bundle arguments = getArguments();
		mTitle = arguments.getString(TITLE_KEY);
		mMessage = arguments.getString(MESSAGE_KEY);
		mPositiveButtonTitle = arguments.getString(POSITIVE_BUTTON_TITLE_KEY);
		mNegativeButtonTitle = arguments.getString(NEGATIVE_BUTTON_TITLE_KEY);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

		builder
				.setTitle(mTitle)
				.setMessage(mMessage)
				.setPositiveButton(
						mPositiveButtonTitle,
						(dialog, which) -> mListener.onPositiveButtonClick(MessageDialog.this)
				)
				.setNegativeButton(
						mNegativeButtonTitle,
						(dialog1, which1) -> mListener.onNegativeButtonClick(MessageDialog.this)
				);

		return builder.create();
	}

	public interface Listener {

		void onPositiveButtonClick(MessageDialog dialog);

		void onNegativeButtonClick(MessageDialog dialog);
	}
}
