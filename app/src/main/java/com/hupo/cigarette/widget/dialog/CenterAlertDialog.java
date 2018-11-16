package com.hupo.cigarette.widget.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupo.cigarette.R;

/**
 * @author Gemini on 2018/10/26.
 *
 *  内容居中显示的弹框
 */
public class CenterAlertDialog extends RLDialog {

    private Listener listener;
//    private CharSequence title,
    private CharSequence msg;
    private Context context;
    private Button btn_left, btn_right;
    private TextView tv_msg;
    private String leftBtnStr, rightBtnStr;
    private int linkify = -1;
    private View mView;

    /**
     *
     * @param context
     * @param msg
     * @param leftBtnStr
     * @param rightBtnStr
     * @param listener
     */
    public CenterAlertDialog(Context context, CharSequence msg, String leftBtnStr, String rightBtnStr, Listener listener) {
        super(context);
        this.msg = msg;
        this.leftBtnStr = leftBtnStr;
        this.rightBtnStr = rightBtnStr;
        this.listener = listener;
        this.context = context;
        super.createView();
        super.setCanceledOnTouchOutside(false);
    }

    /**
     *
     * @param context
     * @param view
     * @param leftBtnStr
     * @param rightBtnStr
     * @param listener
     */
    public CenterAlertDialog(Context context, View view, String leftBtnStr, String rightBtnStr, Listener listener) {
        super(context);
        this.leftBtnStr = leftBtnStr;
        this.rightBtnStr = rightBtnStr;
        this.listener = listener;
        this.context = context;
        this.mView = view;
        super.createView();
        super.setCanceledOnTouchOutside(false);
    }

    /**
     *
     * @param context
     * @param msg
     * @param msgLinkify
     * @param leftBtnStr
     * @param rightBtnStr
     * @param listener
     */
    public CenterAlertDialog(Context context, CharSequence msg, int msgLinkify,
                             String leftBtnStr, String rightBtnStr, Listener listener) {
        super(context);
        this.msg = msg;
        this.linkify = msgLinkify;
        this.leftBtnStr = leftBtnStr;
        this.rightBtnStr = rightBtnStr;
        this.listener = listener;
        this.context = context;
        super.createView();
        super.setCanceledOnTouchOutside(false);
    }

    @Override
    protected View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_alert_center, null);
        view.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

//        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
//        tv_title.setText(title);
        tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        if (mView != null) {
            LinearLayout ll1 = (LinearLayout) view.findViewById(R.id.ll1);
            tv_msg.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            ll1.addView(mView, params);
        }
//		tv_msg.setAutoLinkMask(linkify);
        tv_msg.setText(msg);
        btn_left = (Button) view.findViewById(R.id.btn_left);
        btn_left.setText(leftBtnStr);
        if (linkify == -1) {
            btn_right = (Button) view.findViewById(R.id.btn_right);
            btn_right.setText(rightBtnStr);
        } else {
            btn_right = (Button) view.findViewById(R.id.btn_right);
            btn_right.setText(rightBtnStr);
        }
        btn_left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
                listener.onLeftClick();
            }
        });
        btn_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dismiss();
                listener.onRightClick();
            }
        });
        if (TextUtils.isEmpty(rightBtnStr)) {
            btn_right.setVisibility(View.GONE);
        }
        return view;
    }

    /**
     *
     */
    public interface Listener {
        public void onLeftClick();

        public void onRightClick();
    }
}
