package eu.fra_uas.ochs.klamm.cesljar.gesundheitssystem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class CheckBillDialogFragment extends DialogFragment {

    /**
     * Callback for caller Fragment, to show that accept button was clicked and adding data to database
     */
    public interface DialogListener {
        void onFinishDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.tv_bill_checking);
        builder.setMessage(getResources().getString(R.string.tv_short_billnumber) + getArguments().getString(NewBillFragment.DIALOG_BILL, "") + "\n"
                + getResources().getString(R.string.tv_whom) + getArguments().getString(NewBillFragment.DIALOG_WHOM, "") + "\n"
                + getResources().getString(R.string.tv_amount) + getArguments().getDouble(NewBillFragment.DIALOG_AMOUNT, 0) + " €\n"
                + getResources().getString(R.string.tv_refund) + getArguments().getDouble(NewBillFragment.DIALOG_REFUND, 0) + " €\n"
                + getResources().getString(R.string.tv_date) + getArguments().getString(NewBillFragment.DIALOG_DATE, "") + "\n"
                + getResources().getString(R.string.tv_kind) + getArguments().getString(NewBillFragment.DIALOG_TYPE, "")
        );
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            /**
             * Send the positive button event back to the host fragment
             * @param dialog
             * @param which
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendBackResult();
            }
        });
        builder.setNegativeButton(R.string.login_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder.create();
    }

    public void sendBackResult() {
        DialogListener listener = (DialogListener) getTargetFragment();
        listener.onFinishDialog();
        dismiss();
    }
}
