import java.util.ArrayList;

public class Job{
	int subTime;
	int jobID;
	int est;
	int core;
	int memory;
	int dsk;
	boolean scheduled;
	
	public Job()
	{
		//initiate an obj with null value
	}
    
    public Job(String[] arr)
    {
    	if (arr.length != 7)
    	{
    		System.out.println("Job can not be added. Insufficient value received");
    		System.out.println("--------------------------------------------------");
    	}
    	this.subTime = Integer.parseInt(arr[1]);
    	this.jobID = Integer.parseInt(arr[2]);
    	this.est = Integer.parseInt(arr[3]);
    	this.core = Integer.parseInt(arr[4]);
    	this.memory = Integer.parseInt(arr[5]);
    	this.dsk = Integer.parseInt(arr[6]);
    	this.scheduled = false;
    	
    }
    public void jobDone()
    {
        this.scheduled = true;
    }
}