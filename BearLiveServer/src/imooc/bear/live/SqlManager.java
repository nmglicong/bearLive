package imooc.bear.live;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//获取数据库连接的类
public class SqlManager {

	private static final String dbPro = "jdbc:mysql://";
	private static final String host = "192.168.1.234";// ip地址
	private static final String port = "30112";// 端口号
	private static final String dbName = "5d4b8dd3c5be4";// 数据库名字
	private static final String charset = "?useUnicode=true&charactsetEncoding=utf-8";// 字符集

	private static final String url = dbPro + host + ":" + port + "/" + dbName
			+ charset;
	private static final String user = "b3c15e03e3df4";
	private static final String password = "d10ce1a25ae64";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

}
