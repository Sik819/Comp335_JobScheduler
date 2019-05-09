import java.awt.List;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.BufferedReader;

public class Client {
	Socket socket =  null;
	
	//job
    ArrayList<Job> listJob = new ArrayList<>();
    Job currentJob = new Job();
    
    //server
    ArrayList<Server> listServer = new ArrayList<>();

    //initial server state
    ArrayList<Server> initServer = new ArrayList<>();
    
    //get the largest server
    public String getLargest()
    {
    	return listServer.get(listServer.size()-1).serverType;
    }
    
    //get firsFit
    public String getBestFit()
    {
        int bestFit = Integer.MAX_VALUE;
        Server bestServer = null;
        int minAvail = Integer.MAX_VALUE;
        for(int i = 0; i < listServer.size(); i++) {
            Server thisServer = listServer.get(i);
            if(thisServer.compareJob(currentJob))
            {
                int fitVal = thisServer.core - currentJob.core;
                if (fitVal < bestFit || (fitVal == bestFit && thisServer.avbTime < minAvail)) {

                    bestFit = fitVal;
                    bestServer = thisServer;
                    minAvail = bestServer.avbTime;

                }
        }}
            if(bestServer!=null) {
             bestFit = Integer.MAX_VALUE;
             minAvail = Integer.MAX_VALUE;
                return bestServer.serverType + " " + bestServer.serverID;
            }
            else {
                bestServer = getInitServer();
                if(bestServer!=null)
                return bestServer.serverType + " " + bestServer.serverID;
                else
                    return null;
            }
    }
    



    public void getInitState(ArrayList<Server> list)
    {
        String lookup = "";
        for(Server ser : list)
        {
            if(!lookup.contains(ser.serverType))
            {
                lookup+=ser.serverType;
                initServer.add(ser);
            }
        }
    }

    public Server getInitServer()
    {
        for(Server ser : initServer)
        {
            if(ser.compareJob(currentJob))
            {
                return ser;
            }
        }
        return null;
    }

    //make byte array
    public byte[] sendToServer(String s)
    {
        String temp = s+"\n";
        byte[] bytes = temp.getBytes();
        return bytes;

    }
    
    //reads a message from the server and returns the message as a string
    public String readLine(Socket s) throws UnsupportedEncodingException, IOException
    {
        Job j = new Job();
        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));//receive buffer
        String line = input.readLine();

        if(line.contains(("JOBN"))) {
            addJob(line);
            System.out.println(currentJob.toString());
        }
        return line;
    }
    
    //schedule the job
    public void scheduleJob(PrintStream pr, Socket s, Integer useAlg) throws UnsupportedEncodingException, IOException //add parser par
    {
    	String str = null;
    	if (useAlg == null || useAlg != 1) //null or not ff
    		str = listServer.get(listServer.size()-1).serverType+" 0"; //scheduling to the largest
    	else if (useAlg == 1)
    		str = getBestFit();
    	pr.write(sendToServer("SCHD "+currentJob.jobID+" "+str));
        if (readLine(s).contains("OK"))
        {
            currentJob.jobDone();
            listServer.clear();
        }
    }
    
    //getting job IDs
    public void addJob(String str){
    	String[] jobStr = str.split(" ");
    	Job job = new Job(jobStr);
    	listJob.add(job);
    	currentJob = job;
    			
    	
//        if(s.length()<=1)
//            return j;
//        if(!s.contains(" ")) {
//            j.add(s);
//            return j;
//        }
//        j.add(s.substring(0,s.indexOf(' ')));
//        return  seperateStrings(j,s.substring(s.indexOf(' ')+1));
    	
    }
    
    //send OK until "."
    public void okSender(PrintStream send) throws IOException {
    	while(true)
        {
        	String serverString = readLine(socket); //get server info or "DATA"
        	//System.out.println(serverString);
        	if(serverString.contains("DATA"))
        	{
        		send.write(sendToServer("OK"));
        	}
        	else if(serverString.equals("."))
            {
        		Collections.sort(listServer, new CompareByCore());
                break;
            }
        	else
        	{
            	String[] serverDetails = serverString.split(" ");
                Server server = new Server(serverDetails);
                listServer.add(server);
                send.write(sendToServer("OK"));
        	}
        }
    	
    }

    //constructor client
    
    public Client(String ip, int port, Integer algNumber) throws UnknownHostException, IOException
    {
        socket = new Socket(ip, port);
        System.out.println("Connected with server");
        System.out.println("-----------------------------");
        System.out.println();
        
        //BufferedReader rcv = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintStream send = new PrintStream(socket.getOutputStream()); //send message

        //protocol
        ArrayList<String> al = new ArrayList<String>();
        al.add("HELO");
        al.add("AUTH user");
        al.add("REDY");
        for(String s : al)
        {
            send.write(sendToServer(s));
            String reply = readLine(socket);
        }

        send.write(sendToServer("RESC Avail "+currentJob.getJobRESC()));
        okSender(send);  //get all servers
        
        //do the scheduling
        scheduleJob(send, socket, algNumber);
        while(true)
        {
        	send.write(sendToServer("REDY"));
        	String str = readLine(socket);
        	if(str.contains("JOBN"))
        	{
        		send.write(sendToServer("RESC Avail "+currentJob.getJobRESC()));
        		okSender(send);
                getInitState(listServer);
        		scheduleJob(send, socket, algNumber);
        	}
        	else if(str.equals("NONE"))
        	{
        		break;
        	}
        }
        send.write(sendToServer("QUIT"));
        
        System.out.println();
		System.out.println("Connection closing");
		System.out.println("-----------------------------");
        socket.close();
        send.close();
        
}
    public static void main(String[] args) throws UnknownHostException, IOException {
    	//alg values
    	//	ff = 1
    	//	bf = 
    	//	wf = 
    	
    	Integer algorithm = null;
    	if(args[0].contains("-a") && args[1].contains("bf"))
    		algorithm = 1;

        Client cl = new Client("127.0.0.1", 8096, algorithm);

    }
}