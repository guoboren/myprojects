package servertest.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * @author gbr
 *
 */
public class DateUtil {

	// 默认日期解析格式
	private final static String DEFAULT_PARSER = "yyyy-MM-dd";

	private DateUtil() {
	}

	/**
	 * 将日期转换为字符串
	 * @param date 日期
	 * @param parser 解析格式
	 * @return
	 */
	public static String getDateToString(Date date, String parser) {
		SimpleDateFormat sdf = null;
		if (parser == null) {
			sdf = new SimpleDateFormat(DEFAULT_PARSER);
		} else {
			sdf = new SimpleDateFormat(parser);
		}
		return sdf.format(date);
	}
}
