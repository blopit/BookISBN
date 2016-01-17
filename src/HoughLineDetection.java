import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;



public class HoughLineDetection {

	static String privKey = "W2FXjVJAAn1fB*wSEaTGCkAo*QLmeQ+r*KGDEKcL";
	static String pubKey = "^LJ9FdJYJsZRe^4L6JPh";
	
	public String getISBN( String fname ){
		
		/*JFrame f = new JFrame();
		//f.add(new JFileChooser());
		JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		*/
		String img_proc = "gaussian45.jpg";
		try {
	        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	        
	        /*ArrayList<String> file = new ArrayList<String>(
	        		"");
	        
	        JFileChooser fileChooser = new JFileChooser();
	        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
	        int result = fileChooser.showOpenDialog(this);
	        if (result == JFileChooser.APPROVE_OPTION) {
	            File selectedFile = fileChooser.getSelectedFile();
	            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
	        }*/
	        
	        Mat src = Highgui.imread(fname,
	        Highgui.CV_LOAD_IMAGE_COLOR);
	        Mat source = src.clone();
	        
	        //Imgproc.cvtColor(source, source, Imgproc.COLOR_BGR2GRAY);
	        Imgproc.GaussianBlur(source, source, new Size(5,5), 0);
	        Imgproc.Canny(source, source, 10, 175);
	        
	         Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(7, 7));
	         Imgproc.morphologyEx(source, source, Imgproc.MORPH_CLOSE, kernel);
	        
	        //Mat destination = new Mat(source.rows(),source.cols(),source.type());
	         Imgproc.erode(source, kernel, kernel);
	        
	         Highgui.imwrite("delta.jpg", source);
	         
	         ArrayList<MatOfPoint> contour = new ArrayList<MatOfPoint>();
	         
	         Imgproc.findContours(source, contour, kernel, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
	        
	        ArrayList<MatOfPoint> app = new ArrayList<MatOfPoint>();
	        for (MatOfPoint c : contour){
	        	MatOfPoint2f c2f = new MatOfPoint2f(c.toArray());
	        	MatOfPoint2f approx = new MatOfPoint2f();
	        	MatOfPoint approxf1 = new MatOfPoint();
	        	
	        	Imgproc.approxPolyDP(c2f, approx, Imgproc.arcLength(c2f, true) * 0.02, true);
	        	
	        	Imgproc.drawContours(src, contour, 0, new Scalar(0, 255, 0));
        		Highgui.imwrite("gamma.jpg", source);
        		
	        	if (approx.total() == 4){
	        		
	        		
	        		
	        		double[] temp_double;
        		   temp_double = approx.get(0,0);       
        		   Point p1 = new Point(temp_double[0], temp_double[1]);
        		   temp_double = approx.get(1,0);       
        		   Point p2 = new Point(temp_double[0], temp_double[1]);
        		   temp_double = approx.get(2,0);       
        		   Point p3 = new Point(temp_double[0], temp_double[1]);
        		   temp_double = approx.get(3,0);       
        		   Point p4 = new Point(temp_double[0], temp_double[1]);
        		   List<Point> sr = new ArrayList<Point>();
        		   sr.add(p1);
        		   sr.add(p2);
        		   sr.add(p3);
        		   sr.add(p4);
        		   Mat startM = Converters.vector_Point2f_to_Mat(sr);
        		   src=warp(src,startM);
	        		  
	        		//src = warpPerspective(src, spp, 250, 350);
	        		break;
	        	}
	        	
	        }
	        
	        
	        
	        Highgui.imwrite(img_proc, src);
	        
	        //img_proc = "exampleX.jpg";
	        
	        int time = (int) (System.currentTimeMillis() / 1000L);
	        String img_url = img_proc;
	        		//"http://www.openlettersmonthly.com/issue/wp-content/uploads/2013/03/bn-dune.jpg";
	        //img_url = URLEncoder.encode(img_url);
	        int limit = 30;
	        String nonce = UUID.randomUUID().toString();
	        String boundary = "---------------------------263081694432439";
	        
	        /*String request = "http://api.tineye.com/rest/search/?"
	        		+"api_key=" + pubKey 
	        		+ "&offset=0" 
	        		+"&limit=" + limit
	        		+"&image_upload=" + img_url 
	        		+"&date=" + time
	        		+"&nonce=" + nonce;*/
	        
	        /*String sign = privKey +
	        		"POSTmultipart/form-data; boundary=d8b4f160da95---------------d8b4f160da95" +
	        		time +
	        		nonce +
	        		"http://api.tineye.com/rest/search/" +
	        		"image_url=" + img_url +
	        		"&limit=" + limit +
	        		"&offset=0";*/
	        
	        String sign = privKey +
	        		"POSTmultipart/form-data; boundary="+ boundary +
	        		URLEncoder.encode(img_url) +
	        		time +
	        		nonce +
	        		"http://api.tineye.com/rest/search/" +
	        		//"image_url=" + img_url +
	        		"limit=" + limit +
	        		"&offset=0";
	        
	        //sign = "vibaHBXwUXFqVSg-+kTrqYJZEJkbVeqLc=bo.LlXPOSTmultipart/form-data; boundary=d8b4f160da95---------------d8b4f160da95tineye+logo%281%29.png1350511031wAqXrSG7mJPn5YA6cwDalG.Shttp://api.tineye.com/rest/search/limit=30&offset=0";
	        //privKey = "vibaHBXwUXFqVSg-+kTrqYJZEJkbVeqLc=bo.LlX";
	        
	        String hmac = HMAC_MD5_encode(privKey, sign);
	        
	        //System.out.println(privKey);
	        //System.out.println(sign);
	        //System.out.println(hmac);
	        
	        		try{
	        			HttpClient myHttpClient = new DefaultHttpClient();
	        			MultipartEntityBuilder entitybuilder = MultipartEntityBuilder
	        	        		 .create();
	        			
	        			entitybuilder
	        					.addBinaryBody("image_upload", new File(img_proc),ContentType.APPLICATION_OCTET_STREAM, img_proc)
	        					.addTextBody("api_key", pubKey )
	        					.addTextBody("date", Integer.toString(time))
	        					.addTextBody("limit", Integer.toString(limit))
	        	        		 .addTextBody("offset","0")
	        	        		 .addTextBody("nonce", nonce)
	        	        		 .addTextBody("api_sig", hmac);
	        	        		 //.build();
	        			//entitybuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
	        			entitybuilder.setBoundary(boundary);
	        			
	        			HttpEntity entity = entitybuilder.build();
	        			
	        	        		HttpPost post = new HttpPost("http://api.tineye.com/rest/search/");
	        	        		//post.addHeader("Content-Disposition", "form-data");
	        	        		post.addHeader("Content-Type", ContentType.MULTIPART_FORM_DATA.getMimeType()+"; boundary="+boundary);
	        	        		post.setEntity(entity);
	        	        		
	        	        		
	        	        		
	        	        		HttpResponse httpResponse = myHttpClient.execute(post);
	        	        		
	        	        		System.out.println(post.toString());
	        	        		
	        		    HttpEntity httpEntity = httpResponse.getEntity();
	        		    is = httpEntity.getContent();
	        		    if(is == null){
	        		    	System.out.println("blec");
	        			//Log.d("IS NULL" , "in jsonparser");
	        		    }
	        		} catch (UnsupportedEncodingException e) {
	        	            e.printStackTrace();
	        	        } catch (ClientProtocolException e) {
	        	            e.printStackTrace();
	        	        } catch (IOException e) {
	        	            e.printStackTrace();
	        	        }
	        		
	        		try{
	        		    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
	        		    StringBuilder sb = new StringBuilder();
	        		    String line = null;
	        		    while((line = reader.readLine()) != null){
	        			//Log.d("Line is: ", line);
	        			sb.append(line + "\n");
	        		    }
	        		    is.close();
	        		    json = sb.toString();
	        		} catch (Exception e){
	        		    //Log.e("Buffer Error", "Error parsing data");
	        		}
	        		
	        		try{
	        		    jObj = new JSONObject(json);
	        		} catch (JSONException e){
	        		    e.printStackTrace();
	        		}
	        		
	        
	        
	        //System.exit(0);
	        JSONObject data = jObj;
	        System.out.println(data.toString());
	        
	        String info = "";
	        Boolean flag = false;
	        ArrayList<String> urls = new ArrayList<String>();
	        JSONArray jarray = data.getJSONObject("results").getJSONArray("matches");
	        for(Object job : jarray){
	        	JSONObject j = (JSONObject) ((JSONObject) job).getJSONArray("backlinks").get(0);
	        	String toCheck = j.getString("backlink");
	        	
	        	System.out.println(toCheck);
	        	
	        	if (toCheck.contains("amazon.")){
	        		System.out.println(">> " + toCheck);
	        		flag = true;
	        		break;
	        	} else if (toCheck.contains("barnesandnoble.")){
	        		System.out.println(">> " + toCheck);
	        		Document doc = Jsoup.connect(toCheck).get();
	        		Element elem = doc.getElementById("additionalProductInfo");
	        		info = elem.getElementsByTag("dl").text();
	        		
	        		System.out.println(info);
	        		
	        		String pattern = "ISBN-[0-9]{2}:\\s*([0-9\\-]*)";

	    	        Pattern r = Pattern.compile(pattern);
	    	        Matcher m = r.matcher(info);
	    	        if (m.find( )) {
	    	        	try{
	    		        		System.out.println("Found value: " + m.group(1) );
	    		        		return m.group(1);
	    	        	} catch (Exception e) {
	    		    	        System.out.println("Error: " + e.getMessage());
	    	   	     	}
	    	        } else {
	    	           System.out.println("NO MATCH");
	    	        }
	        		flag = true;
	        		break;
	        	}
	        	
	        	urls.add(toCheck);
	        	/*if (toCheck.contains("barnesandnoble.")){
	        		System.out.println(">> " + toCheck);
	        		continue;
	        	}
	        	
	        	if (toCheck.contains("product") || toCheck.contains("store")){
	        		System.out.println(">> " + toCheck);
	        	}*/
	        	
	        }
	        
	        if (!flag){
	        	for (String ur : urls){
	        		try{
	        		Document doc = Jsoup.connect(ur).get();
	        		info = doc.text();
	        		
			        String pattern = "ISBN.{0,50}?([0-9]{8,})";
		
			        Pattern r = Pattern.compile(pattern);
			        Matcher m = r.matcher(info);
			        if (m.find( )) {
			        	try{
				        		System.out.println("Found value: " + m.group(1) );
				        		return m.group(1);
			        	} catch (Exception e) {
				    	        System.out.println("Error: " + e.getMessage());
			   	     	}
			        } else {
			           System.out.println("NO MATCH");
			        }
	        		} catch (Exception e) {
		    	        System.out.println("Error: " + e.getMessage());
	   	     	}
	        	}
	        }
	        /*if (!primeUrl.equals("")){
	        	
	        }*/
	        
	        //System.out.println(data.toString());

	     } catch (Exception e) {
	        System.out.println("Error: " + e.getMessage());
	     }
		
		return "none";
	}
	
	public static String HMAC_MD5_encode(String key, String message) throws Exception {

        SecretKeySpec keySpec = new SecretKeySpec(
                key.getBytes(),
                "HmacSHA1");

        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(keySpec);
        byte[] rawHmac = mac.doFinal(message.getBytes());

        return Hex.encodeHexString(rawHmac);
    }
	
	
	public static Mat warp(Mat inputMat,Mat startM) {
        int resultWidth = 300;
        int resultHeight = 450;

        Mat outputMat = new Mat(resultWidth, resultHeight, CvType.CV_8UC4);

        Point ocvPOut1 = new Point(0, 0);
        Point ocvPOut2 = new Point(0, resultHeight);
        Point ocvPOut3 = new Point(resultWidth, resultHeight);
        Point ocvPOut4 = new Point(resultWidth, 0);
        List<Point> dest = new ArrayList<Point>();
        dest.add(ocvPOut1);
        dest.add(ocvPOut2);
        dest.add(ocvPOut3);
        dest.add(ocvPOut4);
        Mat endM = Converters.vector_Point2f_to_Mat(dest);      

        Mat perspectiveTransform = Imgproc.getPerspectiveTransform(startM, endM);

        Imgproc.warpPerspective(inputMat, 
                                outputMat,
                                perspectiveTransform,
                                new Size(resultWidth, resultHeight), 
                                Imgproc.INTER_CUBIC);

        return outputMat;
    }
	
	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
	
	public static JSONObject getJSON(String URL){
		
		try{
		    HttpClient httpClient = new DefaultHttpClient();
		    HttpGet httpGet = new HttpGet(URL);
		    
		    HttpResponse httpResponse = httpClient.execute(httpGet);
		    HttpEntity httpEntity = httpResponse.getEntity();
		    is = httpEntity.getContent();
		    if(is == null){
			//Log.d("IS NULL" , "in jsonparser");
		    }
		} catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		
		try{
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
		    StringBuilder sb = new StringBuilder();
		    String line = null;
		    while((line = reader.readLine()) != null){
			//Log.d("Line is: ", line);
			sb.append(line + "\n");
		    }
		    is.close();
		    json = sb.toString();
		} catch (Exception e){
		    //Log.e("Buffer Error", "Error parsing data");
		}
		
		try{
		    jObj = new JSONObject(json);
		} catch (JSONException e){
		    e.printStackTrace();
		}
		
		return jObj;
	    }

}
