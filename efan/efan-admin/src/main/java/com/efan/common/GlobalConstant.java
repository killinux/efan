package com.efan.common;



public final class GlobalConstant {
	
	/** 空格 */
	public static final String BLANK = " ";
	
	/** session 里属性的 key 值 */
	public static final class SessionKey {
		/** 登录用户信息在 session 里的 key 值 */
		public static final String LOGIN_USER = "login_user";
		/** 登录用户信所拥有的权限 */
		public static final String LOGIN_USER_AUTHS = "login_user_auths";
		/** 记录当前环境是不是公网 */
		public static final String ENV_IS_PUB = "env_is_pub";
		/** 记录防止二次提交的 token */
		public static final String AVOID_DUP_SUBMIT_TOKENS = "avoidDupSubmitTokens";
	}
	
	/** 自定义的 response header */
	public static final class CustomResponseHeader {
		/** 纳米系统的状态码，等同于 DWZ 系统的状态码 {@link AjaxResponseStatusCode} */
		public static final String NAMI_STATUS_CODE = "nami_status_code";
	}	
	
	/** Ajax 返回的状态码 */
	public static final class AjaxResponseStatusCode {
		/** 成功 */
		public static final String SUCCESS = "200";
		/** 失败 */
		public static final String FAIL = "300";
		/** 登录失效 */
		public static final String TIMEOUT = "301";
	}
	
	/** 排序方向 */
	public static final class OrderDirection {
		/** 升序 */
		public static final String ASC = "asc";
		/** 降序 */
		public static final String DESC = "desc";
	}
	
	/**用户权限*/
	public static final class User {
		public static final class Auth {
			/** 管理员*/
			public static final int ADMIN = 1;
			/** 普通用户*/
			public static final int NORMAL = 2;
		}
	}
	
	/**应用状态*/
	public static final class App {
		public static final class Status {
			/** 待处理*/
			public static final String WAIT_TO_PROCESSING = "W";
			/** 待发布*/
			public static final String TO_PUBLISH = "T";
			 /** 发布*/
			public static final String PUBLISH = "P";
			/** 下架*/
			public static final String NO_PUBLISHED = "N";
		}
	}
	
}
