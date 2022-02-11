package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.mj.runtime.Run;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPc;
	
	private Obj currentMethod = null;
	
	private int conditionRelOp;
	private List<Integer> conditionOrJumps = new ArrayList<>();
	private Stack<List<Integer>> conditionAndJumps = new Stack<>();
	private Stack<Integer> skipElseStack = new Stack<>();
	
	private Stack<Integer> doStack = new Stack<>();
	private Stack<List<Integer>> breakStack = new Stack<>();
	private Stack<List<Integer>> continueStack = new Stack<>();
	
	Logger log = Logger.getLogger(getClass());

	public int getMainPc() {
		return mainPc;
	}
	
	public void defineFunctionOrd() {
		
	}
	
	public void defineFunctionChr() {
		
	}
	
	public void defineFunctionLen() {
		
	}
	
	public void visit(Program program) {		
		log.debug(Code.dataSize);
	}

	/* constants */
	public void visit(ConstantDecl constantDecl) {
		Obj obj = Tab.find(constantDecl.getConstName());
		Code.load(obj);
	}

	public void visit(ConstantDecls constantDecls) {
		Obj obj = Tab.find(constantDecls.getConstName());
		Code.load(obj);
	}

	/* method declarations */
	public void visit(MethodName methodName) {
		if ("main".equalsIgnoreCase(methodName.getMethodName())) {
			mainPc = Code.pc;
		}
		if ("ord".equalsIgnoreCase(methodName.getMethodName())) {
			log.debug("evooo");
		}
		currentMethod = Tab.find(methodName.getMethodName());
		methodName.obj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(methodName.obj.getLevel());
		Code.put(methodName.obj.getLocalSymbols().size());
	}

	public void visit(MethodDeclStmt methodDecl) {
		if (currentMethod.getType().equals(Tab.noType)) {
			Code.put(Code.exit);
			Code.put(Code.return_);
		} else {
			Code.put(Code.trap);
			Code.put(1);
		}
		currentMethod = null;
	}

	public void visit(MethodDeclNoStmt methodDecl) {
		if (currentMethod.getType().equals(Tab.noType)) {
			Code.put(Code.exit);
			Code.put(Code.return_);
		} else {
			Code.put(Code.trap);
			Code.put(1);
		}
		currentMethod = null;
	}

	/* desginators */
	public void visit(DesignatorLBracket designatorLBracket) {
		Obj arrayObj = ((DesignatorBracket) designatorLBracket.getParent()).getDesignator().obj;
		Code.load(arrayObj);
		
		//prvo se ucitava ovo pa onda Expr u zagradi - ne mora dup
	}
	
	public void visit(DesignatorBracket designatorBracket) {
		//designator je ucitan kod zagrade, a expr se ucitava posle toga, a pre desginatorBracket
	}

	public void visit(DesignatorPoint designatorPoint) {
		Code.load(designatorPoint.getDesignator().obj);		
	}
	
	public void visit(DesignatorIdent designatorIdent) {
		//ne mora jer se poziva FactorNoParen
	}
	
	public void visit(FactorNoParen designator) {
		Code.load(designator.obj);
		/*if (designator.obj.getType().getKind() == Struct.Array) {
			Code.put(Code.dup_x1); 
			Code.put(Code.pop);
			//Code.put(Code.dup);
		}	*/
	}

	public void visit(FactorParen factorParen) {
		Obj obj = factorParen.obj;
		int addrOffset = obj.getAdr() - Code.pc;
		if (!obj.getName().equals("ord") && !obj.getName().equals("chr") && !obj.getName().equals("len")) {
			Code.put(Code.call);
			Code.put2(addrOffset);
		}
		if (obj.getName().equals("len")) {
			Code.put(Code.arraylength);
		}
	}

	/* designator statement */
	public void visit(AssignStmt assignStmt) {
		Obj designatorObj = assignStmt.getDesignator().obj;
		Code.store(designatorObj);
	}
	
	public void visit(DesignatorStmtFuncCall designatorStmtFuncCall) {
		Obj obj = designatorStmtFuncCall.obj;
		int addrOffset = obj.getAdr() - Code.pc;
		if (!obj.getName().equals("ord") && !obj.getName().equals("chr") && !obj.getName().equals("len")) {
			Code.put(Code.call);
			Code.put2(addrOffset);
		}
		if (obj.getName().equals("len")) {
			Code.put(Code.arraylength);
		}
	}

	public void visit(DesignatorStmtInc designatorStmtInc) {
		//stek: adresa, index
		if (designatorStmtInc.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(designatorStmtInc.getDesignator().obj);
		
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(designatorStmtInc.getDesignator().obj);
	}
	
	public void visit(DesignatorStmtDec designatorStmtDec) {
		if (designatorStmtDec.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(designatorStmtDec.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(designatorStmtDec.getDesignator().obj);
	}
	
	public void visit(FactorType factorType) {
		Code.put(Code.new_);
		int size = 4 * (factorType.obj.getType().getMembers().size());
		Code.put2(size);
	}
	
	public void visit(FactorArrayType factorArrayType) {
		Code.put(Code.newarray);
		if (factorArrayType.getType().struct.getKind() == Struct.Char) {
			Code.put(0);
		} else {
			Code.put(1);
		}
	}
	
	/* condition */
	//RELOP
	public void visit(CondFactExpr condFactExpr) {
		Code.loadConst(1);
		conditionRelOp = Code.eq;
		Code.putFalseJump(conditionRelOp, 0);
		conditionAndJumps.peek().add(Code.pc - 2);
	}
	
	public void visit(CondFactExprRelop condFactExprRelop) {
		Code.putFalseJump(conditionRelOp, 0);
		conditionAndJumps.peek().add(Code.pc - 2);
	}
	
	/*  AND  */
	public void visit(CondTerms condTerms) {
		//sve se postavlja u condFact
	}
	
	/* condTerm */
	public void visit(ConditionCondFact conditionCondFact) {	
		
	}
	
	/* OR */
	public void visit(Or or) {
		Code.putJump(0);
		conditionOrJumps.add(Code.pc-2);
		
		for (int adr: conditionAndJumps.pop()) {
			Code.fixup(adr);
		}
		conditionAndJumps.push(new ArrayList<>());
	}
	
	/* condition OR condTerm */
	public void visit(Conditions conditions) {
		
	}
	
	/* condTerm */
	public void visit(ConditionCondTerm conditionCondTerm) {
		
	}
	
	
	public void visit(IfStart ifStart) {
		conditionAndJumps.add(new ArrayList<>());
	}
	
	
	public void visit(OkIfCondition okIfCondition) {
		for (int adr: conditionOrJumps) {
			Code.fixup(adr);
		}
	}
	
	public void visit(IfStmtEnd ifStmtEnd) {
		for (int adr: conditionOrJumps) {
			Code.fixup(adr);
		}
		for (int adr: conditionAndJumps.pop()) {
			Code.fixup(adr);
		}
		conditionOrJumps.clear();

	}
	
	public void visit(ElseStart elseStart) {
		Code.putJump(0);

		for (int adr: conditionAndJumps.pop()) {
			Code.fixup(adr);
		}
		conditionAndJumps.add(new ArrayList<>());
		skipElseStack.add(Code.pc - 2);
	}
	
	public void visit(IfElseStmtEnd ifElseStmtEnd) {
		Code.fixup(skipElseStack.pop());
		conditionAndJumps.pop();
		conditionOrJumps.clear();
	}
	

	public void visit(DoStart doStart) {
		doStack.add(Code.pc);
		//moraju liste
		breakStack.push(new ArrayList<>());
		continueStack.push(new ArrayList<>());
	}
	
	
	
	public void visit(WhileStart whileStart) {
		//novi uslovi
		conditionAndJumps.push(new ArrayList<>());
		for (int adr : continueStack.pop()) {
			Code.fixup(adr);
		}
		//peek ili pop??
	}
	
	public void visit(DoCondition doCondition) {
		
	}
	 
	public void visit(DoStmt doStmt) {
		int doStart = doStack.pop();
		
		Code.put(Code.jmp);
		Code.put2(doStart - Code.pc + 1);
		
		for (int adr : conditionAndJumps.pop()) {
			Code.fixup(adr);
		}
		for (int adr : conditionOrJumps) {
			//umesto fixup
			Code.put2(adr, (doStart - adr + 1));
		}
	
		//Code.put(Code.jmp);
		//Code.fixup()
		//ne radi kad je jedan uslov bez ||
		
		for (int adr : breakStack.pop()) {
			Code.fixup(adr);
		}
	}
	
	//sta kad se zavrsi do while
	
	public void visit(BreakStmt breakStmt) {
		Code.putJump(0);
		breakStack.peek().add(Code.pc - 2);
	}
	
	public void visit(ContinueStmt continueStmt) {
		Code.putJump(0);
		continueStack.peek().add(Code.pc - 2);
	}
		
	/* relational operators */ 
	
	public void visit(RelOpEq op) {
		conditionRelOp = Code.eq;
		//conditionOperators.push(Code.eq);
	}
	
	public void visit(RelOpNe op) {
		conditionRelOp = Code.ne;
		//conditionOperators.push(Code.ne);
	}
	
	public void visit(RelOpGt op) {
		conditionRelOp = Code.gt;
		//conditionOperators.push(Code.gt);
	}
	
	public void visit(RelOpGe op) {
		conditionRelOp = Code.ge;
		//conditionOperators.push(Code.ge);
	}
	
	public void visit(RelOpLt op) {
		conditionRelOp = Code.lt;
		//conditionOperators.push(Code.lt);
	}
	
	public void visit(RelOpLe op) {
		conditionRelOp = Code.le;
		//conditionOperators.push(Code.le);
	}
	
	/* arithmetic operators */
	public void visit(Terms terms) {
		Mulop mulop = terms.getMulop();

		if (mulop instanceof MulOpMul) {
			Code.put(Code.mul);
		} else if (mulop instanceof MulOpDiv) {
			Code.put(Code.div);
		} else if (mulop instanceof MulOpMod) {
			Code.put(Code.rem);
		}
	}


	public void visit(AddOpsList addOpsList) {
		Addop addop = addOpsList.getAddop();

		if (addop instanceof AddOpPlus) {
			Code.put(Code.add);
		} else if (addop instanceof AddOpMinus) {
			Code.put(Code.sub);
		}
	}

	public void visit(OneAddop oneAddop) {
		Addop addop = oneAddop.getAddop();

		if (addop instanceof AddOpPlus) {
			Code.put(Code.add);
		} else if (addop instanceof AddOpMinus) {
			Code.put(Code.sub);
		}
	}
	
	public void visit(MinusTerm minusTerm) {
		Code.loadConst(-1);
		Code.put(Code.mul);
	}

	/* type constants */
	public void visit(FactorTypeConst typeConst) {
		Code.load(typeConst.obj);
	}

	/* statements */
	public void visit(ReturnStmtExpr returnStmtExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
		
	}

	public void visit(ReturnStmt returnStmt) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(PrintStmt printStmt) {
		if (printStmt.getExpr().obj.getType() == Tab.intType
				|| printStmt.getExpr().obj.getType().getKind() == Struct.Bool) {
			Code.loadConst(5);
			Code.put(Code.print);
		} else {
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
	}

	public void visit(PrintStmtNum printStmtNum) {
		if (printStmtNum.getExpr().obj.getType() == Tab.intType
				|| printStmtNum.getExpr().obj.getType().getKind() == Struct.Bool) {
			Code.loadConst(printStmtNum.getNumConst());
			Code.put(Code.print);
		} else {
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
	}
	
	public void visit(ReadStmt readStmt) {
		if (readStmt.getDesignator().obj.getType() == Tab.intType
				|| readStmt.getDesignator().obj.getType().getKind() == Struct.Bool) {
			Code.put(Code.read);
		} else {
			Code.put(Code.bread);
		}
	}

}
