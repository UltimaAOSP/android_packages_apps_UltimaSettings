
package com.ultima.settings.otaupdater;

import android.content.Context;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class UpdateXmlParser extends DefaultHandler {

    private static final String TAG = "UpdateXMLParser";
    
    UpdateResult results = new UpdateResult();
    String value = null;
    Context mContext;
    
    boolean tagName = false;
    boolean tagVersion = false;
    boolean tagCode = false;
    boolean tagUrl = false;
    boolean tagMD5 = false;
    boolean tagLog = false;
    boolean tagAndroid = false;
    
    public UpdateResult parse(File xmlFile, Context c) throws IOException {
        mContext = c;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(xmlFile, this);
            return results;
        } catch (ParserConfigurationException ex) {
            Log.e(TAG, "", ex);
        } catch (SAXException ex) {
            Log.e(TAG, "", ex);
        }
        return null;
    }
    
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {

        if (attributes.getLength() > 0) {

            String tag = "<" + qName;
            for (int i = 0; i < attributes.getLength(); i++) {

                tag += " " + attributes.getLocalName(i) + "="
                        + attributes.getValue(i);
            }
            tag += ">";
        }
        
        if (qName.equalsIgnoreCase("name")) {
            tagName = true;
        }

        if (qName.equalsIgnoreCase("version")) {
            tagVersion = true;
        }

        if (qName.equalsIgnoreCase("code")) {
            tagCode = true;
        }

        if (qName.equalsIgnoreCase("url")) {
            tagUrl = true;
        }

        if (qName.equalsIgnoreCase("checkmd5")) {
            tagMD5 = true;
        }

        if (qName.equalsIgnoreCase("changelog")) {
            tagLog = true;
        }
        
        if (qName.equalsIgnoreCase("android")){
            tagAndroid = true;
        }

    }

    @Override
    public void characters(char ch[], int start, int length)
            throws SAXException {
        value = new String(ch, start, length);
        if (tagName) {
            results.setName(value);
            tagName = false;
        }

        if (tagVersion) {
            results.setVersion(value);
            tagVersion = false;
        }

        if (tagCode) {
            results.setCodename(value);
            tagCode = false;
        }

        if (tagUrl) {
            results.setUrl(value);
            tagUrl = false;
        }

        if (tagMD5) {
            results.setMd5(value);
            tagMD5 = false;
        }

        if (tagLog) {
            results.setChangelog(value);
            tagLog = false;
        }
        if (tagAndroid) {
            results.setAndroidVersion(value);
            tagAndroid = false;
        }

    }    
}
