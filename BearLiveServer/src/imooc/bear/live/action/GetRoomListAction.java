package imooc.bear.live.action;

import imooc.bear.live.Error;
import imooc.bear.live.ResponseObject;
import imooc.bear.live.RoomInfo;
import imooc.bear.live.SqlManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetRoomListAction extends IAction {

	private static final String RequestParamKey_PageIndex = "pageIndex";
	private static final int PageSize = 20;

	@Override
	public void doAction(HttpServletRequest req, HttpServletResponse response)
			throws IOException, SQLException {
		Connection dbConnection = null;
		Statement stmt = null;
		try {
			int pageIndex = getParam(req, RequestParamKey_PageIndex, 0);

			dbConnection = SqlManager.getConnection();
			stmt = dbConnection.createStatement();

			String queryRoomListSql = "SELECT * FROM `RoomInfo` LIMIT "
					+ pageIndex + " , " + PageSize;
			stmt.execute(queryRoomListSql);
			ResultSet resultSet = stmt.getResultSet();
			if (resultSet != null && !resultSet.wasNull()) {
				List<RoomInfo> roomList = new ArrayList<RoomInfo>();
			
				while (resultSet.next()) {
					RoomInfo roomInfo = new RoomInfo();
					roomInfo.roomId = resultSet.getInt("room_id");
					roomInfo.userId = resultSet.getString("user_id");
					roomInfo.userName = resultSet.getString("user_name");
					roomInfo.userAvatar = resultSet.getString("user_avatar");
					roomInfo.liveTitle = resultSet.getString("live_title");
					roomInfo.liveCover = resultSet.getString("live_poster");
					roomInfo.watcherNums = resultSet.getInt("watcher_nums");
					roomList.add(roomInfo);
				}
				ResponseObject responseObject = ResponseObject
						.getSuccessResponse(roomList);
				responseObject.send(response);
			} else {
				// ≤È—Ø ß∞‹¡À
				ResponseObject responseObject = ResponseObject.getFailResponse(
						Error.errorCode_QueryListFail, Error.getQueryListFailMsg());
				responseObject.send(response);
			}

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (dbConnection != null) {
					dbConnection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
}
