package com.example.playmarketversion.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.playmarketversion.BuildConfig;
import com.example.playmarketversion.R;
import com.example.playmarketversion.model.VersionChecker;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    String latestVersion;
    String versionName;
    Button btnUpdate;
    TextView tvAppVersion;
    TextView tvReleaseVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        btnUpdate = findViewById(R.id.btn_update);
        versionName = BuildConfig.VERSION_NAME;
        tvAppVersion = findViewById(R.id.tv_appVersion);
        tvReleaseVersion = findViewById(R.id.tv_appRelease);



        VersionChecker versionChecker = new VersionChecker();

        try {
            latestVersion = versionChecker.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        tvAppVersion.setText("App ="  + versionName);
        tvReleaseVersion.setText("Release = " + latestVersion);

        if (checkVersion(versionName, latestVersion)) {
            btnUpdate.setText("Open App");

            btnUpdate.setOnClickListener(v -> {
                Toast.makeText(this, latestVersion, Toast.LENGTH_SHORT).show();
                openApp();
            });
        }else {
            btnUpdate.setText("Update");
            btnUpdate.setOnClickListener(v -> {
                openPlayStore("https://play.google.com/store/apps/details?id=net.giosis.shopping.sg&hl=en&gl=US");
            });

        }

    }

    public void openPlayStore(String text) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/*");
        startActivity(intent);

    }


    private void openApp() {
        Intent intent = new Intent(this, AppActivity.class);
        startActivity(intent);
    }



    private boolean checkVersion(String appVersion, String release) {
        String[] appVersion1 = appVersion.split("\\.");
        String[] release1 = release.split("\\.");

        if (Integer.parseInt(appVersion1[0]) > Integer.parseInt(release1[0])
                && Integer.parseInt(appVersion1[1]) != Integer.parseInt(release1[1])) {
            return true;
        }

        if (Integer.parseInt(appVersion1[0]) == Integer.parseInt(release1[0])
                && Integer.parseInt(appVersion1[1]) > Integer.parseInt(release1[1])) {
            return true;
        }


        return false;
    }
}
//https://play.google.com/store/apps/details?id=net.giosis.shopping.sg&hl=en&gl=US
