package immunity;

import java.util.HashMap;
import java.util.TreeMap;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Cell {
	// a single Cell is created
	private static Cell instance;
	public static Cell getInstance() {
		if (instance == null) {
			instance = new Cell(space, grid);
		}
		return instance;
	}
	

//	Cell characteristics
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	public static double rcyl = ModelProperties.getInstance().getCellK().get("rcyl");//20.0; // radius tubule
	public static double rendo = ModelProperties.getInstance().getCellK().get("rendo");//20.0; // radius tubule
	public static double mincyl = 6 * Math.PI * rcyl * rcyl; // surface minimum cylinder
// two radius large (almost a sphere)
	public static double minCistern = 4E5;
	public static double maxCistern = 1.6E6;
	public static double rIV = rcyl; //Internal vesicle radius similar to tubule radius 
//	public static double vEndo = 4d / 3d * Math.PI * Math.pow(rEndo, 3); //volume new endosome
//	public static double sEndo = 4d * Math.PI * Math.pow(rEndo, 2); // surface new endosome
// mincyl surface (20 nm rcy= 6*PI*rcyl^2) = 7539.82 volume (2*PI*rcyl^3)= 50265.48
	public static double orgScale = ModelProperties.getInstance().getCellK().get("orgScale");
	public static double timeScale = ModelProperties.getInstance().getCellK().get("timeScale");
//	public static int area = (int) (1500*400*(1/Cell.orgScale)*(1/Cell.orgScale)); //ModelProperties.getInstance().getCellAgentProperties().get("cellArea");// 
//	public static int volume = (int) (1500*400*1000*(1/Cell.orgScale)*(1/Cell.orgScale)*(1/Cell.orgScale)); //ModelProperties.getInstance().getCellAgentProperties().get("cellVolume");//
//	public static double volume = 1500*1500*400/(Math.pow(orgScale,3));//900*10^6, scale 1; 7200 *10^6, scale 0.5
//	public static double area = 1500*1500/(Math.pow(orgScale,2));//2.25 *10^6, scale 1
//  When orgScale=1 zoom =0, when > 1 zoom in , when <1 zoom out.
//	volume of the repast space  (1500 nm x 1500 nm) 400 nm deep (arbitrary heigth of the projection in 2D)
//	global cell and non-cell quantities
	public double tMembrane = 0;// membrane that is not used in endosomes
	public HashMap<String, Double> rabCell = new HashMap<String, Double>();// contains rabs free in cytosol
	public HashMap<String, Double> membraneCell = new HashMap<String, Double>(); // contains membrane factors within the cell 
	public HashMap<String, Double> solubleCell = new HashMap<String, Double>();// contains soluble factors within the cell
	TreeMap<Integer, HashMap<String, Double>> cellTimeSeries = new TreeMap<Integer, HashMap<String, Double>>();
	private double cellVolume;
	private double cellArea;

	// Constructor
	public Cell(ContinuousSpace<Object> space, Grid<Object> grid) {
// Contains factors that are in the cell without specifying organelle or position.
// It is modified by Endosome that uses and changes cytosolic Rabs
// contents.	tMembranes, membrane and soluble content recycling,
		this.space = space;
		this.grid = grid;
		cellArea = ModelProperties.getInstance().getCellAgentProperties().get("cellArea");// 
		cellVolume = ModelProperties.getInstance().getCellAgentProperties().get("cellVolume");//

		solubleCell.putAll(ModelProperties.getInstance().getSolubleCell());
		rabCell.putAll(ModelProperties.getInstance().getInitRabCell());
		tMembrane = 10000000;//ModelProperties.getInstance().cellK.get("tMembrane");
//		cellTimeSeries = null;
	}
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		String name = ModelProperties.getInstance().getCopasiFiles().get("cellCopasi");
		if (Math.random() < 0.1 && !name.equals("null")){
			System.out.println("soluble Cell  wwwww  " +this.getSolubleCell());
			CellCopasiStep.antPresTimeSeriesLoad(this);
		}
			
// eventual use for cell metabolism
	}
	@ScheduledMethod(start = 1, interval = 1)
	public void uptake() {
		if (Math.random() < 0.1){
			System.out.println("soluble Cell  wwwww  " +this.getSolubleCell());
			UptakeStep2.uptake(this);
			}
			
// eventual use for cell metabolism
	}
	// GETTERS AND SETTERS (to get and set Cell contents)


	public double gettMembrane() {
		return tMembrane;
	}
	public double getCellVolume() {
		return cellVolume;
	}
	public double getCellArea() {
		return cellArea;
	}
	public void settMembrane(double tMembrane) {
		this.tMembrane = tMembrane;
	}
	
	public HashMap<String, Double> getRabCell() {
		return rabCell;
	}
	
	public HashMap<String, Double> getMembraneCell() {
		return membraneCell;
	}
	public HashMap<String, Double> getSolubleCell() {
		return solubleCell;
	}
	public final TreeMap<Integer, HashMap<String, Double>> getCellTimeSeries() {
		return cellTimeSeries;
	}
//	public HashMap<String, Double> getcellTimeSeries() {
//		// TODO Auto-generated method stub
//		return null;
//	}
	public ContinuousSpace<Object> getSpace() {
		// TODO Auto-generated method stub
		return space;
	}
	public Grid<Object> getGrid() {
		// TODO Auto-generated method stub
		return grid;
	}


}