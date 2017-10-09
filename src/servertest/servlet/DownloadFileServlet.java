package servertest.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * �����ļ�
 * @author gbr
 *
 */
public class DownloadFileServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//�����ļ�
		downloadFile(request, response);
	}
	/**
	 * �����ļ�
	 * @param request
	 * @param response
	 */
	private void downloadFile(HttpServletRequest request, HttpServletResponse response){
		try {
			request.setCharacterEncoding("UTF-8");
			String fileName = request.getParameter("fileName");
			String path = request.getServletContext().getInitParameter("netdiskFilePosition") + File.separator + fileName;
			File file = new File(path);
			try {
				InputStream fis = new BufferedInputStream(new FileInputStream(file));
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
				fis.close();
				response.reset();
				response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName));
				response.addHeader("Content-Length", file.length() + "");
				OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
				//��ʼ����
				toClient.write(buffer);
				toClient.flush();
				toClient.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	}

}
