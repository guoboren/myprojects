package servertest.service;

import servertest.entity.DataResult;

/**
 * @author gbr
 *
 */
public interface UserService {
	/**
	 * 
	 * ��¼
	 * @param username �û���
	 * @param password ����
	 * @return
	 * @throws Exception
	 */
	public DataResult login(String username,String password) throws Exception;
	/**
	 * ����û����Ƿ��ظ�
	 * @param username �û���
	 * @return
	 */
	public DataResult checkDuplication(String username);
	
	/**
	 * ע��
	 * @param username �û���
	 * @param password ����
	 * @param phone �ֻ�
	 * @param gender �Ա�
	 * @param nickname �ǳ�
	 * @return
	 * @throws Exception
	 */
	public DataResult regist(String username,String password, String phone, String gender, String nickname) throws Exception;
	/**
	 * �޸�����
	 * @param userId �û�id
	 * @param oldpwd ԭ����
	 * @param newpwd ������
	 * @return
	 * @throws Exception 
	 */
	public DataResult modifypwd(int userId, String oldpwd,String newpwd) throws Exception;
	/**
	 * ��ӷ���
	 * @param userId �û�id
	 * @param groupName ����
	 * @return
	 */
	public DataResult addGroup(int userId, String groupName);
	/**
	 * ������ȡ�����б�
	 * @param userId �û�id
	 * @return
	 */
	public DataResult reloadGroup(int userId);
	/**
	 * �����û�
	 * @param condition ����
	 * @return
	 */
	public DataResult searchUser(String condition);
	/**
	 * ��ȡ����
	 * @param userId �û�id
	 * @param groupId ����id
	 * @return
	 */
	public DataResult loadFriend(int userId, int groupId);
	/**
	 * �鿴�û���Ϣ
	 * @param userId �û�id
	 * @return
	 */
	public DataResult getUserInfo(int userId);
	/**
	 * ɾ������
	 * @param userId �û�id
	 * @param groupId ����id
	 * @return
	 */
	public DataResult delGroup(int userId, int groupId);
	/**
	 * ��Ӻ���
	 * @param userId �û�id
	 * @param friendId ����id
	 * @param userUsername �û��û���
	 * @param friendUsername �����û���
	 * @param msg ��֤��Ϣ
	 * @return
	 */
	public DataResult sendAddMsg(int userId, int friendId, String userUsername, String friendUsername, String msg, int groupId);
	/**
	 * ��ȡ��Ϣ�б�
	 * @param userId �û�id
	 * @param type ��Ϣ����
	 * @return
	 */
	public DataResult getMsg(int userId, int type);
	/**
	 * ������Ϣ
	 * @param msgId ��Ϣid
	 * @param status ������
	 * @return
	 */
	public DataResult dealMsg(int msgId, int status, int groupId, int userId, int friendId, String myGroupId);
	/**
	 * ɾ������
	 * @param userId �û�id
	 * @param friendId ����id
	 * @return
	 */
	public DataResult delFriend(int userId, int friendId);
	/**
	 * ɾ����Ϣ
	 * @param msgId ��Ϣid
	 * @return
	 */
	public DataResult delMsg(int msgId);
	/**
	 * ����Ƿ�����Ӻ��ѻ��ѷ�������
	 * @param userId �û�id
	 * @param friendId ����id
	 * @return
	 */
	public DataResult checkFriend(int userId, int friendId);
	/**
	 * ����������Ϣ
	 * @param recieverId �û�id
	 * @param senderId ������id
	 * @param content ����
	 * @return
	 */
	public DataResult sendTalkMsg(int recieverId, int senderId, String content);
	/**
	 * ��ȡ������Ϣ
	 * @param recieverId ������id
	 * @param senderId ������id
	 * @return
	 */
	public DataResult loadTalkMsgs(int senderId, int recieverId);
	/**
	 * ��ȡ�ҵ�ͼƬ
	 * @param userId �û�id
	 * @return
	 */
	public DataResult getMyImgs(int userId);
	/**
	 * ��ȡ�ҵ������ļ�
	 * @param userId �û�id
	 * @return
	 */
	public DataResult getMyFiles(int userId);
}
