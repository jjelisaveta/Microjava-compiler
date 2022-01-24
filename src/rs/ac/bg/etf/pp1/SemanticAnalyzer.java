package rs.ac.bg.etf.pp1;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.lang.model.type.NoType;
import javax.swing.text.TabableView;

import org.apache.log4j.Logger;


import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.symboltable.concepts.*;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;

public class SemanticAnalyzer extends VisitorAdaptor {
	
	static final Struct boolType = Tab.insert(Obj.Type, "bool", new Struct(Struct.Bool)).getType();
	//static final Struct recordType = Tab.insert(Obj.Type, "record", new Struct(Struct.Class)).getType();
	
	static final int RECORD_FP_POS = 2;
	
	Obj currentMethod = null;
	Obj currentRecord = null;
	Obj currentClass = null;
	Stack<Obj> currentMethodCallStack = new Stack<>();
	Obj currentDesignator = null;
	Struct currentType = null;
	boolean returnFound = false;
	boolean errorDetected = false;
	int nVars;
	
	
	List<String> names = new ArrayList<>();
	List<String> variableTypes = new ArrayList<>();
	List<Obj> constValues = new ArrayList<>();
	List<Obj> params = new ArrayList<>();
	boolean mainOk = false;
	int doCnt = 0;
	
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
	
	private boolean myAssignableTo(Struct left, Struct right) {
		//provera za null?
		//primitivni tipovi
		if (left.getKind() != Struct.Class) {
			return left.assignableTo(right);
		} else if (left.equals(right)) {  //equals ili ==
			return true;
		} else {
			//proveri natklase
			return false;
		}
		
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
    		report_error("Semanticka greska: Tip nije pronadjen u tabeli simbola ", type.getParent());
    		type.struct = Tab.noType;
    	}else{
    		if(Obj.Type == typeNode.getKind()){
    			type.struct = typeNode.getType();
    		}else{
    			report_error("Semanticka greska: Ime " + type.getTypeName() + " ne predstavlja tip ", type.getParent());
    			type.struct = Tab.noType;
    		}
    	}
	}
	
	
	/* global variables */
	public void visit(GlobalOneVariableDecl oneVariableDecl) {
		String varName = oneVariableDecl.getVarName();
		if (Tab.find(varName) == Tab.noObj &&  !(names.size() > 0 && names.contains(varName))) {   
			names.add(varName);
			variableTypes.add("var");
		} else {
			report_error("Semanticka greska: identifikator " + varName + " je vec iskoriscen ", oneVariableDecl.getParent());
		}
	}
	
	public void visit(GlobalOneArrayVariableDecl oneArrayVariableDecl) {
		String varName = oneArrayVariableDecl.getVarName();
		if (Tab.find(varName) == Tab.noObj &&  !(names.size() > 0 && names.contains(varName))) {  
			names.add(varName);
			variableTypes.add("array");
		} else {
			report_error("Semanticka greska: identifikator " + varName + " je vec iskoriscen ", oneArrayVariableDecl.getParent());
		}
	}
	
	public void visit(GlobalVarDecls globalVarDecls) {
		Struct varType = globalVarDecls.getType().struct;
		Struct varArrayType = new Struct(Struct.Array, varType);
		while (!names.isEmpty()) {
			String varName = names.remove(0);
			String type = variableTypes.remove(0);
			if (type.equals("var")) {
				Obj obj = Tab.insert(Obj.Var, varName, varType);
				//report_info("Deklarisana globalna promenljiva " + varName, null);
			} else {
				Obj obj = Tab.insert(Obj.Var, varName, varArrayType);
				//report_info("Deklarisan globalni niz " + varName, null);
			}
		}
	}
	
	/* methods */
	public void visit(ReturnType returnType) {
		currentType = returnType.getType().struct;
	}
	
	public void visit(ReturnVoid returnVoid) {
		currentType = Tab.noType;
	}
	
	public void visit(MethodName methodName) {
		String name = methodName.getMethodName();
		if (Tab.currentScope().findSymbol(name) == null || (Tab.currentScope().findSymbol(name)!= null &&  Tab.currentScope().findSymbol(name).getKind() != Obj.Meth)) {
			if (name.equals("main")) 
				mainOk = true;
			currentMethod = Tab.insert(Obj.Meth, name, currentType);
			currentType = null;
			//report_info("Definisana metoda " + name + " " + methodName.getLine(), null);
			Tab.openScope();
			
			if (currentClass != null) {
				Obj obj = Tab.insert(Obj.Var, "this", currentClass.getType());
				obj.setFpPos(params.size());
				params.add(obj);
			}
		} else {
			report_error("Semanticka greska: identifikator " + name + " je vec iskoriscen ", methodName.getParent());
		}
	}
	
	public void visit(MethodDeclStmt methodDecl) {
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		
		if(!returnFound && currentMethod.getType() != Tab.noType){
			report_error("Semanticka greska: izostavljena return naredba u metodi " + currentMethod.getName() + " ", methodDecl.getParent());
		}
		currentMethod = null;
		returnFound = false;
	}
	
	public void visit(MethodDeclNoStmt methodDecl) {
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		
		if(!returnFound && currentMethod.getType() != Tab.noType){
			report_error("Semanticka greska: izostavljena return naredba u metodi "+ currentMethod.getName() + " ", methodDecl.getParent());
		}
		currentMethod = null;
	}
	
	/* formal parameters */
	public void visit(MethodFormParams methodFormParams) {
		params.clear();
		if (currentMethod.getName().equals("main")) {
			report_error("Semanticka greska: main metoda ne sme da ima parametre ", methodFormParams.getParent());
		} 
		if (currentMethod.getName().equals("main") && currentMethod.getType() != Tab.noType) {
			report_error("Semanticka greska: povratna vrednost main metode mora biti void ", methodFormParams.getParent());
		}
	}
	
	public void visit(NoMethodFormParams noMethodFormParams) {
		params.clear();
	}
	
	public void visit(OneFormParam oneFormParam) {
		String paramName = oneFormParam.getParamName();
		Struct type = oneFormParam.getType().struct;
		
		if (Tab.currentScope().findSymbol(paramName) == null) {
			Obj obj = Tab.insert(Obj.Var, paramName, type);
			obj.setFpPos(params.size());
			params.add(obj);
			currentMethod.setLevel(params.size());
			report_info("Deklarisan formalni parametar " + paramName + " broj " + currentMethod.getLevel(), null);
		} else {
			report_error("Semanticka greska: ime " + paramName + " je vec deklarisano.", null);
		}	
	}
	
	public void visit(OneArrayFormParam oneFormParam) {
		String paramName = oneFormParam.getParamName();
		Struct type = new Struct(Struct.Array, oneFormParam.getType().struct);
		if (Tab.currentScope().findSymbol(paramName) == null) {
			Obj obj = Tab.insert(Obj.Var, paramName, type);
			obj.setFpPos(params.size());
			params.add(obj);
			currentMethod.setLevel(params.size());
			report_info("Deklarisan formalni parametar " + paramName, null);
		} else {
			report_error("Semanticka greska: ime " + paramName + " je vec deklarisano.", null);
		}	
	}
	
	/* local variables */
	public void visit(OneVariableDecl oneVariableDecl) {
		String varName = oneVariableDecl.getVarName();
		if (Tab.currentScope().findSymbol(varName) == null &&  !(names.size() > 0 && names.contains(varName))) {   //nije deklarisan
			names.add(varName);
			variableTypes.add("var");
		} else {
			report_error("Semanticka greska: lokalna promenljiva " + varName + " je vec deklarisana", null);
		}
		
	}
	
	public void visit(OneArrayVariableDecl oneArrayVariableDecl) {
		String varName = oneArrayVariableDecl.getVarName();
		if (Tab.currentScope().findSymbol(varName) == null &&  !(names.size() > 0 && names.contains(varName))) {   //nije deklarisan
			names.add(varName);
			variableTypes.add("array");
		} else {
			report_error("Semanticka greska: lokalni niz " + varName + " je vec deklarisan", null);
		}
	}
	
	public void visit(VariableDecl variableDecl) {
		if (Tab.find(variableDecl.getType().getTypeName()) == Tab.noObj) {
			report_error("Semanticka greska: tip " + variableDecl.getType().getTypeName() + " nije pronadjen u tabeli simbola." , null);
			return;
		}
		currentType = variableDecl.getType().struct;
	}
	
	public void visit(VarDecl varDecl) {
		Struct varType = currentType;
		Struct varArrayType = new Struct(Struct.Array, currentType);
		while (!names.isEmpty()) {
			String localVarName = names.remove(0);
			String type = variableTypes.remove(0);
			if (type.equals("var")) {
				Obj node = Tab.insert(Obj.Var, localVarName, varType);
				report_info("Deklarisana lokalna promenljiva " + localVarName, null);
			} else {
				Tab.insert(Obj.Var, localVarName, varArrayType);
				report_info("Deklarisan lokalni niz " + localVarName, null);
			}
		}
		currentType = null;
	}
	
	/* constants */
	public void visit(ConstantDecl constantDecl) {
		currentType = constantDecl.getType().struct;
		if (!currentType.equals(Tab.intType) && !currentType.equals(Tab.charType) && !currentType.equals(boolType)) {
			report_error("Semanticka greska: konstanta mora biti tipa int, char ili bool.", null);
		}
		String constName = constantDecl.getConstName();
		if (Tab.find(constName) == Tab.noObj) {
			if (constantDecl.getTypeConst().obj.getType() == currentType) {
				names.add(constName);
				constValues.add(constantDecl.getTypeConst().obj);
			} else {
				report_error("Semanticka greska: konstanta " + constName + " nije odgovarajuceg tipa.", null);
			}
		}  else {
			report_error("Semanticka greska: identifikator " + constName + " je vec iskoriscen.", null);
		}
	}
	
	public void visit(ConstantDecls constantDecls) {
		String constName = constantDecls.getConstName();
		if (Tab.find(constName) == Tab.noObj && !(names.size() > 0 && names.contains(constName))) {
			if (constantDecls.getTypeConst().obj.getType() == currentType) {
				names.add(constName);
				constValues.add(constantDecls.getTypeConst().obj);
			} else {
				report_error("Semanticka greska: konstanta " + constName + " nije odgovarajuceg tipa.", null);
			}
			
		} else {
			report_error("Semanticka greska: identifikator " + constName + " je vec iskoriscen ", constantDecls.getParent());
		}
	}
	
	public void visit(ConstDecl constDecl) {
		while (!names.isEmpty()) {
			String constName = names.remove(0);
			Obj constObj = constValues.remove(0);
			Obj obj = Tab.insert(Obj.Con, constName, currentType);
			obj.setAdr(constObj.getAdr());
			//report_info("Deklarisana konstanta " + constName, null);
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
	
	/* record */
	public void visit(RecordName recordName) {
		String name = recordName.getRecordName();
		if (Tab.find(name) == Tab.noObj) {
			Struct recordType = new Struct(Struct.Class);
			currentRecord = Tab.insert(Obj.Type, name, recordType);
			currentRecord.setFpPos(RECORD_FP_POS);
			report_info("Deklarisan record " + name, null);
			Tab.openScope();
		} else {
			report_error("Semanticka greska: ime " + name + " je vec deklarisano.", null);
		}
	}
	
	
	public void visit(RecordDecl recordDecl) {
		Tab.chainLocalSymbols(currentRecord.getType());
		Tab.closeScope();
		currentRecord = null;
	}
	
	public void visit(RecordVariableDeclaration varDecl) {
		Struct varType = currentType;
		Struct varArrayType = new Struct(Struct.Array, currentType);
		
		while (!names.isEmpty()) {
			String varName = names.remove(0);
			String type = variableTypes.remove(0);
			if (type.equals("var")) {
				Obj node = Tab.insert(Obj.Fld, varName, varType);
				report_info("Deklarisano polje u record-u " + varName, null);
			} else {
				Tab.insert(Obj.Fld, varName, varArrayType);
				report_info("Deklarisano polje u record-u " + varName, null);
			}
		}
		currentType = null;
	}
	
	/* class */
	public void visit(ClassNameSingle className) {
		String name = className.getClassName();
		
		if (Tab.find(name) == Tab.noObj) {			
			currentClass = Tab.insert(Obj.Type, name, new Struct(Struct.Class));
			Tab.openScope();
		} else {
			report_error("Semanticka greska: ime " + name + " je vec deklarisano.", null);
		}
	}
	
	public void visit(ClassNameExtends className) {
		String name = className.getClassName();
		Struct superClass = className.getType().struct;
		//gde ovo sta sa ovim
		if (Tab.find(name) == Tab.noObj) {			
			currentClass = Tab.insert(Obj.Type, name, new Struct(Struct.Class));
			Tab.openScope();
		} else {
			report_error("Semanticka greska: ime " + name + " je vec deklarisano.", null);
		}
	}
	
	/* class fields */
	public void visit(ClassVariableDecl classVariableDecl) {
		currentType  = classVariableDecl.getType().struct;
	}
	
	public void visit(ClassVariableDeclList classVariableDeclList) {
		Struct varType = currentType;
		Struct varArrayType = new Struct(Struct.Array, currentType);
		int i = 1;
		while (!names.isEmpty()) {
			String varName = names.remove(0);
			String type = variableTypes.remove(0);
			currentClass.setLevel(currentClass.getLevel() + 1);
			
			if (type.equals("var")) {
				Obj node = Tab.insert(Obj.Fld, varName, varType);
				node.setFpPos(i);
				i++;
				report_info("Deklarisano polje " + varName, null);
			} else {
				Obj node = Tab.insert(Obj.Fld, varName, varArrayType);
				node.setFpPos(i);
				i++;
				report_info("Deklarisano polje " + varName, null);
			}
		}
	}
		
	public void visit(ClassDeclMethodsNoConstr classDeclMethodsNoConstr) {
		Tab.chainLocalSymbols(currentClass.getType());
		Tab.closeScope();
		currentClass = null;
	}
	
	public void visit(ClassDeclMethods classDeclMethods) {
		Tab.chainLocalSymbols(currentClass.getType());
		Tab.closeScope();
		currentClass = null;
	}
	
	public void visit(ClassDeclNoMethods classDeclNoMethods) {
		Tab.chainLocalSymbols(currentClass.getType());
		Tab.closeScope();
		currentClass = null;
	}
	
	public void visit(ConstructorName constructorName) {
		String name = constructorName.getClassName();
		if (!currentClass.getName().equals(name)) {
			report_error("Semanticka greska: konstruktor mora imati isto ime kao klasa " + name, null);
		} else {
			currentMethod = Tab.insert(Obj.Meth, name, Tab.noType);
			Tab.openScope();
		}
	}

	public void visit(ConstructorDecl constructorDecl) {
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
	}
	
	/* function call right */
	public void visit(FactorNoParen factorNoParen) {
		factorNoParen.obj = factorNoParen.getDesignator().obj;
		// PITANJE: da li treba jos nesto
	}
	
	public void visit(FactorParen factorParen) {
		factorParen.obj = factorParen.getDesignator().obj;
		report_info("Pronadjen poziv funkcije " + factorParen.obj.getName() + " na liniji " + factorParen.getLine(), null);
	}
	
	public void visit(FactorTypeConst factorTypeConst) {
		factorTypeConst.obj = factorTypeConst.getTypeConst().obj;
	}

	public void visit(FactorType factorType) {
		if (factorType.getType().struct.getKind() != Struct.Class) {
			factorType.obj = Tab.noObj;
			report_error("Semanticka greska: moze se instancirati samo klasni tip.", null);
		} else {
			factorType.obj = new Obj(Obj.Var, "", factorType.getType().struct);
			report_info("Instanciran je objekat tipa " + factorType.getType().getTypeName() + " ", factorType.getParent());
		}
	}
	
	public void visit(FactorArrayType factorType) {
		if (factorType.getType().struct != Tab.noType) {
			factorType.obj = new Obj(Obj.Var, "", new Struct(Struct.Array, factorType.getType().struct));
		}
	}
	
	public void visit(FactorExpr factorExpr) {
		factorExpr.obj = factorExpr.getExpr().obj;
	}
	
	/* addop list */
	public void visit(OneAddop oneAddop) {
		oneAddop.obj = oneAddop.getTerm().obj;
	}
	
	public void visit(AddOpsList addOpsList) {
		Obj el1 = addOpsList.getAddopList().obj;
		Obj el2 = addOpsList.getTerm().obj;
		
		if (el1.getType().getKind() == el2.getType().getKind() && el1.getType().getKind() == Struct.Int) {
			addOpsList.obj = new Obj(Obj.Var, "", el1.getType());
		} else {
			addOpsList.obj = Tab.noObj;
			report_error("Semanticka greska na liniji " + addOpsList.getLine() + ": tipovi moraju biti isti i moraju biti tipa int."  , null);
		}
	}
	
	/* term */
	public void visit(TermFactor termFactor) {
		termFactor.obj = termFactor.getFactor().obj;
	}
	
	public void visit(Terms terms) {
		Obj el1 = terms.getTerm().obj;
		Obj el2 = terms.getFactor().obj;
		
		if (el1.getType().getKind() == el2.getType().getKind() && el1.getType().getKind() == Struct.Int) {
			terms.obj = new Obj(Obj.Var, "", el1.getType());
		} else {
			terms.obj = Tab.noObj;
			report_error("Semanticka greska na liniji " + terms.getLine() + ": tipovi moraju biti isti i moraju biti tipa int."  , null);
		}
	}
	
	/* expr */
	public void visit(ExprPlus exprPlus) {
		exprPlus.obj = exprPlus.getTerm().obj;
	}
	
	public void visit(ExprPlusAddop exprPlusAddop) {
		Obj el1 = exprPlusAddop.getTerm().obj;
		Obj el2 = exprPlusAddop.getAddopList().obj;
		
		if (el1.getType().getKind() == el2.getType().getKind() && el1.getType().getKind() == Struct.Int) {
			exprPlusAddop.obj = new Obj(Obj.Var, "", el1.getType());
		} else {
			exprPlusAddop.obj = Tab.noObj;
			report_error("Semanticka greska na liniji " + exprPlusAddop.getLine() + ": tipovi moraju biti isti i moraju biti int."  , null);
		}
	}
	
	public void visit(ExprMinus exprMinus) {
		if (exprMinus.getTerm().obj.getType().getKind() == Struct.Int) {
			exprMinus.obj = exprMinus.getTerm().obj;
		} else {
			exprMinus.obj = Tab.noObj;
			report_error("Semanticka greska na liniji " + exprMinus.getLine() + ": tip mora biti int."  , null);
		}
	}
	
	public void visit(ExprMinusAddop exprMinusAddop) {
		Obj el1 = exprMinusAddop.getTerm().obj;
		Obj el2 = exprMinusAddop.getAddopList().obj;
		
		if (el1.getType().getKind() == el2.getType().getKind() && el1.getType().getKind() == Struct.Int) {
			exprMinusAddop.obj = new Obj(Obj.Var, "", el1.getType());
		} else {
			exprMinusAddop.obj = Tab.noObj;
			report_error("Semanticka greska na liniji " + exprMinusAddop.getLine() + ": tipovi moraju biti isti i moraju biti int."  , null);
		}
	}
	
	/* condition */
	public void visit(Conditions conditions) {
		Obj el1 = conditions.getCondition().obj;
		Obj el2 = conditions.getCondTerm().obj;
		if (!el1.getType().equals(boolType) || !el2.getType().equals(boolType)) {
			conditions.obj = Tab.noObj;
			report_error("Semanticka greska: tip izraza mora biti bool "  , conditions.getParent());
		} else {
			conditions.obj = new Obj(Obj.Var, "", boolType);
		}
	}
	
	public void visit(ConditionCondTerm conditionCondTerm) {
		if (!conditionCondTerm.getCondTerm().obj.getType().equals(boolType)) {
			conditionCondTerm.obj = Tab.noObj;
			report_error("Semanticka greska: tip izraza mora biti bool "  , conditionCondTerm.getParent());
		} else {
			conditionCondTerm.obj = new Obj(Obj.Var, "", boolType);
		}

	}
	
	/* cond term */
	public void visit(ConditionCondFact conditionCondFact) {
		if (!conditionCondFact.getCondFact().obj.getType().equals(boolType)) {
			conditionCondFact.obj = Tab.noObj;
			//report_error("Semanticka greska: tip izraza mora biti bool "  , conditionCondFact.getParent());
		} else {
			conditionCondFact.obj = conditionCondFact.getCondFact().obj;
		}
		
	}
	
	public void visit(CondTerms condTerms) {
		Obj el1 = condTerms.getCondTerm().obj;
		Obj el2 = condTerms.getCondFact().obj;
		if (!el1.getType().equals(boolType) || !el2.getType().equals(boolType)) {
			condTerms.obj = Tab.noObj;
			//report_error("Semanticka greska: tip izraza mora biti bool "  , condTerms.getParent());
		} else {
			condTerms.obj = new Obj(Obj.Var, "", boolType);
		}
	}
	
	/* cond fact */
	public void visit(CondFactExpr condFactExpr) {
		condFactExpr.obj = condFactExpr.getExpr().obj;
	}
	
	public void visit(CondFactExprRelop condFactExprRelop) {
		Obj el1 = condFactExprRelop.getExpr().obj;
		Obj el2 = condFactExprRelop.getExpr1().obj;
		
		if (el1.getType().equals(el2.getType())) {
			condFactExprRelop.obj = new Obj(Obj.Var, "", boolType);
		} else {
			condFactExprRelop.obj = Tab.noObj;
			report_error("Semanticka greska na liniji " + condFactExprRelop.getLine() + ": tipovi moraju biti isti."  , null);
		}
	}
	
	/* designator */
	public void visit(DesignatorIdent designator) {
		Obj obj = Tab.find(designator.getName());
    	if(obj == Tab.noObj){
    		designator.obj = Tab.noObj;
			report_error("Greska na liniji " + designator.getLine()+ " : ime "+ designator.getName() + " nije deklarisano! ", null);
    	} else {
    		designator.obj = obj;
	    	if (Tab.currentScope().findSymbol(designator.getName()) != null && designator.obj.getKind() == Obj.Var) {
	    		if (currentMethod != null 
	    				&& ((currentClass == null && currentMethod.getLevel() > designator.obj.getAdr()) 
	    				|| (currentClass != null && currentMethod.getLevel() - 1 > designator.obj.getAdr()))) {
	    			report_info("Pristup formalnom parametru " + designator.getName() + " na liniji " + designator.getLine(), null);
	    		} else {
	    			report_info("Pristup lokalnoj promenljivoj " + designator.getName() + " na liniji " + designator.getLine(), null);
	    		}
	    	} else if (designator.obj.getKind() == Obj.Fld ) {
    			//pristup polju rekorda?
    			report_info("Pristup polju unutrasnje klase " + designator.getName() + " na liniji " + designator.getLine(), null);
	    	} else  if (obj.getLevel() == 0 && designator.obj.getKind() == Obj.Var) {
				report_info("Pristup globalnoj promenljivoj " + designator.getName() + " na liniji " + designator.getLine(), null);	
	    	} 
	    	
	    	//jooooj sta sa ovim
	    	if (designator.obj.getKind() == Obj.Meth) {
	    		currentMethodCallStack.push(obj);
	    	}
    	}
	}
	
	public void visit(DesignatorBracket designator) {
		Obj obj = designator.getDesignator().obj;
		//Obj obj = Tab.find(designator.getDesignator().obj.getName());
    	if(obj == Tab.noObj){
			report_error("Semanticka greska: Ime " + designator.obj.getName() +" nije deklarisano! ", designator.getParent());
			//ne moze da se desi - moze ali...
    	} else {
    		
    		if (obj.getType().getKind() == Struct.Array) {
    			//PITANJE: da li treba ime neko i jel treba uopste novi Obj
    			designator.obj = new Obj(Obj.Elem, "", obj.getType().getElemType());
	    		report_info("Pristup elementu niza " + designator.obj.getName() + " na liniji " + designator.getLine(), null);
    		} else {
    			designator.obj = Tab.noObj;
    			report_error("Semanticka greska: " + designator.obj.getName() +" nije niz! ", designator.getParent());
    		}
    	}
    	
    	if (!designator.getExpr().obj.getType().equals(Tab.intType)) {
			report_error("Semanticka greska: Izraz u zagradama mora biti tipa int ", designator.getParent());

    	}
   
		
	}	
	
	public void visit(DesignatorPoint designator) {
		Obj prevDesignator = designator.getDesignator().obj;
		if (prevDesignator.getKind() == Obj.Meth) {
			prevDesignator.getLocalSymbols();
		}
		else if (prevDesignator.getType().getKind() == Struct.Class) {
			boolean ok = false;
			for (Obj o :prevDesignator.getType().getMembers()) {
				
				if (o.getName().equals(designator.getName())) {
					ok = true;
					designator.obj = o;
					if (designator.obj.getKind() == Obj.Meth) {
			    		currentMethodCallStack.push(o);
					}
				//	log.info("og");
					break;
				}
			}
			if (!ok) {
				designator.obj = Tab.noObj;
				if (prevDesignator.getFpPos() == RECORD_FP_POS) 
					report_error("U record-u " + prevDesignator.getName()+ " ne postoji polje "+ designator.getName(), designator.getParent());
				else 
					report_error("U klasi " + prevDesignator.getName()+ " ne postoji polje "+ designator.getName(), designator.getParent());
			} else  {
				//ostalo
				
			}
		} 
	}
	
	
	
	/* desginator statement */
	
	public void visit(DesignatorStmtFuncCall designatorStmtFuncCall) {
		Obj func = designatorStmtFuncCall.getDesignator().obj;
    	if(Obj.Meth == func.getKind()){
    		currentMethodCallStack.pop();
			report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + designatorStmtFuncCall.getLine(), null);
			designatorStmtFuncCall.obj = func;
    	}else{
			report_error("Greska na liniji " + designatorStmtFuncCall.getLine()+" : ime " + func.getName() + " nije funkcija!", null);
			designatorStmtFuncCall.obj = Tab.noObj;
    	}
	}
	
	public void visit(DesignatorStmtAssign designatorStmtAssign) {
		//postaviti obj
	}
	
	public void visit(AssignStmt assignStmt) {
		//koji obj se postavlja
		Designator designator = assignStmt.getDesignator();
		Expr expr = assignStmt.getExpr();
		
		if (myAssignableTo(designator.obj.getType(), expr.obj.getType()) 
				&& (designator.obj.getKind() == Obj.Fld || designator.obj.getKind() == Obj.Var 
						|| designator.obj.getKind() == Obj.Elem)) {
			//assignStmt.obj = sve ok
		} else {
			if (!myAssignableTo(designator.obj.getType(), expr.obj.getType())) {
				report_error("Greska na liniji " + assignStmt.getLine()+" : Tipovi nisu isti.", null);
			}
			if (!(designator.obj.getKind() == Obj.Fld || designator.obj.getKind() == Obj.Var 
				|| designator.obj.getKind() == Obj.Elem)) {
				report_error("Greska na liniji " + assignStmt.getLine() + " :  Designator mora biti promenljiva, element niza ili polje objekta unutrasnje klase.", null);
			}
		}
	}
	
	public void visit(DesignatorStmtInc designatorStmtInc) {
		Designator designator = designatorStmtInc.getDesignator();
		if (designator.obj.getType().getKind() == Struct.Int 
				&& (designator.obj.getKind() == Obj.Fld || designator.obj.getKind() == Obj.Var 
				|| designator.obj.getKind() == Obj.Elem)) {
			designatorStmtInc.obj = designator.obj;
		} else {
			
			if (designator.obj.getType().getKind() != Struct.Int) {
				report_error("Greska na liniji " + designatorStmtInc.getLine()+" : Designator mora biti tipa int.", null);
			}
			if (!(designator.obj.getKind() == Obj.Fld || designator.obj.getKind() == Obj.Var 
				|| designator.obj.getKind() == Obj.Elem)) {
				report_error("Greska na liniji " + designatorStmtInc.getLine()+" :  Designator mora biti promenljivu, element niza ili polje objekta unutrasnje klase. .", null);
			}
			designator.obj = Tab.noObj;
		}
	}
	
	public void visit(DesignatorStmtDec designatorStmtDec) {
		Designator designator = designatorStmtDec.getDesignator();
		if (designator.obj.getType().getKind() == Struct.Int 
				&& (designator.obj.getKind() == Obj.Fld || designator.obj.getKind() == Obj.Var 
				|| designator.obj.getKind() == Obj.Elem)) {
			designatorStmtDec.obj = designator.obj;
		} else {
			
			if (designator.obj.getType().getKind() != Struct.Int) {
				report_error("Greska na liniji " + designatorStmtDec.getLine()+" : Designator mora biti tipa int.", null);
			}
			if (!(designator.obj.getKind() == Obj.Fld || designator.obj.getKind() == Obj.Var 
				|| designator.obj.getKind() == Obj.Elem)) {
				report_error("Greska na liniji " + designatorStmtDec.getLine()+" :  Designator mora biti promenljivu, element niza ili polje objekta unutrasnje klase. .", null);
			}
			designator.obj = Tab.noObj;
		}
	}
	
	/* method call */
	
	/* actual parameters */
	public void visit(ActParamsList actParsList) {
		if ((currentMethodCallStack.peek().getLevel() == 0  && params.size() != 0) || (currentMethodCallStack.peek().getLevel()!= params.size())) {  //proveri za level
			report_error("Greska na liniji " + actParsList.getLine()+ " : parametri metode "+ currentMethodCallStack.peek().getName() +" nisu "
					+ "ispravni! " + currentMethodCallStack.peek().getLevel() + " params.size()=" + params.size(), null);
		} else {
			int i = 0;
			for (Obj param : currentMethodCallStack.peek().getLocalSymbols()) {
				if (i++ != 0) {
					Obj actParam = params.remove(0);
					
					if (!myAssignableTo(actParam.getType(), param.getType())) {
						report_error("Greska na liniji " + actParsList.getLine()+ " : parametar "+ param.getName() +" nije odgovarajuceg tipa!", null);
					}
				}
			}
		}
		params.clear();
	}
	
	public void visit(NoActParams noActParams) {
		params.clear();
	}
	
	public void visit(ActParam actParam) {
		//proveriti tip i 
		params.add(actParam.getExpr().obj);
	}
	
	public void visit(ActParams actParams) {
		params.add(actParams.getExpr().obj);
	}
	
	/* statement */
	public void visit(DesignatorStmt designatorStmt) {
		//
	}
	
	public void visit(DoKeyword doKeyword) {
		doCnt++;
		
	}
	
	public void visit(DoStmt doStmt) {
		if (!doStmt.getCondition().obj.getType().equals(boolType)) {
			report_error("Semanticka greska: tip uslovnog izraza mora biti bool "  , doStmt.getParent());
		}
		
	}
	
	
	public void visit(BreakStmt breakStmt) {
		if (doCnt == 0) {
			report_error("Semanticka greska: naredba break moze biti pozvana samo iz okruzujuce do naredbe.", null);
		} else {
			doCnt--;
		}
	}
	
	public void visit(ContinueStmt continueStmt) {
		if (doCnt == 0) {
			report_error("Semanticka greska: naredba continue moze biti pozvana samo iz okruzujuce do naredbe.", null);
		}
	}
	
	public void visit(ReturnStmtExpr returnStmtExpr) {
		if (currentMethod == null) {
			report_error("Semanticka greska: return naredba mora biti unutar funkcije ili metode "  , returnStmtExpr.getParent());
		} else {
			returnFound = true;
			if (!myAssignableTo(returnStmtExpr.getExpr().obj.getType(), currentMethod.getType())) {
				report_error("Semanticka greska: izraz u return naredbi mora biti istog tipa kao povratna vrednost funkcije "  , returnStmtExpr.getParent());
			}
		}
	}
	

	public void visit(ReturnStmt returnStmt) {
		if (currentMethod == null) {
			report_error("Semanticka greska: return naredba mora biti unutar funkcije ili metode "  , returnStmt.getParent());
		} else {
			returnFound = true;
		}
	}
	
	public void visit(ReadStmt readStmt) {
		Obj obj = readStmt.getDesignator().obj;
		if (obj.getKind() != Obj.Var && obj.getKind() != Obj.Elem && obj.getKind() != Obj.Fld) {
			report_error("Semanticka greska: formalni parametar read naredbe mora biti promenljiva, element niza ili polje "  , readStmt.getParent());
		}
		if (!obj.getType().equals(Tab.intType) && !obj.getType().equals(Tab.charType) && !obj.getType().equals(boolType)) {
			report_error("Semanticka greska: formalni parametar read naredbe mora biti tipa int, char ili bool "  , readStmt.getParent());
		}
	}
	
	public void visit(PrintStmt printStmt) {
		Obj obj = printStmt.getExpr().obj;
		if (!obj.getType().equals(Tab.intType) && !obj.getType().equals(Tab.charType) && !obj.getType().equals(boolType)) {
			report_error("Semanticka greska: formalni parametar print naredbe mora biti tipa int, char ili bool "  , printStmt.getParent());
		}
	}
	
	public void visit(PrintStmtNum printStmtNum) {
		Obj obj = printStmtNum.getExpr().obj;
		if (!obj.getType().equals(Tab.intType) && !obj.getType().equals(Tab.charType) && !obj.getType().equals(boolType)) {
			report_error("Semanticka greska: formalni parametar print naredbe mora biti tipa int, char ili bool "  , printStmtNum.getParent());
		}
	}
	
	public void visit(OkIfCondition okIfCondition) { 
		//da li treba obj?
		if (!okIfCondition.getCondition().obj.getType().equals(boolType)) {
			report_error("Semanticka greska: tip uslovnog izraza mora biti bool "  , okIfCondition.getParent());
		}
	}
	
	public void visit(IfStmt ifStmt) {
		//
	}
	
	public void visit(IfElseStmt ifElseStmt) {
		//
	}
	
	public void visit(MultipleStatements multipleStatements) {
		
	}
	
}

