
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

class EventCustomerSwitchesThread implements Runnable {
	private int deviceId;
	private int kiotUserMappingId;
	private int kiotUserId;
	private String bearerToken;
	private int kiotDeviceId;
	private String switchNumber;
	private String connectionString;
	
	public EventCustomerSwitchesThread(int deviceId, int kiotUserMappingId,int kiotUserId, String bearerToken
			, int kiotDeviceId, String switchNumber,String connectionString) {
		this.deviceId = deviceId;
		this.kiotUserMappingId = kiotUserMappingId;
		this.kiotUserId = kiotUserId;
		this.bearerToken = bearerToken;
		this.kiotDeviceId = kiotDeviceId;
		this.switchNumber = switchNumber;
		this.connectionString = connectionString;
	}

	public void run() {
		try {
			ScheduleDAO sdc= new ScheduleDAO();
		//	getTxData();
			//Invoke node Api
			//
			
	} 
		
		finally {
			if (ScheduleDAO.con != null) {
//				try {
//			//		ScheduleDAO.con.close();  Close later
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
				}
		System.out.println(Thread.currentThread().getName() + " (End)");// prints thread name
	}

	private void processmessage() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}