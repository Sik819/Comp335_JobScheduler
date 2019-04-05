import java.util.ArrayList;

import org.w3c.dom.Element;

public class ServerType {
    String type;
    int limit;
    int bootupTime;
    float rate;
    int coreCount;
    int memory;
    int disk;
    ArrayList<Server> listServer = new ArrayList<>();
    
    public ServerType()
    {
    	this.type = null;
    }
    public ServerType(String type, int limit, int boot, float rate, int core, int memory, int disk)
    {
    	this.type = type;
        this.limit = limit;
        this.bootupTime = boot;
        this.rate = rate;
        this.coreCount = core;
        this.memory = memory;
        this.disk = disk;
        this.listServer = null;
    }
}
