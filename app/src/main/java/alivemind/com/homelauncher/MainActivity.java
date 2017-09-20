package alivemind.com.homelauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends Activity {

    PackageManager manager;
    List<ResolveInfo> appList = new ArrayList<>();
    List<ResolveInfo> filteredList = new ArrayList<>();
    CustomAdapter adapter;
    CustomAsyncTask asyncTask;

    RecyclerView rvList;
    EditText etSearch;

    int viewType = 0;
    ImageView ivSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSearch();
        initList();

        ivSettings = (ImageView) findViewById(R.id.ivSettings);
        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeViewType();
            }
        });

    }

    void initSearch() {

        etSearch = (EditText) findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filerList(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void initList() {

        rvList = (RecyclerView) findViewById(R.id.rvList);
        adapter = new CustomAdapter(new CustomAdapter.AdapterListener() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View cellView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_app, parent, false);
                return new AppHolder(cellView);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

                AppHolder appHolder = (AppHolder) holder;

                if (viewType == 0) {
                    appHolder.tvAppName.setGravity(Gravity.LEFT);
                    appHolder.cellView.setOrientation(LinearLayout.HORIZONTAL);
                } else {
                    appHolder.tvAppName.setGravity(Gravity.CENTER);
                    appHolder.cellView.setOrientation(LinearLayout.VERTICAL);
                }

                final ResolveInfo resolveInfo = filteredList.get(position);
                appHolder.tvAppName.setText(resolveInfo.loadLabel(manager));
                //appHolder.tvPackageName.setText(resolveInfo.activityInfo.packageName);
                appHolder.ivIcon.setImageDrawable(resolveInfo.activityInfo.loadIcon(manager));

                appHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = manager.getLaunchIntentForPackage(resolveInfo.activityInfo.packageName);
                        startActivity(intent);
                    }
                });

                appHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onDelete(position, resolveInfo.activityInfo.packageName);
                        return false;
                    }
                });
            }

            @Override
            public int getItemCount() {
                return filteredList.size();
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }
        });
        rvList.setAdapter(adapter);
    }

    void changeViewType() {
        if (viewType == 0) {
            viewType = 1;
            rvList.setLayoutManager(new GridLayoutManager(MainActivity.this, 2, LinearLayoutManager.VERTICAL, false));
        } else if (viewType == 1) {
            viewType = 2;
            rvList.setLayoutManager(new GridLayoutManager(MainActivity.this, 3, LinearLayoutManager.VERTICAL, false));
        } else if (viewType == 2) {
            viewType = 3;
            rvList.setLayoutManager(new GridLayoutManager(MainActivity.this, 4, LinearLayoutManager.VERTICAL, false));
        } else {
            viewType = 0;
            rvList.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadApps();
    }

    void loadApps() {

        new CustomAsyncTask(new CustomAsyncTask.AsyncListener() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(String result) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onProgressUpdate(String... values) {

            }

            @Override
            public String doInBackground(String... params) {

                Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);

                manager = getPackageManager();
                appList = manager.queryIntentActivities(intent, 0);

                Collections.sort(appList, new Comparator<ResolveInfo>() {
                    @Override
                    public int compare(ResolveInfo o1, ResolveInfo o2) {
                        return o1.loadLabel(manager).toString().compareToIgnoreCase(o2.loadLabel(manager).toString());
                    }
                });
                filteredList = new ArrayList<>(appList);

                return null;
            }
        }).execute();

    }

    void filerList(final String filter) {

        if (asyncTask != null && !asyncTask.isCancelled()) {
            asyncTask.cancel(true);
        }

        asyncTask = (CustomAsyncTask) new CustomAsyncTask(new CustomAsyncTask.AsyncListener() {
            @Override
            public void onPreExecute() {
                filteredList.clear();
            }

            @Override
            public void onPostExecute(String result) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onProgressUpdate(String... values) {

            }

            @Override
            public String doInBackground(String... params) {

                for (ResolveInfo resolveInfo : appList) {
                    if (String.valueOf(resolveInfo.loadLabel(manager)).toLowerCase().contains(filter.toLowerCase())) {
                        filteredList.add(resolveInfo);
                    }
                }
                return null;
            }
        }).execute();

    }

    public void onDelete(int position, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        //Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        intent.setData(Uri.parse("package:" + packageName));
        startActivityForResult(intent, position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(getClass().getName() + ">>.", "Result Code: " + resultCode + ", Request Code: " + requestCode);

        ResolveInfo resolveInfo = filteredList.get(requestCode);
        if (!isPackageExisted(resolveInfo.activityInfo.packageName)) {
            for (ResolveInfo info : appList) {
                if (resolveInfo.activityInfo.packageName.equals(info.activityInfo.packageName)) {
                    appList.remove(info);
                    filteredList.remove(requestCode);
                    adapter.notifyItemRemoved(requestCode);
                    Toast.makeText(this, info.loadLabel(manager) + " Removed", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }


    public boolean isPackageExisted(String targetPackage) {
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }
}
