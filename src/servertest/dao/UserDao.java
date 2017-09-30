package servertest.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import servertest.entity.AddFriendMsg;
import servertest.entity.Friend;
import servertest.entity.FriendSearch;
import servertest.entity.Group;
import servertest.entity.TalkMsg;
import servertest.entity.User;
import servertest.entity.UserFile;
import servertest.entity.UserInf;

@Repository
public class UserDao {
	@Resource
	private HibernateTemplate hibernateTemplate;

	/**
	 * 通过用户名查询用户
	 * 
	 * @param username
	 *            用户名
	 * @return
	 */
	public User getByUsername(String username) {
		String hql = "from User u where u.username = '%s'";
		hql = String.format(hql, username);
		List<?> list = hibernateTemplate.find(hql);
		if (list.size() == 1) {
			return (User) list.get(0);
		}
		return null;
	}

	/**
	 * 保存用户
	 * 
	 * @param user
	 *            用户
	 * @return
	 */
	public Serializable saveUser(User user) {
		return hibernateTemplate.save(user);
	}

	/**
	 * 保存用户信息
	 * 
	 * @param ui
	 *            用户信息
	 * @return
	 */
	public Serializable saveUserInf(UserInf ui) {
		return hibernateTemplate.save(ui);
	}

	/**
	 * 通过id查询用户
	 * 
	 * @param id
	 *            用户id
	 * @return
	 */
	public User getUserById(int id) {
		return hibernateTemplate.get(User.class, id);
	}

	/**
	 * 修改用户信息
	 * 
	 * @param user
	 *            用户
	 * @return
	 */
	public int updateUser(User user) {
		boolean updateSuccess = false;
		try {
			hibernateTemplate.update(user);
			updateSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (updateSuccess) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 插入分组
	 * 
	 * @param group
	 *            分组对象
	 * @return
	 */
	public int saveGroup(Group group) {
		Serializable id = hibernateTemplate.save(group);
		if (id != null) {
			return (Integer) id;
		} else {
			return -1;
		}
	}

	/**
	 * 获取所有分组
	 * 
	 * @param userId
	 *            用户id
	 * @return
	 */
	public List<Group> getAllGroup(int userId) {
		String hql = "from Group g where g.userId = ";
		StringBuilder sb = new StringBuilder(hql);
		sb.append(userId);
		@SuppressWarnings("unchecked")
		List<Group> list = hibernateTemplate.find(sb.toString());
		return list;
	}

	/**
	 * 通过分组id获取用户列表
	 * 
	 * @param groupId
	 *            分组id
	 * @return
	 */
	public List<FriendSearch> getUserInf(int userId, int groupId) {
		final String sql = "SELECT "
				+ "ui.nickname AS nickname,"
				+ "ul.username AS username,"
				+ "ul.id AS userId "
				+ "FROM"
				+ "((friend_inf fi LEFT JOIN user_inf ui ON fi.friend_id = ui.user_id)"
				+ "LEFT JOIN user_login ul ON ul.id = fi.friend_id)" + "WHERE "
				+ "fi.user_id = " + userId + " and " + "fi.group_id = "
				+ groupId + "";
		return hibernateTemplate
				.execute(new HibernateCallback<List<FriendSearch>>() {

					@Override
					public List<FriendSearch> doInHibernate(Session session)
							throws HibernateException, SQLException {
						try {
							List<?> list = session.createSQLQuery(sql).list();
							List<FriendSearch> fsList = new ArrayList<FriendSearch>();
							for (Object object : list) {
								Object[] objArr = (Object[]) object;
								FriendSearch fs = new FriendSearch();
								fs.setNickname((String) objArr[0]);
								fs.setUsername((String) objArr[1]);
								fs.setId((Integer) objArr[2]);
								fsList.add(fs);
							}
							return fsList;
						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					}
				});
	}

	/**
	 * 通过条件搜索用户
	 * 
	 * @param condition
	 *            条件
	 * @return
	 */
	public List<FriendSearch> findByCondition(String condition) {
		final String sql = "SELECT " + "ui.nickname AS nickname,"
				+ "ul.username AS username," + "ul.id AS userId " + "FROM"
				+ "(user_inf ui LEFT JOIN user_login ul ON ul.id = ui.user_id)"
				+ "WHERE " + "ul.username like '%" + condition
				+ "%' or ui.nickname like '%" + condition + "%'";
		return hibernateTemplate
				.execute(new HibernateCallback<List<FriendSearch>>() {

					@Override
					public List<FriendSearch> doInHibernate(Session session)
							throws HibernateException, SQLException {
						List<?> list = session.createSQLQuery(sql).list();
						List<FriendSearch> fsList = new ArrayList<FriendSearch>();
						for (Object object : list) {
							Object[] objArr = (Object[]) object;
							FriendSearch fs = new FriendSearch();
							fs.setNickname((String) objArr[0]);
							fs.setUsername((String) objArr[1]);
							fs.setId((Integer) objArr[2]);
							fsList.add(fs);
						}
						return fsList;
					}
				});
	}

	/**
	 * 通过用户id获取用户信息
	 * 
	 * @param userId
	 *            用户id
	 * @return
	 */
	public UserInf getUserInfoByUserId(int userId) {
		String hql = "from UserInf ui where ui.userId = ";
		StringBuilder sb = new StringBuilder(hql);
		sb.append(userId);
		List<?> list = hibernateTemplate.find(sb.toString());
		if (list.size() == 1) {
			return (UserInf) list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 删除分组
	 * 
	 * @param groupId
	 *            分组id
	 * @param userId
	 *            用户id
	 */
	public boolean delGroup(int groupId, int userId) {
		Group group = new Group();
		group.setId(groupId);
		group.setUserId(userId);
		boolean result = false;
		try {
			hibernateTemplate.delete(group);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 通过分组id删除好友
	 * 
	 * @param groupId
	 *            分组id
	 * @return
	 */
	public boolean delFriendByGroupId(int groupId) {
		List<?> list = hibernateTemplate
				.find("from Friend f where f.groupId = " + groupId);
		boolean result = false;
		try {
			for (Object object : list) {
				int friendId = ((Friend) object).getFriendId();
				int userId = ((Friend) object).getUserId();
				// 删除对方好友
				List<?> list1 = hibernateTemplate
						.find("from Friend f where f.friendId = " + userId
								+ " and f.userId = " + friendId);
				hibernateTemplate.delete((Friend) list1.get(0));
				// 删除聊天记录
				List<?> list2 = hibernateTemplate
						.find("from TalkMsg tm where (tm.senderId = " + userId
								+ " and tm.recieverId = " + friendId
								+ ") or (tm.senderId = " + friendId
								+ " and tm.recieverId = " + userId + ")");
				for (Object object2 : list2) {
					hibernateTemplate.delete((TalkMsg) object2);
				}
				// 删除己方好友
				hibernateTemplate.delete((Friend) object);
			}

			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 插入添加好友消息
	 * 
	 * @return
	 */
	public int insertAddFriendMsg(AddFriendMsg afm) {
		int id = 0;
		id = (Integer) hibernateTemplate.save(afm);
		return id;
	}

	/**
	 * 通过用户id获取消息列表
	 * 
	 * @param userId
	 *            用户id
	 * @param type
	 *            消息类型
	 * @return
	 */
	public List<AddFriendMsg> getMsgByUserId(int userId, int type) {
		System.out.println(userId);
		String hql = null;
		if (type == 0) {
			hql = "from AddFriendMsg afm where afm.friendId = " + userId
					+ " and afm.status = 0";
		} else if (type == 1) {
			hql = "from AddFriendMsg afm where afm.friendId = " + userId
					+ " and (afm.status = 1 or afm.status = 2)";
		}
		@SuppressWarnings("unchecked")
		List<AddFriendMsg> list = hibernateTemplate.find(hql);
		return list;
	}

	/**
	 * 通过主键获取添加好友消息
	 * 
	 * @param msgId
	 *            消息id
	 * @return
	 */
	public AddFriendMsg getMsgById(int msgId) {
		return hibernateTemplate.get(AddFriendMsg.class, msgId);
	}

	/**
	 * 更新消息
	 * 
	 * @param afm
	 *            消息
	 * @return
	 */
	public boolean updateMsg(AddFriendMsg afm) {
		boolean result = false;
		try {
			hibernateTemplate.update(afm);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 插入一条好友记录
	 * 
	 * @param f
	 *            好友
	 * @return
	 */
	public Serializable insertFriend(Friend f) {
		return hibernateTemplate.save(f);
	}

	/**
	 * 通过userId和friendId获取好友
	 * 
	 * @param userId
	 *            用户id
	 * @param friendId
	 *            朋友id
	 * @return
	 */
	public Friend getFriendByUserIdAndFriendId(int userId, int friendId) {
		List<?> list = hibernateTemplate.find("from Friend f where f.userId = "
				+ userId + " and f.friendId = " + friendId);
		if (list.size() > 0) {
			return (Friend) list.get(0);
		}
		return null;
	}

	/**
	 * 通过userId和friendId获取消息
	 * 
	 * @param userId
	 *            用户id
	 * @param friendId
	 *            朋友id
	 * @return
	 */
	public AddFriendMsg getAddMsgByUserIdAndFriendId(int userId, int friendId) {
		List<?> list = hibernateTemplate
				.find("from AddFriendMsg afm where afm.userId = " + userId
						+ " and afm.friendId = " + friendId);
		if (list.size() > 0) {
			return (AddFriendMsg) list.get(0);
		}
		return null;
	}

	/**
	 * 删除好友
	 * 
	 * @param f
	 *            好友
	 * @return
	 */
	public boolean delFriend(Friend f) {
		boolean result = false;
		try {
			hibernateTemplate.delete(f);
			int userId = f.getUserId();
			int friendId = f.getFriendId();
			// 删除聊天记录
			List<?> list2 = hibernateTemplate
					.find("from TalkMsg tm where (tm.senderId = " + userId
							+ " and tm.recieverId = " + friendId
							+ ") or (tm.senderId = " + friendId
							+ " and tm.recieverId = " + userId + ")");
			for (Object object2 : list2) {
				hibernateTemplate.delete((TalkMsg) object2);
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 删除好友
	 * 
	 * @param f
	 *            好友
	 * @return
	 */
	public boolean delMsg(AddFriendMsg afm) {
		boolean result = false;
		try {
			hibernateTemplate.delete(afm);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 插入聊天消息记录
	 * 
	 * @param tm
	 *            消息
	 * @return
	 */
	public Serializable insertTalkMsg(TalkMsg tm) {
		return hibernateTemplate.save(tm);
	}

	/**
	 * 读取聊天消息
	 * 
	 * @return
	 */
	public List<TalkMsg> loadTalkMsgs(int senderId, int recieverId) {

		final int fSenderId = senderId;
		final int fReceiverId = recieverId;

		return hibernateTemplate
				.execute(new HibernateCallback<List<TalkMsg>>() {

					@Override
					public List<TalkMsg> doInHibernate(Session session)
							throws HibernateException, SQLException {
						List<?> list = session.createSQLQuery(
								"select * from talk_msg tm where (tm.sender_id = "
										+ fSenderId + " and tm.reciever_id = "
										+ fReceiverId + ") or (tm.sender_id = "
										+ fReceiverId
										+ " and tm.reciever_id = " + fSenderId
										+ ") limit 20;").list();
						List<TalkMsg> list1 = new ArrayList<TalkMsg>();
						for (Object object : list) {
							Object[] objArr = (Object[]) object;
							TalkMsg tm = new TalkMsg();
							tm.setId(Integer.parseInt(objArr[0].toString()));
							tm.setSenderId(Integer.parseInt(objArr[1]
									.toString()));
							tm.setRecieverId(Integer.parseInt(objArr[2]
									.toString()));
							tm.setContent(objArr[3].toString());
							tm.setCreateTime(objArr[4].toString());
							list1.add(tm);
						}
						return (List<TalkMsg>) list1;
					}
				});
	}

	/**
	 * 通过userId查询图片
	 * 
	 * @param userId
	 *            用户id
	 * @return
	 */
	public List<UserFile> getFilesByUserId(int userId) {
		List<?> list = hibernateTemplate
				.find("from UserFile uf where uf.userId = " + userId);
		List<UserFile> files = new ArrayList<UserFile>();
		for (Object object : list) {
			UserFile uf = (UserFile) object;
			files.add(uf);
		}
		return files;
	}
}
