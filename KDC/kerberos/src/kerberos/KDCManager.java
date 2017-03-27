package kerberos;


import kerberos.AS;
import kerberos.TGS;

public class KDCManager {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		KDCManager startKDC=new KDCManager();
		System.out.println("start KDC");
		//startKDC.authentication();	
		AS as = new AS();
	    TGS tgs = new TGS();
	    while(true){
	    	as.answer();
		    tgs.answer();
	    }	   	
		
	}

	public KDCManager() {
		// TODO Auto-generated constructor stub
	}
	
	public void authentication() {
		AS as = new AS();
		as.answer();
		TGS tgs = new TGS();
		tgs.answer();

	}
}
