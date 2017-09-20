package alivemind.com.homelauncher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.homeLauncher).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
