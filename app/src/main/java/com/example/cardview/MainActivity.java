package com.example.cardview;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {
    private TextView storageInfo;
    private TextView appCountTextView;
    private ProgressBar storageProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        GifImageView gifImageView = findViewById(R.id.gifImageView);
        gifImageView.setImageResource(R.drawable.hackers);

        storageInfo = findViewById(R.id.storageInfo);
        appCountTextView = findViewById(R.id.appCount);
        storageProgressBar = findViewById(R.id.storageProgressBar);
        displayStorageInfo();
        displayInstalledAppCount();



//        color green System bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));

            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // Use for light icons/text
        }

    }

    private void displayStorageInfo() {
        File path = Environment.getDataDirectory(); // For internal storage
        File[] externalStorageVolumes = ContextCompat.getExternalFilesDirs(this, null);

        long totalInternal = getTotalSpace(path);
        long availableInternal = getAvailableSpace(path);
        long usedInternal = totalInternal - availableInternal;

        StringBuilder storageDetails = new StringBuilder();
        storageDetails.append("Internal Storage:\n");
        storageDetails.append("Total: ").append(totalInternal / (1024 * 1024)).append(" MB\n");
        storageDetails.append("Available: ").append(availableInternal / (1024 * 1024)).append(" MB\n");

        // Update progress bar for internal storage
        int internalUsagePercentage = (int) ((usedInternal * 100) / totalInternal);
        storageProgressBar.setProgress(internalUsagePercentage);

        // Loop through external storage volumes
        for (File file : externalStorageVolumes) {
            if (file != null) {
                long totalExternal = getTotalSpace(file);
                long availableExternal = getAvailableSpace(file);
                long usedExternal = totalExternal - availableExternal;

                storageDetails.append("External Storage:\n");
                storageDetails.append("Total: ").append(totalExternal / (1024 * 1024)).append(" MB\n");
                storageDetails.append("Available: ").append(availableExternal / (1024 * 1024)).append(" MB\n");

                // Update progress bar for external storage (if applicable)
                int externalUsagePercentage = (int) ((usedExternal * 100) / totalExternal);
                storageProgressBar.setProgress(externalUsagePercentage);
            }
        }

        storageInfo.setText(storageDetails.toString());
    }

    private void displayInstalledAppCount() {
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> apps = packageManager.getInstalledApplications(0);
        int appCount = apps.size();
        appCountTextView.setText("Installed Apps: " + appCount);
    }

    private long getTotalSpace(File file) {
        return file.getTotalSpace();
    }

    private long getAvailableSpace(File file) {
        return file.getUsableSpace();
    }
}
