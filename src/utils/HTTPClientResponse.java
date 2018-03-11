package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//immutable response class
//-1, "", null represents an error response 
public class HTTPClientResponse {
	
	private final String body;
	private final int statusCode;
	private final Map<String, String> headers;
	private final String protocolVersion;
	
	public HTTPClientResponse(String response) throws IllegalArgumentException{
		System.out.println("response: " + response);
		try {
			String[] message = StringUtils.split(response, "\r\n\r\n");
			String[] headers = StringUtils.split(message[0], "\r\n");
			if (message.length > 1) {
				this.body = message[1];
			} else {
				this.body = "";
			}
			String[] specLine = StringUtils.split(headers[0], " ");
			this.statusCode = Integer.parseInt(specLine[1]);
			this.protocolVersion = specLine[0];
			this.headers = new HashMap<String,String>();
			for (int i = 1; i<headers.length; i++) {
				String[] header = StringUtils.split(headers[i], ": ");
				this.headers.put(header[0], header[1]);
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
	}
	
	public String getBody() {
		return this.body;
	}
	
	public int getStatusCode() {
		return this.statusCode;
	}
	
	public Map<String, String> getHeaders(){
		return this.headers;
	}
	
}
