package de.pixelart.jgg.android.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import de.pixelart.jgg.android.R;

import android.content.Context;

public class SaveFile {
	
	File conDir;
	String w;
	Context context;
	
	public SaveFile(Context context) {
		this.context = context;
	}

	/** Ready to write ? **/
	public void toFile(ArrayList<String> conList) {
		w = "Pixelat JGG App Nummern Liste. App Version:"+context.getString(R.string.version)+"\n\n";
        
		setDir();
		
        for(int i=0; i < conList.size(); i++){
        	String all = conList.get(i);
        	w = w + all+"\n";
        }
        write(w);
	}
	
	private void setDir() {
		/** Create Dir **/
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {	
            conDir = new File(context.getExternalFilesDir(null), "Con");
        }else{
            conDir = new File(context.getFilesDir(),"Con");
        }
		
        if(!conDir.exists()){
            conDir.mkdirs();
        }
	}
	
	/** Write to File **/
	private void write(String all) {
		Writer writer = null;
		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(conDir+"/con.px"), "utf-8"));
		    writer.write(all);
		} catch (IOException ex) {
		} finally {
		   try {
			   writer.close();
		   } catch (Exception ex) {}
		}
	}
	
	
	/** Return File **/
	public File getFile(){
		setDir();
		File con = new File(conDir,"con.px");
		return con;
	}
	
	public String getPah() {
		setDir();
		return conDir.toString();
	}
}
