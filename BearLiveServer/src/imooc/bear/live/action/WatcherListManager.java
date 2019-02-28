package imooc.bear.live.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WatcherListManager {

	private static WatcherListManager manager;
	
	private Map<String,Map<String,Long>> watcherMap = new HashMap<String,Map<String,Long>>();
	private Map<String,ScheduledExecutorService> roomUpdateTimerMap = new HashMap<String,ScheduledExecutorService>();
	
	private WatcherListManager()
	{
		
	}
	
	public static WatcherListManager getInstance()
	{
		if(manager == null){
			synchronized (WatcherListManager.class) {
				if(manager == null){
					manager = new WatcherListManager();
				}
			}
		}
		return manager;
	}
	
	public void addRoom(String roomId){
		watcherMap.put(roomId, null);
	}
	
	public void removeRoom(String roomId){
		watcherMap.remove(roomId);
		ScheduledExecutorService service = roomUpdateTimerMap.remove(roomId);
		if(service!= null){
			service.shutdown();
		}
	}
	
	public void updateRoomUser(final String roomId, String userId){
		if(watcherMap.containsKey(roomId)){
			return;
		}
		
		Map<String,Long> watchers = watcherMap.get(roomId);
		if(watchers == null){
			watchers = new HashMap<String,Long>();
			watcherMap.put(roomId, watchers);
			
			ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
			Runnable command = new Runnable() {
				
				@Override
				public void run() {
					Map<String,Long> watchers = watcherMap.get(roomId);
					if(watchers != null){
						for(String userId : watchers.keySet()){
							long lastUpdateTime = watchers.get(userId);
							if(lastUpdateTime + 10 * 1000 > System.currentTimeMillis())
							{
								//说明这个观众是有效的.
								//无需操作
							}else{
								//观众无效，需要从map中删除这个userid
								watchers.remove(userId);
							}
						}
					}
				}
			};
			service.scheduleWithFixedDelay(command, 0, 10, TimeUnit.SECONDS);
			roomUpdateTimerMap.put(roomId, service);
		}
		watchers.put(userId, System.currentTimeMillis());
		
		
	}

	public void removeWatcher(String roomId, String userId) {
		if(watcherMap.containsKey(roomId)){
			return;
		}
		
		Map<String,Long> watchers = watcherMap.get(roomId);
		if(watchers == null){
			return;
		}
		
		watchers.remove(userId);
	}

	public Set<String> getWatchers(String roomId) {
		Set<String> watcherList = new HashSet<String>();
		if(watcherMap.containsKey(roomId)){
			return watcherList;
		}
		Map<String,Long> watchers = watcherMap.get(roomId);
		if(watchers == null){
			return watcherList;
		}
		
		return watchers.keySet();
		
	}
}
