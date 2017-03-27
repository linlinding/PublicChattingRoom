package com.trust.clientCrypto;

import java.net.Socket;

public class authenticatorclient {
	public String userid;
	public String tgsid;
	public String serverid;
	public String back;
	public String error1 = "unauthorized client";
	public String error2 = "unauthorized AS";
	public String error3 = "unauthorized TGS";
	public String error4 = "unauthorized server";
	public Socket client2ServerSocket;
	
	public authenticatorclient(String userid, String tgsid, String serverid, Socket client2ServerSocket) {
		this.userid = userid;
		this.tgsid = tgsid;
		this.serverid = serverid;
		this.client2ServerSocket=client2ServerSocket;
	}
	
	public String authentication(){
		Client client = new Client(userid,tgsid,serverid,client2ServerSocket);
		String result = client.requestAS();
		if(result.equals(error1)){
		back = "Access rejected by AS";	
		}else{
			String result2 = client.requestTGS(result);
			if(result2.equals(error1)){
				back = "Access rejected by TGS";
			}else{
				if(result.equals(error2)){
					back = error2;
				}else{
					String result3 = client.requestServer(result2);
					back = result3;
					if(result.equals(error1)){
						back = "Access reject by server";
					}
					if(result.equals(error3)){
						back = error3;
					}
					if(result.equals(error4)){
						back = error4;
					}
				}
			}
		}
		return back;
		
	}

}
