package entity;

public class Variable extends Identifier {
	public ClassName type;

	public Variable(String id, String name, ClassName type) {
		super(id, name);
		this.type = type;
	}

	@Override
	public String toString() {
		return "Variable [id=" + id + ", name=" + name + ", type=" + type + "]";
	}
}
