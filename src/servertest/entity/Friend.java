package servertest.entity;

public class Friend {
	private int id;
	private int userId;
	private int friendId;
	private int groupId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getFriendId() {
		return friendId;
	}
	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	@Override
	public String toString() {
		return "Friend [id=" + id + ", userId=" + userId + ", friendId="
				+ friendId + ", groupId=" + groupId + "]";
	}
	
}
