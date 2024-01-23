/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iberlan.file;

import com.gesmallworld.core.acpt.AcptException;
import com.gesmallworld.core.acpt.AcptType;
import com.gesmallworld.core.acpt.BuiltinType;
import com.gesmallworld.core.acpt.ElDef;
import com.gesmallworld.core.acpt.LongVectorType;
import com.gesmallworld.core.acpt.StructType;
import com.gesmallworld.core.geometry.gis.Area;
import com.gesmallworld.core.geometry.gis.Chain;
import com.gesmallworld.core.geometry.gis.Link;
import com.gesmallworld.core.geometry.gis.Point;
import com.gesmallworld.core.geometry.gis.Polygon;
import com.gesmallworld.core.geometry.gis.SimpleChain;
import com.gesmallworld.core.geometry.gis.SimplePoint;
import com.gesmallworld.core.geometry.gis.SimpleArea;
import com.gesmallworld.core.geometry.gis.Text;
import static com.iberlan.file.shape.Globals.debug;
import com.iberlan.geometry.PseudoAreaExport;
import com.iberlan.geometry.PseudoPolygonExport;

import java.io.IOException;

/**
 *
 * @author Fernando
 */
public class AcptGeomRecord extends AcptRecord {

    private static final int NUM_GEOM_BASE = 550;
    private AcptType[] geomTypes = new AcptType[32];

    public AcptGeomRecord() throws AcptException,IOException {
        super();
        initTypes();
    }

    public AcptGeomRecord(boolean autostart) throws AcptException,IOException {
        super(autostart);
        initTypes();
    }

    private void initTypes() throws AcptException {

        AcptType PointType
                = new StructType(Point.class,
                        new ElDef[]{new ElDef("rwo_code", BuiltinType.UINT16),
                            new ElDef("app_code", BuiltinType.UINT16),
                            new ElDef("coord", BuiltinType.COORD),
                            new ElDef("orientation", BuiltinType.FLOAT32),
                            new ElDef("scale", BuiltinType.FLOAT32),
                            new ElDef("mirror", BuiltinType.BOOL)});
        AcptType SimplePointType
                = new StructType(SimplePoint.class,
                        new ElDef[]{new ElDef("rwo_code", BuiltinType.UINT16),
                            new ElDef("app_code", BuiltinType.UINT16),
                            new ElDef("coord", BuiltinType.COORD),
                            new ElDef("orientation", BuiltinType.FLOAT32),
                            new ElDef("scale", BuiltinType.FLOAT32),
                            new ElDef("mirror", BuiltinType.BOOL)});
        AcptType LinkType
                = new StructType(Link.class,
                        new ElDef[]{new ElDef("sectors", BuiltinType.SECTOR_ROPE)});
        AcptType ChainType
                = new StructType(Chain.class,
                        new ElDef[]{new ElDef("rwo_code", BuiltinType.UINT16),
                            new ElDef("app_code", BuiltinType.UINT16),
                            new ElDef("links", new LongVectorType(LinkType))});

        
        AcptType SimpleChainType
                = new StructType(SimpleChain.class,
                        new ElDef[]{new ElDef("rwo_code", BuiltinType.UINT16),
                            new ElDef("app_code", BuiltinType.UINT16),
                            new ElDef("sectors", BuiltinType.SECTOR_ROPE)});

        AcptType HoleType
                = new StructType(Polygon.class,
                        new ElDef[]{new ElDef("links", new LongVectorType(LinkType))});

        AcptType PolygonType
                = new StructType(Polygon.class,
                        new ElDef[]{new ElDef("holes", new LongVectorType(HoleType)),
                            new ElDef("links", new LongVectorType(LinkType))});

        AcptType AreaType
                = new StructType(Area.class,
                        new ElDef[]{new ElDef("rwo_code", BuiltinType.UINT16),
                            new ElDef("app_code", BuiltinType.UINT16),
                            new ElDef("polygons", new LongVectorType(PolygonType))});

        AcptType SimpleAreaType
                = new StructType(SimpleArea.class,
                        new ElDef[]{new ElDef("rwo_code", BuiltinType.UINT16),
                            new ElDef("app_code", BuiltinType.UINT16),
                            new ElDef("sectors", BuiltinType.SECTOR_ROPE)});
        
        AcptType TextType
                = new StructType(Text.class,
                        new ElDef[]{new ElDef("rwo_code", BuiltinType.UINT16),
                            new ElDef("app_code", BuiltinType.UINT16),
                            new ElDef("height", BuiltinType.FLOAT32),
                            new ElDef("coord_1", BuiltinType.COORD),
                            new ElDef("coord_2", BuiltinType.COORD),
                            new ElDef("orientation", BuiltinType.FLOAT32),
                            new ElDef("just", BuiltinType.UINT16),
                            new ElDef("string", BuiltinType.LONG_STRING16)});
        
        AcptType PseudoPointType
                = new StructType(SimplePoint.class,
                        new ElDef[]{new ElDef("rwo_code", BuiltinType.UINT16),
                            new ElDef("app_code", BuiltinType.UINT16),
                            new ElDef("coord", BuiltinType.COORD),
                            new ElDef("orientation", BuiltinType.FLOAT32),
                            new ElDef("scale", BuiltinType.FLOAT32),
                            new ElDef("mirror", BuiltinType.BOOL)});
        AcptType PseudoChainType
                = new StructType(SimpleChain.class,
                        new ElDef[]{new ElDef("rwo_code", BuiltinType.UINT16),
                            new ElDef("app_code", BuiltinType.UINT16),
                            new ElDef("sectors", BuiltinType.SECTOR_ROPE)});
        
        AcptType PseudoPolygonType
                = new StructType(PseudoPolygonExport.class,
                        new ElDef[]{new ElDef("sectors", BuiltinType.SECTOR_ROPE),
                                    new ElDef("holes_sectors", new LongVectorType(BuiltinType.SECTOR_ROPE))});
        
        AcptType PseudoAreaType
                = new StructType(PseudoAreaExport.class,
                        new ElDef[]{new ElDef("rwo_code", BuiltinType.UINT16),
                                    new ElDef("app_code", BuiltinType.UINT16),
                                    new ElDef("sectors", BuiltinType.SECTOR_ROPE),
                                    new ElDef("adjusted_polygons", new LongVectorType(PseudoPolygonType))});
        
        AcptType PseudoTextType
                = new StructType(Text.class,
                        new ElDef[]{new ElDef("rwo_code", BuiltinType.UINT16),
                            new ElDef("app_code", BuiltinType.UINT16),
                            new ElDef("height", BuiltinType.FLOAT32),
                            new ElDef("coord_1", BuiltinType.COORD),
                            new ElDef("coord_2", BuiltinType.COORD),
                            new ElDef("orientation", BuiltinType.FLOAT32),
                            new ElDef("just", BuiltinType.UINT16),
                            new ElDef("string", BuiltinType.LONG_STRING16)});
        
        
       
        
        this.getGeomTypes()[3] = PointType;
        this.getGeomTypes()[4] = LinkType;
        this.getGeomTypes()[7] = ChainType;
        this.getGeomTypes()[12] = HoleType;
        this.getGeomTypes()[13] = PolygonType;
        this.getGeomTypes()[16] = AreaType;
        this.getGeomTypes()[17] = SimpleChainType;
        this.getGeomTypes()[18] = AreaType; //SimpleAreaType;
        this.getGeomTypes()[19] = SimplePointType;
        this.getGeomTypes()[20] = TextType;
        this.getGeomTypes()[21] = SimpleAreaType;
        
        this.getGeomTypes()[22] = PseudoPointType; 
        this.getGeomTypes()[23] = PseudoChainType;        
        this.getGeomTypes()[24] = PseudoAreaType;       
        this.getGeomTypes()[25] = PseudoTextType;
        this.getGeomTypes()[26] = PseudoPolygonType; 
    }

    @Override
    protected AcptType typeForId(int id) {
        AcptType result = getBuiltinTypes()[1];
        if (id <= 64) {
            result = getBuiltinTypes()[id];
        } else if (id >= NUM_GEOM_BASE) {
            result = getGeomTypes()[id - NUM_GEOM_BASE];}
        return result;
    }

    @Override
    protected AcptRow readRecord(FieldDescription[] fields) throws IOException, AcptException {
        /* Recibe desde una imagen de SW la lista de campos FIELDS y devuelve un ACPTRWO construido
           a partir de los datos recibidos. Contempla que alguno de los valores puedan ser geometrias*/
        
             
        AcptRow rec = new AcptRow();
        FieldDescription field;
        AcptType type=null;
        String name=null;
        Object obj;
        int paso = 0;
    
        for (int i = 0; i < fields.length; i++) {
            try{         
        
                paso = 0;
                field = fields[i];
                name = field.getName();
                type = field.getType();
                obj = null;
                paso = paso + 1;//1
       
                if (field.isGeometric()) {
                   
                    paso = paso + 1;//2
                    int codeType = getUShort();
       
                    paso = paso + 1;//3 leido tipo de geometria
                    int indexGeomType = (codeType - NUM_GEOM_BASE);
                           AcptType recType2 = peekObject();   
                    paso = paso + 1;//4 
                  
                    if (recType2 == BuiltinType.UNSET) {
                        skipObject();
                        paso = paso + 1; //5
                    }
                    else if (this.getGeomTypes()[indexGeomType] == type) {
                        obj = getObject(getGeomTypes()[indexGeomType]);
                        paso = paso + 1; //5
                        rec.put(name, obj);
                    } else {
                        skipObject();
                        paso = paso + 1; //5
                    }
                } else if (field.isNullable()) {
                    AcptType recType = peekObject();
                    paso = paso + 2; //3
                    if (recType == BuiltinType.UNSET) {
                        skipObject();
                        paso = paso + 1; //4
                    } else {
                        obj = getObject(type);
                        paso = paso + 1;//4
                        rec.put(name, obj);} 
                }else{
                    paso = paso + 2; //3
                    obj = getObject(type);
                    paso = paso + 1;//4
                    rec.put(name, obj);
                }    
            }catch(Exception ex){
              
                switch(paso)                {
                    case 0:case 1:case 3:case 4:case 5:                    
                        skipObject();
                        break;
                    case 2:
                        skipObject();
                        skipObject();
                        break;
                }            
                throw ex;
            }
        }
        return rec;
    }

    public AcptType[] getGeomTypes() {
        return geomTypes;
    }

}
