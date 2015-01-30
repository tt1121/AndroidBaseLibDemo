package com.example.baselibdemo.httpconnect;

import org.codehaus.jackson.annotate.JsonProperty;

public class PiaoVersionObject {

	public String desc;
	
	public String version;
	
	@JsonProperty("wap Url")
	public String url;
	
	public String minorVer;
	
	public String inver;
	
	public String serverSysTime;
	
	
}
