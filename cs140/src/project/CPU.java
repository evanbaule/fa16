package project;

public class CPU {
	private int accumulator, instructionPointer, memoryBase;

	// Read and write to private fields class fields
	public int getAccumulator() {
		return accumulator;
	}

	public void setAccumulator(int accumulator) {
		this.accumulator = accumulator;
	}

	public int getInstructionPointer() {
		return instructionPointer;
	}

	public void setInstructionPointer(int instructionPointer) {
		this.instructionPointer = instructionPointer;
	}

	public int getMemoryBase() {
		return memoryBase;
	}

	public void setMemoryBase(int memoryBase) {
		this.memoryBase = memoryBase;
	}
	
	public void incrementIP(){
		instructionPointer++;
	}

}
