package derived;
import base.*;
import java.util.ArrayList;

public class HighwayDemo extends Highway
{
    private ArrayList<Truck> trucks;

    /*--------- Constructor -------------*/
    public HighwayDemo()
    {
        super();
        this.trucks=new ArrayList<>();
    }

    /*--------- Checks if highway's capacity is not exceeded -------------*/
    @Override
    public synchronized boolean hasCapacity()
    {
        if(this.trucks.size()<this.getCapacity())
            return true;
        else
            return false;
    }

    /*--------- Adds truck to highway if there is capacity -------------*/
    @Override
    public synchronized boolean add(Truck truck)
    {
        if(this.hasCapacity())
        {
            if(this.trucks.contains(truck))
                return true;
            this.trucks.add(truck);
            return true;
        }
        return false;
    }

    /*--------- Removes truck from highway -------------*/
    @Override
    public synchronized void remove(Truck truck)
    {
        this.trucks.remove(truck);
    }
}
