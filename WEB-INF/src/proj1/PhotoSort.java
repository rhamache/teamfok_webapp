package proj1;

import java.util.Comparator;


public class PhotoSort
{
    static final Comparator<Photo> SEARCH_RANK =  new Comparator<Photo>() {
    	public int compare(Photo p1, Photo p2) {
    		return p1.getRank().compareTo(p2.getRank());
    	}
    };
}
