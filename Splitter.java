
public class Splitter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "4xlarge 6 0 29658 128 1792000 4096000";
		String[] arr = str.split(" ");
		System.out.println(arr.length);
		for (String s : arr)
			System.out.println(s);

	}

}
