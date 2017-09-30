package servertest.dao;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import servertest.entity.UserFile;

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
	 * ����ļ���¼
	 * @param uf �ļ�
	 * @return
	 */
	public Serializable insertUserFile(UserFile uf){
		return hibernateTemplate.save(uf);
		
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
			UserFile uf = (UserFile) hibernateTemplate
					.find("from UserFile uf where uf.userId = " + userId
							+ " and uf.fileName = '" + fileName +"'").get(0);
			hibernateTemplate.delete(uf);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
