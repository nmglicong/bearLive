package imooc.bear.live.action;

import imooc.bear.live.Error;
import imooc.bear.live.ResponseObject;
import imooc.bear.live.SqlManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class QuitRoomAction extends IAction {

	private static final String RequestParamKey_UserId = "userId";
	private static final String RequestParamKey_RoomId = "roomId";

	@Override
	public void doAction(HttpServletRequest req, HttpServletResponse response)
			throws IOException, SQLException {
		Connection dbConnection = null;
		Statement stmt = null;
		try {
			String userIdParam = getParam(req, RequestParamKey_UserId, "");
			int roomIdParam = getParam(req, RequestParamKey_RoomId, -1);

			if (roomIdParam < 0) {
				ResponseObject responseObject = ResponseObject.getFailResponse(
						Error.errorCode_NoRequestParam,
						Error.getNoRequestParamMsg(RequestParamKey_RoomId
								+ "，或值<0"));
				responseObject.send(response);
				return;
			}

			dbConnection = SqlManager.getConnection();
			stmt = dbConnection.createStatement();
			// 数据库就已经完全建立起来了。

			String queryRoomIdSql = "SELECT `user_id`,`watcher_nums` FROM `RoomInfo` WHERE `room_id`=\""
					+ roomIdParam + "\"";
			stmt.execute(queryRoomIdSql);
			ResultSet resultSet = stmt.getResultSet();
			if (resultSet != null && !resultSet.wasNull()) {
				while (resultSet.next()) {
					int watchNums = resultSet.getInt("watcher_nums");
					String userId = resultSet.getString("user_id");
					if (userId != null && userId.equals(userIdParam)) {
						// 说明是主播退出
						createrQuit( userId, response);
						RoomListManager.getInstance().removeRoom(""+ roomIdParam);
						WatcherListManager.getInstance().removeRoom(""+ roomIdParam);
					} else {
						// 说明是观众退出
						watcherQuit(roomIdParam, response, watchNums);
						WatcherListManager.getInstance().removeWatcher("" +roomIdParam,userIdParam );
					}

				}
			} else {
				// 查询失败了，认为是退出成功。比如主播推出后，观众再退出时
				ResponseObject responseObject = ResponseObject
						.getSuccessResponse(null);
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

	private void watcherQuit( int roomIdParam,
			HttpServletResponse response, int watchNums) throws SQLException,
			IOException {

		int fianlWatchNums = (watchNums - 1);
		if (fianlWatchNums < 0) {
			fianlWatchNums = 0;
		}
		Connection dbConnection = null;
		Statement stmt = null;
		dbConnection = SqlManager.getConnection();
		stmt = dbConnection.createStatement();
		String updateWatcherNumsSql = "UPDATE `RoomInfo` SET `watcher_nums`=\""
				+ fianlWatchNums + "\" WHERE `room_id`=\"" + roomIdParam + "\"";
		stmt.execute(updateWatcherNumsSql);
		int updateCount = stmt.getUpdateCount();// 获取受影响的行数
		// 操作成功
		if (updateCount > 0) {
			ResponseObject responseObject = ResponseObject
					.getSuccessResponse(null);
			responseObject.send(response);
		} else {
			ResponseObject responseObject = ResponseObject.getFailResponse(
					Error.errorCode_QuitFail, Error.getQuitFailMsg());
			responseObject.send(response);
		}
	}

	public static void createrQuit( String userId,
			HttpServletResponse response) throws SQLException, IOException {
		Connection dbConnection = null;
		Statement stmt = null;
		dbConnection = SqlManager.getConnection();
		stmt = dbConnection.createStatement();
		String deleteRoomIdSql = "DELETE FROM `RoomInfo` WHERE `user_id`=\""
				+ userId + "\"";
		stmt.execute(deleteRoomIdSql);
		int updateCount = stmt.getUpdateCount();// 获取受影响的行数
		// 操作成功
		if (updateCount > 0) {
			ResponseObject responseObject = ResponseObject
					.getSuccessResponse(null);
			responseObject.send(response);
		} else {
			ResponseObject responseObject = ResponseObject.getFailResponse(
					Error.errorCode_QuitFail, Error.getQuitFailMsg());
			responseObject.send(response);
		}
	}
}
