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


import java.io.BufferedReader;

public class Client extends Job {
    ArrayList<Job> listJOB = new ArrayList<>();

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
        System.out.println("Received "+line);

        if(line.contains(("JOBN"))) {
            seperateStrings(j, line);
            listJOB.add(j);
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


    Socket socket =  null;
    Job newJob = new Job();


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
            String reply = readLine(socket);}
        send.write(sendToServer("RESC Type 4xlarge"));
        String reply = readLine(socket);
        while(true)
        {
            send.write(sendToServer("OK"));
            reply = readLine(socket);
            if(reply.equals("."))
            {
                break;
            }
        }
        System.out.println(listJOB.get(0));
        System.out.println(listJOB.get(0).get(1));
        send.write(sendToServer("SCHD "+listJOB.get(0).get(2)+" 4xlarge 0"));


        socket.close();




    }
    public static void main(String[] args) throws UnknownHostException, IOException {
        Client cl = new Client("127.0.0.1", 8096);

    }
}