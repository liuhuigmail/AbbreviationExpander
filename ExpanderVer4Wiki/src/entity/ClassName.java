package entity;

/**
 * The type (e.g., int, float, Student) 
 */
public class ClassName extends Identifier {	
	public ClassName(String id, String name) {
		super(id, name);
	}

	@Override
	public String toString() {
		return "ClassName [id=" + id + ", name=" + name + "]";
	}
}
