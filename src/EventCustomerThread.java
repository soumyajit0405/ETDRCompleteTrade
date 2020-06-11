
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

class EventCustomerThread implements Runnable {
	public  int customerId;
	public  String startTime;
	public int eventId;
	
	public EventCustomerThread(int customerId, String startTime, int eventId) {
		this.customerId = customerId;
		this.startTime = startTime;
		this.eventId = eventId;
	}

	public void run() {
		try {
			ScheduleDAO sdc= new ScheduleDAO();
			//ArrayList<HashMap<String,Object>> listOfCustomerData=sdc.getEventCustomerData(customerId);
			//Invoke node Api
			
//			if (listOfCustomerData.size() > 0) {
//
//				ExecutorService executor = Executors.newFixedThreadPool(listOfCustomerData.size());// creating a pool of 1000
//																							// threads
//				for (int i = 0; i < listOfCustomerData.size(); i++) {
//					Runnable worker = new EventCustomerSwitchesThread(
//							(int)listOfCustomerData.get(i).get("deviceId"),
//							(int)listOfCustomerData.get(i).get("kiotUserMappingId"),
//							(int)listOfCustomerData.get(i).get("kiotUserId"),
//							(String)listOfCustomerData.get(i).get("bearerToken"),
//							(int)listOfCustomerData.get(i).get("kiotDeviceId"),
//							(String)listOfCustomerData.get(i).get("switchNumber"),
//							(String)listOfCustomerData.get(i).get("connectionString"));
//					System.out.println("List of run workers");
//					executor.execute(worker);// calling execute method of ExecutorService
//				}
//				executor.shutdown();
//				while (!executor.isTerminated()) {
//				}
			
	//		}
		TimeUnit.SECONDS.sleep(2);
		getTxData();
			
	} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	public void getTxData() throws ClassNotFoundException, SQLException, JSONException, IOException {
		ScheduleDAO scd= new ScheduleDAO();
		double meterReading= 0;
		DBHelper dbhelper = new DBHelper();
		HttpConnectorHelper httpconnectorhelper= new HttpConnectorHelper();
		JSONObject inputDetails1= new JSONObject();
		inputDetails1.put("meterId", 6);
		// inputDetails1.put("timestamp", "2020-06-07 21:15:00");
			inputDetails1.put("timestamp", startTime);
		JSONObject responseFromDevice = httpconnectorhelper
				.sendPostWithToken(scd.getConnectionString(customerId), inputDetails1);
		if(responseFromDevice.isNull("meterReading")) {
			meterReading = 0;
		} else {
			meterReading = (double)responseFromDevice.get("meterReading");
		}
		// HashMap<String,String> responseAfterParse =
		// cm.parseInput(responseFrombcnetwork);
			dbhelper.updateEventCustomer(meterReading,eventId,customerId);
		
	}
}