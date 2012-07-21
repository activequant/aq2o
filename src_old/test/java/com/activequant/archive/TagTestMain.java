package com.activequant.archive;

import com.activequant.dao.hbase.TagDao;
import com.activequant.utils.ArrayUtils;

/**
 * not to be run without an underlying hbase, therefore not including in junit testing. 
 * @author ustaudinger
 *
 */
public class TagTestMain {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
	    TagDao td = new TagDao("localhost");
	    String objectType = "INST";
	    String objectId = "ABCD";
	    String tag = "TEST";
	    
	    td.tag(objectType, objectId, tag);
	    td.tag(objectType, "BCD", tag);
	    td.tag(objectType, "CDE", tag);
	    td.tag(objectType, "CDE", tag+"3");
	    td.commit();
	    String[] ids = td.getObjectIDs(objectType, tag);
	    System.out.println(ArrayUtils.toString(ids));	    
	    
	    td.untag(objectType, "CDE", tag);
	    td.untag(objectType, "CDE", tag+"2");
	    td.commit();
	    
	    ids = td.getObjectIDs(objectType, tag);
	    System.out.println(ArrayUtils.toString(ids));	
	    
	    String[] tags = td.getTags(objectType, "CDE");
	    System.out.println(ArrayUtils.toString(tags));
	}

}
