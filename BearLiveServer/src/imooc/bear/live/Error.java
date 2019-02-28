package imooc.bear.live;

public class Error {
	public static final String errorCode_NoAction = "404";
	private static final String errorMsg_NoAction = "没有Action参数";

	public static String getNoActionMsg() {
		return errorMsg_NoAction;
	}

	public static final String errorCode_NoRequestParam = "405";
	private static final String errorMsg_NoRequestParam = "缺少必要参数";

	public static String getNoRequestParamMsg(String requestParam) {
		return errorMsg_NoRequestParam + ":" + requestParam;
	}

	public static final String errorCode_Exception = "500";
	private static final String errorMsg_Exception = "服务器异常";

	public static String getExceptionMsg(String e) {
		return errorMsg_Exception + ":" + e;
	}

	// 以上是通用的error

	// 以下是特定action的error,600,610,620,630
	public static final String errorCode_CreateFail = "600";
	private static final String errorMsg_CreateFail = "创建直播房间失败";

	public static String getCreateFailMsg() {
		return errorMsg_CreateFail;
	}

	public static final String errorCode_QueryFail = "601";
	private static final String errorMsg_QueryFail = "获取直播房间失败";

	public static String getQueryFailMsg() {
		return errorMsg_QueryFail;
	}

	public static final String errorCode_QueryListFail = "602";
	private static final String errorMsg_QueryListFail = "获取直播房间列表失败";

	public static String getQueryListFailMsg() {
		return errorMsg_QueryListFail;
	}
	
	public static final String errorCode_QuitFail = "603";
	private static final String errorMsg_QuitFail = "退出直播房间失败";

	public static String getQuitFailMsg() {
		return errorMsg_QuitFail;
	}

}
