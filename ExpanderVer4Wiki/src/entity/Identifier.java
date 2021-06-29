package entity;

/**
 * The base class of identifiers, whose subclasses include 
 * ClassName, MethodName, Variable, Parameter (a special type of Variable)
 */
public class Identifier {
	public String id;
	public String name;
	public Identifier(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	@Override
	public String toString() {
		return "Identifier [id=" + id + ", name=" + name + "]";
	}
}
