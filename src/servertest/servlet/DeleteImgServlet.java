package servertest.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import net.sf.json.JSONObject;
import servertest.dao.FileDao;
import servertest.entity.DataResult;

public class DeleteImgServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private FileDao fileDao = new ClassPathXmlApplicationContext(
			"applicationContext.xml").getBean(FileDao.class);

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String fileName = request.getParameter("fileName");
		String userId = request.getParameter("userId");
		DataResult result = new DataResult();
		if (deleteFile(request, Integer.parseInt(userId), fileName)) {
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
	private boolean deleteFile(HttpServletRequest request, int userId, String fileName) {
		File file = new File(request.getServletContext().getInitParameter("filePosition") + "/"
				+ fileName);
		if (fileDao.deleteImgByUserIdAndFileName(userId, fileName)) {
			if (file.exists()) {
				file.delete();
				return true;
			}
		}
		return false;
	}

}
