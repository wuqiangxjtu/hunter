package com.sina.amp.trace.hunter.struts2.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AbsJsonObject implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/** 系统级异常 */
    public static final int JSON_OPERATE_SUCCESS = 0;
    public static final int JSON_OPERATE_FAILED = 1;
    
    /** 业务异常 */
    public static final int JSON_OPERATE_FAILED_BUSINESS = 2;
    
    /** 权限 */
    public static final int JSON_OPERATE_FAILED_NOAUTH = 126;
    
    /** 未登陆*/
    public static final int JSON_OPERATE_FAILED_NOTLOGIN = 127;
    
    /**用户未绑定*/
    public static final int JSON_OPERATE_FAILED_NOTBIND = 128;
    
    public static final String MESSAGE_GLOBAL = "global";
    public static final String MESSAGE_FIELD = "field";
    
	private int status;
	
	/**
	 * 返回结果的附加错误信息
	 */
	private Map<String,  Object> statusInfo = null;
	
	/**
	 * 字段错误信息
	 */
	private Map<String, String> fieldMsgs = null;
	

	/**
	 * 全局错误信息
	 */
	private String globalMsg = null;

	/**
	 * 返回的具体数据
	 */
	private Map<String, Object> data;
	
	public AbsJsonObject() {
		this.status = JSON_OPERATE_SUCCESS;
		this.statusInfo = new HashMap<String, Object>();
		this.fieldMsgs = new HashMap<String, String>();
		this.data = new HashMap<String, Object>();
	}
	
	/**
	 * 添加数据
	 * @param key
	 * @param value
	 */
	public void addData(String key, Object value){
        if(key == null){
            return ;
        }
        if(this.data ==null){
            this.data = new HashMap<String, Object>();
        }
        this.data.put(key, value);
    }
    
    public Map<String, Object> getData() {
        return data;
    }
    
    public void setData(Map<String, Object> data) {
        this.data = data;
    }

//  status info	
	/**
	 * 把错误信息添加到statusInfo中
	 */
	public Map<String, Object> getStatusInfo() {
		// 若存在全局错误信息，添加全局错误信息后直接返回
		if (this.globalMsg != null) {
			statusInfo.put(MESSAGE_GLOBAL, this.globalMsg);
		} else if (this.fieldMsgs.size() > 0) {
			statusInfo.put(MESSAGE_FIELD, this.fieldMsgs);
		}	
		return statusInfo;
	}
	
	
	public void setStatusInfo(Map<String, Object> statusInfo) {
		this.statusInfo = statusInfo;
	}
	
	
	/**
	 * 添加
	 * @param global
	 */
	public void addGlobalStatusInfo(String global) {
		this.globalMsg = global;
	}
	
	public void addFieldStatusInfo(String field, String message) {
		this.fieldMsgs.put(field, message);
	}
	

	

// status	
	public int getStatus() {
		return status;
	}

	
	public void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * 设置status为成功
	 */
	public void setSuccess() {
		this.status = JSON_OPERATE_SUCCESS;
	}
	
	/**
	 * 设置status系统级异常
	 */
	public void setFail() {
		this.status = JSON_OPERATE_FAILED; 
	}
	
	/**
	 * 设置status为业务异常
	 */
	public void setBusinessFail() {
		this.status = JSON_OPERATE_FAILED_BUSINESS;
	}
	
	/**
	 * 
	 */
	public void setNoAuth() {
		this.status = JSON_OPERATE_FAILED_NOAUTH;
	}
	
	public void setNoBind() {
		this.status = JSON_OPERATE_FAILED_NOTBIND;
	}
	
	public void setNotLogin() {
		this.status = JSON_OPERATE_FAILED_NOTLOGIN;
	}
	
	
	
//	field msg method
//	public Map<String, String> getFieldMsgs() {
//		return fieldMsgs;
//	}
//
//	public void setFieldMsgs(Map<String, String> fieldMsgs) {
//		this.fieldMsgs = fieldMsgs;
//	}
//	
//	public void addFieldMsg(String key, String value) {
//		if (key == null) {
//			return;
//		}
//		this.fieldMsgs.put(key, value);
//	}
	
//	global msg method
//	public String getGlobalMsg() {
//		return globalMsg;
//	}
//
//	public void setGlobalMsg(String globalMsg) {
//		this.globalMsg = globalMsg;
//	}
	
}
