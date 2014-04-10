package com.ultima.settings.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.Html;
import android.text.Spanned;

import com.ultima.settings.SettingsApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {
   private static Context mContext = SettingsApplication.getContext();
   
   private static final String SPACE = " ";
   private static final String BULLET_SYMBOL = "&#8226";
   private static final String EOL = System.getProperty("line.separator");
   private static final String TAB = "\t";
    
    public static Boolean doesPropExist(String propName, String propValue) {
        boolean valid = false;

        try {
            Process process = Runtime.getRuntime().exec("getprop");
            BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) 
            {
                if(line.contains("[" + propName +"]: [" + propValue + "]")){
                    valid = true;
                }
            }
        } 
        catch (IOException e){
            e.printStackTrace();
        }
        return valid;
    }
    
    public static String getProp(String propName) {
        Process p = null;
        String result = "";
        try {
            p = new ProcessBuilder("/system/bin/getprop", propName).redirectErrorStream(true).start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line=br.readLine()) != null){
                result = line;
            }
            //p.destroy();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    
    public static boolean appInstalled(String uri) {
        PackageManager pm = mContext.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed ;
    }
    
    static public String getBulletList(String header, String []items) {
        StringBuilder listBuilder = new StringBuilder();
        if (header != null && !header.isEmpty()) {
            listBuilder.append(header + EOL + EOL);
        }
        if (items != null && items.length != 0) {
            for (String item : items) {
                Spanned formattedItem = Html.fromHtml(BULLET_SYMBOL + SPACE + item);
                listBuilder.append(TAB + formattedItem + EOL);
            }
        }
        return listBuilder.toString();
    }
}
