package imooc.bear.live.action;

import imooc.bear.live.SqlManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RoomListManager {

	private static RoomListManager manager;

	private Map<String, Long> roomMap = new HashMap<String, Long>();
	
	private ScheduledExecutorService service = Executors
				.newSingleThreadScheduledExecutor();
	
	private Runnable command = new Runnable() {
			@Override
			public void run() {
				// 10秒钟执行的任务
				System.out.println("RoomListManager 10秒钟执行的任务");
				for (String roomId : roomMap.keySet()) {
					long lastUpdateTime = roomMap.get(roomId);
					if (lastUpdateTime + 10 * 1000 > System.currentTimeMillis()) {
						// 说明这个房间是有效的.
						// 无需操作

						System.out.println(roomId + "RoomListManager 这个房间是有效的");
					} else {
						// 房间无效，需要从数据库中删除这个roomid
						System.out.println(roomId + "RoomListManager 房间无效，需要从数据库中删除这个roomid");
						deleteRoom(roomId);
						roomMap.remove(roomId);
						WatcherListManager.getInstance().removeRoom(roomId);
					}
				}
				
			}
		};
		
	private RoomListManager() {
		addExistRooms();
		startMangeTimer();
	}

	private void startMangeTimer() {
		System.out.println("RoomListManager 开始10秒钟执行的任务");
		service.scheduleWithFixedDelay(command , 0, 10, TimeUnit.SECONDS);
	}

	protected void deleteRoom(String roomId) {
		// 操作数据库
		Connection dbConnection = null;
		Statement stmt = null;

		try {
			dbConnection = SqlManager.getConnection();
			stmt = dbConnection.createStatement();
			// 数据库就已经完全建立起来了。

			String deleteRoomIdSql = "DELETE FROM `RoomInfo` WHERE `room_id`=\""
					+ roomId + "\"";
			stmt.execute(deleteRoomIdSql);
			int updateCount = stmt.getUpdateCount();// 获取受影响的行数
			System.out.println(roomId + "RoomListManager 删除影响行数：" + updateCount);
		} catch (SQLException e) {
			e.printStackTrace();
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

	public static RoomListManager getInstance() {
		if (manager == null) {
			synchronized (RoomListManager.class) {
				if (manager == null) {
					manager = new RoomListManager();
				}
			}
		}
		return manager;
	}

	private void addExistRooms() {
		// 操作数据库
		Connection dbConnection = null;
		Statement stmt = null;

		try {
			dbConnection = SqlManager.getConnection();
			stmt = dbConnection.createStatement();
			// 数据库就已经完全建立起来了。

			String getRoomIdSql = "SELECT `room_id` FROM `RoomInfo`";
			stmt.execute(getRoomIdSql);
			ResultSet resultSet = stmt.getResultSet();
			if (resultSet != null && !resultSet.wasNull()) {
				List<String> roomList = new ArrayList<String>();
				while (resultSet.next()) {
					String roomId = resultSet.getInt("room_id") + "";
					updateRoom(roomId);
				}
			} else {
				// 查询失败了
			}
		} catch (SQLException e) {
			e.printStackTrace();
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

	public void updateRoom(String roomId) {
		roomMap.put(roomId, System.currentTimeMillis());
	}

	public void removeRoom(String roomId) {
		roomMap.remove(roomId);
	}

}
