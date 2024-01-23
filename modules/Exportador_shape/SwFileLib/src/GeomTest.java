/*
 *
 * GeomTest.java
 *
 * Test program which works with geom_test.magik, taking a number of
 * geometries and working out the perimiter and bounds, and then
 * passing this back.
 */



import com.gesmallworld.core.acp.*;
import com.gesmallworld.core.acpt.*;
import com.gesmallworld.core.geometry.basic.*;
import com.gesmallworld.core.geometry.gis.*;

public class GeomTest extends AcptAcp {
    protected int getMaxProtocol()  { return 0; }
    protected int getMinProtocol()  { return 0; }
    protected String getProgramId() { return "geom_test"; }

    
    public GeomTest() throws Exception {
		initTypes();
		
		while (Test() == 0);
    }

    static AcptType PointType, ChainType, AreaType, SimpleLinearType, TextType,
	ResultType;
    
    static void initTypes() throws AcptException {
		AcptType LinkType, HoleType, PolygonType, BoundsType;

		PointType =
			new StructType(Point.class, 
				   new ElDef[] {new ElDef("rwo_code",    BuiltinType.UINT16),
						new ElDef("app_code",    BuiltinType.UINT16),
						new ElDef("coord",       BuiltinType.COORD),
						new ElDef("orientation", BuiltinType.FLOAT32),
						new ElDef("scale",       BuiltinType.FLOAT32),
						new ElDef("mirror",      BuiltinType.BOOL)});
		LinkType =
			new StructType(Link.class,
				   new ElDef[] {new ElDef("sectors", BuiltinType.SECTOR_ROPE)});
		ChainType =
			new StructType(Chain.class,
				   new ElDef[] {new ElDef("rwo_code", BuiltinType.UINT16),
						new ElDef("app_code", BuiltinType.UINT16),
						new ElDef("links",    new LongVectorType(LinkType))});
		
		HoleType =
			new StructType(Polygon.class,
				   new ElDef[] {new ElDef("links", new LongVectorType(LinkType))});
		
		PolygonType =
			new StructType(Polygon.class,
				   new ElDef[] {new ElDef("holes", new LongVectorType(HoleType)),
						new ElDef("links", new LongVectorType(LinkType))});
		
		AreaType = 
			new StructType(Area.class,
				   new ElDef[] {new ElDef("rwo_code", BuiltinType.UINT16),
						new ElDef("app_code", BuiltinType.UINT16),
						new ElDef("polygons", new LongVectorType(PolygonType))});
		
		SimpleLinearType =
			new StructType(SimpleChain.class,
				   new ElDef[] {new ElDef("rwo_code", BuiltinType.UINT16),
						new ElDef("app_code", BuiltinType.UINT16),
						new ElDef("sectors",  BuiltinType.SECTOR_ROPE)});
		
		TextType =
			new StructType(Text.class,
				   new ElDef[] {new ElDef("rwo_code",    BuiltinType.UINT16),
						new ElDef("app_code",    BuiltinType.UINT16),
						new ElDef("height",      BuiltinType.FLOAT32),
						new ElDef("coord_1",     BuiltinType.COORD),
						new ElDef("coord_2",     BuiltinType.COORD),
						new ElDef("orientation", BuiltinType.FLOAT32),
						new ElDef("just",        BuiltinType.UINT16),
						new ElDef("string",      BuiltinType.LONG_STRING16)});
		
		BoundsType =
			new StructFromVecType(BoundingBox.class, BuiltinType.FLOAT64, 4);
		
		ResultType =
			new VecFromStructType(new Object[2],
					  new ElDef[] {new ElDef(0, BoundsType),
							   new ElDef(1, BuiltinType.FLOAT64)});
    }	
	
    public int Test() throws AcptException {
		AcptType[] types = new AcptType[] {null, PointType, ChainType, AreaType,
						   SimpleLinearType, TextType};
		int code, i, j;
		Object[] geoms;
		Object[][] results; // Bounds, Perimiter

		code = ((Integer)getObject(BuiltinType.INT32)).intValue();
		if (code == 0) return -1;

		geoms = getObjectVector(types[code]);

		results = new Object[geoms.length][];

		for(i=0; i<geoms.length; i++) {
			results[i] = new Object[2];

			testIndividualGeom(code, geoms[i], results[i]);
		}

		putObjectVector(ResultType, results);
		
		return 0;
    }

    public void testIndividualGeom(int code, Object geom, Object[] result) {
	int i,j,k;
	Link[] links;
	Polygon[] polygons;

	result[1] = new Double(0.0);
	
	switch(code) {

	case 1: 
	    Point point = (Point)geom;
	    testUpdateBounds(((Point)geom).coord, result); 
	    break;
	    
	case 2:
	    Chain chain = (Chain) geom;
	    links = chain.links;
	    for (j=0; j<links.length; j++) 
	      testUpdatePerimiterAndBounds(links[j].sectors, result, true);
	    break;

	case 3:
	    Area area = (Area)geom;
	    polygons = area.polygons;
	    for (i=0; i<polygons.length; i++) {
		Polygon polygon = polygons[i];
		links = polygon.links;
		for (j=0; j<links.length; j++) 
		  testUpdatePerimiterAndBounds(links[j].sectors, result, true);
		
		for (k=0; k<polygon.holes.length; k++) {
		    Polygon hole = polygon.holes[k];
		    links = hole.links;

		    for (j=0; j<links.length; j++) 
		      testUpdatePerimiterAndBounds(links[j].sectors, result, false);
		}
	    }

	    break;
	    
	case 4:
	    SimpleChain sc = (SimpleChain)geom;
	    testUpdatePerimiterAndBounds(sc.sectors, result, true);
	    break;

	case 5:
	    Text text = (Text)geom;
	    double dx = text.coord_1.x - text.coord_2.x;
	    double dy = text.coord_1.y - text.coord_2.y;
	    testUpdateBounds(text.coord_1, result);
	    testUpdateBounds(text.coord_2, result);
	    
	    result[1] = new Double(Math.sqrt(dx*dx+dy*dy));
	    break;
	}

	if (result[0] == null) result[0] = new BoundingBox(0,0,0,0);
    }

    public void testUpdateBounds(Coordinate coord, Object[] result) {
	if (result[0] == null) result[0] = new BoundingBox(coord, coord);
	else {
	    BoundingBox bounds = (BoundingBox)result[0];
	    if (coord.x < bounds.min.x) bounds.min.x = coord.x;
	    else if (coord.x > bounds.max.x) bounds.max.x = coord.x;

	    if (coord.y < bounds.min.y) bounds.min.y = coord.y;
	    else if (coord.y > bounds.max.y) bounds.max.y = coord.y;
	}
    }

    public void testUpdatePerimiterAndBounds(SectorRope sr, Object[] result,
					      boolean updateBounds) {
	int i, j;
	double sector_length = 0.0;
	Sector[] sectors = sr.sectors;

	for (i=0; i<sectors.length; i++) {
	    if (!(sectors[i] instanceof SimpleSector)) continue;

	    SimpleSector sector = (SimpleSector)sectors[i];
	    Coordinate[] coords = sector.coords;  
	    Coordinate coord, prev_coord = new Coordinate(0.0, 0.0);
	    double dx, dy;

	    for (j=0; j<coords.length; j++) {
		coord = coords[j];

		if (j > 0) {
		    dx=coord.x-prev_coord.x;
		    dy=coord.y-prev_coord.y;

		    sector_length += Math.sqrt(dx*dx+dy*dy);
		}

		prev_coord = coord;

		if (updateBounds) testUpdateBounds(coord, result);
	    }
	}
	result[1] = new Double(((Double)result[1]).doubleValue()+sector_length);
    }
}
