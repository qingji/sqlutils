package im.qingji.sqlutil.convert.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;

import im.qingji.sqlutil.convert.ConvertException;

public class ClobConverter extends AbstractConverter {

	@Override
	public Object convert(Class<?> targetType, Object obj) throws ConvertException {
		if (targetType.equals(obj.getClass())) {
			return obj;
		}
		if (targetType == Clob.class) {
			return (Clob) obj;
		} else if (targetType == String.class) {
			return cast((Clob) obj);
		}
		throw unsupport(targetType, obj);
	}

	private static String cast(Clob clob) {
		if (clob == null) {
			return "";
		}
		try {
			StringBuilder content = new StringBuilder();
			Reader reader = clob.getCharacterStream();
			BufferedReader bufReader = new BufferedReader(reader);
			int tmp = reader.read();
			if (tmp != -1) {
				content.append((char) tmp);
			}
			while (bufReader.ready()) {
				content.append(bufReader.readLine());
			}
			return content.toString();
		} catch (SQLException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

}
