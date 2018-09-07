package util;

import android.widget.Toast;

import application.KCApplication;

public class ToastUtil {

    private static Toast mToast;

    /**
     * 显示String类型的内容
     * @param content
     */
    public static void show(String content) {
        if (mToast == null) {
            mToast = Toast.makeText(KCApplication.getContext(), content, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(content);
        }

        mToast.show();
    }

    /**
     * 显示int类型的内容
     * @param content
     */
    public static void show(int content) {
        if (mToast == null) {
            mToast = Toast.makeText(KCApplication.getContext(), String.valueOf(content), Toast.LENGTH_SHORT);
        } else {
            mToast.setText(String.valueOf(content));
        }
        mToast.show();
    }

    /**
     * 显示double类型的内容
     * @param content
     */
    public static void show(double content) {
        if (mToast == null) {
            mToast = Toast.makeText(KCApplication.getContext(), String.valueOf(content), Toast.LENGTH_SHORT);
        } else {
            mToast.setText(String.valueOf(content));
        }

        mToast.show();
    }

    /**
     * 显示float类型的内容
     * @param content
     */
    public static void show(float content) {
        if (mToast == null) {
            mToast = Toast.makeText(KCApplication.getContext(), String.valueOf(content), Toast.LENGTH_SHORT);
        } else {
            mToast.setText(String.valueOf(content));
        }

        mToast.show();
    }

    /**
     * 显示boolean类型的内容
     * @param content
     */
    public static void show(boolean content) {
        if (mToast == null) {
            mToast = Toast.makeText(KCApplication.getContext(), String.valueOf(content), Toast.LENGTH_SHORT);
        } else {
            mToast.setText(String.valueOf(content));
        }

        mToast.show();
    }

    /**
     * 显示char类型的内容
     * @param content
     */
    public static void show(char content) {
        if (mToast == null) {
            mToast = Toast.makeText(KCApplication.getContext(), String.valueOf(content), Toast.LENGTH_SHORT);
        } else {
            mToast.setText(String.valueOf(content));
        }

        mToast.show();
    }

}
