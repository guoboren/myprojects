package servertest.dao;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import servertest.entity.UserImg;
import servertest.entity.UserNetDiskFile;

/**
 * �ļ�dao
 * @author gbr
 *
 */
@Repository
public class FileDao {
	@Resource
	private HibernateTemplate hibernateTemplate;
	/**
	 * ���ͼƬ��¼
	 * @param uf �ļ�
	 * @return
	 */
	public Serializable insertUserImg(UserImg uf){
		return hibernateTemplate.save(uf);
		
	}
	/**
	 * ����ļ���¼
	 * @param uf �ļ�
	 * @return
	 */
	public Serializable insertUserFile(UserNetDiskFile udf){
		return hibernateTemplate.save(udf);
		
	}


	/**
	 * ɾ��ͼƬ
	 * 
	 * @param userId
	 *            �û�id
	 * @param fileName
	 *            �ļ���
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
	 * ɾ���ļ�
	 * 
	 * @param userId
	 *            �û�id
	 * @param fileName
	 *            �ļ���
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
