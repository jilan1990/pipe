package luluouter;

import luluouter.inner.InnerServer;
import luluouter.outer.OuterServer;

public class OuterMaster {

	public static void main(String[] args) {

		System.out.println("outer服务器启动...\n");    
		OuterServer outerServer = new OuterServer();
		outerServer.init();
        
		System.out.println("inner服务器启动...\n");    
		InnerServer innerServer = new InnerServer();
		innerServer.init();
	}

}
