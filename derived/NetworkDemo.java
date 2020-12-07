package derived;
import java.util.ArrayList;
import base.*;
public class NetworkDemo extends Network
{
    private ArrayList<Hub> hubs;
    private ArrayList<Highway> highways;
    private ArrayList<Truck> trucks;

    /*--------- Constructor -------------*/
    public NetworkDemo()
    {
        super();
        this.hubs=new ArrayList<>();
        this.highways=new ArrayList<>();
        this.trucks=new ArrayList<>();
    }
    
    @Override
    public void add(Hub hub)
    {
        this.hubs.add(hub);
    }

    @Override
    public void add(Highway hwy)
    {
        this.highways.add(hwy);
    }

    @Override
    public void add(Truck truck)
    {
        this.trucks.add(truck);
    }

    /*--------- Calls start on Trucks and Hubs -------------*/
    @Override
    public void start()
    {
        for(int i=0;i<this.hubs.size();i++)
        {
            this.hubs.get(i).start();
        }
        for(int i=0;i<this.trucks.size();i++)
        {
            this.trucks.get(i).start();
        }
    }

    /*--------- Calls start on Trucks and Hubs -------------*/
    @Override
    public void redisplay(Display disp)
    {
        for(int i=0;i<this.trucks.size();i++)
        {
            this.trucks.get(i).draw(disp);
        }
        for(int i=0;i<this.hubs.size();i++)
        {
            this.hubs.get(i).draw(disp);
        }
        for(int i=0;i<this.highways.size();i++)
        {
            this.highways.get(i).draw(disp);
        }

    }

    /*--------- returns nearest hub to a given location -------------*/
    @Override
    protected Hub findNearestHubForLoc(Location loc)
    {
        int min=Integer.MAX_VALUE;
        Hub min_hub=null;
        for(Hub hub: this.hubs)
        {
            if(min>hub.getLoc().distSqrd(loc))
            {
                min=hub.getLoc().distSqrd(loc);
                min_hub=hub;
            }
        }
        return min_hub;
    }
}
