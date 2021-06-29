package entity;

public class Parameter extends Identifier{
	public ClassName type;
	
	public Parameter(String id, String name, ClassName type) {
		super(id, name);
		this.type = type;
	}

	@Override
	public String toString() {
		return "Parameter [id=" + id + ", name=" + name + ", type=" + type + "]";
	}	
}
