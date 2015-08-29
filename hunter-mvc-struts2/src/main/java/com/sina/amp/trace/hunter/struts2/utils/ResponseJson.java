package com.sina.amp.trace.hunter.struts2.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

public class ResponseJson implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6713081128932905467L;
	private int status;
	private String info;
	private Map<String, Object> data;

	public ResponseJson() {
		this.data = new HashMap<String, Object>();
	}

	public void addData(String key, Object value) {
		if (key == null) {
			return;
		}
		if (this.data == null) {
			this.data = new HashMap<String, Object>();
		}
		this.data.put(key, value);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void setInfo(Errors errors) {
		if (errors.hasErrors()) {
			// 和前端约定，只获取第一个错误信息
			ObjectError objectError = errors.getAllErrors().get(0);
			info = objectError.getDefaultMessage();
		}

	}
}