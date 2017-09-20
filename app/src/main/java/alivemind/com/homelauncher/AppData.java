package alivemind.com.homelauncher;

import android.graphics.drawable.Drawable;

/**
 * Created by Admin on 9/19/2017.
 */

public class AppData {

    private CharSequence appName;
    private String packageName;
    private Drawable appIcon;

    public CharSequence getAppName() {
        return appName;
    }

    public void setAppName(CharSequence appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}
