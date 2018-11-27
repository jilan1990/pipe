package luluouter;

import luluouter.msg.inner.InnerServer;

public class OuterMaster {

	public static void main(String[] args) {

        // System.out.println("outer ...\n");
        // OuterServer outerServer = new OuterServer();
        // outerServer.init();
        int port = 5273;
        if (args != null && args.length >= 1) {
            port = Integer.parseInt(args[0]);
        }
        
        System.out.println("inner ...\n");
        InnerServer innerServer = new InnerServer(port);
		innerServer.init();
	}

}
