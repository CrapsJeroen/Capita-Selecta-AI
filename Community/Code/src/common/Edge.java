package common;
import java.util.ArrayList;
import java.util.List;

public class Edge {
//	private final List<Vertex> neighbours;
	private final Vertex first;
	private final Vertex second;
	private final int weight;

	public Edge(Vertex first, Vertex second, int weight) {
	    
	    if(first.getId() < second.getId()){
	        this.first = first;
	        this.second = second;
	    }else{
	        this.first = second;
	        this.second = first;
	    }
		this.weight = weight;
	}

	public List<Vertex> getNeighbours() {
	    List<Vertex> result = new ArrayList<Vertex>();
	    result.add(first);
	    result.add(second);
		return result;
	}
	
	public Vertex connectsTo(Vertex vertex){
	    if(!vertex.equals(first) && !vertex.equals(second)) return null;
	    if(vertex.equals(first)) return second;
	    return first;
	}

	@Override
	public String toString(){
		return ""+ first.getId() + "	" + second.getId();
	}
	
	@Override
    public int hashCode() {
	    int hash = first.hashCode();
	    hash += 7 * second.hashCode();
	    hash += 13 * weight;
	    return hash;
	}
	
	@Override
	public boolean equals(Object o){
	    if(o.getClass() != getClass()) return false;
	    Edge other = (Edge) o;
	    if(other.weight != weight) return false;
	    if(other.first != first) return false;
	    if(other.second != second) return false;
	    return true;
	}
}
