package com.dms.ptp.dto;

import java.util.List;

import com.dms.ptp.entity.User;

public class UserList {
	
	private int total;
	List<User> items;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<User> getItems() {
		return items;
	}
	public void setItems(List<User> items) {
		this.items = items;
	}

}
