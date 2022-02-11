package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;

public class SymTableVisitor extends SymbolTableVisitor {

	private StringBuilder sb = new StringBuilder();
	private static final String TAB = "\t";
	
	private int currentIndent = 0;
	
	private void addIndent() {
		for (int i = 0; i < currentIndent; i++) {
			sb.append(TAB);
		}
	}
	
	@Override
	public void visitObjNode(Obj objToVisit) {
		switch (objToVisit.getKind()) {
			case Obj.Con:  sb.append("Con "); break;
			case Obj.Var:  sb.append("Var "); break;
			case Obj.Type: sb.append("Type "); break;
			case Obj.Meth: sb.append("Meth "); break;
			case Obj.Fld:  sb.append("Fld "); break;
			case Obj.Prog: sb.append("Prog "); break;
		}
		
		sb.append(objToVisit.getName() + ":");
		
		//if (objToVisit.getKind() == Obj.Var && )
		//da ne udje u petlju
		if (!objToVisit.getName().equals("this")) {
			objToVisit.getType().accept(this);
		}
		sb.append(", " + objToVisit.getAdr());
		sb.append(", " + objToVisit.getLevel());
		
		if (objToVisit.getKind() == Obj.Prog || objToVisit.getKind() == Obj.Meth) {
			currentIndent++;
			sb.append("\n");
		}
		

		for (Obj obj : objToVisit.getLocalSymbols()) {
			addIndent();
			obj.accept(this);
			sb.append("\n");
		}
		
		if (objToVisit.getKind() == Obj.Prog || objToVisit.getKind() == Obj.Meth) {
			currentIndent--;
		}
					
		
	}

	@Override
	public void visitScopeNode(Scope scopeToVisit) {
		for (Obj obj : scopeToVisit.values()) {
			obj.accept(this);
			sb.append("\n");
		}		
	}

	@Override
	public void visitStructNode(Struct structToVisit) {
		switch (structToVisit.getKind()) {
			case Struct.None:
				sb.append("notype");
				break;
			case Struct.Int:
				sb.append("int");
				break;
			case Struct.Bool:
				sb.append("bool");
				break;
			case Struct.Char:
				sb.append("char");
				break;
			case Struct.Array:
				sb.append("Arr of ");
				
				switch (structToVisit.getElemType().getKind()) {
					case Struct.None:
						sb.append("notype");
						break;
					case Struct.Int:
						sb.append("int");
						break;
					case Struct.Bool:
						sb.append("bool");
						break;
					case Struct.Char:
						sb.append("char");
						break;
					case Struct.Class:
						sb.append("Class");
						break;
				}
				break;
			case Struct.Class:
				sb.append("Class [");
				for (Obj obj : structToVisit.getMembers()) {
					obj.accept(this);
				}
				sb.append("]");
				break;
		}
		
	}

	@Override
	public String getOutput() {
		return sb.toString();
	}

}
