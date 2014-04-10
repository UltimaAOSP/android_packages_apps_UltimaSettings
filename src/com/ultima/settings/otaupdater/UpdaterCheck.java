package com.ultima.settings.otaupdater;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ultima.settings.R;
import com.ultima.settings.SettingsApplication;
import com.ultima.settings.utils.Preferences;
import com.ultima.settings.utils.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdaterCheck extends Activity {
    
    public final String TAG = "OTA Updater";
    protected static final String MANIFEST = "update_manifest.xml";
    private static String PREFS_FILE = "UpdaterPrefs";
    private static String LAST_CHECKED = "updater_last_update_check";
    
    private Context mContext = SettingsApplication.getContext();
    
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    
    ProgressDialog mLoadingDialog;
    UpdateResult updateResult;

    public UpdaterCheck() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setTheme(Preferences.getTheme());           
        setContentView(R.layout.updater_check);
        setupActionBar();
        
        settings = getSharedPreferences(PREFS_FILE, 0);
        editor = settings.edit();
        
        findViewById(R.id.updater_no_update_layout).setVisibility(View.GONE);
        findViewById(R.id.updater_update_avail_layout).setVisibility(View.GONE);
        
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setIndeterminate(true);
        mLoadingDialog.setMessage("Checking for update...");

        mLoadingDialog.show();      
        new LoadUpdateManifest().execute();
    }

    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    public void checkUpdates(View v){
        Toast.makeText(this, "Layout", Toast.LENGTH_SHORT).show();
    }
    
    private void updateNoUpdateView(){
        String time;
        boolean is24 = DateFormat.is24HourFormat(mContext);
        Date now = new Date();
        Locale locale = Locale.getDefault();
        TextView lastChecked = (TextView) findViewById(R.id.updater_last_checked);
        
        String resourceString = mContext.getResources().getString(R.string.updater_last_checked) + " ";
     
        if(is24){
            time = new SimpleDateFormat("d, MMMM HH:mm", locale).format(now);
        } else {
            time = new SimpleDateFormat("d, MMMM hh:mm a", locale).format(now);
        }    
        
        String CheckedTime = settings.getString(LAST_CHECKED, time);
        
        lastChecked.setText(resourceString + CheckedTime);
        editor.putString(LAST_CHECKED, time);
        editor.commit();
    }
    
    private void updateAvailableUpdateView(){
        TextView updateName = (TextView) findViewById(R.id.updater_update_available);
        TextView changeLogContent = (TextView) findViewById(R.id.updater_update_changelog_content);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.update_download_progress_bar);
        TextView progressCounter = (TextView) findViewById(R.id.updater_download_progress_counter);
        final Button downloadButton = (Button) findViewById(R.id.updater_download_update_button);
        
        downloadButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if(downloadButton.getText().equals("Download")){
                    downloadButton.setText("Cancel");
                } else {
                    downloadButton.setText("Download");
                }               
            }
        });
        
        progressBar.setProgress(50);
        
        updateName.setText(updateResult.getName()
                + " " + updateResult.getAndroidVersion()
                + " " + updateResult.getCodename()
                + " v" + updateResult.getVersion()
                + " Update");
        
        
        progressCounter.setText("0MB/" + "MB");
        
        
        String[] changeLogStr = updateResult.getChangelog().split(";");
        changeLogContent.setText(Utils.getBulletList(
                updateResult.getCodename() + " v" + updateResult.getVersion(), changeLogStr));
    }
    
    private void onFinishLoad(){
        if(updateResult != null){
            String currentVersion = Utils.getProp("ro.ua.version");
            String manifestVer = updateResult.getVersion();

            if(currentVersion.equalsIgnoreCase(manifestVer)){
                findViewById(R.id.updater_no_update_layout).setVisibility(View.VISIBLE);
                updateNoUpdateView();
            } else {
                findViewById(R.id.updater_update_avail_layout).setVisibility(View.VISIBLE);
                updateAvailableUpdateView();
            }
        }
    }
    
    private class LoadUpdateManifest extends
    AsyncTask<Void, Boolean, UpdateResult> {

        @Override
        protected UpdateResult doInBackground(Void... v) {

            try {
                InputStream input = null;

                    URL url = new URL(getString(R.string.updater_manifest_url));
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    // this will be useful so that you can show a typical
                    // 0-100%
                    // progress bar
                    int fileLength = connection.getContentLength();

                    // download the file
                    input = new BufferedInputStream(url.openStream());
                
                OutputStream output = getApplicationContext().openFileOutput(
                        MANIFEST, MODE_PRIVATE);

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    setProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

                // file finished downloading, parse it!
                UpdateXmlParser parser = new UpdateXmlParser();
                return parser.parse(new File(getApplicationContext().getFilesDir(), MANIFEST),
                        getApplicationContext());
            } catch (Exception e) {
                Log.d(TAG, "Exception!", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(UpdateResult result) {
            updateResult = result;
            mLoadingDialog.cancel();
            onFinishLoad();
            super.onPostExecute(result);
        }
    }
}
