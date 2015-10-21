package com.aliyun.mns.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aliyun.mns.client.AsyncCallback;
import com.aliyun.mns.client.AsyncResult;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.common.BatchDeleteException;
import com.aliyun.mns.common.BatchSendException;
import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.ErrorMessageResult;
import com.aliyun.mns.model.Message;

public class QueueMessageOperator extends QueueOperator {

	CloudQueue queue = null;

	public QueueMessageOperator(String queueName) {
		super();
		// TODO Auto-generated constructor stub
		queue = this.createQuene(queueName);
	}

	public void sendMessage(String msg) {
		try {
			// queue.create();

			// 发送消息
			Message message = new Message();
			message.setMessageBody(msg);
			Message putMsg = queue.putMessage(message);
			System.out
					.println("PutMessage has MsgId: " + putMsg.getMessageId());

		} catch (ClientException ex) {
			// 错误处理
			ex.printStackTrace();
		} catch (ServiceException ex) {
			// 错误处理
			ex.printStackTrace();
		}
	}

	public Message lookMessage() {
		// 查看消息
		Message peekMsg = queue.peekMessage();
		System.out.println("PeekMessage has MsgId: " + peekMsg.getMessageId());
		System.out.println("PeekMessage Body: "
				+ peekMsg.getMessageBodyAsString());
		return peekMsg;
	}

	public String lookStringMessage() {
		// 查看消息
		Message peekMsg = queue.peekMessage();
		System.out.println("PeekMessage has MsgId: " + peekMsg.getMessageId());
		System.out.println("PeekMessage Body: "
				+ peekMsg.getMessageBodyAsString());
		return peekMsg.getMessageBodyAsString();
	}
	
	public Message getMessage() {
		// 获取消息
		Message popMsg = queue.popMessage();
		System.out.println("PopMessage Body: "
				+ popMsg.getMessageBodyAsString());
		return popMsg;

	}
	public String getStringMessage() {
		// 获取消息
		Message popMsg = queue.popMessage();
		System.out.println("PopMessage Body: "
				+ popMsg.getMessageBodyAsString());
		return popMsg.getMessageBodyAsString();

	}
	public String changeMessageVisableTime() {
		Message popMsg = queue.popMessage();
		// 更改消息可见时间
		String receiptHandle = popMsg.getReceiptHandle();
		int visibilityTimeout = 100;
		String rh = queue.changeMessageVisibilityTimeout(receiptHandle,
				visibilityTimeout);
		System.out.println("ReceiptHandle:" + rh);
		return rh;
	}

	public void deleteMessage(int visibilityTimeout) {
		Message popMsg = queue.popMessage();
		// 更改消息可见时间
		String receiptHandle = popMsg.getReceiptHandle();
		if (visibilityTimeout <= 0) {
			visibilityTimeout = 100;
		}
		String rh = queue.changeMessageVisibilityTimeout(receiptHandle,
				visibilityTimeout);
		// 删除消息
		queue.deleteMessage(rh);
	}

	public void deleteQueue() {
		// 删除队列
		queue.delete();
	}

	public void sendMultiMessage(List<String> msgList) {
		int batchMsgSize = 16; // 不能大于16
		// 待发送的消息
		List<Message> msgs = new ArrayList<Message>();
		for (int i = 0; i < batchMsgSize; i++) {
			Message message = new Message();
			message.setMessageBody(msgList.get(i));
			msgs.add(message);
		}

		// 批量发送消息
		List<Message> putMsgs = queue.batchPutMessage(msgs);
		for (Message putMsg : putMsgs) {
			System.out
					.println("PutMessage has MsgId: " + putMsg.getMessageId());
		}
	}

	public void asyncSendMultiMessage(List<String> msgList) {
		int batchMsgSize = 16; // 不能大于16
		// 待发送的消息
		List<Message> asyncMsgs = new ArrayList<Message>();
		for (int i = 0; i < batchMsgSize; i++) {
			Message asyncMsg = new Message();
			asyncMsg.setMessageBody(msgList.get(i));
			asyncMsgs.add(asyncMsg);
		}

		// 异步批量发送消息
		AsyncCallback<List<Message>> putCallback = new AsyncCallback<List<Message>>() {

			@Override
			public void onSuccess(List<Message> result) {
				for (Message putMsg : result) {
					System.out.println("PutMessage has MsgId:"
							+ putMsg.getMessageId());
				}
			}

			@Override
			public void onFail(Exception ex) {
				if (ex instanceof BatchSendException) {
					List<Message> messages = ((BatchSendException) ex)
							.getMessages();
					for (Message msg : messages) {
						if (msg.isErrorMessage()) {
							ErrorMessageResult errorMessageDetail = msg
									.getErrorMessageDetail();
							System.out.println("PutMessage Fail."
									+ " ErrorCode: "
									+ errorMessageDetail.getErrorCode()
									+ " ErrorMessage: "
									+ errorMessageDetail.getErrorMessage());
						} else {
							System.out.println(msg);
						}
					}
				} else {
					System.out.println("AsyncBatchPutMessage Exception: ");
					ex.printStackTrace();
				}
			}
		};
		AsyncResult<List<Message>> asyncBatchPutMessage = queue
				.asyncBatchPutMessage(asyncMsgs, putCallback);
		// 等待异步完成，仅是Sample中简化使用
		asyncBatchPutMessage.getResult();
	}

	public List<Message> lookMultiMessage(int batchMsgSize) {
		if (batchMsgSize <= 0 && batchMsgSize > 16) {
			batchMsgSize = 16;
		}
		// 批量查看消息
		List<Message> batchPeekMessage = queue.batchPeekMessage(batchMsgSize);
		for (Message peekMsg : batchPeekMessage) {
			System.out.println("PeekMessage has MsgId:"
					+ peekMsg.getMessageId());
		}
		return batchPeekMessage;
	}

	public List<Message> asyncLookMultiMessage(int batchMsgSize) {
		// 异步批量查看消息
		AsyncCallback<List<Message>> peekCallback = new AsyncCallback<List<Message>>() {
			@Override
			public void onSuccess(List<Message> result) {
				for (Message msg : result) {
					System.out.println("AsyncBatchPeekMessage has MsgId: "
							+ msg.getMessageId());
				}
			}

			@Override
			public void onFail(Exception ex) {
				System.out.println("AsyncBatchPeekMessage Exception: ");
				ex.printStackTrace();
			}
		};
		AsyncResult<List<Message>> asyncBatchPeekMessage = queue
				.asyncBatchPeekMessage(batchMsgSize, peekCallback);
		// 等待异步完成，仅是Sample中简化使用
		return asyncBatchPeekMessage.getResult();
	}

	public List<String> getMultiMessage(int batchMsgSize) {
		if (batchMsgSize <= 0 && batchMsgSize > 16) {
			batchMsgSize = 16;
		}
		List<String> receiptsToDelete = new ArrayList<String>();
		// 批量获取消息
		List<Message> batchPopMessage = queue.batchPopMessage(batchMsgSize);
		for (Message popMsg : batchPopMessage) {
			System.out.println("PopMessage has MsgId: "
					+ popMsg.getMessageId());
			receiptsToDelete.add(popMsg.getReceiptHandle());
		}
		return receiptsToDelete;
	}
	
	public List<Message> asyncGetMultiMessage(int batchMsgSize) {
		if (batchMsgSize <= 0 && batchMsgSize > 16) {
			batchMsgSize = 16;
		}
		// 异步批量获取消息
		class AsyncBatchPopCallback implements AsyncCallback<List<Message>> {

			@Override
			public void onSuccess(List<Message> result) {
				for (Message msg : result) {
					System.out.println("AsyncBatchPopMessage has MsgId: "
							+ msg.getMessageId());
					receipts.add(msg.getReceiptHandle());
				}
			}

			@Override
			public void onFail(Exception ex) {
				System.out.println("AsyncBatchPopMessage Exception: ");
				ex.printStackTrace();
			}

			public List<String> receipts = new ArrayList<String>();
		}
		;
		AsyncBatchPopCallback popCallback = new AsyncBatchPopCallback();
		AsyncResult<List<Message>> asyncBatchPopMessage = queue
				.asyncBatchPopMessage(batchMsgSize, popCallback);
		// 等待异步完成，仅是Sample中简化使用
		return asyncBatchPopMessage.getResult();
	}
	
	public void deleteMultiMessage() {
		List<String> receiptsToDelete = new ArrayList<String>();
		// 删除消息
		queue.batchDeleteMessage(receiptsToDelete);
	}
	
	public void asyncdeleteMultiMessage() {
		List<String> receiptsToDelete = new ArrayList<String>();
		// 异步批量获取消息
        class AsyncBatchPopCallback implements AsyncCallback<List<Message> > {

            @Override
            public void onSuccess(List<Message> result) {
                for (Message msg : result) {
                    System.out.println("AsyncBatchPopMessage has MsgId: " + msg.getMessageId());
                    receipts.add(msg.getReceiptHandle());
                }
            }

            @Override
            public void onFail(Exception ex) {
                System.out.println("AsyncBatchPopMessage Exception: ");
                ex.printStackTrace();
            }
            
            public List<String> receipts = new ArrayList<String>();
        };
        AsyncBatchPopCallback popCallback = new AsyncBatchPopCallback();
		AsyncCallback<Void> deleteCallback = new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				System.out.println("Async BatchDelete messages success!");
			}

			@Override
			public void onFail(Exception ex) {
				if (ex instanceof BatchDeleteException) {
					Map<String, ErrorMessageResult> errorMessages = ((BatchDeleteException) ex)
							.getErrorMessages();
					for (String receiptHandle : errorMessages.keySet()) {
						ErrorMessageResult error = errorMessages
								.get(receiptHandle);
						System.out.println("ReceiptHandle to delete : "
								+ receiptHandle + ", errorcode: "
								+ error.getErrorCode() + ", errormessage: "
								+ error.getErrorMessage());
					}
				} else {
					System.out
							.println("AsyncBatchDeleteMessage Exception: ");
					ex.printStackTrace();
				}
			}

		};
		List<String> receiptsWithDeleted = new ArrayList<String>();
		receiptsWithDeleted.addAll(popCallback.receipts);
		receiptsWithDeleted.addAll(receiptsToDelete);
		AsyncResult<Void> asyncBatchDeleteMessage = queue
				.asyncBatchDeleteMessage(receiptsWithDeleted,
						deleteCallback);
		// 等待异步完成，仅是Sample中简化使用
		asyncBatchDeleteMessage.getResult();
	}
	
}
