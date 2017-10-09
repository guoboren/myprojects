package servertest.service;

import servertest.entity.DataResult;

/**
 * @author gbr
 *
 */
public interface UserService {
	/**
	 * 
	 * 登录
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 * @throws Exception
	 */
	public DataResult login(String username,String password) throws Exception;
	/**
	 * 检查用户名是否重复
	 * @param username 用户名
	 * @return
	 */
	public DataResult checkDuplication(String username);
	
	/**
	 * 注册
	 * @param username 用户名
	 * @param password 密码
	 * @param phone 手机
	 * @param gender 性别
	 * @param nickname 昵称
	 * @return
	 * @throws Exception
	 */
	public DataResult regist(String username,String password, String phone, String gender, String nickname) throws Exception;
	/**
	 * 修改密码
	 * @param userId 用户id
	 * @param oldpwd 原密码
	 * @param newpwd 新密码
	 * @return
	 * @throws Exception 
	 */
	public DataResult modifypwd(int userId, String oldpwd,String newpwd) throws Exception;
	/**
	 * 添加分组
	 * @param userId 用户id
	 * @param groupName 组名
	 * @return
	 */
	public DataResult addGroup(int userId, String groupName);
	/**
	 * 重新拉取分组列表
	 * @param userId 用户id
	 * @return
	 */
	public DataResult reloadGroup(int userId);
	/**
	 * 搜索用户
	 * @param condition 条件
	 * @return
	 */
	public DataResult searchUser(String condition);
	/**
	 * 获取好友
	 * @param userId 用户id
	 * @param groupId 分组id
	 * @return
	 */
	public DataResult loadFriend(int userId, int groupId);
	/**
	 * 查看用户信息
	 * @param userId 用户id
	 * @return
	 */
	public DataResult getUserInfo(int userId);
	/**
	 * 删除分组
	 * @param userId 用户id
	 * @param groupId 分组id
	 * @return
	 */
	public DataResult delGroup(int userId, int groupId);
	/**
	 * 添加好友
	 * @param userId 用户id
	 * @param friendId 好友id
	 * @param userUsername 用户用户名
	 * @param friendUsername 好友用户名
	 * @param msg 验证消息
	 * @return
	 */
	public DataResult sendAddMsg(int userId, int friendId, String userUsername, String friendUsername, String msg, int groupId);
	/**
	 * 获取消息列表
	 * @param userId 用户id
	 * @param type 消息类型
	 * @return
	 */
	public DataResult getMsg(int userId, int type);
	/**
	 * 处理消息
	 * @param msgId 消息id
	 * @param status 处理结果
	 * @return
	 */
	public DataResult dealMsg(int msgId, int status, int groupId, int userId, int friendId, String myGroupId);
	/**
	 * 删除好友
	 * @param userId 用户id
	 * @param friendId 好友id
	 * @return
	 */
	public DataResult delFriend(int userId, int friendId);
	/**
	 * 删除消息
	 * @param msgId 消息id
	 * @return
	 */
	public DataResult delMsg(int msgId);
	/**
	 * 检查是否已添加好友或已发送申请
	 * @param userId 用户id
	 * @param friendId 好友id
	 * @return
	 */
	public DataResult checkFriend(int userId, int friendId);
	/**
	 * 发送聊天消息
	 * @param recieverId 用户id
	 * @param senderId 发送人id
	 * @param content 内容
	 * @return
	 */
	public DataResult sendTalkMsg(int recieverId, int senderId, String content);
	/**
	 * 读取聊天消息
	 * @param recieverId 接受者id
	 * @param senderId 发送者id
	 * @return
	 */
	public DataResult loadTalkMsgs(int senderId, int recieverId);
	/**
	 * 读取我的图片
	 * @param userId 用户id
	 * @return
	 */
	public DataResult getMyImgs(int userId);
	/**
	 * 读取我的网盘文件
	 * @param userId 用户id
	 * @return
	 */
	public DataResult getMyFiles(int userId);
}
