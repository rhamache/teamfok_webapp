package proj1;

import java.util.Comparator;


public class PhotoSort
{
    public static final Comparator<Photo> SEARCH_RANK =  new Comparator<Photo>() {
    	public int compare(Photo p1, Photo p2) {
    		return p1.getRank().compareTo(p2.getRank());
    	}
    };
    
    public static final Comparator<Photo> SEARCH_RANK_REVERSE =  new Comparator<Photo>() {
    	public int compare(Photo p1, Photo p2) {
    		return p2.getRank().compareTo(p1.getRank());
    	}
    };
}
