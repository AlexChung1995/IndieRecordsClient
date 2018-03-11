package utils;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.net.URI;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public final class HTTPClientRequest {
	
	private String url;
	private int port;
	private String method;
	private HashMap<String,String> headers;
	private HashMap<String,String> body;
	
	private CookieManager cookies = null;
	
	public HTTPClientRequest() {
		this.cookies = new CookieManager();
	}
	
	public HTTPClientResponse sendRequest(String url, int port, String method, HashMap<String,String> headers, HashMap<String,String> body, 
			boolean cookies, int readOutMilli, int connectOutMilli) throws UnknownHostException, IOException {
		String[] hostAndPath = StringUtils.split(url, "/", 1);
		Socket host = new Socket(hostAndPath[0],port);
		String path = "/";
		String request = "";
		String bodyString = "";
		if (method == "POST" || method == "PUT") {
			request += "\r\n";
			for (Entry<String,String> entry: body.entrySet()) {
				bodyString += entry.getKey()+"="+entry.getValue()+"&";
			}
		}
		if (hostAndPath.length > 1) {
			path += hostAndPath[1];
		}
		request += method + " " + path + " HTTP/1.1\r\n";
		System.out.println("command spec: " + request);
		request += "Host: " + hostAndPath[0] + "\r\n";
		request += "Content-Length: " + bodyString.length() + "\r\n";
		for (Entry<String,String> entry: headers.entrySet()) {
			request += entry.getKey() + ": " + entry.getValue() + "\r\n";
		}
		
		PrintWriter out = new PrintWriter(host.getOutputStream(), true);
		out.println(request);
		out.flush();
		byte[] line = new byte[1024];
		host.getInputStream().read(line);
		String response = StringUtils.stringify(line,1);
		while (host.getInputStream().available() > 0) {
			line = new byte[1024];
			host.getInputStream().read(line);
			response += StringUtils.stringify(line, 1);
			System.out.println(response);	
		}
		return new HTTPClientResponse(response);
	}
	
	
	
	public String headersToString(HashMap<String,String> headers) {
		return "";
	}
	
	public String formToXML(HashMap<String,String> body) {
		return "";
	}
	
}
