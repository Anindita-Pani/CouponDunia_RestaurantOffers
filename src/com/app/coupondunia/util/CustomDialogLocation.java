package com.app.coupondunia.util;

/* Dialog to show the location enable message*/
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class CustomDialogLocation extends DialogFragment {
	String btnConfirmText, btnCancelText, msg;
	Context context;
	OnClickListener btnConfirmListenr, btncancelListner; // listener for both
															// the button
															// -cancel & ok

	public CustomDialogLocation(Context context, String btnConfirmText,
			String btnCancelText, String msg) {
		this.btnCancelText = btnCancelText;
		this.btnConfirmText = btnConfirmText;
		this.context = context;
		this.msg = msg;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setMessage(msg)
				.setPositiveButton(btnConfirmText, btnConfirmListenr)
				.setNegativeButton(btnCancelText, btncancelListner);
		builder.setCancelable(false);
		return builder.create();

	}

	public void SetPositiveBtnListner(OnClickListener btnPosClick) {
		btnConfirmListenr = btnPosClick;
	}

	public void SetNegativeBtnListner(OnClickListener btnNegClick) {
		btncancelListner = btnNegClick;
	}

	public void show(FragmentManager fragmentManager, String string) {
		// TODO Auto-generated method stub

	}

}
