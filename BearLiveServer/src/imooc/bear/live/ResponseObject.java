package imooc.bear.live;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class ResponseObject {

	public static final String CODE_SUCCESS = "1";
	public static final String CODE_FAIL = "0";

	private static Gson GsonInstance = new Gson();

	public String code;
	public String errCode;
	public String errMsg;
	public Object data;

	public void send(HttpServletResponse response) throws IOException {
		
		if(response == null){
			return;
		}
		
		response.setHeader("Content-type", "text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter writer = response.getWriter();
		writer.println(GsonInstance.toJson(this));
	}

	public static ResponseObject getSuccessResponse(Object data) {
		ResponseObject responseObject = new ResponseObject();
		responseObject.code = CODE_SUCCESS;
		responseObject.errCode = "";
		responseObject.errMsg = "";
		responseObject.data = data;
		return responseObject;
	}

	public static ResponseObject getFailResponse(String errCode, String errMsg) {
		ResponseObject responseObject = new ResponseObject();
		responseObject.code = CODE_FAIL;
		responseObject.errCode = errCode;
		responseObject.errMsg = errMsg;
		responseObject.data = null;
		return responseObject;
	}

}
