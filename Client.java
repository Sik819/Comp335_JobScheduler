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

public class Client extends Job {
	String temp1 = "";
    ArrayList<Job> listJOB = new ArrayList<>();
    Job currentJob = new Job();
 //   static Parser p = new Parser();
    Socket socket =  null;

    //make byte array
    public byte[] sendToServer(String s)
    {
        String temp = s+"\n";
        byte[] bytes = temp.getBytes();
        return bytes;

    }
    //read line
    public String readLine(Socket s) throws UnsupportedEncodingException, IOException
    {
        Job j = new Job();
        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));//receive buffer
        String line = input.readLine();
//        System.out.println("Received "+line);

        if(line.contains(("JOBN"))) {
            seperateStrings(j, line);
            listJOB.add(j);
            currentJob = j;
        }

        //		if(line.contains("JOBN"))
//		{
//			int length = 0;
//			int begin = 0;
//			while(length <= line.length())
//			{
//				char ch = line.charAt(length);
//				if(ch == ' ')
//				{
//					j.jobAl.add(line.substring(begin, length));
//				}
//				length++;
//				length = begin;
//			}
//			return j;
//		}
        return line;
    }
    
    //schedule the job
    public void scheduleJob(PrintStream pr, Socket s, Job j) throws UnsupportedEncodingException, IOException //add parser par
    {
    	pr.write(sendToServer("SCHD "+j.get(2)+" "+temp1+" 0"));
        if (readLine(s).contains("OK"))
        {
            j.jobDone();
        }
    }
    //getting job IDs
    public Job seperateStrings(Job j , String s){
        if(s.length()<=1)
            return j;
        if(!s.contains(" ")) {
            j.add(s);
            return j;
        }
        j.add(s.substring(0,s.indexOf(' ')));
        return  seperateStrings(j,s.substring(s.indexOf(' ')+1));
    }
    
    //send ok until .
    public void okSender(PrintStream send, String reply) throws IOException {
    	ArrayList<String> check = new ArrayList<>();
    	while(true)
        {
            send.write(sendToServer("OK"));
            reply = readLine(socket);
            check.add(reply);
            if(reply.equals("."))
            {
            	String temp = check.get(check.size()-2);
            	temp1 = temp.substring(0, temp.indexOf(" "));
                break;
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
//            if(s.equalsIgnoreCase("AUTH USER") && reply.equalsIgnoreCase(("OK"))) {
//                try {
//                    p.parse();
//                } catch (ParserConfigurationException | SAXException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        send.write(sendToServer("RESC All"));
        String reply = readLine(socket); //get server info
        okSender(send, reply);  //get all server info
        System.out.println("Job(s) :"+currentJob);
        //do the scheduling
        send.write(sendToServer("SCHD "+currentJob.get(2)+" "+temp1+" 0"));
        //scheduleJob(send, socket, currentJob, p);
        while(true)
        {
        	send.write(sendToServer("REDY"));
        	String str = readLine(socket);
        	if(str.contains("JOBN"))
        	{
        		send.write(sendToServer("RESC All"));
        		readLine(socket);
        		okSender(send, reply);
        		scheduleJob( send, socket, currentJob);
        	}
        	if(str.equals("NONE"))
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