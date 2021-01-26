

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class ScheduleDAO {

	static Connection con;
	Statement stmt = null;
	PreparedStatement pStmt = null;
	 String startTime="";
	 public ArrayList<HashMap<String,Object>> getEvents(String date, String time) throws SQLException, ClassNotFoundException
	 {
		 PreparedStatement pstmt = null;
		  time=time+":00";
		  startTime = date+" "+time;
		  
		//JDBCConnection connref =new JDBCConnection();
		 if (con == null ) {
				con = JDBCConnection.getOracleConnection();
		 }
		 // String query1="update all_events set event_status_id=3 where event_end_time ='2021-01-25 06:15:00' and event_type_id = 2";
		 //String query1="update all_events set event_status_id=3 where event_end_time ='"+date+" "+time+"' and event_type_id = 2";
//		 System.out.println(query1);
//		 PreparedStatement pstmt1=con.prepareStatement(query1);
//		 pstmt1.executeUpdate();
		 String query2="update event_customer_mapping set event_customer_status_id=8 where event_customer_status_id = 13 and event_id in "
		 		+ "  (select event_id from  all_events where event_end_time ='"+date+" "+time+"' and event_type_id = 2)";
		 
//		 String query2="update event_customer_mapping set event_customer_status_id=8 where event_customer_status_id = 13 and event_id in "
//			 		+ "  (select event_id from  all_events where event_end_time ='2021-01-25 06:45:00' and event_type_id = 2)";
//			 
		 PreparedStatement  pstmt2=ScheduleDAO.con.prepareStatement(query2); 
		 pstmt2.execute();
		 System.out.println("After query 1");
		//	System.out.println("select aso.sell_order_id,ubc.private_key,ubc.public_key,abc.order_id from all_sell_orders aso,all_blockchain_orders abc, user_blockchain_keys ubc where aso.transfer_start_ts ='"+date+" "+time+"' and abc.general_order_id=aso.sell_order_id and abc.order_type='SELL_ORDER' and ubc.user_id  = aso.seller_id and aso.order_status_id=1");
		 // String query="select aso.sell_order_id,ubc.private_key,ubc.public_key,abc.order_id,abc.all_blockchain_orders_id from all_sell_orders aso,all_blockchain_orders abc, user_blockchain_keys ubc where aso.transfer_start_ts ='"+date+" "+time+"' and abc.general_order_id=aso.sell_order_id and abc.order_type='SELL_ORDER' and ubc.user_id  = aso.seller_id and aso.order_status_id=3";
		 String query="select a.event_id,a.event_type_id from all_events a where  a.event_status_id= 8 and a.event_end_time ='"+date+" "+time+"'";
		 System.out.println("query to pick events" +query  );
			//String query="select a.event_id,a.event_type_id from all_events a where  a.event_status_id= 8 and a.event_end_time ='2021-01-20 11:45:00'";
			pstmt=con.prepareStatement(query);
		// pstmt.setString(1,controllerId);
		 ResultSet rs= pstmt.executeQuery();
		 ArrayList<HashMap<String,Object>> al=new ArrayList<>();
		 while(rs.next())
		 {
			 HashMap<String,Object> data=new HashMap<>();
			 data.put("eventId",(rs.getInt("event_id")));
			data.put("startTime",startTime);
			 data.put("eventTypeId",(rs.getInt("event_type_id")));
			// data.put("startTime","2021-01-25 06:45:00");
			 al.add(data);
			// initiateActions(rs.getString("user_id"),rs.getString("status"),rs.getString("controller_id"),rs.getInt("device_id"),"Timer");
			//topic=rs.getString(1);
		 }
		 System.out.println("Length of events" + al.size());

		 //\ updateEventsManually(date, time);
		return  al;
	 }

	 
	 public void updateEventStatus(int eventId, String startTime) throws SQLException, ClassNotFoundException
	 {
		 PreparedStatement pstmt = null, pstmst1= null;
		 double committedPower =0, actualPower =0;
		 int eventSetId=0;
		//JDBCConnection connref =new JDBCConnection();
		 if (con == null ) {
				con = JDBCConnection.getOracleConnection();
		 }
		 System.out.println("In update Event Status of Event Id" + eventId);
		 String query = "select commited_power,actual_power,event_set_id from all_events where event_id=?";
			pstmt = con.prepareStatement(query);
			 pstmt.setInt(1,eventId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				committedPower = rs.getDouble(1);
				actualPower = rs.getDouble(2);
				eventSetId = rs.getInt(3);
		}
			double resultantPower= committedPower- actualPower;
		 // query="update all_events set event_status_id=3,short_fall="+resultantPower+" where event_id =?";
		 query="update all_events set event_status_id=3 where event_id =?";
		 pstmt=con.prepareStatement(query);
		pstmt.setInt(1,eventId);
		 pstmt.executeUpdate();
		 System.out.println("All events Completed");
		 query="update all_event_sets set actual_power=actual_power+? where event_set_id =?";
		 pstmt=con.prepareStatement(query);
		 pstmt.setDouble(1,actualPower);
		pstmt.setInt(2,eventSetId);
		 pstmt.executeUpdate();
//		 
//				query =" update all_event_sets set status_id=5 where event_set_id =?";
//				pstmst1=con.prepareStatement(query);
//				pstmst1.setInt(1, eventSetId);
//				pstmst1.execute();
//				System.out.println("All Event Set Completed");
		 updateEventSet(eventSetId,startTime); 
	 }
	 

		public void updateEventSet(int eventSetId, String endTime) throws ClassNotFoundException, SQLException {
			PreparedStatement pstmt1 = null, pstmt2= null;
			int val = 0;
			if (con == null) {
				con = JDBCConnection.getOracleConnection();
			}
			System.out.println("In update Event Set Status of Event Id" + eventSetId);
			String query = "SELECT count(*) FROM all_events where event_end_time > ? and event_set_id = ?";
			pstmt2=con.prepareStatement(query);
			pstmt2.setString(1, endTime);
			pstmt2.setInt(2, eventSetId);
			ResultSet rs = pstmt2.executeQuery();
			while (rs.next()) {
				val = rs.getInt(1);
			}
			System.out.println("Position of event "+ val);
			if (val == 0) {
				query = "update all_event_sets set status_id=5 where event_set_id =?";
				pstmt1=con.prepareStatement(query);
				pstmt1.setInt(1, eventSetId);
				pstmt1.execute();
				System.out.println("Event Set Completyed ");
			}
			
			 


		}

	 
		public String getBlockChainSettings() throws ClassNotFoundException, SQLException {
			PreparedStatement pstmt = null;
			String val = "";
			if (con == null) {
				con = JDBCConnection.getOracleConnection();
			}
			String query = "select value from general_config where name='dr_blockchain_enabled'";
			pstmt = con.prepareStatement(query);
			// pstmt.setString(1,controllerId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				val = rs.getString(1);
			}
			if (val.equalsIgnoreCase("N")) {
			//autoUpdateTrades();
			}
			return val;

		}
		
		
		public ArrayList<HashMap<String,Object>> getEventCustomer(int eventId) throws ClassNotFoundException, SQLException {
			PreparedStatement pstmt = null;
			ArrayList<HashMap<String,Object>> customerList = new ArrayList<>();
			if (con == null) {
				con = JDBCConnection.getOracleConnection();
			}
			String query = "select customer_id,event_customer_mapping_id from event_customer_mapping where event_id="+eventId +" and  event_customer_status_id=13";
			pstmt = con.prepareStatement(query);
			// pstmt.setString(1,controllerId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				HashMap<String,Object> data=new HashMap<>();
				 data.put("customerId",(rs.getInt("customer_id")));
				 data.put("eventCustomerMapping",(rs.getInt("event_customer_mapping_id")));
				customerList.add(data);
			}
			
			return customerList;

		}

		
		public ArrayList<HashMap<String,Object>> getEventCustomerData(int customerId,int eventCustomerMappingId, int eventId) throws ClassNotFoundException, SQLException {
			PreparedStatement pstmt = null,pstmt1 = null;
			int count =0;
			ArrayList<HashMap<String,Object>> customerData = new ArrayList<>();
			if (con == null) {
				con = JDBCConnection.getOracleConnection();
			}
			String query ="";
			String query2 = "select distinct event_customer_devices.user_dr_device,user_dr_devices.device_name,user_dr_devices.device_type_id from " + 
					"	event_customer_devices, user_dr_devices where user_dr_devices.user_dr_device_id=event_customer_devices.user_dr_device and event_customer_mapping = "+eventCustomerMappingId;
			pstmt1 = con.prepareStatement(query2);
			// pstmt.setString(1,controllerId);
			ResultSet rs1 = pstmt1.executeQuery();
			while (rs1.next()) {
				if (rs1.getInt("device_type_id") == 1) {
					 query ="select distinct kiot_user_mappings.kiot_user_mapping_id,kiot_user_mappings.kiot_user_id,kiot_user_mappings.bearer_token,\r\n" + 
							"all_kiot_remotes.kiot_device_id,user_dr_devices.device_type_id,user_dr_devices.device_name \r\n" + 
							",all_kiot_remotes.custom_data\r\n" + 
							" from \r\n" + 
							"all_kiot_remotes , kiot_user_mappings  , all_users,user_dr_devices  \r\n" + 
							"where all_users.user_id = "+customerId+" and all_users.dr_contract_number=kiot_user_mappings.contract_number and \r\n" + 
							"user_dr_devices.user_id=all_users.user_id\r\n" + 
							"and kiot_user_mappings.kiot_user_mapping_id is not null and \r\n" + 
							"kiot_user_mappings.kiot_user_mapping_id = all_kiot_remotes.kiot_user_mapping_id\r\n" + 
							"and user_dr_devices.user_dr_device_id = "+rs1.getInt("user_dr_device")+" "+"and all_kiot_remotes.id = user_dr_devices.remote_number";
					
					pstmt = con.prepareStatement(query);
					// pstmt.setString(1,controllerId);
					ResultSet rs = pstmt.executeQuery();
					while (rs.next()) {
						HashMap<String,Object> data = new HashMap<String, Object>();
						data.put("kiotUserMappingId",rs.getInt(1));
						data.put("kiotUserId",rs.getString(2));
						data.put("bearerToken",rs.getString(3));
						data.put("kiotDeviceId",rs.getString(4));
						data.put("customData",rs.getString(7));
						data.put("deviceTypeId",rs.getInt(5));
						data.put("deviceName",rs.getString(6));
						customerData.add(data);
						count++;
					}		
				}
				
				else {
					 query = "select distinct kiot_user_mappings.kiot_user_mapping_id,kiot_user_mappings.kiot_user_id,kiot_user_mappings.bearer_token, " + 
								"all_kiot_switches.kiot_device_id,user_dr_devices.device_type_id,user_dr_devices.device_name " + 
								",all_kiot_switches.custom_data " + 
								" from " + 
								"all_kiot_switches , kiot_user_mappings  , all_users,user_dr_devices  " + 
								"where all_users.user_id =  "+customerId+" and all_users.dr_contract_number=kiot_user_mappings.contract_number and " + 
								"user_dr_devices.user_id=all_users.user_id " + 
								"and kiot_user_mappings.kiot_user_mapping_id is not null and " + 
								"kiot_user_mappings.kiot_user_mapping_id = all_kiot_switches.kiot_user_mapping_id " + 
								"and user_dr_devices.user_dr_device_id = "+rs1.getInt("user_dr_device")+" " + 
								"and all_kiot_switches.id = user_dr_devices.port_number";
						
						pstmt = con.prepareStatement(query);
						// pstmt.setString(1,controllerId);
						ResultSet rs = pstmt.executeQuery();
						while (rs.next()) {
							HashMap<String,Object> data = new HashMap<String, Object>();
							data.put("kiotUserMappingId",rs.getInt(1));
							data.put("kiotUserId",rs.getString(2));
							data.put("bearerToken",rs.getString(3));
							data.put("kiotDeviceId",rs.getString(4));
							data.put("customData",rs.getString(7));
							data.put("deviceTypeId",rs.getInt(5));
							data.put("deviceName",rs.getString(6));
							customerData.add(data);
							count++;
						}		
					}
				
			}
			String query1 = "";
			
			if (count == 0) {
				 query="update event_customer_mapping set event_customer_status_id=12 where event_id=? and customer_id=?";
				  pstmt=ScheduleDAO.con.prepareStatement(query);
				  pstmt.setInt(1,eventId); 
				  pstmt.setInt(2,customerId); 
				  pstmt.execute();
			}
			
			return customerData;

		}

		
		public String getConnectionString(int customerId, int eventId) throws ClassNotFoundException, SQLException {
			PreparedStatement pstmt = null;
			int count=0;
			ArrayList<HashMap<String,Object>> customerData = new ArrayList<>();
			if (con == null) {
				con = JDBCConnection.getOracleConnection();
			}
			String connectionString = "";
			String query = "select dr_device_repository.connection_string from 	dr_device_repository ,all_users \n" + 
					"where all_users.user_id = "+customerId +" and all_users.dr_contract_number=dr_device_repository.contract_number";
			pstmt = con.prepareStatement(query);
			// pstmt.setString(1,controllerId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				count++;
				connectionString=rs.getString(1);
			}
			if (count ==0) {
				query="update event_customer_mapping set event_customer_status_id=11 where event_id=? and customer_id=?";
				  pstmt=ScheduleDAO.con.prepareStatement(query);
				  pstmt.setInt(1,eventId); 
				  pstmt.setInt(2,customerId); 
				  pstmt.execute();
			}
			
			return connectionString;

		}

		public int getEventCustomerStatus(int customerId, int eventId) throws ClassNotFoundException, SQLException {
			PreparedStatement pstmt = null;
			int customerStatus=0;
			ArrayList<HashMap<String,Object>> customerData = new ArrayList<>();
			if (con == null) {
				con = JDBCConnection.getOracleConnection();
			}
			String query="select event_customer_status_id from event_customer_mapping where event_id=? and customer_id=?";
			  pstmt=ScheduleDAO.con.prepareStatement(query);
			  pstmt.setInt(1,eventId); 
			  pstmt.setInt(2,customerId); 
			  ResultSet rs = pstmt.executeQuery();
			  while(rs.next()) {
				  customerStatus = rs.getInt("event_customer_status_id");
			 }
			
			return customerStatus;

		}


}
