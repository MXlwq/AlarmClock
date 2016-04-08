package com.liwenquan.sl;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by LWQ on 2016/4/1.
 */
public class ClockIntentJSONSerialize {

    private Context mContext;
    private String mFilename;

    public ClockIntentJSONSerialize(Context mContext, String mFilename) {
        this.mContext = mContext;
        this.mFilename = mFilename;
    }

    public ArrayList<Clock> loadClocks() throws IOException,JSONException{
        ArrayList<Clock> clocks=new ArrayList<Clock>();
        BufferedReader reader=null;
        try{
            InputStream in=mContext.openFileInput(mFilename);
            reader=new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString=new StringBuilder();
            String line=null;
            while((line=reader.readLine())!=null)
            {
                jsonString.append(line);
            }
            JSONArray array=(JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for(int i=0;i<array.length();i++){
                clocks.add(new Clock(array.getJSONObject(i)));
            }
        }
        catch (FileNotFoundException e){

        }
        finally {
            if(reader!=null)
                reader.close();
        }
        return clocks;
    }


    public void saveClocks(ArrayList<Clock> clocks) throws JSONException,IOException {
        JSONArray array = new JSONArray();
        for (Clock c : clocks) {
            array.put(c.toJSON());
        }
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        }
        finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
