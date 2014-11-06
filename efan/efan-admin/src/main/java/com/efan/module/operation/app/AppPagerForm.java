package com.efan.module.operation.app;

import com.efan.common.AbstractPagerForm;

public class AppPagerForm extends AbstractPagerForm {
	private String name;
	private long catagory;
	private long channel;
	private String pubStatus;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCatagory() {
		return catagory;
	}

	public void setCatagory(long catagory) {
		this.catagory = catagory;
	}

	public long getChannel() {
		return channel;
	}

	public void setChannel(long channel) {
		this.channel = channel;
	}

	public String getPubStatus() {
		return pubStatus;
	}

	public void setPubStatus(String pubStatus) {
		this.pubStatus = pubStatus;
	}
}
