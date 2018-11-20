package luluouter;

import luluouter.inner.InnerServer;
import luluouter.outer.OuterServer;

public class OuterMaster {

	public static void main(String[] args) {

		System.out.println("outer����������...\n");    
		OuterServer outerServer = new OuterServer();
		outerServer.init();
        
		System.out.println("inner����������...\n");    
		InnerServer innerServer = new InnerServer();
		innerServer.init();
	}

}
