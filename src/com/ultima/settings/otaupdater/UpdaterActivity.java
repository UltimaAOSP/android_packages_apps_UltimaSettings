package com.ultima.settings.otaupdater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import com.ultima.settings.R;
import com.ultima.settings.utils.Preferences;
import com.ultima.settings.utils.Utils;

public class UpdaterActivity extends Activity {

    public UpdaterActivity() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setTheme(Preferences.getTheme());           
        setContentView(R.layout.updater_main);
        setupActionBar();
        
        getRomInformation();
    }

    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    public void openUpdates(View v){
        Intent intent = new Intent(this, UpdaterCheck.class);
        startActivity(intent);
    }
    
    public void openDownloads(View v){
        //Intent intent = new Intent(this, UpdaterCheck.class);
        //startActivity(intent);
    }
    
    public void openGapps(View v){
        //Intent intent = new Intent(this, UpdaterCheck.class);
        //startActivity(intent);
    }
    
    private void getRomInformation(){
        String htmlColorOpen = "<font color='#33b5e5'>";
        String htmlColorClose = "</font>";
               
        //ROM name
        TextView romName = (TextView) findViewById(R.id.updater_rom_name);        
        String romNameTitle = getApplicationContext().getResources().getString(R.string.updater_rom_name) + " ";
        String romNameActual = Utils.getProp("ro.ua.romname");        
        romName.setText(Html.fromHtml(romNameTitle + htmlColorOpen + romNameActual + htmlColorClose));
        
        //ROM version
        TextView romVersion = (TextView) findViewById(R.id.updater_rom_version);        
        String romVersionTitle = getApplicationContext().getResources().getString(R.string.updater_rom_version) + " ";
        String romVersionActual = Utils.getProp("ro.ua.version");        
        romVersion.setText(Html.fromHtml(romVersionTitle + htmlColorOpen + romVersionActual + htmlColorClose));
        
        //ROM codename
        TextView romCodeName = (TextView) findViewById(R.id.updater_rom_codename);        
        String romCodeNameTitle = getApplicationContext().getResources().getString(R.string.updater_rom_codename) + " ";
        String romCodeNameActual = Utils.getProp("ro.ua.codename");       
        romCodeName.setText(Html.fromHtml(romCodeNameTitle + htmlColorOpen + romCodeNameActual + htmlColorClose));
        
        //ROM date
        TextView romDate = (TextView) findViewById(R.id.updater_rom_date);        
        String romDateTitle = getApplicationContext().getResources().getString(R.string.updater_rom_build_date) + " ";
        String romDateActual = Utils.getProp("ro.build.date");
        romDate.setText(Html.fromHtml(romDateTitle + htmlColorOpen + romDateActual + htmlColorClose));
        
        //ROM date
        TextView romAndroid = (TextView) findViewById(R.id.updater_android_version);        
        String romAndroidTitle = getApplicationContext().getResources().getString(R.string.updater_android_verison) + " ";
        String romAndroidActual = Utils.getProp("ro.build.version.release");       
        romAndroid.setText(Html.fromHtml(romAndroidTitle + htmlColorOpen + romAndroidActual + htmlColorClose));

    }
}
