package com.aliyun.mns.util;

import java.util.List;

import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.PagingListResult;
import com.aliyun.mns.model.QueueMeta;

public class QueueOperator extends BaseMessage {
	
	public QueueOperator() {
		client=getMNSClient();
	}
	public CloudQueue createQuene(String queueName) {
		 // 创建队列一
        QueueMeta meta1 = new QueueMeta();
        meta1.setQueueName(queueName);
        meta1.setPollingWaitSeconds(15);
        meta1.setMaxMessageSize(2048L);
        CloudQueue queue1 = client.createQueue(meta1);
        System.out.println("Queue1 URL: " + queue1.getQueueURL());
        return queue1;
	}
	public CloudQueue createQuene2(String queueName) {
		// 创建队列二
        QueueMeta meta2 = new QueueMeta();
        meta2.setQueueName(queueName);
        meta2.setPollingWaitSeconds(15);
        meta2.setMaxMessageSize(2048L);

        CloudQueue queue2 = client.getQueueRef(queueName);
        String queueURL = queue2.create(meta2);
        System.out.println("Queeu2 URL: " + queueURL);

        return queue2;
	}
	public CloudQueue getQueueList(String queueName) {
		 CloudQueue queue = client.getQueueRef(queueName);
         return queue;
	}
	public boolean deleteQueue(String queueName) {
		boolean result=true;
		try {
			 CloudQueue queue = client.getQueueRef(queueName);
	         //queue.delete();
	         // 幂等性
	         queue.delete();
		} catch (Exception e) {
			// TODO: handle exception
			result=false;
		}finally{
			return result;
		}
	}
}
