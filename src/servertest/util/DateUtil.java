package servertest.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ���ڹ�����
 * @author gbr
 *
 */
public class DateUtil {

	// Ĭ�����ڽ�����ʽ
	private final static String DEFAULT_PARSER = "yyyy-MM-dd";

	private DateUtil() {
	}

	/**
	 * ������ת��Ϊ�ַ���
	 * @param date ����
	 * @param parser ������ʽ
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
