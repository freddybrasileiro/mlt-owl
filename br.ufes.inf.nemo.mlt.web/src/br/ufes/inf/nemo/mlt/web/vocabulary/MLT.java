package br.ufes.inf.nemo.mlt.web.vocabulary;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class MLT {
	private final static String NS = "http://www.nemo.inf.ufes.br/mlt" + "#";
	private final static String prefix = "mlt";

    public static String getURI(){ 
    	return NS; 
    }
    
    public static String getPrefix() {
		return prefix;
	}
    
    protected final static Resource resource( String local ){ 
    	return ResourceFactory.createResource( NS + local ); 
    }
    
    protected final static Property property( String local ){ 
    	return ResourceFactory.createProperty( NS + local ); 
    }

    public final static Resource TokenIndividual = resource( "TokenIndividual" );
    
    public final static Resource _1stOrderClass = resource( "1stOrderClass" );
    
    public final static Resource _2ndOrderClass = resource( "2ndOrderClass" );
    
    public final static Resource _3rdOrderClass = resource( "3rdOrderClass" );

    public final static Property characterizes = property( "characterizes" );
    
    public final static Property completelyCharacterizes = property( "completelyCharacterizes" );
    
    public final static Property disjointlyCharacterizes = property( "disjointlyCharacterizes" );
    
    public final static Property incompletelyCharacterizes = property( "incompletelyCharacterizes" );
    
    public final static Property overlappinglyCharacterizes = property( "overlappinglyCharacterizes" );
    
    public final static Property partitions = property( "partitions" );
    
    public final static Property isPowertypeOf = property( "isPowertypeOf" );
    
    public final static Property isSubordinatedTo = property( "isSubordinatedTo" );
    
}
