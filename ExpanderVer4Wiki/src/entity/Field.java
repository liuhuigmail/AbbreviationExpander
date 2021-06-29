package entity;

public class Field extends Identifier{
	public ClassName type;
	
	public Field(String id, String name, ClassName type) {
		super(id, name);
		this.type = type;
	}
	
	public Field(Variable variable) {
		super(variable.id, variable.name);
		this.type = variable.type;
	}

	@Override
	public String toString() {
		return "Field [id=" + id + ", name=" + name + ", type=" + type + "]";
	}	
}
