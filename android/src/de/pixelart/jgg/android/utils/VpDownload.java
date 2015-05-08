package de.pixelart.jgg.android.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import de.pixelart.jgg.android.model.VpPost;

/**
 *  Download und speichere Vertretungsplan
 * @author Deniz
 * @since 1.6.0
 */
public class VpDownload {

	Context context;
	ConnectionDetector cd;
	String auth = "0da147a6-48cd-4a50-8b85-5f15b9e884e6";
	String url = "https://iphone.dsbcontrol.de/iPhoneService.svc/DSB/timetables/";
	String urlH, urlM, nameH, nameM, conH, conM;
	
	
	public VpDownload(Context context) {
		this.context = context;
		url = url+auth;
	}
	
	/**
	 * Ausgabe der Daten
	 * @return
	 */
	public String[] getContents() {
		String[] content = {nameH, conH, nameM, conM};
		return  content;
	}
	
	/**
	 * Delete files for new download
	 */
	public void delete() {
		File root = android.os.Environment.getExternalStorageDirectory();
		File dir = new File (root.getAbsolutePath() + "/jgg");
		File vp = new File(dir, "vp.json");
		File h = new File(dir, "heute.jgg");
		File m = new File(dir, "morgen.jgg");
		vp.delete();
		h.delete();
		m.delete();
	}
	
	/**
	 * Download vom VP
	 */
	public void work() {
		InputStream source;
		String content;
		
		/** Check if exists else donwload **/
		File root = android.os.Environment.getExternalStorageDirectory();
		File dir = new File (root.getAbsolutePath() + "/jgg");
		File file = new File(dir, "vp.json");
		if(file.exists()) {
			content = getFromFile(file.toString());
			source = new ByteArrayInputStream(content.getBytes());
		}else {
			saveToFile(url,"vp.json");
			content = getFromFile(file.toString());
			source = new ByteArrayInputStream(content.getBytes());
		}
		
		try {
			Gson gson = new Gson();        
			Reader reader = new InputStreamReader(source);  
			VpPost[] vpp = gson.fromJson(reader, VpPost[].class);

			if(vpp[0].getTimetableName().toLowerCase().contains("heute")) {
				nameH = vpp[0].getTimetableName();
				urlH = vpp[0].getTimetableurl();
				nameM = vpp[1].getTimetableName();
				urlM = vpp[1].getTimetableurl();
				
			}else if(vpp[1].getTimetableName().toLowerCase().contains("heute")) {
				nameH = vpp[1].getTimetableName();
				urlH = vpp[1].getTimetableurl();
				nameM = vpp[0].getTimetableName();
				urlM = vpp[0].getTimetableurl();
			}
			
			reader.close();
		}catch(Exception e) {
		}
		
		// Heute
		File hfile = new File(dir, "heute.jgg");
		if(hfile.exists()) {
			conH = getFromFile(hfile.toString());
		}else {
			saveToFile(urlH,"heute.jgg");
			conH = getFromFile(hfile.toString());
		}
		
		// Morgen
		File mfile = new File(dir, "morgen.jgg");
		if(mfile.exists()) {
			conM = getFromFile(mfile.toString());
		}else {
			saveToFile(urlM,"morgen.jgg");
			conM = getFromFile(mfile.toString());
		}
		
    }
    
	private InputStream retrieveStream(String url) {
		DefaultHttpClient client = new DefaultHttpClient(); 
		HttpGet getRequest = new HttpGet(url);
		try {
			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();
			if(statusCode != HttpStatus.SC_OK) { 
				Log.w("WPBA", "Error " + statusCode + " for URL " + url); 
				return null;
			}
			HttpEntity getResponseEntity = getResponse.getEntity();
			return getResponseEntity.getContent();
		}catch (IOException e) {
			getRequest.abort();
			Log.w("WPBA", "Error for URL " + url, e);
		}
		return null;
    }
	
	private void saveToFile(String uri, String file) {
		if(isCon()) {
			try {
				File root = android.os.Environment.getExternalStorageDirectory();
				File dir = new File (root.getAbsolutePath() + "/jgg");
				if(dir.exists() == false) {
					dir.mkdirs();
				}
				
				File dfile = new File(dir, file);
				InputStream is = retrieveStream(uri);
				BufferedInputStream bis = new BufferedInputStream(is);
				ByteArrayBuffer baf = new ByteArrayBuffer(5000);
				int current = 0;
				while ((current = bis.read()) != -1) {
					baf.append((byte) current);
				}
				
				FileOutputStream fos = new FileOutputStream(dfile);
				fos.write(baf.toByteArray());
				fos.flush();
				fos.close();
			}catch(IOException e) {
			}
		}else {
			cd.makeAlert();
		}
	}
	
	private String getFromFile(String file) {
		FileInputStream fis;
		String content = "";
		try {
			fis = new FileInputStream(new File(file));
			byte[] input = new byte[fis.available()];
			while (fis.read(input) != -1) {}
			content += new String(input, "ISO-8859-1");
			fis.close();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace(); 
		}	
		return content;
	}
	
	private Boolean isCon() {
		cd = new ConnectionDetector(context);
		return cd.isConnectingToInternet();
	}
		
}