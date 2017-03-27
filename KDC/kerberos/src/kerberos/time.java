package kerberos;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class time {

	public time() {
		// TODO Auto-generated constructor stub
	}

	public String begintime(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(date);
		return str;
		
	}
	
	public String endtime(){
		Date d = new Date();  
        Calendar now = Calendar.getInstance();  
        now.setTime(d);  
        now.set(Calendar.HOUR, now.get(Calendar.HOUR) + 1);  
        now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + 30);  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");  
        String str = sdf.format(now.getTime());
        return str;
          
		
	}
	
	public void print(String s){
		int n = s.length();
		for(int i = 0; i < n; i += 50){
			if(i + 50 >= n){
				System.out.println(s.substring(i, n));
			}
			else System.out.println(s.substring(i, i + 50));
		}
	}

}