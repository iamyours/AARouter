package io.github.iamyours.aarouter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

public class Postcard {
    private String activityName;
    private Bundle mBundle;

    public Postcard(String activityName) {
        this.activityName = activityName;
        mBundle = new Bundle();
    }

    public Postcard withString(String key, String value) {
        mBundle.putString(key, value);
        return this;
    }

    public Postcard withInt(String key, int value) {
        mBundle.putInt(key, value);
        return this;
    }

    public Postcard with(Bundle bundle) {
        if (null != bundle) {
            mBundle = bundle;
        }
        return this;
    }

    public void navigation(Activity context, int requestCode) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(context.getPackageName(), activityName));
        intent.putExtras(mBundle);
        context.startActivityForResult(intent, requestCode);
    }
}
