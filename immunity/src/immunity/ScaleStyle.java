package immunity;

import java.awt.Color;
import java.awt.Font;

//import javax.media.opengl.GL2;

import repast.simphony.visualizationOGL2D.StyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.render.RenderState;
import saf.v3d.scene.Position;
import saf.v3d.scene.VShape;
import saf.v3d.scene.VSpatial;

public class ScaleStyle implements StyleOGL2D<Scale> {

	ShapeFactory2D factory;
	private double String;
	
	@Override
	public void init(ShapeFactory2D factory) {
		this.factory = factory;

	}

	@Override
	public VSpatial getVSpatial(Scale object, VSpatial spatial) {
// World is a 50 X 50 space.  Each unit of space has a size of 15 
// hence the world is 750 X 750 size in repast units, that correspond to 
//	a 1500nm x 1500nm cellular space at orgScale = 1.  
// To convert from cell units (in nm) to repast space = nm/2  *  orgSpace
//	the scale is taking into account in the scale of the shape (see below);
		int scale500nm = (int) (500d/2d);
		VSpatial createRectangle = this.factory.createRectangle(scale500nm*10, (int) (25/Cell.orgScale));
		return createRectangle;

	}

	@Override
	public Color getColor(Scale object) {

		return new Color(0, 0, 255);
	}

	@Override
	public int getBorderSize(Scale object) {
		//if larger than 0, form a nice grid
		return 0;
	}

	@Override
	public Color getBorderColor(Scale object) {
		return new Color(100);
	}

	@Override
	public float getRotation(Scale object) {
		return 0;
	}

	@Override
	public float getScale(Scale object) {	
		return (float) Cell.orgScale;
	}

	@Override
	public String getLabel(Scale object) {
		String time = "5000 nm   " + Scale.getTimeString();
		
		
		return time; 
	}
	@Override
	public Font getLabelFont(Scale object) {
		return new Font("sansserif", Font.BOLD, (int) (2/Cell.orgScale));
	}

	@Override
	public float getLabelXOffset(Scale object) {
		// TODO Auto-generated method stub
		return (float) (2/Cell.orgScale);
	}

	@Override
	public float getLabelYOffset(Scale object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Position getLabelPosition(Scale object) {
		// TODO Auto-generated method stub
		return Position.CENTER;
	}

	@Override
	public Color getLabelColor(Scale object) {
		// TODO Auto-generated method stub
		return new Color(255,255,255);
	}

}
