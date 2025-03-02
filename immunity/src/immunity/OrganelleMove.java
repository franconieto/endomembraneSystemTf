package immunity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;

public class OrganelleMove {

	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	private static List<MT> mts;
	public static double cellLimit = 3 * Cell.orgScale;
	
	public static void moveTowards(Endosome endosome) {

		if ( endosome.area >= Cell.minCistern/20// minimal cistern Golgi absolute Scale hacer constante
				&& isGolgi(endosome))
		{ // test if it is Golgi
//			System.out.println(endosome.heading + " INITIAL HEADING");
//			endosome.heading = -90;
			moveCistern(endosome);		
			NdPoint myPoint = space.getLocation(endosome);
			double x = myPoint.getX();
			endosome.setXcoor(x);
			double y = myPoint.getY();
			endosome.setYcoor(y);
		}
		else {
			moveNormal(endosome);
			NdPoint myPoint = space.getLocation(endosome);
			double x = myPoint.getX();
			endosome.setXcoor(x);
			double y = myPoint.getY();
			endosome.setYcoor(y);
		}
	}
	
	
	private static boolean isGolgi(Endosome endosome) {
		double areaGolgi = 0d;
		for (String rab : endosome.rabContent.keySet()){
			String name = ModelProperties.getInstance().rabOrganelle.get(rab);
			if (name.contains("Golgi")) {areaGolgi = areaGolgi + endosome.rabContent.get(rab);} 
		}
		boolean isGolgi = false;
		if (areaGolgi/endosome.area >= 0.5) {
			isGolgi = true;
		}
		return isGolgi;
	}


	private static void moveCistern(Endosome endosome) {
		double scale = Cell.orgScale;
		space = endosome.getSpace();
		grid = endosome.getGrid();		
		String maxRab = Collections.max(endosome.rabContent.entrySet(), Map.Entry.comparingByValue()).getKey();
		String organelleName = ModelProperties.getInstance().rabOrganelle.get(maxRab);
		double between = 4*scale;//distance between cisterna Math.random();
		double high = 29;//distance from the bottom
//		endosome.setHeading(-90d);// = -90d;			
//		System.out.println(endosome.heading + " final HEADING");
		
		if (organelleName.contains("cisGolgi")) {
			space.moveTo(endosome, 25, between*1+high);
			grid.moveTo(endosome, 25, (int)(between*1+high));
		}
		if (organelleName.contains("medialGolgi")) {
			space.moveTo(endosome, 25, between*2+high);
			grid.moveTo(endosome, 25, (int)(between*2+high));
		}
		if (organelleName.contains("transGolgi")) {
			space.moveTo(endosome, 25, between*3+high);
			grid.moveTo(endosome, 25, (int)(between*3+high));
		}
		
	}


	public static void moveNormal(Endosome endosome) {
//		if (endosome.area > 200000) return;
		space = endosome.getSpace();
		grid = endosome.getGrid();
		/*
		 * Direction in Repast 0 to the right 180 to the left -90 down +90 up
		 * Move with random speed inversely proportional to the radius of an sphere with the endosome
		 * volume.  The speed of a small organelle of radius 20 nm is taken as unit.  
		To move, three situations are considered
		1- Near the borders, the movement is: speed random between 0 and a value that depends on the endosome size
		heading, the original heading plus a random number that depends on the momentum
		2- Away of microtubules is the same than near borders
		3- Near MT, the speed is fixed and the heading is in the direction of the Mt or 180 that of the Mt
		 */

		NdPoint myPoint = space.getLocation(endosome);
//		NdPoint myPoint = endosome.getEndosomeLocation(endosome);
		
		double x = myPoint.getX();
//		endosome.setXcoor(x);
		double y = myPoint.getY();
//		endosome.setYcoor(y);
		

//	If near the borders, move only with 10% probability MT independent
		double cellSize = 50;
		double cellCenterX = 25;
		double cellCenterY = 25;		
		double nucleusSize = 5;
		double nucleusCenterX = 25;
		double nucleusCenterY = 21;	
//		If near the border, change heading randomly (100%) and stop move with 10% probability
		if (!isPointInSquare(x, y, cellCenterX, cellCenterY, cellSize- 5 * cellLimit)) { // near the cell border  LARGECELL
			endosome.heading = Math.random()*360;
//	    	System.out.println(" en el borde  " + x+"  " + y);
			changeDirectionRnd(endosome);
//		return;	
		}
//		If near the nucleus, change heading randomly (5%) and stop move with 10% probability

	
		else if (isPointInCircle(x, y, nucleusCenterX, nucleusCenterY, nucleusSize)) { // near the nucleus
				if (Math.random() < 0.05) endosome.heading = Math.random()*360;
				changeDirectionRnd(endosome);
				
			}
		else
//			if not near the borders
		{
//			boolean onMt = false;
			changeDirectionMt(endosome);

		}
		
//		Having the heading and speed, make the movement.  If out of the space, limit
//		the movement
			if (endosome.speed == 0) return;// random movement 90% of the time return speed=0
		    double xx = x + Math.cos(endosome.heading * Math.PI / 180d)
			* endosome.speed*Cell.orgScale/Cell.timeScale;
		    double yy = y + Math.sin(endosome.heading * Math.PI / 180d)
			* endosome.speed * Cell.orgScale/Cell.timeScale;	

//		    if move out the cell, goes to the center of the cell and change heading randomly
		    if (!isPointInSquare(xx, yy, cellCenterX, cellCenterY, cellSize-2*cellLimit)) {
//		    	System.out.println("FUERA DE CELULA  " + xx+"  " + yy);
		    	double[] newPoint = movePointToward(xx, yy, cellCenterX, cellCenterY, 2*cellLimit);
			    xx = newPoint[0];
			    yy = newPoint[1];
			    endosome.heading = Math.random()*360;

		    	}
	//    	System.out.println("FUERA DE CELULA  " + xx+"  " + yy);
		space.moveTo(endosome, xx, yy);
		grid.moveTo(endosome, (int) xx, (int) yy);
	}
	
	public static void changeDirectionRnd(Endosome endosome) {
//		90% of the time, the speed is 0 and the endosome does not move
		if (Math.random()<0.9) {
			endosome.speed = 0;
			return;
		}
//		double initialh = endosome.heading;
//		Endosome.endosomeShape(endosome);

// when near the borders or no MT is nearby, the organelle rotates randomly
// according with i) its present heading, ii) a gaussian random number (0+- 30degree/momentum) 
//	As unit momentum I take that of a sphere of radius 20.
//	Momentum of a ellipsoid = volume*(large radius^2 + small radius^2)/5.  For the sphere or radius 20
//	4/3*PI*r^3*(20^2+20^2)/5 = 26808257/5 = 5.361.651.
//		To prevent the tubules to move, I did not consider the volume in the calculation
//		then a 20 nm sphere has a "pseudo" momentum of 800
//NEW RULE FOR RANDOM CHANGE OF HEADING
//A free rnd movement 360.  The probability decrease with size
//An inertial movement.  Gaussian arround 0 with an angle that decreases with size
//An inertial movement depending on the momentum.  Gaussian around 0 or 180

//			double momentum = (endosome.a * endosome.a + endosome.c * endosome.c)/800;
//			Random fRandom = new Random();
//			double finalh = 0;
//			finalh = finalh + fRandom.nextGaussian() * 45d/endosome.size;// inertial depending size
////			finalh = finalh + fRandom.nextGaussian() * 1d * 800d/momentum;// inertial depending momentum
//			finalh = initialh + finalh;

// The speed is random between 0 and a value inversely proportional to the endosome size
			endosome.speed = 20d/endosome.size*Math.random()* Cell.orgScale/Cell.timeScale;
			return;
		}
	
	public static void changeDirectionMt(Endosome endosome){
		if (mts == null) {
			mts = associateMt();
		}
		double mtDir = 0;
		String rabDir = "";
/*
 * mtDirection decides if the endosome is going to move to the (-) end
 * of the MT (dyneine like or to the plus end (kinesine like). -1 goes
 * to the nucleus, 1 to the PM
 * 
 */

//		Collections.shuffle(mts); 19-7-21 No need to shuffle because the closest MT will be selected
		double dist = 1000;
		MT mt = null;
//NEW		RULE 19-7-2021.  The organelle will sense the MT around it and select the closest one (minimal absolute distance)
		for (MT mmt : mts) {
			double ndist = distance(endosome, mmt);
//			The distance is in space units from 0 to 50. At scale 1, the space is 1500 nm.  At 
//			scale 0.5 it is 3000 nm.
//			Hence to convert to nm, I must multiply by 45 (2250/50) and divide by scale. An organelle will sense MT
//			at a distance less than its size.
			if (Math.abs(ndist) <= Math.abs(dist)) {
				dist = ndist; 
				mt = mmt;
			}
		}

		if (Math.abs(dist*30d/Cell.orgScale) < endosome.size) {

				if (endosome.a >endosome.c) {moveGolgiVesicles(endosome);}
				boolean isTubule = (endosome.volume/(endosome.area - 2*Math.PI*Cell.rcyl*Cell.rcyl) <=Cell.rcyl/2); // should be /2
// select a mtDir according with the domains present in the endosome.  Larger probability for the more aboundant domain
// 0 means to plus endo of MT (to PM); +1 means to the minus end of MT (to nucleus)
				rabDir = mtDirection(endosome);
//				System.out.println (dist +" distancia " + rabDir);
				if (isTubule)
					{
//					System.out.println("IS TUBULE"+ rabDir);
					mtDir = ModelProperties.getInstance().mtTropismTubule.get(rabDir);
					if (Math.random()<Math.abs(mtDir)) {
//+1 means to plus end of MT (to PM); -1 means to the minus end of MT (to nucleus)
						if (Math.signum(mtDir)>=0) {mtDir = 0;} else {mtDir = 1;}
						}
						else {
						changeDirectionRnd(endosome);
						return;
						}
					} // if no a tubule
				else
					{
					mtDir = ModelProperties.getInstance().mtTropismRest.get(rabDir);
//					System.out.println("IS NOT TUBULE"+ mtDir);
					if (Math.random()< Math.abs(mtDir)) {
//+1 means to plus end of MT (to PM); -1 means to the minus end of MT (to nucleus)
						if (Math.signum(mtDir)>=0) {mtDir = 0;} else {mtDir = 1;}
						}
						else {
						changeDirectionRnd(endosome);
						return;
						}
					}
//				Changes the heading to the heading of the MT
//				Moves the endosome to the MT position
				double mth = mt.getMtheading();
				double yy = dist * Math.sin((mth+90)* Math.PI/180);
				double xx = dist * Math.cos((mth+90)* Math.PI/180);
				NdPoint pt = space.getLocation(endosome);
				double xpt = pt.getX()-xx;
				double ypt = pt.getY()-yy;
			    if (ypt >= 50-cellLimit) ypt = 50 -cellLimit;
				if (ypt <= 0+cellLimit) ypt = cellLimit;
				space.moveTo(endosome, xpt, ypt);
				grid.moveTo(endosome, (int) xpt, (int) ypt);
//				dist = distance(endosome, mt);
//				Changes the speed to a standard speed in MT independet of size
				endosome.speed = 1d*Cell.orgScale/Cell.timeScale;
				endosome.heading = -(mtDir * 180f + mt.getMtheading()+270f);
//				System.out.println(endosome.speed +" speed heading "+ endosome.heading+" MTheading" + mt.getMtheading());
				return;
			}
		
//		If no Mts, then random
		else 
		{changeDirectionRnd(endosome);
		return;
		}
	}
	private static void moveGolgiVesicles(Endosome endosome) {
		space = endosome.getSpace();
		grid = endosome.getGrid();		
		double deltaX = Math.random()*10-5;//when near MT rnd en zona Golgi
		double deltaY = Math.random()*6-3;//when near MT rnd en zona Golgi

			space.moveTo(endosome, 25 + deltaX, 14 + deltaY);
			grid.moveTo(endosome, (int) (25 + deltaX), (int)(14 + deltaY));
			
		NdPoint myPoint = space.getLocation(endosome);
		double x = myPoint.getX();
		endosome.setXcoor(x);
		double y = myPoint.getY();
		endosome.setYcoor(y);
		
	}


	public static String mtDirection(Endosome endosome) {
//		Picks a Rab domain according to the relative area of the domains in the organelle
//		More abundant Rabs have more probability of being selected
//		Returns the moving properties on MT of this domain 
		double rnd = Math.random();// select a random number
		double mtd = 0d;
//		Start adding the rabs domains present in the organelle until the value is larger than the random number selected
		for (String rab : endosome.rabContent.keySet()) {
			mtd = mtd + endosome.rabContent.get(rab) / endosome.area;
			if (rnd <= mtd) {
				return rab;
			}
		}
		String rab = Collections.max(endosome.rabContent.entrySet(), Map.Entry.comparingByValue()).getKey();
		return rab;// never used
	}
	public static List<MT> associateMt() {
		List<MT> mts = new ArrayList<MT>();
		for (Object obj : grid.getObjects()) {
			if (obj instanceof MT) {
				mts.add((MT) obj);
			}
		}
		return mts;
	}

	private static double distance(Endosome endosome, MT obj) {

		// If the line passes through two points P1=(x1,y1) and P2=(x2,y2) then
		// the distance of (x0,y0) from the line is calculate from wiki
		NdPoint pt = space.getLocation(endosome);
		double xpt = pt.getX();
		double ypt = pt.getY();
		double ymax = (double) ((MT) obj).getYend();
		double ymin = (double) ((MT) obj).getYorigin();
		double xmax = (double) ((MT) obj).getXend();
		double xmin = (double) ((MT) obj).getXorigin();
		
        // Calculate the projection of C onto the line containing AB
        Point A = new Point(xmin, ymin);
        Point B = new Point(xmax, ymax);
        Point C = new Point(xpt, ypt);
        LineSegment segment = new LineSegment(A, B);
        double ACx = C.x - segment.A.x;
        double ACy = C.y - segment.A.y;
        double ABx = segment.B.x - segment.A.x;
        double ABy = segment.B.y - segment.A.y;
        double dotProduct = ACx * ABx + ACy * ABy;
        double t = dotProduct / (ABx * ABx + ABy * ABy);

        // Check if the projection point lies within the segment bounds
        if (!(t >= 0 && t <= 1)) return 2000;
		
		double a = (xmax - xmin) * (ymin - ypt) - (ymax - ymin)
				* (xmin - xpt);
		double b = Math.sqrt((ymax - ymin) * (ymax - ymin) + (xmax - xmin)
				* (xmax - xmin));
		double distance = a / b;
//		The distance has a sign that it is used to move the organelle to the position in the Mt
		return distance;
	}
	
    public static boolean isPointInCircle(double x, double y, double x0, double y0, double r) {
        double distanceSquared = Math.pow(x - x0, 2) + Math.pow(y - y0, 2);
        double radiusSquared = Math.pow(r, 2);
        return distanceSquared <= radiusSquared;
    }
    
    public static boolean isPointInSquare(double x, double y, double x0, double y0, double ll) {
        double halfSide = ll / 2.0;
        // Calculate the boundaries of the square
        double left = x0 - halfSide;
        double right = x0 + halfSide;
        double top = y0 + halfSide;
        double bottom = y0 - halfSide;

        // Check if the point is within the boundaries
        return (x >= left && x <= right && y >= bottom && y <= top);
    }
    public static double[] movePointToward(double x, double y, double x0, double y0, double d) {
        double dx = x0 - x;
        double dy = y0 - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance == 0) {
            // The starting point and target point are the same
            return new double[] { x, y };
        }

        // Calculate the ratio to scale the direction vector
        double ratio = d / distance;

        // Calculate the new coordinates
        double newX = x + ratio * dx;
        double newY = y + ratio * dy;

        return new double[] { newX, newY };
    }

}

