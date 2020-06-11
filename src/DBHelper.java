import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class DBHelper {

	static Connection con;
	 public String getPlayerId(String email) throws SQLException, ClassNotFoundException
		{
			//JDBCConnection connref =new JDBCConnection();
		 if (ScheduleDAO.con == null ) {
				con = JDBCConnection.getOracleConnection();
		 }
		 Connection	con = JDBCConnection.getOracleConnection(); 
			PreparedStatement pstmt = null;
			String playerId="";
			if(con!=null)
			{
					String query="select extra_info1 from wh_user where user_id=?";
					 pstmt=con.prepareStatement(query);
					 pstmt.setString(1,email);
					 ResultSet rs= pstmt.executeQuery();
					 while(rs.next())
					 {
						 playerId=rs.getString(1);
					 }
					 
					
			}
			return playerId;
			}

	 public String getDeviceName(String controllerId,int deviceId) throws SQLException, ClassNotFoundException
		{
			//JDBCConnection connref =new JDBCConnection();
		 if (ScheduleDAO.con == null ) {
				con = JDBCConnection.getOracleConnection();
		 } 
			PreparedStatement pstmt = null;
			String deviceName="";
			if(con!=null)
			{
					String query="select device_name from wh_device where device_id="+deviceId+ " and controller_id='"+controllerId+"'";
					 pstmt=con.prepareStatement(query);
					 
					 ResultSet rs= pstmt.executeQuery();
					 while(rs.next())
					 {
						 deviceName=rs.getString(1);
					 }
					 
					
			}
			return deviceName;
			}

	 
	 public void updateEventCustomer(double meterReading, int eventId, int userId) throws SQLException, ClassNotFoundException
		{
		try {	
		 //JDBCConnection connref =new JDBCConnection();
		 if (ScheduleDAO.con == null ) {
				con = JDBCConnection.getOracleConnection();
		 } 
			PreparedStatement pstmt = null;
			double actualPower=0;
			if(ScheduleDAO.con!=null)
			{
					String query="update event_customer_mapping set customer_net_meter_reading_e=? ,actual_power=(100-(?-customer_net_meter_reading_s)*4),event_customer_status_id=8 where event_id =? and customer_id=?";
					  pstmt=ScheduleDAO.con.prepareStatement(query);
					  pstmt.setDouble(1,meterReading);
					  pstmt.setDouble(2,meterReading);
					  pstmt.setInt(3,eventId); 
					  pstmt.setInt(4,userId); 
					  pstmt.execute();
					  
					  
					  query="select actual_power from event_customer_mapping where event_id=? and customer_id=?";
					  pstmt=ScheduleDAO.con.prepareStatement(query);
					  pstmt.setInt(1,eventId); 
					  pstmt.setInt(2,userId); 
					  ResultSet rs=pstmt.executeQuery();
					  while(rs.next()) {
						  actualPower=rs.getDouble(1);
					  }
					  query="update all_events set actual_power=actual_power+? where event_id=?";
					  pstmt=ScheduleDAO.con.prepareStatement(query);
					  pstmt.setDouble(1,actualPower);
					  pstmt.setInt(2,eventId); 
					  pstmt.execute();
					 
					
			}
			}
		
	 catch(Exception e) {
		 e.printStackTrace();
	 }

		}
	 
}
