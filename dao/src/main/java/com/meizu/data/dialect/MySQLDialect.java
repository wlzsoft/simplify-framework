package com.meizu.data.dialect;
/**
 */
public class MySQLDialect extends Dialect{

	public boolean supportsLimitOffset(){
		return true;
	}
	
    public boolean supportsLimit() {   
        return true;   
    }  
    
    protected String getLimitString(String sql, int offset,String offsetPlaceholder, int limit, String limitPlaceholder) {
        if (offset > 0) {   
        	return sql + " limit "+offsetPlaceholder+","+limitPlaceholder; 
        } else {   
            return sql + " limit "+limitPlaceholder;
        }  
	}   
  
}
