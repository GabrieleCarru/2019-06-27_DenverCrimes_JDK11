package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;


public class EventsDAO {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<String> getTypes() {
		
		String sql = "select distinct(`offense_category_id`) as type " + 
				"from events " + 
				"order by `offense_category_id` ";
		
		List<String> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				result.add(rs.getString("type"));
			}
			
			conn.close();
			return result;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public List<Integer> getYears() {
		
		String sql = "select distinct(year(`reported_date`)) as anno " + 
				"from events " + 
				"order by year(`reported_date`) "; 
		
		List<Integer> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				result.add(rs.getInt("anno"));
			}
			
			conn.close();
			return result;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public List<String> getTypesIDByYear(Integer anno, String type) {
		
		String sql = "select distinct(`offense_type_id`) as id" + 
				"from events " + 
				"where `offense_category_id` = ? " + 
				"and year(`reported_date`) = ? " + 
				"order by `offense_type_id` "; 
		
		List<String> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				result.add(rs.getString("id"));
			}
			
			conn.close();
			return result;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<Adiacenza> getAdiacenzeVerticiArco(String categoria, Integer anno) {
		
		String sql = "select e1.`offense_type_id` as id1, e2.`offense_type_id` as id2, " + 
				"count(distinct(e1.`district_id`)) as peso " + 
				"from events e1, events e2 " + 
				"where e1.`offense_type_id` > e2.`offense_type_id` " + 
				"and e1.`district_id` = e2.`district_id` " + 
				"and e1.`offense_category_id` = ? " + 
				"and e2.`offense_category_id`= ? " + 
				"and year(e1.`reported_date`) = ? and year(e2.`reported_date`) = ? " + 
				"group by e1.`offense_type_id`, e2.`offense_type_id` "; 
		
		List<Adiacenza> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, categoria);
			st.setString(2, categoria);
			st.setInt(3, anno);
			st.setInt(4, anno);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				result.add(new Adiacenza(rs.getString("id1"), rs.getString("id2"), rs.getInt("peso")));
			}
			
			conn.close();
			return result;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}



}
