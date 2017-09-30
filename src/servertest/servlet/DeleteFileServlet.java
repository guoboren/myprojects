package servertest.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import net.sf.json.JSONObject;
import servertest.dao.FileDao;
import servertest.entity.DataResult;

public class DeleteFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private FileDao fileDao = new ClassPathXmlApplicationContext(
			"applicationContext.xml").getBean(FileDao.class);
	private ServletConfig config; // 文件存储位置放在filePosition中

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String fileName = request.getParameter("fileName");
		String userId = request.getParameter("userId");
		DataResult result = new DataResult();
		if (deleteFile(Integer.parseInt(userId), fileName)) {
			result.setStatus(0);
		} else {
			result.setStatus(1);
		}
		JSONObject jsonObj = JSONObject.fromObject(result);
		PrintWriter pw = response.getWriter();
		pw.write(jsonObj.toString());
		pw.flush();
		pw.close();
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 */
	@Transactional
	private boolean deleteFile(int userId, String fileName) {
		File file = new File(config.getInitParameter("filePosition") + "/"
				+ fileName);
		if (fileDao.deleteFileByUserIdAndFileName(userId, fileName)) {
			if (file.exists()) {
				file.delete();
				return true;
			}
		}
		return false;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		this.config = config;
	}
}
