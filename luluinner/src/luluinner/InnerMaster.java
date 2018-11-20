package luluinner;

import luluinner.upper.UpperAgentMaster;

public class InnerMaster {

    public static void main(String[] args) {
        UpperAgentMaster.getInstance().init();
        // MessageMaster.getInstance().addMessageWorker(agent);
    }

}
