package com.aliyun.mns.util;

public class QueueMessageOperatorTest {
	public static void main(String[] args) {
		QueueMessageOperator queueMessageOperator=new QueueMessageOperator("mytest-queue");
		queueMessageOperator.sendMessage("发送消息");
		System.out.println(queueMessageOperator.getStringMessage());
	}
}
