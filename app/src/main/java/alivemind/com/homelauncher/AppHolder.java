package alivemind.com.homelauncher;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Admin on 5/15/2017.
 */

public class AppHolder extends RecyclerView.ViewHolder {

    public LinearLayout cellView;
    public TextView tvAppName;
    public ImageView ivIcon;

    public AppHolder(View view) {
        super(view);
        cellView = (LinearLayout) view.findViewById(R.id.cellView);
        tvAppName = (TextView) view.findViewById(R.id.tvAppName);
        ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
    }

}
