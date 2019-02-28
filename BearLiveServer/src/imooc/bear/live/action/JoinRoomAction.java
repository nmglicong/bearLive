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

public class JoinRoomAction extends IAction {

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
						// 说明是主播加入，不做操作。按道理不应该走这个
					} else {
						// 说明是观众加入
						watcherJoin(stmt, roomIdParam, response, watchNums);
					}

				}
			} else {
				// 查询失败了，也认为是添加成功。
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

	private void watcherJoin(Statement stmt, int roomIdParam,
			HttpServletResponse response, int watchNums) throws SQLException,
			IOException {

		int fianlWatchNums = (watchNums + 1);

		String updateWatcherNumsSql = "UPDATE `RoomInfo` SET `watcher_nums`=\""
				+ fianlWatchNums + "\" WHERE `room_id`=\"" + roomIdParam + "\"";
		stmt.execute(updateWatcherNumsSql);

		// 成功不成功都认为成功了。
		ResponseObject responseObject = ResponseObject.getSuccessResponse(null);
		responseObject.send(response);

	}

}
