package servertest.service.impl;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import servertest.dao.UserDao;
import servertest.entity.AddFriendMsg;
import servertest.entity.DataResult;
import servertest.entity.Friend;
import servertest.entity.Group;
import servertest.entity.TalkMsg;
import servertest.entity.User;
import servertest.entity.UserInf;
import servertest.service.UserService;
import servertest.util.DateUtil;
import servertest.util.UserUtil;

@Service
public class UserServiceImpl implements UserService {
	@Resource
	private UserDao userDao;

	/**
	 * 登录
	 * 
	 * @throws Exception
	 */
	@Override
	public DataResult login(String username, String password) throws Exception {
		User user = userDao.getByUsername(username);
		String pwd = user.getPassword();
		DataResult dataResult = new DataResult();
		if (pwd != null && pwd.equals(UserUtil.convertStringToMD5(password))) {
			String userNickname = userDao.getUserInfoByUserId(user.getId()).getNickname();
			System.out.println(userNickname);
			dataResult.setData(user);
			dataResult.setStatus(0);
			dataResult.setMsg(userNickname);
		} else {
			dataResult.setStatus(1);
			dataResult.setMsg("用户名或密码错误");
		}

		return dataResult;
	}

	@Override
	public DataResult checkDuplication(String username) {
		DataResult dataResult = new DataResult();
		User user = userDao.getByUsername(username);
		if (user == null) {
			dataResult.setStatus(0);
		} else {
			dataResult.setStatus(1);
		}
		return dataResult;
	}

	@Transactional
	@Override
	public DataResult regist(String username, String password, String phone,
			String gender, String nickname) throws Exception {
		DataResult dataResult = new DataResult();
		User user = new User();
		user.setPassword(UserUtil.convertStringToMD5(password));
		user.setUsername(username);
		Serializable userId = userDao.saveUser(user);
		if (userId != null) {
			UserInf ui = new UserInf();
			ui.setNickname(nickname);
			ui.setPhone(phone);
			ui.setUserId((Integer) userId);
			ui.setGender(gender);
			Serializable uiId = userDao.saveUserInf(ui);
			Group g = new Group();
			g.setGroupName("默认分组");
			g.setUserId((Integer)userId);
			int gId = userDao.saveGroup(g);
			if (uiId != null && gId != -1) {
				dataResult.setStatus(0);
				dataResult.setMsg("注册成功");
			}
		}else{
			dataResult.setStatus(1);
			dataResult.setMsg("注册失败");
		}
		return dataResult;
	}

	@Transactional
	@Override
	public DataResult modifypwd(int userId, String oldpwd, String newpwd)
			throws Exception {
		DataResult dataResult = new DataResult();
		User user = userDao.getUserById(userId);
		String password = user.getPassword();

		if (!password.equals(UserUtil.convertStringToMD5(oldpwd))) {// 原密码错误
			dataResult.setMsg("原密码错误");
			dataResult.setStatus(1);
		} else {// 原密码正确
			user.setPassword(UserUtil.convertStringToMD5(newpwd));
			int lines = userDao.updateUser(user);
			if (lines == 1) {// 修改成功
				dataResult.setStatus(0);
			} else {
				dataResult.setStatus(2);
				dataResult.setMsg("数据库异常");
			}
		}
		return dataResult;
	}

	@Transactional
	@Override
	public DataResult addGroup(int userId, String groupName) {
		DataResult dataResult = new DataResult();
		Group group = new Group();
		group.setUserId(userId);
		group.setGroupName(groupName);
		int id = userDao.saveGroup(group);
		if (id != -1) {
			dataResult.setStatus(0);
			group.setId(id);
			dataResult.setData(id);
		} else {
			dataResult.setStatus(1);
		}
		return dataResult;
	}

	@Override
	public DataResult reloadGroup(int userId) {
		DataResult dataResult = new DataResult();
		dataResult.setStatus(0);
		dataResult.setData(userDao.getAllGroup(userId));
		return dataResult;
	}

	@Override
	public DataResult loadFriend(int userId, int groupId) {
		DataResult dataResult = new DataResult();
		dataResult.setStatus(0);
		dataResult.setData(userDao.getUserInf(userId, groupId));
		return dataResult;
	}

	@Override
	public DataResult searchUser(String condition) {
		DataResult dataResult = new DataResult();
		dataResult.setStatus(0);
		dataResult.setData(userDao.findByCondition(condition));
		return dataResult;
	}

	@Override
	public DataResult getUserInfo(int userId) {
		DataResult dataResult = new DataResult();
		UserInf ui = userDao.getUserInfoByUserId(userId);
		if (ui != null) {
			dataResult.setStatus(0);
			dataResult.setData(ui);
		} else {
			dataResult.setStatus(1);
		}
		return dataResult;
	}

	@Transactional
	@Override
	public DataResult delGroup(int userId, int groupId) {
		DataResult dataResult = new DataResult();
		if (userDao.delFriendByGroupId(groupId)
				&& userDao.delGroup(groupId, userId)) {
			dataResult.setStatus(0);
		} else {
			dataResult.setStatus(1);
		}
		return dataResult;
	}

	@Transactional
	@Override
	public DataResult sendAddMsg(int userId, int friendId, String userUsername,
			String friendUsername, String msg, int groupId) {
		DataResult dataResult = new DataResult();
		AddFriendMsg afm = new AddFriendMsg();
		afm.setFriendId(friendId);
		afm.setFriendUsername(friendUsername);
		afm.setMsg(msg);
		afm.setUserId(userId);
		afm.setUserUsername(userUsername);
		afm.setStatus(0);
		afm.setGroupId(groupId);
		afm.setUpdateDate(DateUtil.getDateToString(new Date(), null));
		int id = userDao.insertAddFriendMsg(afm);
		if (id >= 1) {
			dataResult.setStatus(0);
		} else {
			dataResult.setStatus(1);
		}
		return dataResult;
	}

	@Override
	public DataResult getMsg(int userId, int type) {
		DataResult dataResult = new DataResult();
		dataResult.setStatus(0);
		dataResult.setData(userDao.getMsgByUserId(userId, type));
		return dataResult;
	}

	@Transactional
	@Override
	public DataResult dealMsg(int msgId, int status, int groupId, int userId,
			int friendId, String myGroupId) {
		DataResult dataResult = new DataResult();
		AddFriendMsg afm = userDao.getMsgById(msgId);
		afm.setStatus(status);
		boolean insertResult1 = false;
		boolean insertResult2 = false;
		// 同意
		if (status == 1 && myGroupId != null) {
			Friend f = new Friend();
			f.setFriendId(friendId);
			f.setGroupId(groupId);
			f.setUserId(userId);
			if (userDao.insertFriend(f) != null) {
				insertResult1 = true;
			}
			Friend f1 = new Friend();
			f1.setFriendId(userId);
			f1.setGroupId(Integer.parseInt(myGroupId));
			f1.setUserId(friendId);
			if (userDao.insertFriend(f1) != null) {
				insertResult2 = true;
			}
		}
		boolean updateResult = userDao.updateMsg(afm);
		if ((status == 2 && updateResult)
				|| (status == 1 && updateResult && insertResult1 && insertResult2)) {
			dataResult.setStatus(0);
		} else {
			dataResult.setStatus(1);
		}
		return dataResult;
	}

	@Transactional
	@Override
	public DataResult delFriend(int userId, int friendId) {
		DataResult dataResult = new DataResult();
		Friend f1 = userDao.getFriendByUserIdAndFriendId(userId, friendId);
		Friend f2 = userDao.getFriendByUserIdAndFriendId(friendId, userId);
		if (f1 != null && f2 != null) {
			boolean result1 = userDao.delFriend(f1);
			boolean result2 = userDao.delFriend(f2);
			if (result1 && result2) {
				dataResult.setStatus(0);
				return dataResult;
			}
		}
		dataResult.setStatus(1);
		return dataResult;
	}

	@Transactional
	@Override
	public DataResult delMsg(int msgId) {
		DataResult dataResult = new DataResult();
		if (userDao.delMsg(userDao.getMsgById(msgId))) {
			dataResult.setStatus(0);
		} else {
			dataResult.setStatus(1);
		}
		return dataResult;
	}

	@Override
	public DataResult checkFriend(int userId, int friendId) {
		DataResult dataResult = new DataResult();
		Friend f = userDao.getFriendByUserIdAndFriendId(userId, friendId);
		AddFriendMsg afm = userDao.getAddMsgByUserIdAndFriendId(userId,
				friendId);

		if (f == null && afm == null) {// 未添加
			dataResult.setStatus(0);
		}
		if (f == null && afm != null) {// 已发送请求
			dataResult.setStatus(1);
		}
		if (f != null) {// 已添加
			dataResult.setStatus(2);
		}
		return dataResult;
	}

	@Override
	public DataResult sendTalkMsg(int recieverId, int senderId, String content) {
		DataResult dataResult = new DataResult();
		TalkMsg tm = new TalkMsg();
		tm.setContent(content);
		tm.setCreateTime(DateUtil.getDateToString(new Date(),
				"yyyy-MM-dd hh:mm:ss"));
		tm.setSenderId(senderId);
		tm.setRecieverId(recieverId);
		Integer id = (Integer) userDao.insertTalkMsg(tm);
		if (id != null) {
			tm.setId(id);
			dataResult.setStatus(0);
			dataResult.setData(tm);
		} else {
			dataResult.setStatus(1);
		}
		return dataResult;
	}

	@Override
	public DataResult loadTalkMsgs(int senderId, int recieverId) {
		DataResult dataResult = new DataResult();
		dataResult.setData(userDao.loadTalkMsgs(senderId, recieverId));
		dataResult.setStatus(0);
		return dataResult;
	}

}
