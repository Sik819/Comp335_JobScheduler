import java.util.ArrayList;

public class Job extends ArrayList<String>{
    public boolean scheduled = false;
    public void jobDone()
    {
        this.scheduled = true;
    }


    public Job()
    {

    }
}