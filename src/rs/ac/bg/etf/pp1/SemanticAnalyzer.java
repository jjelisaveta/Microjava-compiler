package rs.ac.bg.etf.pp1;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.symboltable.concepts.*;
import rs.ac.bg.etf.pp1.ast.BoolConst;
import rs.ac.bg.etf.pp1.ast.CharConst;
import rs.ac.bg.etf.pp1.ast.ConstDecl;
import rs.ac.bg.etf.pp1.ast.ConstantDecl;
import rs.ac.bg.etf.pp1.ast.ConstantDecls;
import rs.ac.bg.etf.pp1.ast.GlobalOneVarDecl;
import rs.ac.bg.etf.pp1.ast.GlobalVarDeclList;
import rs.ac.bg.etf.pp1.ast.GlobalVarDecls;
import rs.ac.bg.etf.pp1.ast.NumConst;
import rs.ac.bg.etf.pp1.ast.OneArrayVariableDecl;
import rs.ac.bg.etf.pp1.ast.OneVarDecl;
import rs.ac.bg.etf.pp1.ast.OneVariableDecl;
import rs.ac.bg.etf.pp1.ast.ProgName;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.ast.Type;
import rs.ac.bg.etf.pp1.ast.TypeConst;
import rs.etf.pp1.symboltable.*;

public class SemanticAnalyzer extends VisitorAdaptor {
	
	static final Struct boolType = Tab.insert(Obj.Type, "bool", new Struct(Struct.Bool)).getType();
	
	int printCallCount = 0;
	int varDeclCount = 0;
	Obj currentMethod = null;
	Struct currentType = null;
	boolean returnFound = false;
	boolean errorDetected = false;
	int nVars;
	
	
	List<String> names = new ArrayList<>();
	List<String> variableTypes = new ArrayList<>();
	List<Obj> constValues = new ArrayList<>();
	boolean mainOk = false;
	
	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	
	public void visit(ProgName progName) {
		progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
		Tab.openScope();
	}
	
	public void visit(Program program) {
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}
	
	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTypeName());
		if(typeNode == Tab.noObj){
    		report_error("Semanticka greska: Tip " + type.getTypeName() + " nije pronadjen u tabeli simbola! ", null);
    		type.struct = Tab.noType;
    	}else{
    		if(Obj.Type == typeNode.getKind()){
    			type.struct = typeNode.getType();
    		}else{
    			report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip!", type);
    			type.struct = Tab.noType;
    		}
    	}
	}
	
	public void visit(OneVariableDecl oneVariableDecl) {
		String varName = oneVariableDecl.getVarName();
		if (Tab.find(varName) == Tab.noObj &&  !(names.size() > 0 && names.contains(varName))) {   //nije deklarisan
			names.add(varName);
			variableTypes.add("var");
		} else {
			report_error("Semanticka greska: globalna promenljiva " + varName + " je vec deklarisana", null);
		}
		
	}
	
	public void visit(OneArrayVariableDecl oneArrayVariableDecl) {
		String varName = oneArrayVariableDecl.getVarName();
		if (Tab.find(varName) == Tab.noObj &&  !(names.size() > 0 && names.contains(varName))) {   //nije deklarisan
			names.add(varName);
			variableTypes.add("array");
		} else {
			report_error("Semanticka greska: globalni niz " + varName + " je vec deklarisan", null);
		}
	}
	
	public void visit(GlobalVarDecls globalVarDecls) {
		Struct varType = globalVarDecls.getType().struct;
		Struct varArrayType = new Struct(Struct.Array, varType);
		while (!names.isEmpty()) {
			String varName = names.get(0);
			if (variableTypes.get(0).equals("var")) {
				Obj node = Tab.insert(Obj.Var, varName, varType);
				report_info("Deklarisana globalna promenljiva " + varName, null);
			} else {
				Tab.insert(Obj.Var, varName, varArrayType);
				report_info("Deklarisan globalni niz " + varName, null);
			}
			names.remove(0);
			variableTypes.remove(0);
		}
		
	}
	
	public void visit(ConstantDecl constantDecl) {
		currentType = constantDecl.getType().struct;
		if (!currentType.equals(Tab.intType) && !currentType.equals(Tab.charType) && !currentType.equals(boolType)) {
			report_error("Semanticka greska: konstanta mora biti tipa int, char ili bool.", null);
		}
		String constName = constantDecl.getConstName();
		if (Tab.find(constName) == Tab.noObj) {
			names.add(constName);
			constValues.add(constantDecl.getTypeConst().obj);
			log.info("aaaa " + constantDecl.getTypeConst().obj.getAdr());
		}  else {
			report_error("Semanticka greska: konstanta " + constName + " je vec deklarisana.", null);
		}
	}
	
	public void visit(ConstantDecls constantDecls) {
		String constName = constantDecls.getConstName();
		if (Tab.find(constName) == Tab.noObj && !(names.size() > 0 && names.contains(constName))) {
			if (constantDecls.getTypeConst().obj.getType() == currentType) {
				names.add(constName);
				constValues.add(constantDecls.getTypeConst().obj);
				log.info("aaaa " + constantDecls.getTypeConst().obj.getAdr());
			} else {
				report_error("Semanticka greska: konstanta " + constName + " nije odgovarajuceg tipa.", null);
			}
			
		} else {
			report_error("Semanticka greska: konstanta " + constName + " je vec deklarisana.", null);
		}
	}
	
	public void visit(ConstDecl constDecl) {
		while (!names.isEmpty()) {
			String constName = names.remove(0);
			Obj constObj = constValues.remove(0);
			Tab.insert(Obj.Con, constName, currentType);
			report_info("Deklarisana konstanta " + constName, null);
		}
		currentType = null;
	}
	
	public void visit(NumConst numConst) {
		numConst.obj = new Obj(Obj.Con, null, Tab.intType);
		numConst.obj.setAdr(numConst.getNumConst());
	}
	
	public void visit(CharConst charConst) {
		charConst.obj = new Obj(Obj.Con, null, Tab.charType);
		charConst.obj.setAdr(charConst.getCharConst());
	}
	
	public void visit(BoolConst boolConst) {
		boolConst.obj = new Obj(Obj.Con, null, boolType);
		Integer intValue = boolConst.getBoolConst().equals("true") ? 1 : 0;
		boolConst.obj.setAdr(intValue);
	}
	
	public void visit(GlobalVarDeclList globalVarDeclList) {
		
	}
}

