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
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.io.BufferedReader;

public class Client {
	Socket socket =  null;
	
	//job
    ArrayList<Job> listJob = new ArrayList<>();
    Job currentJob = new Job();
    
    //server
    ArrayList<Server> listServer = new ArrayList<>();
    
    //get the largest server
    public String getLargest()
    {
    	return listServer.get(listServer.size()-1).serverType;
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
//      System.out.println("Received "+line);

        if(line.contains(("JOBN"))) {
            addJob(line);
        }
        return line;
    }
    
    //schedule the job
    public void scheduleJob(PrintStream pr, Socket s) throws UnsupportedEncodingException, IOException //add parser par
    {
    	String str = listServer.get(listServer.size()-1).serverType; //scheduling to the largest
    	pr.write(sendToServer("SCHD "+currentJob.jobID+" "+str+" 0"));
        if (readLine(s).contains("OK"))
        {
            currentJob.jobDone();
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

    //constructor
    public Client(String ip, int port) throws UnknownHostException, IOException
    {
        socket = new Socket(ip, port);
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
            System.out.println("Sending "+s);
            String reply = readLine(socket);
        }
        
        System.out.println(currentJob.toString());
        send.write(sendToServer("RESC Avail "+currentJob.getJobRESC()));
        okSender(send);  //get all servers
        
        //do the scheduling
        scheduleJob(send, socket);
        while(true)
        {
        	send.write(sendToServer("REDY"));
        	String str = readLine(socket);
        	if(str.contains("JOBN"))
        	{
        		send.write(sendToServer("RESC Avail "+currentJob.getJobRESC()));
        		okSender(send);
        		scheduleJob(send, socket);
        	}
        	else if(str.equals("NONE"))
        	{
        		break;
        	}
        }
        send.write(sendToServer("QUIT"));
		System.out.println("Connection closing");
		System.out.println("-----------------------------");
        socket.close();
        send.close();
        
}
    public static void main(String[] args) throws UnknownHostException, IOException {
//        if(args.length!=1)
//            throw new RuntimeException("Enter file path of your server");
//        else
//            p = new Parser(args);
        Client cl = new Client("127.0.0.1", 8096);

    }
}