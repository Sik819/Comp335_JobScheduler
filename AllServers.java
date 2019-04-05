import java.util.ArrayList;

public class AllServers {
	StringBuilder sb = new StringBuilder();
	String lookUp = "";
	ArrayList<ServerType> servers = new ArrayList<>();
	int numType = servers.size();
	
	public AllServers()
	{
		//do nothing
	}
	public void addIfNotExist(String type, int limit, int boot, float rate, int core, int memory, int disk)
	{
		if(!this.lookUp.contains(type))
		{
			this.sb.append(type);
			lookUp = sb.toString();
			ServerType temp = new ServerType(type, limit, boot, rate, core, memory, disk);
			this.servers.add(temp);
			System.out.println("------Server Added------");
		}
		numType = servers.size();
	}
	public String getLargest()
	{
		String str = "";
		int tempCore = servers.get(0).coreCount;
		for(ServerType type : servers)
		{
			if(type.coreCount>tempCore)
			{
				tempCore = type.coreCount;
				str = type.type;
			}
		}
		return str;
		
	}
}
