/*
 * Métodos para transformar las geometrías de Smallworld en tipo Shape
 */
package com.iberlan.file;

import com.gesmallworld.core.geometry.gis.*;
import com.gesmallworld.core.geometry.basic.*;
import static com.iberlan.file.shape.Globals.debug;
import com.iberlan.geometry.PseudoAreaExport;
import com.iberlan.geometry.PseudoPolygonExport;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.JTS;

/**
 *
 * @author Maria Jesús Santaolaya
 */
public class TransformGeoms {
    
      
    public static MultiLineString createShapeMultiLine(Chain swLine)  throws Exception{
        /* Transforma una geometría de tipo Chain en una geometría MultiLineString */
        
        MultiLineString mLine;
        Collection<LineString> colLines = new ArrayList<>(); 
        
        Link[] links = swLine.links; 
        GeometryFactory factoryGeo = JTSFactoryFinder.getGeometryFactory(null);      
        int j;         
        
        for (j=0; j<links.length; j++) {
            Link lk = links[j];
            Collection<Coordinate> colCoordsLinks = getCoordinatesCollection(lk);
            LineString  newline=(LineString)factoryGeo.createLineString(colCoordsLinks.toArray(new Coordinate[] {}));
            colLines.add(newline);
        }
               
        // Crear MultiLineString    
        mLine = factoryGeo.createMultiLineString(colLines.toArray(new LineString[] {})); 
               
        return mLine;
    }
    
    public static LineString createShapeLine(SimpleChain swLine)  throws Exception{
        /* Transforma una geometría de tipo SimpleChain en una geometría LineString */        
        
        GeometryFactory factoryGeo = JTSFactoryFinder.getGeometryFactory(null);  
        Collection<Coordinate> colCoordsSectors = getCoordinatesCollection(swLine.sectors);
        LineString  newline=(LineString)factoryGeo.createLineString(colCoordsSectors.toArray(new Coordinate[] {}));       
               
        return newline;
    }
    
    public static com.vividsolutions.jts.geom.Point createShapePoint(Object swPoint)  throws Exception{
      
        com.vividsolutions.jts.geom.Point aPoint= null;
        com.gesmallworld.core.geometry.basic.Coordinate coord = null;
        
        if(swPoint instanceof Point){
            coord = ((Point) swPoint).coord;
        }
        else if(swPoint instanceof SimplePoint){
            coord = ((SimplePoint) swPoint).coord;            
        }
        else if(swPoint instanceof Text){
             Text texto = (Text) swPoint;          
             coord = texto.coord_1;
        }

        GeometryFactory factoryGeo = JTSFactoryFinder.getGeometryFactory(null); 
        aPoint = factoryGeo.createPoint(new Coordinate(coord.x,coord.y));
        return aPoint;      
    }
    
    
    public static com.vividsolutions.jts.geom.MultiPolygon createShapeArea(Area swArea)  throws Exception{
        /* Transforma una geometría de tipo Area en una geometría Multipolygon */
        
         com.vividsolutions.jts.geom.MultiPolygon mPolygon;
         Polygon[] polygons = swArea.polygons;
         Collection<com.vividsolutions.jts.geom.Polygon> colPolygons = new ArrayList<>();          
         int i;         
         
	 for (i=0; i<polygons.length; i++) {	
             Polygon polygon = polygons[i];
             com.vividsolutions.jts.geom.Polygon shPolygon = createShapePolygon(polygon);
             colPolygons.add(shPolygon);
	    }            
         
        GeometryFactory factoryGeo = JTSFactoryFinder.getGeometryFactory(null); 
        mPolygon = factoryGeo.createMultiPolygon(colPolygons.toArray(new com.vividsolutions.jts.geom.Polygon[] {}));
        return mPolygon;
         
     }
     
     public static com.vividsolutions.jts.geom.MultiPolygon createShapeArea(PseudoAreaExport swPseudoArea)  throws Exception{
        /* Transforma una geometría de tipo PseudoAreaExport en una geometría Multipolygon */
        
        com.vividsolutions.jts.geom.MultiPolygon mPolygon;
        
        PseudoPolygonExport[] polygons = swPseudoArea.adjusted_polygons;
        Collection<com.vividsolutions.jts.geom.Polygon> colPolygons = new ArrayList<>();          
        int i;
         
        for (i=0; i<polygons.length; i++) {	
             PseudoPolygonExport polygon = polygons[i];
             com.vividsolutions.jts.geom.Polygon shPolygon = createShapePolygon(polygon);
             colPolygons.add(shPolygon);
	} 
        
        GeometryFactory factoryGeo = JTSFactoryFinder.getGeometryFactory(null);
        mPolygon = factoryGeo.createMultiPolygon(colPolygons.toArray(new com.vividsolutions.jts.geom.Polygon[] {}));
                    
        return mPolygon;
     }
     
     public static com.vividsolutions.jts.geom.Polygon createShapeArea(SimpleArea swSimpleArea)  throws Exception{
         
        com.vividsolutions.jts.geom.Polygon shPolygon = null;
        GeometryFactory factoryGeo = JTSFactoryFinder.getGeometryFactory(null);
        Collection<Coordinate> colCoordsSectorsR = getCoordinatesCollection(swSimpleArea.sectors);
        shPolygon = factoryGeo.createPolygon(colCoordsSectorsR.toArray(new Coordinate[] {})); 
      
        return shPolygon;
     }
     
     private static com.vividsolutions.jts.geom.Polygon createShapePolygon(PseudoPolygonExport swPolygon)  throws Exception{
         
         //outer bounds
         com.vividsolutions.jts.geom.Polygon shPolygon = null;
         GeometryFactory factoryGeo = JTSFactoryFinder.getGeometryFactory(null);
                 
        LinearRing ring = null;
        LinearRing holes[]= null;                     
                
         /* Crear LinearRing del contorno exterior*/
        ring=createLinearRing(swPolygon.sectors); 
        
         /* Crear array de LineraRing de los holes*/  
        Collection<LinearRing> col_holes_rings = new ArrayList<LinearRing>(); 
        int i;
        
        debug.appendToFile(String.format("Holes %d", swPolygon.holes_sectors.length));
        for (i=0; i< swPolygon.holes_sectors.length; i++) {
            
            SectorRope hole_sectors = swPolygon.holes_sectors[i];
            LinearRing l_hole = null;  
            l_hole=createLinearRing(hole_sectors);
            col_holes_rings.add(l_hole);
	}
             
        holes = col_holes_rings.toArray(new LinearRing[] {});
        shPolygon = factoryGeo.createPolygon(ring,holes);
        
         return shPolygon;
     }
     private static com.vividsolutions.jts.geom.Polygon createShapePolygon(Polygon swPolygon)  throws Exception{
         
        com.vividsolutions.jts.geom.Polygon shPolygon = null;
        LinearRing ring = null;
        LinearRing holes[]= null;        
                  
        GeometryFactory factoryGeo = JTSFactoryFinder.getGeometryFactory(null);
             
         /* Crear LinearRing del contorno exterior*/
        ring=createLinearRing(swPolygon.links); 
        
         /* Crear array de LineraRing de los holes*/  
        Collection<LinearRing> col_holes = new ArrayList<LinearRing>(); 
        int i;
        
        for (i=0; i< swPolygon.holes.length; i++) {
            
            Polygon hole = swPolygon.holes[i];
            LinearRing l_hole = null;  

            l_hole=createLinearRing(hole.links);
            col_holes.add(l_hole);
	}
             
        holes = col_holes.toArray(new LinearRing[] {});
	shPolygon = factoryGeo.createPolygon(ring,holes);
         
        return shPolygon;
     }
           
     private static LinearRing createLinearRing(SectorRope aSectorR)  throws Exception{
        LinearRing ring = null;
        
        GeometryFactory factoryGeo = JTSFactoryFinder.getGeometryFactory(null); 
        Collection<Coordinate> colCoord = getCoordinatesCollection(aSectorR);
        
        if (colCoord.size() < 3) {
            throw new Exception("ERR: Ring has fewer than 3 points, so orientation cannot be determined");}
        
        ring=factoryGeo.createLinearRing( colCoord.toArray(new Coordinate[] {}));  
        
        if (!ring.isClosed()) {
            throw new Exception("ERR: Ring isn't closed");
        } 
        //ring.isClosed()
        return ring;         
     }
     
     private static LinearRing createLinearRing(Link[] arrayLinks)  throws Exception{
        LinearRing ring = null;
        
        GeometryFactory factoryGeo = JTSFactoryFinder.getGeometryFactory(null); 
        Collection<Coordinate> colCoordsLinks = getCoordinatesCollection(arrayLinks);
        
        if (colCoordsLinks.size() < 3) {
            throw new Exception("ERR: Ring has fewer than 3 points, so orientation cannot be determined");}
        
        ring=factoryGeo.createLinearRing( colCoordsLinks.toArray(new Coordinate[] {}));  
        
        if (!ring.isClosed()) {
            throw new Exception("ERR: Ring isn't closed");
        } 
        //ring.isClosed()
        return ring;         
     }
     
     private static Collection<Coordinate> getCoordinatesCollection(Link[] arrayLinks)  throws Exception{
            /* Devuelve una coleccion de coordenadas Shape a partir del array de
               links Smallworld com.gesmallworld.core.geometry.gis.Link [] */
         
             Collection<Coordinate> colCoords = new ArrayList<Coordinate>();
             int i;
             
             for (i=0; i<arrayLinks.length; i++) {
                colCoords.addAll(getCoordinatesCollection(arrayLinks[i]));                                          
             }
             
             return colCoords;
     }
     
     private static Collection<Coordinate> getCoordinatesCollection(Link aLink)  throws Exception{
        
        Collection<Coordinate> colCoords = new ArrayList<Coordinate>();
         
        SectorRope sr = aLink.sectors;  
        colCoords = getCoordinatesCollection(sr);
         
        return colCoords;
     }
     
      private static Collection<Coordinate> getCoordinatesCollection(SectorRope aSectorR)  throws Exception{
        
        Collection<Coordinate> colCoords = new ArrayList<>();
        
        Sector[] sectors = aSectorR.sectors;                     
        int numelem = sectors.length; 
        int i;
         
       //System.err.println(String.format("Num sectores: %s",numelem));            
               

        for (i=0; i<numelem; i++) {
         //  System.err.println(String.format("Tipo sector: %s",sectors[i]));  
           
           if (sectors[i] instanceof SimpleSector){
            
           // System.err.println("Es SimpleSector"); 
            SimpleSector aSSector = (SimpleSector)sectors[i];
            colCoords.addAll(getCoordinatesCollection(aSSector));
            //System.err.println(String.format("Col coords: %s",colCoords.size())); 
           }
           else if (sectors[i] instanceof SectorH) {
            //System.err.println("Es SectorH");    
            SectorH aSectorH = (SectorH)sectors[i];
            colCoords.addAll(getCoordinatesCollection(aSectorH));
           }
           else if (sectors[i] instanceof SectorZ) 
           {
              // System.err.println("Es SectorZ"); 
            SectorZ aSectorZ = (SectorZ)sectors[i];
            colCoords.addAll(getCoordinatesCollection(aSectorZ));   
           }
        }       
          
        return colCoords;
      }
      
      private static Collection<Coordinate> getCoordinatesCollection(SimpleSector aSector)  throws Exception{
          return getCoordinatesCollection_H_Z(aSector);
      }
      
      private static Collection<Coordinate> getCoordinatesCollection(SectorZ aSector)  throws Exception{
          return getCoordinatesCollection_H_Z(aSector);
      }
      
      private static Collection<Coordinate> getCoordinatesCollection_H_Z(Sector aSector)  throws Exception{
          Collection<Coordinate> colCoords = new ArrayList<>();
          com.gesmallworld.core.geometry.basic.Coordinate[] coords = aSector.coords;  

          int j;
          for (j=0; j<coords.length; j++) {
              com.gesmallworld.core.geometry.basic.Coordinate co = coords[j];
              Coordinate cd = new Coordinate(co.getX(),co.getY()); ;
              colCoords.add(cd);                           
            }
          return colCoords;
      }
      
      private static Collection<Coordinate> getCoordinatesCollection(SectorH aSector)  throws Exception{
          
          GeometryFactory factoryGeomLines = JTSFactoryFinder.getGeometryFactory(null);
          Collection<Coordinate> colCoords = new ArrayList<>();
          Iterator <TanArc> iteratorTan = aSector.segmentIterator();
          
          
          while (iteratorTan.hasNext()) {
            LineString  curveLine;
            TanArc aSeg = iteratorTan.next();
            Collection<Coordinate> colCoordsT = new ArrayList<>();
            
            Coordinate start = new Coordinate(aSeg.getStartPoint().getX(),
                             aSeg.getStartPoint().getY());                     
            Coordinate middle = new Coordinate(aSeg.getMiddlePoint().getX(),
                             aSeg.getMiddlePoint().getY());
            Coordinate end = new Coordinate(aSeg.getEndPoint().getX(),
                             aSeg.getEndPoint().getY());
            
            colCoordsT.add(start);            
            colCoordsT.add(middle);            
            colCoordsT.add(end);
                     
            curveLine=factoryGeomLines.createLineString(colCoordsT.toArray(new Coordinate[] {}));    
            com.vividsolutions.jts.geom.Geometry aAproxCurve = JTS.smooth(curveLine, 0.0);
            
            Coordinate[] coordsC = aAproxCurve.getCoordinates();
            
            int k;
            for (k=0; k<coordsC.length; k++) {
                 colCoords.add(coordsC[k]);                           
            }
          }
               
          return colCoords;
      }
     
}
