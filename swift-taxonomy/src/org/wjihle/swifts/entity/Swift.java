package org.wjihle.swifts.entity;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/*
 * SWIFT JDO ENTITY CLASS
 */

/*
 * Mark the Swift JDO Entity class for Persistence. Note that this class is also JDO enabled.
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Swift {

	
	/*
	 * We have specified that a valueStrategy and the value of which says 
	 * that the underlying JDO implementation will take care of providing
	 * it a unique ID. The Id value will be filled up appropriately at the
	 * time of persistence
	 */
	
	/*
	 * Gave Bad Request Error...
	 */
//	@PrimaryKey
//    (valueStrategy = IdGeneratorStrategy.IDENTITY)
//	private long id;
	
	
	/*
	 * This works but you have to provide an id (:
	 */
	@PrimaryKey
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;	
	
	private String kingdom; 
	private String phylum;	
	private String taxonclass;	
	private String taxonorder;
	private String family;
	private String genus;
	private String species;
	private String cname;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getCname() {
		return cname;
		
		
	}
	public void setCname(String commonName) {
		this.cname = commonName;
	}
	public String getKingdom() {
		return "Animalia";
	}
	public void setKingdom(String kingdom) {
		this.kingdom = kingdom;
	}
	public String getPhylum() {
		return "Chordata";
	}
	public void setPhylum(String phylum) {
		this.phylum = phylum;
	}
	public String getTaxonclass() {
		return "Aves";
	}
	public void setTaxonclass(String taxonclass) {
		this.taxonclass = taxonclass;
	}
	public String getTaxonorder() {
		return "Apodiformes";
	}
	public void setTaxonorder(String taxonorder) {
		this.taxonorder = taxonorder;
	}
	public String getFamily() {
		return "Apodinae";
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public String getGenus() {
		return genus;
	}
	public void setGenus(String genus) {
		this.genus = genus;
	}
	public String getSpecies() {
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
	}	

	@Override
	public String toString(){
	
		return "Swift [id="+id +",CommonName="+cname +", Genus=" + genus + ", Species=" + species + "]";
	}	
	
	
}
