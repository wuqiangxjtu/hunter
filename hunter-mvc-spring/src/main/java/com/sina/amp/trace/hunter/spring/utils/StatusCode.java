package com.sina.amp.trace.hunter.spring.utils;

public class StatusCode {
	
	/** 操作成功 */
	public static final int OPERATE_SUCCESS = 0;
	
	/** 操作失败 */
	public static final int OPERATE_FAILED = 1;
	
	/** 业务异常 */
	public static final int OPERATE_FAILED_BUSINESS = 2;
	
	/** 权限异常 */
	public static final int OPERATE_FAILED_NOAUTH = 126;
	
	/** 未登陆*/
	public static final int OPERATE_FAILED_NOTLOGIN = 127;
	
	/** 用户未绑定 */
	public static final int OPERATE_FAILED_NOTBIND = 128;
	
	/** session 失效 */
	public static final int SESSION_EXPIRED = 129;
	
	/** 请求重定向 */
	public static final int REQUEST_REDIRECT = 302;
	
	/** 参数错误 */
	public static final int PARAM_ERROR = 400;
	
	/** 未找到 */
	public static final int NOT_FOUND = 404;
	
	/** 系统错误 */
	public static final int SYSTEM_ERROR = 500;
}