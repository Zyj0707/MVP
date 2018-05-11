package com.zyj.base.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.ArrayRes;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.zyj.base.R;
import com.zyj.base.ui.interfaces.OnInputListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lan on 2016/7/7.
 */
public class UIHelper {
    private static Toast mToast;
    private static Snackbar mSnackbar;
    private static ProgressDialog progressDialog;
    private static AlertDialog alertDialog;
    private static AlertDialog inputDialog;
    private static Timer timer;
    private static AlertTimerTask timerTask;
    private static final long sTime = 2;         //对话框显示时间，单位秒

    public static void showToast(Context context, String str) {
        showToast(context, str, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, @StringRes int stringId) {
        showToast(context, context.getString(stringId), Toast.LENGTH_SHORT);
    }

    private static void showToast(Context context, CharSequence str, int duration) {
        cancelToast();

        mToast = Toast.makeText(context, str, duration);
        mToast.show();
    }

    public static void showSnackbar(View view, String str) {
        showSnackbar(view, str, Snackbar.LENGTH_LONG);
    }

    private static void showSnackbar(View view, CharSequence str, int duration) {
        cancelSnackbar();

        mSnackbar = Snackbar.make(view, str, duration);
        mSnackbar.show();
    }

    public static void showProgress(Context context, int strId) {
        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        String msg = context.getString(strId);
        progressDialog = ProgressDialog.show(context, "", msg, true, true);
    }

    public static void showProgressValue(Context context, int strId) {
        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        String msg = context.getString(strId);
        progressDialog = ProgressDialog.show(context, "", msg, true, true);
    }

    public static void setProgress(int progress) {
        if(progressDialog != null) {
            progressDialog.setMessage(progress + "%");
        }
    }

    public static void showSingleDialog(Context context, String msg) {
        cancelAlertDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showSingleDialog(Context context, String msg, DialogInterface.OnClickListener confirmListener) {
        cancelAlertDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg)
                .setCancelable(false)
                .setOnDismissListener(onDismissListener)
                .setPositiveButton(R.string.confirm, confirmListener);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showAlertDialog(Context context, String msg, DialogInterface.OnClickListener confirmListener) {
        cancelAlertDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm, confirmListener)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showSingleDialog(Context context, @StringRes int msgId) {
        String msg = context.getString(msgId);
        showSingleDialog(context, msg);
    }

    public static void showSingleDialog(Context context, @StringRes int msgId, DialogInterface.OnClickListener confirmListener) {
        String msg = context.getString(msgId);
        showSingleDialog(context, msg, confirmListener);
    }

    public static void showAlertDialog(Context context, @StringRes int msgId, DialogInterface.OnClickListener confirmListener) {
        String msg = context.getString(msgId);
        showAlertDialog(context, msg, confirmListener);
    }

    public static void showListDialog(Context context, @ArrayRes int resArray,
                                      DialogInterface.OnClickListener confirmListener) {
        cancelAlertDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(resArray, confirmListener);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showFeedbackInputDialog(final Context context, final OnInputListener onInputListener) {
        if(inputDialog != null) {
            inputDialog.dismiss();
            inputDialog = null;
        }

        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_opinion_input, null);
        final EditText editContent = (EditText) view.findViewById(R.id.editInput);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = editContent.getText().toString();

                        if (StringUtil.isEmpty(content)) {
                            showToast(context, R.string.common_input_empty);
                        }
                        else {
                            dialog.cancel();
                            if (onInputListener != null) {
                                onInputListener.onInput(content);
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        inputDialog = builder.create();
        inputDialog.show();
    }

    private static DialogInterface.OnDismissListener onDismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            cancelTick();
        }
    } ;

    public static void cancelToast() {
        if(mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    public static void cancelSnackbar() {
        if(mSnackbar != null) {
            mSnackbar.dismiss();
            mSnackbar = null;
        }
    }

    public static void dismiss() {
        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static void cancelAlertDialog() {
        if(alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    public static void cancelInputDialog() {
        if(inputDialog != null) {
            inputDialog.dismiss();
            inputDialog = null;
        }
    }

    private static void timeTick() {
        cancelTick();
        timer = new Timer();
        timerTask = new AlertTimerTask();
        timer.schedule(timerTask, sTime*1000);//2秒后开始
    }

    public static void cancelTick() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private static class AlertTimerTask extends TimerTask {
        @Override
        public void run() {
            cancelAlertDialog();
        }
    }

}
