package servertest.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import servertest.dao.UserDao;
import servertest.entity.DataResult;
import servertest.entity.User;
import servertest.service.UserService;

@Component
public class UserAction {
	private DataResult dataResult;

	@Resource
	private UserService userService;
	@Resource
	private UserDao dao;

	private static Logger logger = Logger.getLogger(UserAction.class);

	/**
	 * 登录
	 * 
	 * @return
	 */
	public String login() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String password = request.getParameter("password");
		String username = request.getParameter("username");
		try {
			dataResult = userService.login(username, password);
			User user = (User) dataResult.getData();
			if (dataResult.getStatus() == 0) {
				HttpSession session = request.getSession();
				StringBuilder sb = new StringBuilder("user::");
				session.getServletContext().setAttribute(
						sb.append(user.getId()).toString(), user);
			}
		} catch (Exception e) {
			logger.error("[加密出错] -> " + e);
		}
		return "success";
	}

	/**
	 * 登出
	 * 
	 * @return
	 */
	public String logout() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = request.getParameter("userId");
		HttpSession session = request.getSession();
		String key = new StringBuilder("user::").append(userId).toString();
		session.getServletContext().setAttribute(key, null);
		dataResult = new DataResult();
		// 判断是否删除了登陆信息
		if (session.getServletContext().getAttribute(key) == null) {
			dataResult.setStatus(0);
		} else {
			dataResult.setStatus(1);
		}
		return "success";
	}

	/**
	 * 
	 * 登录检查
	 * 
	 * @return
	 */
	public String checkLogin() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String userId = request.getParameter("userId");
		StringBuilder sb = new StringBuilder("user::");
		String key = sb.append(userId).toString();
		Object value = session.getServletContext().getAttribute(key);
		dataResult = new DataResult();
		if (value == null) {
			// 没有登录信息
			dataResult.setStatus(1);
		} else {
			// 已登录
			dataResult.setStatus(0);
			dataResult.setData(value);
		}
		return "success";
	}

	/**
	 * 注册账号
	 * 
	 * @return
	 */
	public String regist() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String phone = request.getParameter("phone");
		String gender = request.getParameter("gender");
		String nickname = request.getParameter("nickname");
		try {
			dataResult = userService.regist(username, password, phone, gender, nickname);
		} catch (Exception e) {
			logger.error("[密码加密失败] -> " + e);
		}
		return "success";
	}

	/**
	 * 注册时检查用户名是否重复
	 * 
	 * @return
	 */
	public String checkDuplication() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String username = request.getParameter("username");
		dataResult = userService.checkDuplication(username);
		return "success";
	}

	/**
	 * 修改密码
	 * 
	 * @return
	 */
	public String modifypwd() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String oldpwd = request.getParameter("oldpwd");
			String newpwd = request.getParameter("newpwd");
			int userId = Integer.parseInt(request.getParameter("userId"));
			try {
				dataResult = userService.modifypwd(userId, oldpwd, newpwd);
			} catch (Exception e) {
				logger.error("[密码加密失败] -> " + e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	/**
	 * 添加分组
	 * 
	 * @return
	 */
	public String addGroup() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = request.getParameter("userId");
		String groupName = request.getParameter("groupName");
		dataResult = userService.addGroup(Integer.parseInt(userId), groupName);
		return "success";
	}

	/**
	 * 重新拉取分组列表
	 * 
	 * @return
	 */
	public String reloadGroup() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = request.getParameter("userId");
		dataResult = userService.reloadGroup(Integer.parseInt(userId));
		return "success";
	}

	/**
	 * 获取好友
	 * 
	 * @return
	 */
	public String loadFriend() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = request.getParameter("userId");
		String groupId = request.getParameter("groupId");
		dataResult = userService.loadFriend(Integer.parseInt(userId),
				Integer.parseInt(groupId));
		return "success";
	}
	
	/**
	 * 搜索用户
	 * @return
	 */
	public String searchUser(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String condition = request.getParameter("condition");
		dataResult = userService.searchUser(condition);
		return "success";
	}
	
	/**
	 * 查看用户信息
	 * @return
	 */
	public String getUserInfo(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = request.getParameter("userId");
		System.out.println(userId);
		dataResult = userService.getUserInfo(Integer.parseInt(userId));
		return "success";
	}
	
	/**
	 * 删除分组
	 * @return
	 */
	public String delGroup(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = request.getParameter("userId");
		String groupId = request.getParameter("groupId");
		try{
		dataResult = userService.delGroup(Integer.parseInt(userId), Integer.parseInt(groupId));
		}catch(Exception e){
			e.printStackTrace();
		}
		return "success";
	}
	
	/**
	 * 添加好友
	 * @return
	 */
	public String sendAddMsg(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = request.getParameter("userId");
		String friendId = request.getParameter("friendId");
		String userUsername = request.getParameter("userUsername");
		String friendUsername = request.getParameter("friendUsername");
		String msg = request.getParameter("msg");
		String groupId = request.getParameter("groupId");
		try{
			dataResult = userService.sendAddMsg(Integer.parseInt(userId), Integer.parseInt(friendId), userUsername, friendUsername, msg, Integer.parseInt(groupId));
		}catch(Exception e){ e.printStackTrace();}
		return "success";
	}
	
	/**
	 * 获取消息列表
	 * @return
	 */
	public String getMsg(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = request.getParameter("userId");
		String type = request.getParameter("type");
		dataResult = userService.getMsg(Integer.parseInt(userId),Integer.parseInt(type));
		return "success";
	}
	
	/**
	 * 处理消息
	 * @return
	 */
	public String dealMsg(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String msgId = request.getParameter("msgId");
		String status = request.getParameter("status");
		String groupId = request.getParameter("groupId");
		String userId = request.getParameter("userId");
		String friendId = request.getParameter("friendId");
		String myGroupId = request.getParameter("myGroupId");
		try{
		dataResult = userService.dealMsg(Integer.parseInt(msgId), Integer.parseInt(status), 
				Integer.parseInt(groupId), Integer.parseInt(userId), Integer.parseInt(friendId), myGroupId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "success";
	}
	/**
	 * 获取消息列表
	 * @return
	 */
	public String delFriend(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = request.getParameter("userId");
		String friendId = request.getParameter("friendId");
		dataResult = userService.delFriend(Integer.parseInt(userId), Integer.parseInt(friendId));
		return "success";
	}
	/**
	 * 删除消息
	 * @return
	 */
	public String delMsg(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String msgId = request.getParameter("msgId");
		dataResult = userService.delMsg(Integer.parseInt(msgId));
		return "success";
	}
	
	/**
	 * 检查是否已添加好友或已发送申请
	 * @return
	 */
	public String checkFriend(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = request.getParameter("userId");
		String friendId = request.getParameter("friendId");
		dataResult = userService.checkFriend(Integer.parseInt(userId), Integer.parseInt(friendId));
		return "success";
	}
	
	/**
	 * 发送聊天消息
	 * @return
	 */
	public String sendTalkMsg(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String recieverId = request.getParameter("recieverId");
		String senderId = request.getParameter("senderId");
		String content = request.getParameter("content");
		dataResult = userService.sendTalkMsg(Integer.parseInt(recieverId), Integer.parseInt(senderId), content);
		return "success";
	}
	
	/**
	 * 读取聊天消息
	 * @return
	 */
	public String loadTalkMsgs(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String recieverId = request.getParameter("recieverId");
		String senderId = request.getParameter("senderId");
		dataResult = userService.loadTalkMsgs(Integer.parseInt(recieverId), Integer.parseInt(senderId));
		return "success";
		
	}
	/**
	 * 读取用户的图片
	 * @return
	 */
	public String getMyImgs(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = request.getParameter("userId");
		dataResult = userService.getMyImgs(Integer.parseInt(userId));
		return "success";
	}
	
	

	public DataResult getDataResult() {
		return dataResult;
	}

	public void setDataResult(DataResult dataResult) {
		this.dataResult = dataResult;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
