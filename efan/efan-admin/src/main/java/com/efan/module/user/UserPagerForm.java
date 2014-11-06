package com.efan.module.user;

import com.efan.common.AbstractPagerForm;

public class UserPagerForm extends AbstractPagerForm {
	private int userListSearchIsDeleted;

	public int getUserListSearchIsDeleted() {
		return userListSearchIsDeleted;
	}

	public void setUserListSearchIsDeleted(int userListSearchIsDeleted) {
		this.userListSearchIsDeleted = userListSearchIsDeleted;
	}

}
