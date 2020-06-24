package it.polito.tdp.crimes.db;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Event;

public class TestDAO {

	public static void main(String[] args) {
		EventsDAO dao = new EventsDAO();
		
		List<String> typeID = new ArrayList<>(dao.getTypesIDByYear(2015, "other-crimes-against-persons"));
		for(String s : typeID) {
			System.out.println(s.toString() + "\n");
		}
		
	}

}
