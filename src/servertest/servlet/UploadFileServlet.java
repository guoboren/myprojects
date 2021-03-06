package servertest.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import servertest.dao.FileDao;
import servertest.entity.DataResult;
import servertest.entity.UserImg;
import servertest.entity.UserNetDiskFile;

public class UploadFileServlet extends HttpServlet {
	
	private FileDao fileDao = new ClassPathXmlApplicationContext("applicationContext.xml").getBean(FileDao.class);
	
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		DataResult result = new DataResult();
		boolean uploadResult = uploadFiles(request);
		if(uploadResult){
			result.setStatus(0);
			JSONObject obj = JSONObject.fromObject(result);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.append(obj.toString());
			out.flush();
			out.close();
		}
	}
	
	
	/**
	 * �ϴ��ļ�
	 * @param request
	 * @return
	 */
	@Transactional
	private boolean uploadFiles(HttpServletRequest request){
		
		DiskFileItemFactory factory=new DiskFileItemFactory();
		ServletFileUpload upload=new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		try {
			String newName = null;
		    List<FileItem> items=upload.parseRequest(request);
		    for(FileItem item : items){
				UserNetDiskFile udf = new UserNetDiskFile();
		        if(!item.isFormField()){
		            String fileName = item.getName();
		            System.out.println(fileName);
		            String name = fileName.split("\\.")[0];
		            String extend = fileName.split("\\.")[1];
		            newName = name + "~~" + System.currentTimeMillis() + "." + extend;
		            item.write(new File(request.getServletContext().getInitParameter("netdiskFilePosition"), newName));
		            udf.setFileName(newName);
		            udf.setUserId(Integer.parseInt(item.getFieldName()));
		        }
		        udf.setId((Integer)fileDao.insertUserFile(udf));
		    }
		    return true;
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return false;
	}

	
	
	
	
}
