package project;

import java.util.Map;
import java.util.TreeMap;

public class MachineModel {
	public final Map<Integer, Instruction> INSTRUCTIONS = new TreeMap<>();
	private CPU cpu = new CPU();
	private Memory memory = new Memory();
	private HaltCallBack callback;
	private Code code = new Code();
	private Job[] jobs = new Job[2];
	private Job currentJob;

	public Job getCurrentJob() {
		return currentJob;
	}

	public void setJob(int i) {
		if (i != 0 && i != 1) {
			throw new IllegalArgumentException();
		}
		currentJob.setCurrentAcc(cpu.getAccumulator());
		currentJob.setCurrentIP(cpu.getInstructionPointer());
		currentJob = jobs[i];

		cpu.setAccumulator(currentJob.getCurrentAcc());
		cpu.setInstructionPointer(currentJob.getCurrentIP());
		cpu.setMemoryBase(currentJob.getStartMemoryIndex());
	}

	public MachineModel() {
		this(() -> System.exit(0));
	}

	public MachineModel(HaltCallBack callback) {
		this.callback = callback;
		for (int i = 0; i < jobs.length; i++) {
			jobs[i] = new Job();
		}
		currentJob = jobs[0];
		jobs[0].setStartCodeIndex(0);
		jobs[0].setStartMemoryIndex(0);

		jobs[1].setStartCodeIndex(Code.CODE_MAX / 4);
		jobs[1].setStartMemoryIndex(Memory.DATA_SIZE / 2);
		// INSTRUCTION SET

		// INSTRUCTION_MAP entry for "NOP"
		INSTRUCTIONS.put(0x0, arg -> {
			cpu.incrementIP();
		});

		// LOADS
		// INSTRUCTION_MAP entry for "LODI"
		INSTRUCTIONS.put(0x1, arg -> {
			cpu.setAccumulator(arg);
			cpu.incrementIP();

		});
		// INSTRUCTION_MAP entry for "LOD"
		INSTRUCTIONS.put(0x2, arg -> {
			int arg1 = memory.getData(cpu.getMemoryBase() + arg);
			cpu.setAccumulator(arg1);
			cpu.incrementIP();

		});
		// INSTRUCTION_MAP entry for "LODN"
		INSTRUCTIONS.put(0x3, arg -> {
			int arg1 = memory.getData(arg + cpu.getMemoryBase());
			int arg2 = memory.getData(arg1 + cpu.getMemoryBase());
			cpu.setAccumulator(arg2);
			cpu.incrementIP();
		});

		// STORES
		// INSTRUCTION_MAP entry for "STO"
		INSTRUCTIONS.put(0x4, arg -> {
			memory.setData(arg + cpu.getMemoryBase(), cpu.getAccumulator());
			cpu.incrementIP();

		});
		// INSTRUCTION_MAP entry for "STON"
		INSTRUCTIONS.put(0x5, arg -> {
			int arg1 = memory.getData(arg + cpu.getMemoryBase());
			memory.setData(arg1 + cpu.getMemoryBase(), cpu.getAccumulator());
			cpu.incrementIP();
		});

		// JUMPS
		// INSTRUCTION_MAP entry for "JMPI"
		INSTRUCTIONS.put(0x6, arg -> {
			cpu.setInstructionPointer(cpu.getInstructionPointer() + arg);

		});
		// INSTRUCTION_MAP entry for "JUMP"
		INSTRUCTIONS.put(0x7, arg -> {
			cpu.setInstructionPointer(cpu.getInstructionPointer() + memory.getData(arg + cpu.getMemoryBase()));

		});
		// INSTRUCTION_MAP entry for "JMZI"
		INSTRUCTIONS.put(0x8, arg -> {
			if (cpu.getAccumulator() == 0)
				cpu.setInstructionPointer(cpu.getInstructionPointer() + (arg));

			else
				cpu.incrementIP();

		});

		// INSTRUCTION_MAP entry for "JMPN"
		INSTRUCTIONS.put(0x1B, arg -> {
			int target = memory.getData(cpu.getMemoryBase() + arg);
			cpu.setInstructionPointer(currentJob.getStartCodeIndex() + target);
		});

		// INSTRUCTION_MAP entry for "JMPZ"
		INSTRUCTIONS.put(0x9, arg -> {
			if (cpu.getAccumulator() == 0)
				cpu.setInstructionPointer(cpu.getInstructionPointer() + memory.getData(arg + cpu.getMemoryBase()));

			else
				cpu.incrementIP();

		});

		// ARITHMETIC

		// ADDITION

		// INSTRUCTION_MAP entry for "ADDI"
		INSTRUCTIONS.put(0xA, arg ->

		{
			cpu.setAccumulator(cpu.getAccumulator() + arg);
			cpu.incrementIP();
		});

		// INSTRUCTION_MAP entry for "ADD"
		INSTRUCTIONS.put(0xB, arg -> {
			int arg1 = memory.getData(cpu.getMemoryBase() + arg);
			cpu.setAccumulator(cpu.getAccumulator() + arg1);
			cpu.incrementIP();
		});

		// INSTRUCTION_MAP entry for "ADDN"
		INSTRUCTIONS.put(0xC, arg -> {
			int arg1 = memory.getData(cpu.getMemoryBase() + arg);
			int arg2 = memory.getData(cpu.getMemoryBase() + arg1);
			cpu.setAccumulator(cpu.getAccumulator() + arg2);
			cpu.incrementIP();
		});

		// SUBTRACTION

		// INSTRUCTION_MAP entry for "SUBI"
		INSTRUCTIONS.put(0xD, arg ->

		{
			cpu.setAccumulator(cpu.getAccumulator() - arg);
			cpu.incrementIP();
		});

		// INSTRUCTION_MAP entry for "SUB"
		INSTRUCTIONS.put(0xE, arg -> {
			int arg1 = memory.getData(cpu.getMemoryBase() + arg);
			cpu.setAccumulator(cpu.getAccumulator() - arg1);
			cpu.incrementIP();
		});

		// INSTRUCTION_MAP entry for "SUBN"
		INSTRUCTIONS.put(0xF, arg -> {
			int arg1 = memory.getData(cpu.getMemoryBase() + arg);
			int arg2 = memory.getData(cpu.getMemoryBase() + arg1);
			cpu.setAccumulator(cpu.getAccumulator() - arg2);
			cpu.incrementIP();
		});

		// MULTIPLICATION

		// INSTRUCTION_MAP entry for "MULI"
		INSTRUCTIONS.put(0x10, arg ->

		{
			cpu.setAccumulator(cpu.getAccumulator() * arg);
			cpu.incrementIP();
		});

		// INSTRUCTION_MAP entry for "MUL"
		INSTRUCTIONS.put(0x11, arg -> {
			int arg1 = memory.getData(cpu.getMemoryBase() + arg);
			cpu.setAccumulator(cpu.getAccumulator() * arg1);
			cpu.incrementIP();
		});

		// INSTRUCTION_MAP entry for "MULN"
		INSTRUCTIONS.put(0x12, arg -> {
			int arg1 = memory.getData(cpu.getMemoryBase() + arg);
			int arg2 = memory.getData(cpu.getMemoryBase() + arg1);
			cpu.setAccumulator(cpu.getAccumulator() * arg2);
			cpu.incrementIP();
		});

		// DIVISION

		// INSTRUCTION_MAP entry for "DIVI"
		INSTRUCTIONS.put(0x13, arg ->

		{
			if (arg == 0) {
				throw new DivideByZeroException();
			}

			else {
				cpu.setAccumulator(cpu.getAccumulator() / arg);
				cpu.incrementIP();
			}

		});

		// INSTRUCTION_MAP entry for "DIV"
		INSTRUCTIONS.put(0x14, arg -> {
			int arg1 = memory.getData(cpu.getMemoryBase() + arg);
			if (arg1 == 0) {
				throw new DivideByZeroException();
			}
			cpu.setAccumulator(cpu.getAccumulator() / arg1);
			cpu.incrementIP();

		});

		// INSTRUCTION_MAP entry for "DIVN"
		INSTRUCTIONS.put(0x15, arg -> {
			int arg1 = memory.getData(cpu.getMemoryBase() + arg);
			int arg2 = memory.getData(cpu.getMemoryBase() + arg1);
			if (arg1 == 0 || arg2 == 0) {
				throw new DivideByZeroException();
			}
			cpu.setAccumulator(cpu.getAccumulator() / arg2);
			cpu.incrementIP();

		});

		// BOOLEAN LOGIC
		// INSTRUCTION_MAP entry for "ANDI"
		INSTRUCTIONS.put(0x16, arg -> {
			if (cpu.getAccumulator() != 0 && arg != 0)
				cpu.setAccumulator(1);
			else
				cpu.setAccumulator(0);
			cpu.incrementIP();

		});

		// INSTRUCTION_MAP entry for "AND"
		INSTRUCTIONS.put(0x17, arg -> {
			if (cpu.getAccumulator() != 0 && memory.getData(arg + cpu.getMemoryBase()) != 0)
				cpu.setAccumulator(1);
			else
				cpu.setAccumulator(0);
			cpu.incrementIP();

		});

		// INSTRUCTION_MAP entry for "NOT"
		INSTRUCTIONS.put(0x18, arg -> {
			if (cpu.getAccumulator() != 0)
				cpu.setAccumulator(0);
			else
				cpu.setAccumulator(1);
			cpu.incrementIP();
		});

		// INSTRUCTION_MAP entry for "CMPL"
		INSTRUCTIONS.put(0x19, arg -> {
			if (memory.getData(arg + cpu.getMemoryBase()) < 0)
				cpu.setAccumulator(1);
			else
				cpu.setAccumulator(0);
			cpu.incrementIP();

		});

		// INSTRUCTION_MAP entry for "CMPZ"
		INSTRUCTIONS.put(0x1A, arg -> {
			if (memory.getData((arg + cpu.getMemoryBase())) == 0)
				cpu.setAccumulator(1);
			else
				cpu.setAccumulator(0);
			cpu.incrementIP();
		});

		// INSTRUCTION_MAP entry for "HALT"
		INSTRUCTIONS.put(0x1F, arg -> {
			callback.halt();
		});

	}

	public void reset() {
		currentJob.reset();
	}

	public States getCurrentState() {
		return currentJob.getCurrentState();
	}

	public void setCurrentState(States currentState) {
		currentJob.setCurrentState(currentState);

	}

	public int getStartCodeIndex() {
		return currentJob.getStartCodeIndex();
	}

	public void setStartCodeIndex(int startcodeIndex) {
		currentJob.setStartCodeIndex(startcodeIndex);
	}

	public int getCodeSize() {
		return currentJob.getCodeSize();
	}

	public void setCodeSize(int codeSize) {
		currentJob.setCodeSize(codeSize);
	}

	public int getStartMemoryIndex() {
		return currentJob.getStartMemoryIndex();
	}

	public void setStartMemoryIndex(int startmemoryIndex) {
		currentJob.setStartMemoryIndex(startmemoryIndex);
	}

	public int getCurrentIP() {
		return currentJob.getCurrentIP();
	}

	public void setCurrentIP(int currentIP) {
		currentJob.setCurrentIP(currentIP);
	}

	public int getCurrentAcc() {
		return currentJob.getCurrentAcc();
	}

	public void setCurrentAcc(int currentAcc) {
		currentJob.setCurrentAcc(currentAcc);
	}

	public int hashCode() {
		return currentJob.hashCode();
	}

	public boolean equals(Object obj) {
		return currentJob.equals(obj);
	}

	public String toString() {
		return currentJob.toString();
	}

	public int[] getData() {
		return memory.getData();
	}

	public int getAccumulator() {
		return cpu.getAccumulator();
	}

	public void setAccumulator(int accumulator) {
		cpu.setAccumulator(accumulator);
	}

	public int getInstructionPointer() {
		return cpu.getInstructionPointer();
	}

	public void setInstructionPointer(int instructionPointer) {
		cpu.setInstructionPointer(instructionPointer);
	}

	public int getMemoryBase() {
		return cpu.getMemoryBase();
	}

	public void setMemoryBase(int memoryBase) {
		cpu.setMemoryBase(memoryBase);
	}

	public int getData(int idx) {
		return memory.getData(idx);
	}

	public void setData(int idx, int val) {
		memory.setData(idx, val);
	}

	public Instruction get(int ins) {
		return INSTRUCTIONS.get(ins);
	}

	public Code getCode() {
		return code;
	}

	public void setCode(int i, int op, int arg) {
		code.setCode(i, op, arg);
	}

	public int getChangedIndex() {
		return memory.getChangedIndex();
	}

	public int getOp(int i) {
		return code.getOp(i);
	}

	public int getArg(int i) {
		return code.getArg(i);
	}

	public void step() {
		try {
			int ip = cpu.getInstructionPointer();
			if (!(currentJob.getStartCodeIndex() <= ip
					&& ip < currentJob.getStartCodeIndex() + currentJob.getCodeSize())) {
				throw new CodeAccessException("Instruction Pointer out of Bounds");
			}

			int opcode = getOp(ip);
			int arg = getArg(ip);
			get(opcode).execute(arg);

		} catch (Exception e) {
			callback.halt();
			throw e;
		}
	}

	public void clearJob() {
		memory.clear(currentJob.getStartMemoryIndex(), currentJob.getStartMemoryIndex() + Memory.DATA_SIZE / 2);
		code.clear(currentJob.getStartCodeIndex(), currentJob.getStartCodeIndex() + currentJob.getCodeSize());
		setAccumulator(0);
		cpu.setInstructionPointer(currentJob.getStartCodeIndex());
		currentJob.reset();
	}

}
