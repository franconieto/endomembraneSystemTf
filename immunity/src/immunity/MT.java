package immunity;

import java.io.FileWriter;
import java.io.IOException;

//import com.thoughtworks.xstream.XStream;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class MT {
	// globals
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	double xorigin = 40d;
	double xend = 40d;
	double yorigin = 0d;
	double yend = 50d;
	double mth = Math.atan((yend - yorigin) / (xend - xorigin));
	public double mtheading = -mth * 180 / Math.PI;
	public double length = 0.0;

	// constructor
	public MT(ContinuousSpace<Object> sp, Grid<Object> gr) {
		this.space = sp;
		this.grid = gr;
	}

	@ScheduledMethod(start = 1, interval = 100)
	public void step() {
		if (Math.random() <0.01)
			changePosition(this);
	}

	public void changePosition(MT mt) {
		//if (Math.random() < 0.1) return;
		// move the origin and the end of the MT
//		xorigin = RandomHelper.nextDoubleFromTo(15, 35);
//		if (xorigin <= 25) {xend = xorigin -RandomHelper.nextDoubleFromTo(0, xorigin);}
//		else {xend = xorigin + RandomHelper.nextDoubleFromTo(0, 50-xorigin);}
//		double mth = Math.atan((50) / (xend - xorigin));
//		System.out.println("a-tang");
//		System.out.println(mth * 180 / Math.PI);
//		if (mth < 0) {
//			mth = 180 + (mth * 180 / Math.PI);
//		} else
//		{	mth = mth * 180 / Math.PI;}
////		xorigin = 25;
////		xend = RandomHelper.nextDoubleFromTo(0, 50);

		xorigin= 25;
		yorigin = 25;
//		double squareDiag = Math.sqrt(25*25 + 25*25);
//		double angle90 = 360 - mtheading;
//		if (angle90 > 180) {angle90 = -(angle90 - 360);}
//		if (angle90 > 90) angle90 = -(angle90 - 180);
//        double complementaryAngle = Math.abs(45 - angle90);
//        // Ensure the resulting angle is positive and within [0, 90]
//        double distance = squareDiag*Math.cos(complementaryAngle*Math.PI/180);
//		System.out.println(distance + "  "+mtheading +" complementary angle  "+ complementaryAngle);
//
//        xend = 25 + distance * Math.cos(-mtheading*Math.PI / 180);
//		yend = 25 + distance * Math.sin(-mtheading*Math.PI / 180);
		int randomSide = (int) Math.floor(Math.random()*4);
		switch (randomSide) {
		case 0 : {
			xend = 0;
			yend = Math.random()*50;
			break;
		}
		case 1 : {
			xend = 50;
			yend = Math.random()*50;
			break;
		}
		case 2 : {
			xend = Math.random()*50;
			yend = 0;
			break;
		}
		case 3 : {
			xend = Math.random()*50;
			yend = 50;
			break;
		}
		}
		
		mtheading = Math.atan2(xend-25, yend-25)*180/Math.PI; //-mth;
		double x = (xend + xorigin)/2 ;//25 * Math.cos(mtheading*Math.PI / 180);
		double y = (yend + yorigin)/2 ;//25 * Math.sin(mtheading*Math.PI / 180);
//		double y = 25+;
//		double x = 24.5;//xorigin + 25 * Math.cos(mtheading * Math.PI / 180);
		space.moveTo(mt, x, y);
		grid.moveTo(mt, (int) x, (int) y);
		length = Math.sqrt((xend-xorigin)*(xend-xorigin)+(yend-yorigin)*(yend-yorigin));
//System.out.println(mtheading + "  "+xend +" XY al azar del cuadrado  "+ yend);		
//writing to a xml file.  It works, but I will not be able to use to strart a simulation
//		XStream xstream = new XStream();
//		String file = "C:/Users/lmayo/Desktop/pruebaXML.xml";
//		try {
//			xstream.toXML(mt, new FileWriter(file));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	// GETTERS AND SETTERS
	public double getXorigin() {
		return xorigin;
	}

	public double getXend() {
		return xend;
	}

	public double getYorigin() {
		return yorigin;
	}

	public double getYend() {
		return yend;
	}

	public double getMtheading() {
		return mtheading;
	}

	public double getLength() {
		return length;
	}

}
