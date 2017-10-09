package servertest.dao;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import servertest.entity.UserImg;
import servertest.entity.UserNetDiskFile;

/**
 * 文件dao
 * @author gbr
 *
 */
@Repository
public class FileDao {
	@Resource
	private HibernateTemplate hibernateTemplate;
	/**
	 * 添加图片记录
	 * @param uf 文件
	 * @return
	 */
	public Serializable insertUserImg(UserImg uf){
		return hibernateTemplate.save(uf);
		
	}
	/**
	 * 添加文件记录
	 * @param uf 文件
	 * @return
	 */
	public Serializable insertUserFile(UserNetDiskFile udf){
		return hibernateTemplate.save(udf);
		
	}


	/**
	 * 删除图片
	 * 
	 * @param userId
	 *            用户id
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public boolean deleteImgByUserIdAndFileName(int userId, String fileName) {
		try {
			UserImg uf = (UserImg) hibernateTemplate
					.find("from UserFile uf where uf.userId = " + userId
							+ " and uf.fileName = '" + fileName +"'").get(0);
			hibernateTemplate.delete(uf);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 删除文件
	 * 
	 * @param userId
	 *            用户id
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public boolean deleteFileByUserIdAndFileName(int userId, String fileName) {
		try {
			UserNetDiskFile udf = (UserNetDiskFile) hibernateTemplate
					.find("from UserNetDiskFile uf where uf.userId = " + userId
							+ " and uf.fileName = '" + fileName +"'").get(0);
			hibernateTemplate.delete(udf);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
