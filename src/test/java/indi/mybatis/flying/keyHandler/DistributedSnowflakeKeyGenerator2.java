package indi.mybatis.flying.keyHandler;

import javax.annotation.PostConstruct;

import indi.mybatis.flying.type.KeyHandler;

/*本handler返回String型的主键*/
public class DistributedSnowflakeKeyGenerator2 implements KeyHandler {

	private static final long TWEPOCH = 1546272000000L;// 从2019-01-01 00:00:00计数

	/** 机器id所占的位数 */
	private static final long WORKER_ID_BITS = 5L;

	/** 数据标识id所占的位数 */
	private static final long DATACENTER_ID_BITS = 5L;

	/** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
	private static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);

	/** 支持的最大数据标识id，结果是31 */
	private static final long MAX_DATACENTER_ID = -1L ^ (-1L << DATACENTER_ID_BITS);

	/** 序列在id中占的位数 */
	private static final long SEQUENCE_BITS = 12L;

	/** 机器ID向左移12位 */
	private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

	/** 数据标识id向左移17位(12+5) */
	private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

	/** 时间截向左移22位(5+5+12)，这样留给时间戳的位数就有41位(63-22) */
	private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

	/** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
	private static final long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);

	/** 工作机器ID(0~31) */
	private long theWorkerId = 31;

	/** 数据中心ID(0~31) */
	private long theDatacenterId = 31;

	/** 毫秒内序列(0~4095) */
	private static long sequence = 0L;

	/** 上次生成ID的时间截 */
	private static long lastTimestamp = -1L;

	private long workerId;

	private long datacenterId;

	// ==============================Constructors=====================================
	/**
	 * 构造函数
	 * 
	 * @param workerId     工作ID (0~31)
	 * @param datacenterId 数据中心ID (0~31)
	 */
	public DistributedSnowflakeKeyGenerator2() {
		if (theWorkerId > MAX_WORKER_ID || theWorkerId < 0) {
			throw new IllegalArgumentException(
					String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
		}
		if (theDatacenterId > MAX_DATACENTER_ID || theDatacenterId < 0) {
			throw new IllegalArgumentException(
					String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATACENTER_ID));
		}
	}

	// ==============================Methods==========================================
	/**
	 * 获得下一个ID (该方法是线程安全的)
	 * 
	 * @return SnowflakeId
	 */
	public synchronized long nextId() {
		long timestamp = timeGen();

		// 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
		if (timestamp < lastTimestamp) {
			throw new RuntimeException(String.format(
					"Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}

		// 如果是同一时间生成的，则进行毫秒内序列
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & SEQUENCE_MASK;
			// 毫秒内序列溢出
			if (sequence == 0) {
				// 阻塞到下一个毫秒,获得新的时间戳
				timestamp = tilNextMillis(lastTimestamp);
			}
		}
		// 时间戳改变，毫秒内序列重置
		else {
			sequence = 0L;
		}

		// 上次生成ID的时间截
		lastTimestamp = timestamp;

		// 移位并通过或运算拼到一起组成64位的ID
		return ((timestamp - TWEPOCH) << TIMESTAMP_LEFT_SHIFT) //
				| (theDatacenterId << DATACENTER_ID_SHIFT) //
				| (theWorkerId << WORKER_ID_SHIFT) //
				| sequence;
	}

	/**
	 * 阻塞到下一个毫秒，直到获得新的时间戳
	 * 
	 * @param lastTimestamp 上次生成ID的时间截
	 * @return 当前时间戳
	 */
	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	/**
	 * 返回以毫秒为单位的当前时间
	 * 
	 * @return 当前时间(毫秒)
	 */
	protected long timeGen() {
		return System.currentTimeMillis();
	}

	@Override
	public String getKey() {
		return String.valueOf(nextId());
	}

	@PostConstruct
	private void init() {
		theWorkerId = workerId;
		theDatacenterId = datacenterId;
	}

	public long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(long workerId) {
		this.workerId = workerId;
	}

	public long getDatacenterId() {
		return datacenterId;
	}

	public void setDatacenterId(long datacenterId) {
		this.datacenterId = datacenterId;
	}

}
