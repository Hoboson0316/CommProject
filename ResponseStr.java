package com.foresee.sx.sjyy.utils;




import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/** 
 * 
 * @author   作者 :   hanbaoshan
 * @date     创建时间：  2017年4月21日 下午2:41:27 
 * @version  版本：          1.0 
 * @function 功能：          返回给前台信息组装
 */
public class ResponseStr {
	
	public Logger log = Logger.getLogger(ResponseStr.class);

	private String code;
	private String message;
	private Object content=new Object();

	public String getCode() {
		return code;
	}

	public ResponseStr(String code, String message, Object content) {
		super();
		this.code = code;
		this.message = message;
		this.content = content;
	}

	public ResponseStr(String code, String message) {
		super();
		this.code = code;
		this.message = message;
		this.content = new JSONObject();
	}

	public ResponseStr() {
		super();
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	@Override
	public String toString() {
		JSONObject result = new JSONObject();
		JSONObject status = new JSONObject();
		status.put("code", this.getCode());
		status.put("message", this.getMessage());
		result.put("data", this.getContent());
		result.put("status", status);
		log.debug("返回页面数据----"+JSON.toJSONString(result));
		return JSON.toJSONString(result);
	}
}
