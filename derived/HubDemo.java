package derived;
import java.util.ArrayList;
import base.*;

public class HubDemo extends Hub
{
    private ArrayList<Truck> trucks; 

    /*--------- Constructor -------------*/
    public HubDemo(Location loc)
    {
        super(loc);
        this.trucks = new ArrayList<>();
    }
    /*--------- Adds truck to the hub if capacity is not reached -------------*/
    @Override
    public synchronized boolean add(Truck truck)
    {
        if(this.getCapacity()>this.getTrucks().size())
        {
            if(this.getTrucks().contains(truck))
                return true;
            this.getTrucks().add(truck);
            return true;
        }
        else
        {
            return false;
        }
    }

    /*--------- Removes truck from hub -------------*/
    @Override
    protected synchronized void remove(Truck truck)
    {
        if(this.getTrucks().size()!=0)
            this.getTrucks().remove(truck);
    }

    /*--------- Updates the state of the hub instance 
        by pushing out trucks to a highway in a path to the hub 
                nearest to its destination-------------*/
    @Override
    protected void processQ(int deltaT)
    {
        Truck arr[]=this.trucks.toArray(new Truck[this.trucks.size()]);
        /*--------- Find NextHighway for every hub and try to push it out -------------*/
        for(Truck truck: arr)
        {
            Highway nexthwy=this.getNextHighway(truck.getLastHub(),Network.getNearestHub(truck.getDest()));
            if(nexthwy==null)
            {
                continue;
            }
            
            if(nexthwy.hasCapacity())
            {
                nexthwy.add(truck);
                truck.enter(nexthwy);
                this.remove(truck);
            }
        }
    }

    /*--------- returns next higway for a truck -------------*/
    @Override
    public Highway getNextHighway(Hub last, Hub dest)
    {
        if(this.equals(dest))
            return null;
        ArrayList<Hub> map = new ArrayList<>();
        ArrayList<Highway> path = new ArrayList<>();
        map.add(last);
        DFS(this,map,dest,path);
        if(path.size()==0)
            return null;
        return path.get(path.size()-1);
    }

    private ArrayList<Truck> getTrucks()
    {
        return this.trucks;
    }

    /*--------- DFS to find a valid path -------------*/
    private void DFS(Hub v, ArrayList<Hub> map,Hub dest, ArrayList<Highway> path) 
    { 
        map.add(v); 
        if(dest.equals(v))
        {
            return;
        }
        
        for(Highway a: v.getHighways()) 
        { 
            Hub n = a.getEnd(); 
            if (!map.contains(n)) 
            {    
                DFS(n,map,dest,path); 
            }
            if(map.contains(dest))
            {
                path.add(a);
                return;
            }

        } 
    } 

}
