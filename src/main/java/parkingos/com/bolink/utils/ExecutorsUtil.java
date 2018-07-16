package parkingos.com.bolink.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorsUtil {
	private static ExecutorService fixedThreadPool;
	private static Byte[] syncLock = new Byte[]{};
	
	public static ExecutorService getExecutorService(){
		if(fixedThreadPool != null){
			return fixedThreadPool;
		}
		synchronized (syncLock) {
			if(fixedThreadPool == null){
				fixedThreadPool = Executors.newFixedThreadPool(500);
			}
		}
		return fixedThreadPool;
	}

	public  static void shutDown(){
		if(fixedThreadPool!=null)
			fixedThreadPool.shutdown();
	}
}
