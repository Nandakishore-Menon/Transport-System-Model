package derived;
import base.*;
import java.util.ArrayList;

public class TruckDemo extends Truck
{

    private Hub lasthub,nexthub,currenthub;
    private int time;
    private Highway hwy;
    private static int stohspeed=15;
    // private static int id=1; //used in case each truck has unique name
    // private int truckid;

     /*--------- returns truck name-------------*/
    @Override
    public String getTruckName()
    {
        // return super.getTruckName()+"19057 "+Integer.toString(this.truckid);
        return super.getTruckName()+"19057 ";
    }
     /*--------- Constructor -------------*/
    public TruckDemo()
    {
        super();
        // this.truckid=id;
        // id+=1;
        this.time=0;
        this.setLastHub(null);
        this.setNextHub(null);
        this.setCurrentHub(null);
        this.setHighway(null);
    }
     /*--------- adds the truck instance to highway -------------*/
    @Override
    public void enter(Highway hwy)
    {
        this.setLastHub(hwy.getStart());
        this.setNextHub(hwy.getEnd());
        this.setCurrentHub(null);
        this.setHighway(hwy);
        hwy.add(this);
    }

     /*--------- Updates the state of the truck instance -------------*/
    @Override
    protected void update(int deltaT)
    {
        this.addTime(deltaT);
        /*--------- Truck's state changes only after a certain Start Time -------------*/
        if(this.getStartTime()<=this.getTime())
        {
             /*--------- When truck is on a highway-------------*/
            if(this.getCurrentHub()==null && this.getHighway()!=null)
            {
                 /*--------- Computes the change in co-ordinates -------------*/
                int x = this.getNextHub().getLoc().getX();
                int y = this.getNextHub().getLoc().getY();
                int deltax = x - this.getLastHub().getLoc().getX();
                int deltay = y - this.getLastHub().getLoc().getY();
                double sin = (deltay)/Math.sqrt(Math.pow(deltax,2)+Math.pow(deltay,2));
                double cos = (deltax)/Math.sqrt(Math.pow(deltax,2)+Math.pow(deltay,2));
                int dx,dy;
                if(this.getTime()%(deltaT*2)==0)
                {
                    dx = (int)Math.round(this.getHighway().getMaxSpeed()*deltaT*cos/1000.0);
                    dy = (int)Math.round(this.getHighway().getMaxSpeed()*deltaT*sin/1000.0);
                }
                else
                {
                    dx = (int)Math.floor(this.getHighway().getMaxSpeed()*deltaT*cos/1000.0);
                    dy = (int)Math.floor(this.getHighway().getMaxSpeed()*deltaT*sin/1000.0);
                }
                Location move = new Location(this.getLoc().getX()+dx,this.getLoc().getY()+dy);
                if(move.distSqrd(this.getLastHub().getLoc())>=this.getNextHub().getLoc().distSqrd(this.getLastHub().getLoc()))
                {
                    /*--------- If the hub we are approaching is the Hub closest 
                    to destination, then dont add to hub, and jump to dest station.-------------*/
                    if(this.getNextHub().equals(NetworkDemo.getNearestHub(this.getDest())))
                    {
                        this.setLoc(this.getNextHub().getLoc());
                        this.getHighway().remove(this);
                        this.setHighway(null);
                        this.setLastHub(this.getNextHub());
                        this.setNextHub(null);
                        return;
                    }
                     /*--------- else add truck to hub and remove it from highway-------------*/
                    this.setLoc(this.getNextHub().getLoc());
                    if(this.getNextHub().add(this))
                    {
                        this.setCurrentHub(this.getNextHub());
                        this.getHighway().remove(this);
                        this.setHighway(null);
                        return;
                    }
                }
                this.setLoc(move);

            }
             /*--------- Either truck is at Source or Destination -------------*/
            if(this.getHighway()==null && this.getCurrentHub()==null)
            {
                /*--------- Do nothing if it is at dest -------------*/
                if(this.getLoc().getX()==this.getDest().getX() && this.getLoc().getY()==this.getDest().getY())
                {
                    return;
                }
                if(this.getNextHub()==null && this.getLastHub()==null)
                {
                    /*--------- If at source, add it to nearest hub -------------*/
                    Hub firsthub= NetworkDemo.getNearestHub(this.getSource());
                    if(firsthub.equals(NetworkDemo.getNearestHub(this.getDest())))
                    {
                        this.setNextHub(firsthub);
                        this.setLastHub(firsthub);
                        // this.setLoc(this.getDest());
                        return;
                    }
                    /*--------- Move to the hub closest to source -------------*/
                    int x = firsthub.getLoc().getX();
                    int y = firsthub.getLoc().getY();
                    int deltax = x - this.getLoc().getX();
                    int deltay = y - this.getLoc().getY();
                    double sin = (deltay)/Math.sqrt(Math.pow(deltax,2)+Math.pow(deltay,2));
                    double cos = (deltax)/Math.sqrt(Math.pow(deltax,2)+Math.pow(deltay,2));
                    int dx,dy;
                    dx = (int)Math.floor(TruckDemo.stohspeed*deltaT*cos/1000.0);
                    dy = (int)Math.floor(TruckDemo.stohspeed*deltaT*sin/1000.0);
                    Location move = new Location(this.getLoc().getX()+dx,this.getLoc().getY()+dy);
                    if(move.distSqrd(firsthub.getLoc())<=30)
                    {
                        this.setLoc(firsthub.getLoc());
                        if(firsthub.add(this))
                        {
                            this.setCurrentHub(firsthub);
                            this.setLastHub(firsthub);
                        }
                        return;
                    }
                    this.setLoc(move);


                    // this.setLoc(firsthub.getLoc());
                    
                }
                else
                {
                    /* Move to dest from hub */
                    int x = this.getDest().getX();
                    int y = this.getDest().getY();
                    int deltax = x - this.getLoc().getX();
                    int deltay = y - this.getLoc().getY();
                    double sin = (deltay)/Math.sqrt(Math.pow(deltax,2)+Math.pow(deltay,2));
                    double cos = (deltax)/Math.sqrt(Math.pow(deltax,2)+Math.pow(deltay,2));
                    int dx,dy;
                    dx = (int)Math.round(TruckDemo.stohspeed*deltaT*cos/1000.0);
                    dy = (int)Math.round(TruckDemo.stohspeed*deltaT*sin/1000.0);
                    Location move = new Location(this.getLoc().getX()+dx,this.getLoc().getY()+dy);
                    if(move.distSqrd(this.getDest())<=10)
                    {
                        this.setLoc(this.getDest());
                        return;
                    }
                    this.setLoc(move);

                }
            }

        }
    }

    @Override
    public Hub getLastHub()
    {
        return this.lasthub;
    }
    private void setLastHub(Hub hub)
    {
        this.lasthub=hub;
    }
    private Hub getNextHub()
    {
        return this.nexthub;
    }
    private void setNextHub(Hub hub)
    {
        this.nexthub=hub;
    }
    private Hub getCurrentHub()
    {
        return this.currenthub;
    }
    private void setCurrentHub(Hub hub)
    {
        this.currenthub=hub;
    }
    private Highway getHighway()
    {
        return this.hwy;
    }
    private void setHighway(Highway hwy)
    {
        this.hwy=hwy;
    }
    private int getTime()
    {
        return this.time;
    }
    private void addTime(int time)
    {
        this.time+=time;
    }

}
