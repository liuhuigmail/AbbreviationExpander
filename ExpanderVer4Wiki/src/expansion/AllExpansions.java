package expansion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import Step1.Utils;
import entity.Argument;
import entity.ClassName;
import entity.Field;
import entity.Identifier;
import entity.MethodName;
import entity.Parameter;
import entity.Variable;
import relation.AssignInfo;
import relation.ClassInfo;
import relation.MethodDeclarationInfo;
import relation.MethodInvocationInfo;

public class AllExpansions
{
	public static HashMap<String, ClassName> idToClassName = new HashMap<>();
	public static HashMap<String, MethodName> idToMethodName = new HashMap<>();
	public static HashMap<String, Field> idToField = new HashMap<>();
	public static HashMap<String, Parameter> idToParameter = new HashMap<>();
	public static HashMap<String, Variable> idToVariable = new HashMap<>();

	// type to ding
	public static HashMap<String, Identifier> idToIdentifier = new HashMap<>();

	public static HashMap<String, HashSet<String>> idToFiles = new HashMap<>();

	public static HashMap<String, ArrayList<String>> childToParent = new HashMap<>();
	public static HashMap<String, ArrayList<String>> parentToChild = new HashMap<>();

	public static ArrayList<MethodDeclarationInfo> methodDeclarationInfos = new ArrayList<>();
	public static ArrayList<MethodInvocationInfo> methodInvocationInfos = new ArrayList<>();
	public static ArrayList<AssignInfo> assignInfos = new ArrayList<>();
	public static ArrayList<ClassInfo> classInfos = new ArrayList<>();
	public static HashMap<String, HashSet<String>> idToComments = new HashMap<>();

	public static HashMap<String, ClassNameExpansions> idToClassNameExpansions = new HashMap<>();
	public static HashMap<String, FieldNameExpansions> idToFieldNameExpansions = new HashMap<>();
	public static HashMap<String, MethodNameExpansions> idToMethodNameExpansions = new HashMap<>();
	public static HashMap<String, ParameterNameExpansions> idToParameterNameExpansions = new HashMap<>();
	public static HashMap<String, VariableNameExpansions> idToVariableNameExpansions = new HashMap<>();
// lx
	public static HashMap<String, Integer> idToLineNum = new HashMap<String, Integer>();

// lx
	private static void handleMethodDeclaration()
	{
		for (MethodDeclarationInfo methodDeclarationInfo : methodDeclarationInfos)
		{
			ArrayList<Parameter> parameters = methodDeclarationInfo.parameters;
			for (int i = 0; i < parameters.size(); i++)
			{
				// method name --> parameter
				AllExpansions.addExpansion(methodDeclarationInfo.methodName, parameters.get(i), "MethodName",
						"parameter");
				// parameter --> method name
				AllExpansions.addExpansion(parameters.get(i), methodDeclarationInfo.methodName, "ParameterName",
						"enclosingMethod");
			}

			ArrayList<Identifier> identifiers = methodDeclarationInfo.identifiers;
			for (int i = 0; i < identifiers.size(); i++)
			{
				Identifier identifier = identifiers.get(i);
				if (idToVariable.keySet().contains(identifier.id))
				{
					addExpansion(identifier, methodDeclarationInfo.methodName, "VariableName", "enclosingMethod");
				}
			}
		}
	}

	private static void handleExtend()
	{
		handleChildToParent();

		for (ClassInfo classInfo : classInfos)
		{
			ClassName className = classInfo.className;

			ArrayList<Field> fields = classInfo.fields;
			for (int i = 0; i < fields.size(); i++)
			{
				Field field = fields.get(i);
				AllExpansions.idToField.put(className.id, field);

				AllExpansions.addExpansion(className, field, "ClassName", "fields");
				AllExpansions.addExpansion(field, className, "FieldName", "enclosingClass");
			}
			ArrayList<MethodName> methodNames = classInfo.methodNames;
			for (int i = 0; i < methodNames.size(); i++)
			{
				MethodName methodName = methodNames.get(i);
				AllExpansions.addExpansion(className, methodName, "ClassName", "methods");
				AllExpansions.addExpansion(methodName, className, "MethodName", "enclosingClass");
			}

			ArrayList<Identifier> identifiers = classInfo.identifiers;
			for (int i = 0; i < identifiers.size(); i++)
			{
				Identifier identifier = identifiers.get(i);
				if (idToVariable.keySet().contains(identifier.id))
				{
					addExpansion(identifier, className, "VariableName", "enclosingClass");
				}
			}
		}
	}

	public static void handleChildToParent()
	{
		for (String id : idToClassName.keySet())
		{
			if (idToClassName.get(id) instanceof ClassName)
			{
				ArrayList<String> parents = childToParent.get(id);
				ArrayList<String> ancestor = new ArrayList<>();
				if (parents != null)
				{
					for (String string : parents)
					{
						AllExpansions.addExpansion(idToClassName.get(id), idToClassName.get(string), "ClassName",
								"parents");
					}
					search(childToParent, ancestor, parents);
				}
				ArrayList<String> subclass = parentToChild.get(id);
				ArrayList<String> subsubclass = new ArrayList<>();
				if (subclass != null)
				{
					for (String string : subclass)
					{
						AllExpansions.addExpansion(idToClassName.get(id), idToClassName.get(string), "ClassName",
								"subclass");
					}
					search(parentToChild, subsubclass, subclass);
				}
				for (String string : ancestor)
				{
					AllExpansions.addExpansion(idToClassName.get(id), idToClassName.get(string), "ClassName",
							"ancestor");
				}
				for (String string : subsubclass)
				{
					AllExpansions.addExpansion(idToClassName.get(id), idToClassName.get(string), "ClassName",
							"subsubclass");
				}
			}

		}
	}

	public static void search(HashMap<String, ArrayList<String>> tree, ArrayList<String> ancestor,
			ArrayList<String> parents)
	{
		ArrayList<String> temp = new ArrayList<>();
		for (int i = 0; i < parents.size(); i++)
		{
			String t = parents.get(i);
			ArrayList<String> tt = tree.get(t);
			if (tt == null)
			{
				continue;
			}
			temp.addAll(tt);
		}
		if (temp.size() == 0)
		{
			return;
		}
		ancestor.addAll(temp);
		search(tree, ancestor, temp);
	}

	private static String printHashSet(HashSet<String> idSet)
	{
		String result = "";
		for (String id : idSet)
		{
			if (idToClassName.keySet().contains(id))
			{
				Identifier identifier = idToClassName.get(id);
				result += "ClassName:" + identifier.name + ";";
			} else if (idToMethodName.keySet().contains(id))
			{
				Identifier identifier = idToMethodName.get(id);
				result += "MethodName:" + identifier.name + ";";
			} else if (idToField.keySet().contains(id))
			{
				Identifier identifier = idToField.get(id);
				result += "FieldName:" + identifier.name + ";";
			} else if (idToParameter.keySet().contains(id))
			{
				Identifier identifier = idToParameter.get(id);
				result += "ParameterName:" + identifier.name + ";";
			} else if (idToVariable.keySet().contains(id))
			{
				Identifier identifier = idToVariable.get(id);
				result += "VariableName:" + identifier.name + ";";
			}
		}
		return result;
	}

	private static String commaToOther(String str)
	{
		return str.replaceAll(",", "_");
	}

	private static void toFile()
	{
		Utils.appendFile("id," + "files," + "name," + "typeOfIdentifier," + "subclass," + "subsubclass," + "parents,"
				+ "ancestor," + "methods," + "fields," + "comment," + "type," + "enclosingClass," + "assignment,"
				+ "methodInvocated," + "parameterArgument," + "parameter," + "enclosingMethod" + "\n");

		for (String id : idToClassName.keySet())
		{
			if (!idToClassNameExpansions.keySet().contains(id))
			{
				Utils.appendFile(commaToOther(id));
				Utils.appendFile("," + commaToOther(printHashSetString(idToFiles.get(id))));
				Utils.appendFile("," + commaToOther(idToClassName.get(id).name));
				Utils.appendFile("," + commaToOther("ClassName"));
				Utils.appendFile(",,,,,,,,,,,,,,");
				Utils.appendFile("\n");
			}
		}
		for (String id : idToMethodName.keySet())
		{
			if (!idToMethodNameExpansions.keySet().contains(id))
			{
				Utils.appendFile(commaToOther(id));
				Utils.appendFile("," + commaToOther(printHashSetString(idToFiles.get(id))));
				Utils.appendFile("," + commaToOther(idToMethodName.get(id).name));
				Utils.appendFile("," + commaToOther("MethodName"));
				Utils.appendFile(",,,,,,,,,,,,,,");
				Utils.appendFile("\n");
			}
		}
		for (String id : idToParameter.keySet())
		{
			if (!idToParameterNameExpansions.keySet().contains(id))
			{
				Utils.appendFile(commaToOther(id));
				Utils.appendFile("," + commaToOther(printHashSetString(idToFiles.get(id))));
				Utils.appendFile("," + commaToOther(idToParameter.get(id).name));
				Utils.appendFile("," + commaToOther("ParameterName"));
				Utils.appendFile(",,,,,,,,,,,,,,");
				Utils.appendFile("\n");
			}
		}
		for (String id : idToField.keySet())
		{
			if (!idToFieldNameExpansions.keySet().contains(id))
			{
				Utils.appendFile(commaToOther(id));
				Utils.appendFile("," + commaToOther(printHashSetString(idToFiles.get(id))));
				Utils.appendFile("," + commaToOther(idToField.get(id).name));
				Utils.appendFile("," + commaToOther("FieldName"));
				Utils.appendFile(",,,,,,,,,,,,,,");
				Utils.appendFile("\n");
			}
		}
		for (String id : idToVariable.keySet())
		{
			if (!idToVariableNameExpansions.keySet().contains(id))
			{
				Utils.appendFile(commaToOther(id));
				Utils.appendFile("," + commaToOther(printHashSetString(idToFiles.get(id))));
				Utils.appendFile("," + commaToOther(idToVariable.get(id).name));
				Utils.appendFile("," + commaToOther("VariableName"));
				Utils.appendFile(",,,,,,,,,,,,,,");
				Utils.appendFile("\n");
			}
		}

		for (String id : idToClassNameExpansions.keySet())
		{
			ClassNameExpansions temp = idToClassNameExpansions.get(id);
			Utils.appendFile(commaToOther(id));
			Utils.appendFile("," + commaToOther(printHashSetString(idToFiles.get(id))));
			Utils.appendFile("," + commaToOther(idToClassName.get(id).name));
			Utils.appendFile("," + commaToOther("ClassName"));
			Utils.appendFile("," + commaToOther(printHashSet(temp.subclass)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.subsubclass)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.parents)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.ancestor)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.methods)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.fields)));
			Utils.appendFile("," + commaToOther(printHashSetString(temp.comment)));
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile("\n");

		}
		for (String id : idToMethodNameExpansions.keySet())
		{
			MethodNameExpansions temp = idToMethodNameExpansions.get(id);
			Utils.appendFile(commaToOther(id));
			Utils.appendFile("," + commaToOther(printHashSetString(idToFiles.get(id))));
			Utils.appendFile("," + commaToOther(idToMethodName.get(id).name));
			Utils.appendFile("," + commaToOther("MethodName"));
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile("," + commaToOther(printHashSetString(temp.comment)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.type)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.enclosingClass)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.assignment)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.methodInvocated)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.parameterArgument)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.parameter)));
			Utils.appendFile(",");
			Utils.appendFile("\n");

		}
		for (String id : idToFieldNameExpansions.keySet())
		{
			FieldNameExpansions temp = idToFieldNameExpansions.get(id);
			Utils.appendFile(commaToOther(id));
			Utils.appendFile("," + commaToOther(printHashSetString(idToFiles.get(id))));
			Utils.appendFile("," + commaToOther(idToField.get(id).name));
			Utils.appendFile("," + commaToOther("FieldName"));
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile("," + commaToOther(printHashSetString(temp.comment)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.type)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.enclosingClass)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.assignment)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.methodInvocated)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.parameterArgument)));
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile("\n");

		}
		for (String id : idToParameterNameExpansions.keySet())
		{
			ParameterNameExpansions temp = idToParameterNameExpansions.get(id);
			Utils.appendFile(commaToOther(id));
			Utils.appendFile("," + commaToOther(printHashSetString(idToFiles.get(id))));
			Utils.appendFile("," + commaToOther(idToParameter.get(id).name));
			Utils.appendFile("," + commaToOther("ParameterName"));
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile("," + commaToOther(printHashSetString(temp.comment)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.type)));
			Utils.appendFile(",");
			Utils.appendFile("," + commaToOther(printHashSet(temp.assignment)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.methodInvocated)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.parameterArgument)));
			Utils.appendFile(",");
			Utils.appendFile("," + commaToOther(printHashSet(temp.enclosingMethod)));
			Utils.appendFile("\n");

		}
		for (String id : idToVariableNameExpansions.keySet())
		{
			VariableNameExpansions temp = idToVariableNameExpansions.get(id);
			Utils.appendFile(commaToOther(id));
			Utils.appendFile("," + commaToOther(printHashSetString(idToFiles.get(id))));
			Utils.appendFile("," + commaToOther(idToVariable.get(id).name));
			Utils.appendFile("," + commaToOther("VariableName"));
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile(",");
			Utils.appendFile("," + commaToOther(printHashSetString(temp.comment)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.type)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.enclosingClass)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.assignment)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.methodInvocated)));
			Utils.appendFile("," + commaToOther(printHashSet(temp.parameterArgument)));
			Utils.appendFile(",");
			Utils.appendFile("," + commaToOther(printHashSet(temp.enclosingMethod)));
			Utils.appendFile("\n");

		}

	}

	private static String printHashSetString(HashSet<String> hashSet)
	{
		String result = "";
		for (String string : hashSet)
		{
			result += string + ";";
		}
		return result;
	}

	private static String parseComment(String comment)
	{
		String result = "";
		for (int i = 0; i < comment.length(); i++)
		{
			if (Utils.isLetter(comment.charAt(i)))
			{
				result += comment.charAt(i);
			} else
			{
				result += " ";
			}
		}
		while (result.contains("  "))
		{
			result = result.replaceAll("  ", " ");
		}
		return result.trim();
	}

	private static void handleExpansionType()
	{
		for (String id : idToMethodName.keySet())
		{
			MethodName methodName = idToMethodName.get(id);
			if (methodName.type != null)
			{
				addExpansion(methodName, methodName.type, "MethodName", "type");
			}
		}
		for (String id : idToField.keySet())
		{
			Field field = idToField.get(id);
			addExpansion(field, field.type, "FieldName", "type");
		}
		for (String id : idToParameter.keySet())
		{
			Parameter parameter = idToParameter.get(id);
			addExpansion(parameter, parameter.type, "ParameterName", "type");
		}
		for (String id : idToVariable.keySet())
		{
			Variable variable = (Variable) idToVariable.get(id);
			addExpansion(variable, variable.type, "VariableName", "type");
		}
	}

	private static void handleMethodInvocation()
	{
		HashMap<String, MethodDeclarationInfo> hashMap = new HashMap<>();
		for (int i = 0; i < methodDeclarationInfos.size(); i++)
		{
			hashMap.put(methodDeclarationInfos.get(i).methodName.id, methodDeclarationInfos.get(i));
		}
		for (int i = 0; i < methodInvocationInfos.size(); i++)
		{
			MethodInvocationInfo methodInvocationInfo = methodInvocationInfos.get(i);
			String methodId = methodInvocationInfo.methodName.id;
			if (hashMap.containsKey(methodId))
			{
				MethodDeclarationInfo methodDeclarationInfo = hashMap.get(methodId);
				ArrayList<Parameter> parameters = methodDeclarationInfo.parameters;
				ArrayList<Argument> arguments = methodInvocationInfo.arguments;
				if (parameters.size() != arguments.size())
				{
					System.err.println(
							"not equal size of parameters and arguments " + parameters.size() + " " + arguments.size());
					continue;
				}
				for (int j = 0; j < parameters.size(); j++)
				{
					Parameter parameter = parameters.get(j);
					Argument argument = arguments.get(j);
					for (int k = 0; k < argument.identifiers.size(); k++)
					{
						Identifier identifier = argument.identifiers.get(k);
						if (!idToClassName.keySet().contains(identifier.id))
						{
							addExpansion(parameter, identifier, "ParameterName", "argument");
						}
					}
				}
				for (int j = 0; j < methodInvocationInfo.arguments.size(); j++)
				{
					Parameter parameter = parameters.get(j);
					Argument argument = arguments.get(j);
					for (int k = 0; k < argument.identifiers.size(); k++)
					{
						Identifier identifier = argument.identifiers.get(k);
						if (!idToClassName.keySet().contains(identifier.id))
						{
							addExpansionAccordingToType(identifier, methodInvocationInfo.methodName, "methodInvocated");
							addExpansionAccordingToType(identifier, parameter, "parameterArgument");
						}
					}
				}
			} else
			{
				System.err.println("NOT CONTAIN:\t" + methodId);
			}
		}
	}

	public static void postprocess()
	{
		handleFieldParameterVariable();
		handleAssign();
		handleMethodInvocation();
		handleComment();

		handleExtend();
		handleMethodDeclaration();
		handleExpansionType();

		toFile();
	}

	private static void handleAssign()
	{
		for (AssignInfo assignInfo : assignInfos)
		{
			ArrayList<Identifier> left = assignInfo.left;
			ArrayList<Identifier> right = assignInfo.right;

			if (right != null)
			{

				for (int i = 0; i < left.size(); i++)
				{
					Identifier identifier1 = left.get(i);
					for (int j = 0; j < right.size(); j++)
					{
						Identifier identifier2 = right.get(j);
						addExpansionAccordingToType(identifier1, identifier2, "assignment");
						addExpansionAccordingToType(identifier2, identifier1, "assignment");
					}
				}
			}
		}
	}

	private static void handleComment()
	{
		for (String id : idToComments.keySet())
		{
			String comment = "";
			HashSet<String> temp = idToComments.get(id);
			for (String string : temp)
			{
				comment += string + " ";
			}
			if (idToClassName.keySet().contains(id))
			{
				addExpansion(idToClassName.get(id), new Identifier(null, parseComment(comment)), "ClassName",
						"comment");
			} else if (idToMethodName.keySet().contains(id))
			{
				addExpansion(idToMethodName.get(id), new Identifier(null, parseComment(comment)), "MethodName",
						"comment");
			} else if (idToField.keySet().contains(id))
			{
				addExpansion(idToField.get(id), new Identifier(null, parseComment(comment)), "FieldName", "comment");
			} else if (idToParameter.keySet().contains(id))
			{
				addExpansion(idToParameter.get(id), new Identifier(null, parseComment(comment)), "ParameterName",
						"comment");
			} else if (idToVariable.keySet().contains(id))
			{
				addExpansion(idToVariable.get(id), new Identifier(null, parseComment(comment)), "VariableName",
						"comment");
			}
		}
	}

	private static void addExpansionAccordingToType(Identifier identifier1, Identifier identifier2, String relationType)
	{
		String id = identifier1.id;
		if (idToClassName.keySet().contains(id))
		{
			addExpansion(identifier1, identifier2, "ClassName", relationType);
		} else if (idToMethodName.keySet().contains(id))
		{
			addExpansion(identifier1, identifier2, "MethodName", relationType);
		} else if (idToField.keySet().contains(id))
		{
			addExpansion(identifier1, identifier2, "FieldName", relationType);
		} else if (idToParameter.keySet().contains(id))
		{
			addExpansion(identifier1, identifier2, "ParameterName", relationType);
		} else if (idToVariable.keySet().contains(id))
		{
			addExpansion(identifier1, identifier2, "VariableName", relationType);
		}
	}

	private static void handleFieldParameterVariable()
	{
		for (String id : idToIdentifier.keySet())
		{
			if (idToParameter.keySet().contains(id) || idToField.keySet().contains(id)
					|| idToClassName.keySet().contains(id) || idToMethodName.keySet().contains(id))
			{
			} else
			{
				Identifier identifier = idToIdentifier.get(id);

				if (identifier instanceof Field)
				{
					idToField.put(id, (Field) identifier);
				} else if (identifier instanceof ClassName)
				{
					idToClassName.put(id, (ClassName) identifier);
				} else if (identifier instanceof Parameter)
				{
					idToParameter.put(id, (Parameter) identifier);
				} else if (identifier instanceof MethodName)
				{
					idToMethodName.put(id, (MethodName) identifier);
				} else if (identifier instanceof Variable)
				{
					idToVariable.put(id, (Variable) identifier);
				}
			}
		}
	}

	public static void addExpansion(Identifier left, Identifier right, String identifierType, String relationType)
	{
		switch (identifierType) {
		case "ClassName": {
			switch (relationType) {
			case "subclass":
				if (idToClassNameExpansions.containsKey(left.id))
				{
					idToClassNameExpansions.get(left.id).subclass.add(right.id);
				} else
				{
					ClassNameExpansions expansion = new ClassNameExpansions();
					expansion.subclass.add(right.id);
					idToClassNameExpansions.put(left.id, expansion);
				}
				break;
			case "subsubclass":
				if (idToClassNameExpansions.containsKey(left.id))
				{
					idToClassNameExpansions.get(left.id).subsubclass.add(right.id);
				} else
				{
					ClassNameExpansions expansion = new ClassNameExpansions();
					expansion.subsubclass.add(right.id);
					idToClassNameExpansions.put(left.id, expansion);
				}
				break;
			case "parents":
				if (idToClassNameExpansions.containsKey(left.id))
				{
					idToClassNameExpansions.get(left.id).parents.add(right.id);
				} else
				{
					ClassNameExpansions expansion = new ClassNameExpansions();
					expansion.parents.add(right.id);
					idToClassNameExpansions.put(left.id, expansion);
				}
				break;
			case "ancestor":
				if (idToClassNameExpansions.containsKey(left.id))
				{
					idToClassNameExpansions.get(left.id).ancestor.add(right.id);
				} else
				{
					ClassNameExpansions expansion = new ClassNameExpansions();
					expansion.ancestor.add(right.id);
					idToClassNameExpansions.put(left.id, expansion);
				}
				break;
			case "methods":
				if (idToClassNameExpansions.containsKey(left.id))
				{
					idToClassNameExpansions.get(left.id).methods.add(right.id);
				} else
				{
					ClassNameExpansions expansion = new ClassNameExpansions();
					expansion.methods.add(right.id);
					idToClassNameExpansions.put(left.id, expansion);
				}
				break;
			case "fields":
				if (idToClassNameExpansions.containsKey(left.id))
				{
					idToClassNameExpansions.get(left.id).fields.add(right.id);
				} else
				{
					ClassNameExpansions expansion = new ClassNameExpansions();
					expansion.fields.add(right.id);
					idToClassNameExpansions.put(left.id, expansion);
				}
				break;
			case "comment":
				if (idToClassNameExpansions.containsKey(left.id))
				{
					idToClassNameExpansions.get(left.id).comment.add(right.name);
				} else
				{
					ClassNameExpansions expansion = new ClassNameExpansions();
					expansion.comment.add(right.name);
					idToClassNameExpansions.put(left.id, expansion);
				}
				break;
			default:
				break;
			}
		}
			break;
		case "FieldName": {
			switch (relationType) {
			case "type":
				if (idToFieldNameExpansions.containsKey(left.id))
				{
					idToFieldNameExpansions.get(left.id).type.add(right.id);
				} else
				{
					FieldNameExpansions expansion = new FieldNameExpansions();
					expansion.type.add(right.id);
					idToFieldNameExpansions.put(left.id, expansion);
				}
				break;
			case "comment":
				if (idToFieldNameExpansions.containsKey(left.id))
				{
					idToFieldNameExpansions.get(left.id).comment.add(right.name);
				} else
				{
					FieldNameExpansions expansion = new FieldNameExpansions();
					expansion.comment.add(right.name);
					idToFieldNameExpansions.put(left.id, expansion);
				}
				break;
			case "enclosingClass":
				if (idToFieldNameExpansions.containsKey(left.id))
				{
					idToFieldNameExpansions.get(left.id).enclosingClass.add(right.id);
				} else
				{
					FieldNameExpansions expansion = new FieldNameExpansions();
					expansion.enclosingClass.add(right.id);
					idToFieldNameExpansions.put(left.id, expansion);
				}
				break;
			case "assignment":
				if (idToFieldNameExpansions.containsKey(left.id))
				{
					idToFieldNameExpansions.get(left.id).assignment.add(right.id);
				} else
				{
					FieldNameExpansions expansion = new FieldNameExpansions();
					expansion.assignment.add(right.id);
					idToFieldNameExpansions.put(left.id, expansion);
				}
				break;
			case "methodInvocated":
				if (idToFieldNameExpansions.containsKey(left.id))
				{
					idToFieldNameExpansions.get(left.id).methodInvocated.add(right.id);
				} else
				{
					FieldNameExpansions expansion = new FieldNameExpansions();
					expansion.methodInvocated.add(right.id);
					idToFieldNameExpansions.put(left.id, expansion);
				}
				break;
			case "parameterArgument":
				if (idToFieldNameExpansions.containsKey(left.id))
				{
					idToFieldNameExpansions.get(left.id).parameterArgument.add(right.id);
				} else
				{
					FieldNameExpansions expansion = new FieldNameExpansions();
					expansion.parameterArgument.add(right.id);
					idToFieldNameExpansions.put(left.id, expansion);
				}
				break;
			default:
				break;
			}
		}
			break;
		case "MethodName": {
			switch (relationType) {
			case "type":
				if (idToMethodNameExpansions.containsKey(left.id))
				{
					idToMethodNameExpansions.get(left.id).type.add(right.id);
				} else
				{
					MethodNameExpansions expansion = new MethodNameExpansions();
					expansion.type.add(right.id);
					idToMethodNameExpansions.put(left.id, expansion);
				}
				break;
			case "comment":
				if (idToMethodNameExpansions.containsKey(left.id))
				{
					idToMethodNameExpansions.get(left.id).comment.add(right.name);
				} else
				{
					MethodNameExpansions expansion = new MethodNameExpansions();
					expansion.comment.add(right.name);
					idToMethodNameExpansions.put(left.id, expansion);
				}
				break;
			case "enclosingClass":
				if (idToMethodNameExpansions.containsKey(left.id))
				{
					idToMethodNameExpansions.get(left.id).enclosingClass.add(right.id);
				} else
				{
					MethodNameExpansions expansion = new MethodNameExpansions();
					expansion.enclosingClass.add(right.id);
					idToMethodNameExpansions.put(left.id, expansion);
				}
				break;
			case "parameter":
				if (idToMethodNameExpansions.containsKey(left.id))
				{
					idToMethodNameExpansions.get(left.id).parameter.add(right.id);
				} else
				{
					MethodNameExpansions expansion = new MethodNameExpansions();
					expansion.parameter.add(right.id);
					idToMethodNameExpansions.put(left.id, expansion);
				}
				break;
			case "assignment":
				if (idToMethodNameExpansions.containsKey(left.id))
				{
					idToMethodNameExpansions.get(left.id).assignment.add(right.id);
				} else
				{
					MethodNameExpansions expansion = new MethodNameExpansions();
					expansion.assignment.add(right.id);
					idToMethodNameExpansions.put(left.id, expansion);
				}
				break;
			case "parameterArgument":
				if (idToMethodNameExpansions.containsKey(left.id))
				{
					idToMethodNameExpansions.get(left.id).parameterArgument.add(right.id);
				} else
				{
					MethodNameExpansions expansion = new MethodNameExpansions();
					expansion.parameterArgument.add(right.id);
					idToMethodNameExpansions.put(left.id, expansion);
				}
				break;
			case "methodInvocated":
				if (idToMethodNameExpansions.containsKey(left.id))
				{
					idToMethodNameExpansions.get(left.id).methodInvocated.add(right.id);
				} else
				{
					MethodNameExpansions expansion = new MethodNameExpansions();
					expansion.methodInvocated.add(right.id);
					idToMethodNameExpansions.put(left.id, expansion);
				}
				break;
			default:
				break;
			}
		}
			break;
		case "ParameterName": {
			switch (relationType) {
			case "type":
				if (idToParameterNameExpansions.containsKey(left.id))
				{
					idToParameterNameExpansions.get(left.id).type.add(right.id);
				} else
				{
					ParameterNameExpansions expansion = new ParameterNameExpansions();
					expansion.type.add(right.id);
					idToParameterNameExpansions.put(left.id, expansion);
				}
				break;
			case "comment":
				if (idToParameterNameExpansions.containsKey(left.id))
				{
					idToParameterNameExpansions.get(left.id).comment.add(right.name);
				} else
				{
					ParameterNameExpansions expansion = new ParameterNameExpansions();
					expansion.comment.add(right.name);
					idToParameterNameExpansions.put(left.id, expansion);
				}
				break;
			case "parameterArgument":
				if (idToParameterNameExpansions.containsKey(left.id))
				{
					idToParameterNameExpansions.get(left.id).parameterArgument.add(right.id);
				} else
				{
					ParameterNameExpansions expansion = new ParameterNameExpansions();
					expansion.parameterArgument.add(right.id);
					idToParameterNameExpansions.put(left.id, expansion);
				}
				break;
			case "enclosingMethod":
				if (idToParameterNameExpansions.containsKey(left.id))
				{
					idToParameterNameExpansions.get(left.id).enclosingMethod.add(right.id);
				} else
				{
					ParameterNameExpansions expansion = new ParameterNameExpansions();
					expansion.enclosingMethod.add(right.id);
					idToParameterNameExpansions.put(left.id, expansion);
				}
				break;
			case "argument":
				if (idToParameterNameExpansions.containsKey(left.id))
				{
					idToParameterNameExpansions.get(left.id).argument.add(right.id);
				} else
				{
					ParameterNameExpansions expansion = new ParameterNameExpansions();
					expansion.argument.add(right.id);
					idToParameterNameExpansions.put(left.id, expansion);
				}
				break;
			case "assignment":
				if (idToParameterNameExpansions.containsKey(left.id))
				{
					idToParameterNameExpansions.get(left.id).assignment.add(right.id);
				} else
				{
					ParameterNameExpansions expansion = new ParameterNameExpansions();
					expansion.assignment.add(right.id);
					idToParameterNameExpansions.put(left.id, expansion);
				}
				break;
			case "methodInvocated":
				if (idToParameterNameExpansions.containsKey(left.id))
				{
					idToParameterNameExpansions.get(left.id).methodInvocated.add(right.id);
				} else
				{
					ParameterNameExpansions expansion = new ParameterNameExpansions();
					expansion.methodInvocated.add(right.id);
					idToParameterNameExpansions.put(left.id, expansion);
				}
				break;
			default:
				break;
			}
		}
			break;
		case "VariableName": {
			switch (relationType) {
			case "type":
				if (idToVariableNameExpansions.containsKey(left.id))
				{
					idToVariableNameExpansions.get(left.id).type.add(right.id);
				} else
				{
					VariableNameExpansions expansion = new VariableNameExpansions();
					expansion.type.add(right.id);
					idToVariableNameExpansions.put(left.id, expansion);
				}
				break;
			case "methodInvocated":
				if (idToVariableNameExpansions.containsKey(left.id))
				{
					idToVariableNameExpansions.get(left.id).methodInvocated.add(right.id);
				} else
				{
					VariableNameExpansions expansion = new VariableNameExpansions();
					expansion.methodInvocated.add(right.id);
					idToVariableNameExpansions.put(left.id, expansion);
				}
				break;
			case "comment":
				if (idToVariableNameExpansions.containsKey(left.id))
				{
					idToVariableNameExpansions.get(left.id).comment.add(right.name);
				} else
				{
					VariableNameExpansions expansion = new VariableNameExpansions();
					expansion.comment.add(right.name);
					idToVariableNameExpansions.put(left.id, expansion);
				}
				break;
			case "enclosingMethod":
				if (idToVariableNameExpansions.containsKey(left.id))
				{
					idToVariableNameExpansions.get(left.id).enclosingMethod.add(right.id);
				} else
				{
					VariableNameExpansions expansion = new VariableNameExpansions();
					expansion.enclosingMethod.add(right.id);
					idToVariableNameExpansions.put(left.id, expansion);
				}
				break;
			case "enclosingClass":
				if (idToVariableNameExpansions.containsKey(left.id))
				{
					idToVariableNameExpansions.get(left.id).enclosingClass.add(right.id);
				} else
				{
					VariableNameExpansions expansion = new VariableNameExpansions();
					expansion.enclosingClass.add(right.id);
					idToVariableNameExpansions.put(left.id, expansion);
				}
				break;
			case "parameterArgument":
				if (idToVariableNameExpansions.containsKey(left.id))
				{
					idToVariableNameExpansions.get(left.id).parameterArgument.add(right.id);
				} else
				{
					VariableNameExpansions expansion = new VariableNameExpansions();
					expansion.parameterArgument.add(right.id);
					idToVariableNameExpansions.put(left.id, expansion);
				}
				break;
			case "assignment":
				if (idToVariableNameExpansions.containsKey(left.id))
				{
					idToVariableNameExpansions.get(left.id).assignment.add(right.id);
				} else
				{
					VariableNameExpansions expansion = new VariableNameExpansions();
					expansion.assignment.add(right.id);
					idToVariableNameExpansions.put(left.id, expansion);
				}
				break;
			default:
				break;
			}
		}
			break;
		default:
			break;
		}
	}
}
