package immunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class EndoplasmicReticulum {
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	private static EndoplasmicReticulum instance;
	static {
		instance = new EndoplasmicReticulum(space, grid);
	}
	public static EndoplasmicReticulum getInstance() {
		return instance;
	}
	public static HashMap<String, Double> initialERProperties = new HashMap<String, Double>();
	public HashMap<String, Double> membraneRecycle = new HashMap<String, Double>(ModelProperties.getInstance().getInitERmembraneRecycle()); // contains membrane recycled 
	public HashMap<String, Double> solubleRecycle = new HashMap<String, Double>();// contains soluble recycled
	public static int ercolor = 0;
	public static int red = 0;
	public static int green = 0;	
	public static int blue = 0;	
	public double endoplasmicReticulumArea = 0;
	public double endoplasmicReticulumVolume = 0;
//	public double endoplasmicReticulumVolume;
//	public double endoplasmicReticulumArea;
//	public static double initialendoplasmicReticulumVolume;
//	public static double initialendoplasmicReticulumArea;
//	public int area = (int) (1500*400*(1/Cell.orgScale)*(1/Cell.orgScale)); //ModelProperties.getInstance().getendoplasmicReticulumProperties().get("endoplasmicReticulumArea");// 
//	public int volume = (int) (1500*400*1000*(1/Cell.orgScale)*(1/Cell.orgScale)*(1/Cell.orgScale)); //ModelProperties.getInstance().getendoplasmicReticulumProperties().get("endoplasmicReticulumVolume");//
	TreeMap<Integer, HashMap<String, Double>> endoplasmicReticulumTimeSeries = new TreeMap<Integer, HashMap<String, Double>>();
	public String endoplasmicReticulumCopasi = ModelProperties.getInstance().getCopasiFiles().get("endoplasmicReticulumCopasi");
// nm2 1500nm x 400nm. Space in repast at scale =1 and arbitrary height of the space projected
//	in 2D

	// Constructor
	public EndoplasmicReticulum(ContinuousSpace<Object> sp, Grid<Object> gr){
// Contains the contents that are in the ER.  It is modified by Endosome that uses and changes the ER
// contents.	
//		Initial values from the InputIntrTransport2
//		These values changes with data from frozenEndosomes.csv and by fusion and fission of ER.
		ModelProperties modelProperties = ModelProperties.getInstance();
		double orgScale = modelProperties.getCellK().get("orgScale");
		
		initialERProperties = modelProperties.getInitERProperties();
		endoplasmicReticulumArea = initialERProperties.get("endoplasmicReticulumArea");// 

		//1500 y 400 es lado y el alto de la membrana considerada en escala original. 4 son cuatro lados.
//		System.out.println("IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII "+ endoplasmicReticulumArea);
		
		//400, 1500 y 1500 es el cubo en nm en escala original	
		endoplasmicReticulumVolume = initialERProperties.get("endoplasmicReticulumVolume");//

		for (String met : modelProperties.getInitERmembraneRecycle().keySet()){
		membraneRecycle.put(met, modelProperties.initERmembraneRecycle.get(met));
		}
//		System.out.println("ER membraneRecycle "+ membraneRecycle + endoplasmicReticulumArea);
		for (String met : modelProperties.initERsolubleRecycle.keySet() ){
		solubleRecycle.put(met, modelProperties.initERsolubleRecycle.get(met));
		}
//		System.out.println("ER solubleRecycle "+ solubleRecycle);		
//		for (String met : modelProperties.solubleMet ){
//		solubleRecycle.put(met,  0.0);
//		}
//		System.out.println("solubleRecycle "+solubleRecycle);		
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		changeColor();
		growth();
		
//		this.membraneRecycle = endoplasmicReticulum.getInstance().getMembraneRecycle();
//		this.solubleRecycle = endoplasmicReticulum.getInstance().getSolubleRecycle();
//		this.endoplasmicReticulumTimeSeries=endoplasmicReticulum.getInstance().getendoplasmicReticulumTimeSeries();
//		if (Math.random() < 0 && endoplasmicReticulumCopasi.endsWith(".cps"))endoplasmicReticulumCopasiStep.antPresTimeSeriesLoad(this);
//		this.changeColor();

		}
	public void growth() {
	if(Math.random()>0.05)return;//aumento a 0.05 para crear mas organelas
//	int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
	double growth = 1.0005; // Bajo 1.001
//	EndoplasmicReticulum.getInstance();
	//	if (tick < 120000) growth = 1.005;
//	else if (tick >= 120000 && tick < 300000)growth = 1.0025;
//	else growth = 1.01;
//	System.out.println("soluble Cell  wwwww  " +this.getSolubleCell());
//	As set here, it growth at 0.005(probability)*0.005(from 1.005)*1000(ticks per min) = 0.025 of ER 
//	area per 1000 tick (1 min) = 2.5%/min
	EndoplasmicReticulum.getInstance().setEndoplasmicReticulumArea(endoplasmicReticulumArea * growth);
//	System.out.println("INITIAL AREA ER  " + areaER);
//	EndoplasmicReticulum.getInstance().setendoplasmicReticulumArea(areaER*growth);//1.005
//	System.out.println("FINAL AREA ER  " + EndoplasmicReticulum.getendoplasmicReticulumArea());
	}
	
	public void changeColor() {
		double c1 = 0d;
		{
//		c1 = membraneRecycle.get("Tf");
		c1 = 20*c1/endoplasmicReticulumArea;
		if (c1>1) c1=1;
		ercolor = (int) (c1*255);
		}

//		System.out.println(endoplasmicReticulum.getInstance().getMembraneRecycle()+"\n COLOR PLASMA  " + ERcolor+" " + ercolor);
	}
	

	// GETTERS AND SETTERS (to get and set Cell contents)
//	public static EndoplasmicReticulum getInstance() {
//		return instance;
//	}
		public HashMap<String, Double> getMembraneRecycle() {
		return this.membraneRecycle;
	}

	public HashMap<String, Double> getSolubleRecycle() {
		return this.solubleRecycle;
	}

	public int getErcolor() {
		return ercolor;
	}

	public double getEndoplasmicReticulumArea() {
		return endoplasmicReticulumArea;
	}


	public double getEndoplasmicReticulumVolume() {
		return endoplasmicReticulumVolume;
	}
	
	public final TreeMap<Integer, HashMap<String, Double>> getendoplasmicReticulumTimeSeries() {
		return endoplasmicReticulumTimeSeries;
	}
	
//	public final void setendoplasmicReticulumArea(double endoplasmicReticulumArea) {
//		EndoplasmicReticulum.endoplasmicReticulumArea = endoplasmicReticulumArea;
//	}

	public final double getInitialendoplasmicReticulumVolume() {
		return initialERProperties.get("endoplasmicReticulumVolume");
	}

	public final double getInitialendoplasmicReticulumArea() {
		return initialERProperties.get("endoplasmicReticulumArea");
	}
	public void setEndoplasmicReticulumArea(double endoplasmicReticulumArea) {
		this.endoplasmicReticulumArea = endoplasmicReticulumArea;
	}

	public void setEndoplasmicReticulumVolume(double endoplasmicReticulumVolume) {
		this.endoplasmicReticulumVolume = endoplasmicReticulumVolume;
		
	}



}