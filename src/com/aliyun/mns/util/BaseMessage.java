package com.aliyun.mns.util;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.MNSClient;

public abstract class BaseMessage {
	public MNSClient client = null;
	public MNSClient getMNSClient() {
		CloudAccount account = new CloudAccount(Config.AccessKeyID,
				Config.AccessKeySecret,
				Config.MesageAddress);
		client = account.getMNSClient();
		return client;
	}
}
