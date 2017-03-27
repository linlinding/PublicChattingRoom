package com.trust.server.model;

import java.util.*;
public class ManageClientThread {
	public static Map hm = new HashMap<String, ServerClientThread>();
	
	public static void addClientThread(String username, ServerClientThread sct){
		hm.put(username, sct);
		
	}
	
	public static ServerClientThread getClientThread(String username){
		return (ServerClientThread)hm.get(username);
	}
	
	public static Set<String> getKeySets(){
		return hm.keySet();
	}
	
}
