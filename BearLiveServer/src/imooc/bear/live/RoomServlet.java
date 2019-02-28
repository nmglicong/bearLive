package imooc.bear.live;

import imooc.bear.live.action.CreateRoomAction;
import imooc.bear.live.action.GetRoomListAction;
import imooc.bear.live.action.GetWatchersAction;
import imooc.bear.live.action.HeartBeatAction;
import imooc.bear.live.action.JoinRoomAction;
import imooc.bear.live.action.QuitRoomAction;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RoomServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String RequestParamKey_Action = "action";
	private static final String RequestAction_Create = "create";
	private static final String RequestAction_Join = "join";
	private static final String RequestAction_Quit = "quit";
	private static final String RequestAction_GetList = "getList";
	private static final String RequestAction_GetWatcher = "getWatcher";
	private static final String RequestAction_HeartBeat = "heartBeat";

	// http://XXXX.com?action=create&userId=xxx&userAvatar=xxx&...
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// super.doGet(req, resp);
		// 处理用户的请求
		String action = req.getParameter(RequestParamKey_Action);
		if (action == null || "".equals(action)) {
			ResponseObject responseObject = ResponseObject.getFailResponse(
					Error.errorCode_NoAction, Error.getNoActionMsg());
			responseObject.send(resp);
			return;
		}
		try {
			if (RequestAction_Create.equals(action)) {
				// 创建一个直播房间。
				new CreateRoomAction().doAction(req, resp);
			} else if (RequestAction_Join.equals(action)) {
				// 加入一个直播房间。
				new JoinRoomAction().doAction(req, resp);
			} else if (RequestAction_Quit.equals(action)) {
				// 退出一个直播房间。
				new QuitRoomAction().doAction(req, resp);
			} else if (RequestAction_GetList.equals(action)) {
				// 获取直播房间列表。
				new GetRoomListAction().doAction(req, resp);
			} else if (RequestAction_GetWatcher.equals(action)) {
				// 获取房间中的观众
				new GetWatchersAction().doAction(req, resp);
			} else if (RequestAction_HeartBeat.equals(action)) {
				// 心跳包
				new HeartBeatAction().doAction(req, resp);
			} else {
				ResponseObject responseObject = ResponseObject.getFailResponse(
						Error.errorCode_NoRequestParam,
						Error.getNoRequestParamMsg(RequestParamKey_Action));
				responseObject.send(resp);
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			// 数据库异常，返回错误信息
			ResponseObject responseObject = ResponseObject.getFailResponse(
					Error.errorCode_Exception,
					Error.getExceptionMsg(e.getMessage()));
			try {
				responseObject.send(resp);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
