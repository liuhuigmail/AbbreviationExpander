package entity;

public class MethodName extends Identifier {
	public ClassName type;
	
	public MethodName(String id, String name, ClassName type) {
		super(id, name);
		this.type = type;
	}

	@Override
	public String toString() {
		return "MethodName [id=" + id + ", name=" + name + ", type=" + type + "]";
	}
}
