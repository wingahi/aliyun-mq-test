# aliyun-mq-test
阿里云消息队列工具方法。封装方法有：


	public void sendMessage(String msg) {
	// 发送消息

	public Message lookMessage() {
		// 查看消息
	
	public String lookStringMessage() {
		// 查看消息
	

	public Message getMessage() {
		// 获取消息
	

	public String getStringMessage() {
		// 获取消息
	

	public String changeMessageVisableTime() {
		// 更改消息可见时间
	

	public void deleteMessage(int visibilityTimeout) {
		// 更改消息可见时间
	

	public void deleteQueue() {
		// 删除队列
	

	public void sendMultiMessage(List<String> msgList) {
		// 批量发送消息
	

	public void asyncSendMultiMessage(List<String> msgList) {
		int batchMsgSize = 16; // 不能大于16

		// 异步批量发送消息
	

	public List<Message> lookMultiMessage(int batchMsgSize) {
		// 批量查看消息

	public List<Message> asyncLookMultiMessage(int batchMsgSize) {
		// 异步批量查看消息
		

	public List<String> getMultiMessage(int batchMsgSize) {
		// 批量获取消息


	public List<Message> asyncGetMultiMessage(int batchMsgSize) {
		// 异步批量获取消息
		

	public void deleteMultiMessage() {
		// 删除消息


	public void asyncdeleteMultiMessage() {
	// 异步批量删除消息
