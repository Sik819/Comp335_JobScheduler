
public class Splitter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
    	String[] servers ={"medium", "0", "0", "2760", "4", "20000", "64000"};
    	Server temp = new Server(servers);
    	String[] jobs = {"JOBN", "2700", "65", "1057", "3", "2000", "4700"};
    	Job a = new Job(jobs);
    	System.out.println(temp.compareJob(a));

	}

}
